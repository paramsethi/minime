package me.mini.client;

import java.util.concurrent.locks.ReentrantLock;

import me.mini.entity.UrlEntity;
import me.mini.utils.AppConstants;
import me.mini.utils.MiniMeException;
import me.mini.utils.PropertyBag;
import me.mini.utils.Utils;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.CqlResult;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.ColumnFamilyQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

/**
 * Singleton Thread safe Cassandra Client
 * 
 * @author parampreetsethi
 * 
 */
public class CassandraClient implements AppConstants {
	private static CassandraClient client = null;
	private static Keyspace keyspace;
	private static AstyanaxContext<Keyspace> keyspaceContext;
	private static ReentrantLock lock = new ReentrantLock();
	private final ColumnFamily<String, String> CF_URL_MAP = new ColumnFamily<String, String>(
			COLUMN_FAMILY_NAME, // Column Family Name
			StringSerializer.get(), // Key Serializer
			StringSerializer.get()); // Column Serializer

	private static final String ORIG_URL_QUERY = "SELECT url_hash, orig_url FROM url_mapping WHERE orig_url = ?";
	private static final String URL_HASH_QUERY = "SELECT url_hash, orig_url FROM url_mapping WHERE url_hash = ?";
	private static final String URL_INSERT_QUERY = "INSERT INTO url_mapping (url_hash, orig_url) VALUES (?, ?);";

	private CassandraClient() {
	}

	/**
	 * Get Singleton Cassandra Client instance
	 * 
	 * @return
	 */
	public static CassandraClient getInstance() throws MiniMeException {
		CassandraClient helper = client;
		if (helper == null) {
			try {
				lock.lock();
				if (helper == null) {// Double check locking
					helper = new CassandraClient();
					helper.initializeConnection();
				}
				client = helper;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new MiniMeException(ex);
			} finally {
				lock.unlock();
			}
		}
		return client;
	}

	/**
	 * Initialize Cassandra connection
	 */
	private void initializeConnection() {
		keyspaceContext = new AstyanaxContext.Builder()
				.forCluster(PropertyBag.getProperty("cluster_name"))
				.forKeyspace(PropertyBag.getProperty("cassandra_keyspace"))
				.withAstyanaxConfiguration(
						new AstyanaxConfigurationImpl()
								.setCqlVersion(
										PropertyBag.getProperty("cql_version"))
								.setTargetCassandraVersion(
										PropertyBag
												.getProperty("cassandra_version")))
				.withAstyanaxConfiguration(
						new AstyanaxConfigurationImpl()
								.setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE))
				.withConnectionPoolConfiguration(
						new ConnectionPoolConfigurationImpl(PropertyBag
								.getProperty("connection_pool"))
								.setPort(
										PropertyBag
												.getIntProperty("cassandra_port"))
								.setMaxConnsPerHost(1)
								.setSeeds(
										PropertyBag
												.getProperty("cassandra_seed")))
				.withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
				.buildKeyspace(ThriftFamilyFactory.getInstance());

		keyspaceContext.start();

		keyspace = keyspaceContext.getClient();
	}

	/**
	 * Delete url entry by hash key
	 * 
	 * @param urlHash
	 */
	public void deleteQueryExecute(String urlHash) throws MiniMeException {
		if (!Utils.isStringNullOrEmpty(urlHash)) {
			try {
				MutationBatch m = keyspace.prepareMutationBatch();

				// Deleting an entire row
				m.withRow(CF_URL_MAP, urlHash).delete();

				m.execute();
			} catch (ConnectionException e) {
				e.printStackTrace();
				throw new MiniMeException();
			}
		}
	}

	/**
	 * First check if original url already exists in database If yes return the
	 * existing entry
	 * 
	 * If not create new record in the database
	 * 
	 * @param entity
	 * @throws MiniMeException
	 */
	public boolean writeQueryExecute(UrlEntity entity) throws MiniMeException {
		if (entity != null) {
			// Double check to make sure consistent data
			UrlEntity data = getByOrigUrl(entity.getOrigUrl());
			if (data == null) {
				try {
					keyspace.prepareQuery(CF_URL_MAP).withCql(URL_INSERT_QUERY)
							.asPreparedStatement()
							.withStringValue(entity.getUrlHash())
							.withStringValue(entity.getOrigUrl()).execute();
					return true;
				} catch (ConnectionException cex) {
					cex.printStackTrace();
					throw new MiniMeException(cex);
				} catch (Exception ex) {
					ex.printStackTrace();
					throw new MiniMeException(ex);
				}
			}
		}

		return false;
	}

	/**
	 * Get data based on url hash
	 * 
	 * @param urlHash
	 * @return
	 * @throws MiniMeException
	 */
	public UrlEntity getByShortUrlHash(String urlHash) throws MiniMeException {
		UrlEntity entity = null;

		if (!Utils.isStringNullOrEmpty(urlHash)) {
			Rows<String, String> rows = readQueryExecute(urlHash,
					URL_HASH_QUERY);

			if (rows != null && !rows.isEmpty()) {
				for (String key : rows.getKeys()) {
					Row<String, String> row = rows.getRow(key);
					ColumnList<String> columns = row.getColumns();
					if (columns.size() >= 2) {
						entity = new UrlEntity();
						entity.setUrlHash(columns.getColumnByIndex(0)
								.getStringValue());
						entity.setOrigUrl(columns.getColumnByIndex(1)
								.getStringValue());
					}
				}
			}
		}

		return entity;
	}

	/**
	 * Get data based on given origUrl
	 * 
	 * @param origUrl
	 * @return
	 * @throws MiniMeException
	 */
	public UrlEntity getByOrigUrl(String origUrl) throws MiniMeException {
		UrlEntity entity = null;

		if (!Utils.isStringNullOrEmpty(origUrl)) {
			Rows<String, String> rows = readQueryExecute(origUrl,
					ORIG_URL_QUERY);

			if (rows != null && !rows.isEmpty()) {

				for (Row<String, String> row : rows) {
					ColumnList<String> columns = row.getColumns();
					if (columns.size() >= 2) {
						entity = new UrlEntity();
						entity.setUrlHash(columns.getColumnByIndex(0)
								.getStringValue());
						entity.setOrigUrl(columns.getColumnByIndex(1)
								.getStringValue());
					}
				}
			}

		}

		return entity;
	}

	/**
	 * Read data based on given param and query
	 * 
	 * @param param
	 * @return
	 */
	public Rows<String, String> readQueryExecute(String param, String query)
			throws MiniMeException {
		try {
			OperationResult<CqlResult<String, String>> result;

			ColumnFamilyQuery<String, String> columnFamilyQuery = keyspace
					.prepareQuery(CF_URL_MAP);

			result = columnFamilyQuery.withCql(query).asPreparedStatement()
					.withStringValue(param).execute();
			Rows<String, String> rows = result.getResult().getRows();
			return rows;
		} catch (ConnectionException cex) {
			cex.printStackTrace();
			throw new MiniMeException(cex);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new MiniMeException(ex);
		}
	}

	public static void main(String[] args) throws MiniMeException {
		CassandraClient client = CassandraClient.getInstance();
		client.initializeConnection();
		UrlEntity entity = client.getByShortUrlHash("abc");
		entity.setOrigUrl("http://def.com");
		entity.setUrlHash("def");
		client.writeQueryExecute(entity);
	}
}
