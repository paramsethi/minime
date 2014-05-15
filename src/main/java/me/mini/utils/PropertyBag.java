package me.mini.utils;

import me.mini.bean.SystemStatics;

import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * File for setting and getting properties minime.properties
 *
 * @author parampreetsethi
 */
public class PropertyBag {

    private static Properties properties = null;
    private static final String FILE_NAME = "minime.properties";
    private static final Logger log = Logger.getLogger(PropertyBag.class);

    static {
        loadProperties();
    }

    private static void loadProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                log.info(String.format("Loading property file from: %s", FILE_NAME));
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(FILE_NAME));
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    /**
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key, null);
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty(key, defaultValue);
    }

    /**
     * @param key
     * @return
     */
    public static int getIntProperty(String key) {
        if (properties == null) {
            loadProperties();
        }
        return Integer.parseInt(properties.getProperty(key, null));
    }

    /**
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getIntProperty(String key, Integer defaultValue) {
        if (properties == null) {
            loadProperties();
        }
        String defaultValueString = defaultValue == null ? null : defaultValue.toString();
        String value = properties.getProperty(key, defaultValueString);
        return Integer.parseInt(value);
    }

}
