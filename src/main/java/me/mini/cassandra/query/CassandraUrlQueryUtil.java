package me.mini.cassandra.query;

import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.*;
import com.netflix.astyanax.query.ColumnFamilyQuery;
import com.netflix.astyanax.query.PreparedCqlQuery;
import com.netflix.astyanax.serializers.StringSerializer;

import me.mini.bean.UrlMapping;
import me.mini.cassandra.client.CassandraClient;
import me.mini.utils.GlobalUtils;
import me.mini.utils.MinimeException;

import org.apache.log4j.Logger;

/**
 * Singleton design pattern
 * 
 * @author parampreetsethi
 *
 */
public class CassandraUrlQueryUtil {

    private CassandraClient client;
    private ColumnFamily<String, String> columnFamily;
    private static CassandraUrlQueryUtil cassandraUrlQueryUtil;
    public static final String COLUMN_FAMILY_NAME = "url_mapping";
    private static final Logger log = Logger.getLogger(CassandraUrlQueryUtil.class);


    private interface Query {
        public static final String ORIG_URL_QUERY = "SELECT url_hash, orig_url FROM url_mapping WHERE orig_url = ?";
        public static final String URL_HASH_QUERY = "SELECT url_hash, orig_url FROM url_mapping WHERE url_hash = ?";
        public static final String URL_INSERT_QUERY = "INSERT INTO url_mapping (url_hash, orig_url) VALUES (?, ?);";
        public static final String COUNT_ALL_QUERY = "SELECT COUNT(1) FROM url_mapping LIMIT 999999999";
    }

	public static CassandraUrlQueryUtil getInstance() throws MinimeException{
		if (cassandraUrlQueryUtil == null) {
			new CassandraUrlQueryUtil().initialize();
		}
		return cassandraUrlQueryUtil;
	}

    private CassandraUrlQueryUtil() {
    }

    private synchronized void initialize() throws MinimeException{
        try {
        	cassandraUrlQueryUtil = new CassandraUrlQueryUtil();
        	cassandraUrlQueryUtil.columnFamily = getColumnFamily();
        	cassandraUrlQueryUtil.client = CassandraClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MinimeException(e);
        }
    }

    /**
     * First check if original url already exists in database If yes return the
     * existing entry
     * <p/>
     * If not create new record in the database
     *
     * @param entity
     * @throws me.mini.utils.MinimeException
     */
    public boolean writeQuery(UrlMapping entity) throws MinimeException {
        if (entity == null) {
            return false;
        }
        // Double check to make sure consistent data
        UrlMapping data = queryByOrigUrl(entity.getOrigUrl());
        if (data != null) {
            return false;
        }
        try {
            log.info(String.format("CQL: %s , with values: %s, %s", Query.URL_INSERT_QUERY, entity.getUrlHash(), entity.getOrigUrl()));
            client.getKeyspace().prepareQuery(columnFamily).withCql(Query.URL_INSERT_QUERY)
                    .asPreparedStatement()
                    .withStringValue(entity.getUrlHash())
                    .withStringValue(entity.getOrigUrl()).execute();
            return true;
        } catch (ConnectionException cex) {
            cex.printStackTrace();
            throw new MinimeException(cex);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new MinimeException(ex);
        }
    }

    /**
     * Get url mappings for a on given short url
     *
     * @param urlHash
     * @return
     * @throws me.mini.utils.MinimeException
     */
    public UrlMapping queryByUrlHash(String urlHash) throws MinimeException {
        return queryUrlMapping(Query.URL_HASH_QUERY, urlHash);
    }

    /**
     * Get url mappings for a on given origUrl
     *
     * @param origUrl
     * @return
     * @throws me.mini.utils.MinimeException
     */
    public UrlMapping queryByOrigUrl(String origUrl) throws MinimeException {
        return queryUrlMapping(Query.ORIG_URL_QUERY, origUrl);
    }

    /**
     * Execute reads on url_mapping via prepared query and it's parameter values
     *
     * @param preparedQuery
     * @param value
     * @return
     * @throws MinimeException
     */
    public UrlMapping queryUrlMapping(String preparedQuery, String value) throws MinimeException {
        if (GlobalUtils.isStringNullOrEmpty(preparedQuery) || GlobalUtils.isStringNullOrEmpty(value)) {
            return null;
        }
        UrlMapping entity = null;
        ColumnFamilyQuery<String, String> columnFamilyQuery = client.getKeyspace().prepareQuery(columnFamily);
        PreparedCqlQuery<String, String> cqlQuery = columnFamilyQuery.withCql(preparedQuery).asPreparedStatement();
        try {
            log.info(String.format("CQL: %s , with values: %s", preparedQuery, value));
            OperationResult<CqlResult<String, String>> result = cqlQuery.withStringValue(value).execute();
            Rows<String, String> rows = result.getResult().getRows();
            if (rows == null || rows.isEmpty()) {
                return null;
            }
            for (Row<String, String> row : rows) {
                ColumnList<String> columns = row.getColumns();
                // avoid zombie cassandra records that have non-existing id value
                if (columns.size() < 2) {
                    continue;
                }
                entity = new UrlMapping();
                entity.setUrlHash(columns.getColumnByIndex(0).getStringValue());
                entity.setOrigUrl(columns.getColumnByIndex(1).getStringValue());
                break;
            }
        } catch (ConnectionException cex) {
            cex.printStackTrace();
            throw new MinimeException(cex);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new MinimeException(ex);
        }
        return entity;
    }

    public long countQuery() throws MinimeException {
        ColumnFamilyQuery<String, String> columnFamilyQuery = client.getKeyspace().prepareQuery(columnFamily);
        PreparedCqlQuery<String, String> cqlQuery = columnFamilyQuery.withCql(Query.COUNT_ALL_QUERY).asPreparedStatement();
        try {
            log.info(String.format("CQL: %s", Query.COUNT_ALL_QUERY));
            OperationResult<CqlResult<String, String>> result = cqlQuery.execute();
            Rows<String, String> rows = result.getResult().getRows();
            if (rows == null || rows.isEmpty()) {
                return -1;
            }
            for (Row<String, String> row : rows) {
                ColumnList<String> columns = row.getColumns();
                long count = columns.getColumnByIndex(0).getLongValue();
                return count;
            }
        } catch (ConnectionException cex) {
            cex.printStackTrace();
            throw new MinimeException(cex);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new MinimeException(ex);
        }
        return -1;
    }


	/**
	 * Delete url entry by hash key
	 * 
	 * @param urlHash
	 */
	public void deleteQueryExecute(String urlHash) throws MinimeException {
		if (!GlobalUtils.isStringNullOrEmpty(urlHash)) {
			try {
				MutationBatch m = client.getKeyspace().prepareMutationBatch();
				// Deleting an entire row
				m.withRow(columnFamily, urlHash).delete();
				m.execute();
			} catch (ConnectionException e) {
				e.printStackTrace();
				throw new MinimeException(e);
			}
		}
	}

    private ColumnFamily<String, String> getColumnFamily() {
        ColumnFamily<String, String> cf = new ColumnFamily<String, String>(COLUMN_FAMILY_NAME, StringSerializer.get(), StringSerializer.get());
        return cf;
    }

}
