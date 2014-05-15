package me.mini.utils;

import org.apache.commons.validator.routines.UrlValidator;

public class GlobalUtils {

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
		UrlValidator urlValidator = new UrlValidator();
		return urlValidator.isValid(url);
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
