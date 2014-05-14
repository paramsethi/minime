package me.mini.utils;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

public class Utils {

	private static final Logger log = Logger.getLogger(Utils.class);

	/**
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isValidShortUrl(String url) {
		return url != null && url.matches("^http://minime.com/[a-z0-9]$");
	}

	/**
	 * 
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
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isStringNullOrEmpty(String data) {
		return data == null || data.trim().length() <= 0;
	}

}
