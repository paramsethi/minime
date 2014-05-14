package me.mini.utils;

/**
 * Application constants
 * 
 * @author parampreetsethi
 * 
 */
public interface AppConstants {

	String LOCALHOST = "localhost";

	String COLUMN_FAMILY_NAME = "url_mapping";

	char[] HASH_CHARS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'x',
			'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	int HASH_SIZE = 6;

	String SHORT_URL_REGEX = "";
	String URL_PROTOCOL = "http";
	String URL_PROTOCOL_SEPARATOR = "://";

	String ERROR_URL = "exception";

	String GENERIC_ERROR = "An application error has occured.";
	String MISSING_URL = "URL does not exist.";
	String EXISTING_CUSTOM_ALIAS = "Custom alias already exists. Please provide a different alias.";
	String URL_ALREADY_EXISTS = "URL already shortened. Remove custom alias parameter to see the existing url.";
	String MISSING_REQUIRED_PARAMETER = "Missing required parameter. The correct format of API call is - For shortening: http://mini.me/min?origurl=<LONG_URL>. For getting original: http://mini.me/unmin?shorturl=http://mini.me/<url_hash>.";
	String UNSUPPORTED_RESPONSE_FORMAT = "Response format currently not supported. Please use xml as response format.";
	String INVALID_URL = "Invalid url.";

	int GENERIC_ERROR_CODE = -1;
	int MISSING_URL_CODE = -2;
	int EXISTING_CUSTOM_ALIAS_CODE = -3;
	int URL_ALREADY_EXISTS_CODE = -4;
	int MISSING_REQUIRED_PARAMETER_CODE = -5;
	int UNSUPPORTED_RESPONSE_FORMAT_CODE = -6;
	int INVALID_URL_CODE = -7;

}
