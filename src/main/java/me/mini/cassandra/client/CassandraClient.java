package me.mini.cassandra.client;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import me.mini.utils.MinimeException;
import org.apache.log4j.Logger;

/**
 * Singleton Thread safe Cassandra Client
 *
 * @author parampreetsethi
 */
public class CassandraClient {

    private static Keyspace keyspace;
    private static CassandraClient client = null;
    private static final Logger log = Logger.getLogger(CassandraClient.class);


    /**
     * private constructor to ensure singleton instantiation via {@link CassandraClient#getKeyspace()}
     */
    private CassandraClient() {
        initialize();
    }

    /**
     * Get Singleton Cassandra Client instance
     *
     * @return
     */
    public static CassandraClient getInstance() throws MinimeException {
        if (client == null) {
            client = new CassandraClient();
        }
        return client;
    }

    /**
     * Initialize Cassandra connection
     */
    private synchronized void initialize() {
        log.info("Initializing astyanax context");
        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
                .forCluster(CassandraClientConfig.clusterName())
                .forKeyspace(CassandraClientConfig.keyspace())
                .withAstyanaxConfiguration(
                        new AstyanaxConfigurationImpl()
                                .setCqlVersion(CassandraClientConfig.cqlVersion())
                                .setTargetCassandraVersion(CassandraClientConfig.cassandraVersion())
                )
                .withAstyanaxConfiguration(
                        new AstyanaxConfigurationImpl()
                                .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
                )
                .withConnectionPoolConfiguration(
                        new ConnectionPoolConfigurationImpl(CassandraClientConfig.connectionPool())
                                .setPort(CassandraClientConfig.port()).setMaxConnsPerHost(1)
                                .setSeeds(CassandraClientConfig.seeds())
                )
                .withConnectionPoolMonitor(
                        new CountingConnectionPoolMonitor())
                .buildKeyspace(ThriftFamilyFactory.getInstance());

        context.start();
        keyspace = context.getClient();
        log.info("Done initializing astyanax context");
    }

    public Keyspace getKeyspace() {
        if (keyspace == null) {
            initialize();
        }
        return keyspace;
    }

}
