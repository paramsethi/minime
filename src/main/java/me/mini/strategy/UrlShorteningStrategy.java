package me.mini.strategy;

import me.mini.utils.AppConstants;

/**
 * URL shortening strategy
 * 
 * @author parampreetsethi
 *
 */
public interface UrlShorteningStrategy extends AppConstants {

	public String getUrlHash();

}
