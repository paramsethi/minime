package me.mini.cassandra.client;

import me.mini.utils.PropertyBag;
/**
 * 
 * @author parampreetsethi
 *
 */
public class CassandraClientConfig {

    private static interface Property {
        String KEYSPACE = "cassandra_keyspace";
        String CQL_VERSION = "cql_version";
        String CLUSTER_NAME = "cluster_name";
        String CASSANDRA_VERSION = "cassandra_version";
        String CONNECTION_POOL = "connection_pool";
        String PORT = "cassandra_port";
        String SEEDS = "cassandra_seeds";
    }

    public static String keyspace() {
        return PropertyBag.getProperty(Property.KEYSPACE);
    }

    public static String cqlVersion() {
        return PropertyBag.getProperty(Property.CQL_VERSION);
    }

    public static String clusterName() {
        return PropertyBag.getProperty(Property.CLUSTER_NAME);
    }

    public static String cassandraVersion() {
        return PropertyBag.getProperty(Property.CASSANDRA_VERSION);
    }

    public static String connectionPool() {
        return PropertyBag.getProperty(Property.CONNECTION_POOL);
    }

    public static int port() {
        return PropertyBag.getIntProperty(Property.PORT);
    }

    public static String seeds() {
        return PropertyBag.getProperty(Property.SEEDS);
    }

}
