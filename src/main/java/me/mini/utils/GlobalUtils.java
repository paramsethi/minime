package me.mini.utils;

import org.apache.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;

public class GlobalUtils {

    private static final Logger log = Logger.getLogger(GlobalUtils.class);

    /**
     * @param data
     * @return
     */
    public static boolean isStringNullOrEmpty(String data) {
        return data == null || data.trim().length() <= 0;
    }

    /**
     * @param url
     * @return
     */
    public static boolean isValidUrl(String url) {
        try {
            URI uri = new URI(url);
            log.debug("Parsed URL " + uri);
            return true;
        } catch (URISyntaxException uex) {
            return false;
        }
    }

    /**
     * @param url
     * @return
     */
    public static boolean isValidShortUrl(String url) {
        return url != null && url.matches("^http://minime.com/[a-z0-9]$");
    }

    /**
     * Get url hash from the given url
     *
     * @param url
     * @return
     */
    public static String extractHashFromUrl(String url) {
        if (url.lastIndexOf("/") > 0 && url.lastIndexOf("/") + 1 < url.length()) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return url;
    }

}
