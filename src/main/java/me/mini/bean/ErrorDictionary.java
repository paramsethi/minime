package me.mini.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "error")
public class ErrorDictionary {

    private int errorCode;

    private String errorMessage;

    private interface ErrorCode {
        static final int GENERIC_ERROR_CODE = -1;
        static final int MISSING_URL_CODE = -2;
        static final int EXISTING_CUSTOM_ALIAS_CODE = -3;
        static final int URL_ALREADY_EXISTS_CODE = -4;
        static final int MISSING_REQUIRED_PARAMETER_CODE = -5;
        static final int UNSUPPORTED_RESPONSE_FORMAT_CODE = -6;
        static final int UNSUPPORTED_OPERATION_CODE = -7;
        static final int INVALID_URL_CODE = -8;
    }

    private interface ErrorMessage {
        static final String GENERIC_ERROR_MSG = "An application error has occured.";
        static final String MISSING_URL_MSG = "URL does not exist.";
        static final String EXISTING_CUSTOM_ALIAS_MSG = "Custom alias already exists. Please provide a different alias.";
        static final String URL_ALREADY_EXISTS_MSG = "URL already shortened. Remove custom alias parameter to see the existing url.";
        static final String MISSING_REQUIRED_PARAMETER_MSG = "Missing required parameter. The correct format of API call is - For shortening: http://mini.me/min?origurl=<LONG_URL>. For getting original: http://mini.me/unmin?shorturl=http://mini.me/<url_hash>.";
        static final String UNSUPPORTED_RESPONSE_FORMAT_MSG = "Response format currently not supported. Please use xml as response format.";
        static final String UNSUPPORTED_OPERATION_MSG = "Response operation currently not supported. Supported operations:  min, unmin, error, stats";
        static final String INVALID_URL_MSG = "Invalid url.";
    }

    public ErrorDictionary(){	
    }
    
    public ErrorDictionary(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static final ErrorDictionary GENERIC_ERROR = new ErrorDictionary(ErrorCode.GENERIC_ERROR_CODE, ErrorMessage.GENERIC_ERROR_MSG);
    public static final ErrorDictionary MISSING_URL_ERROR = new ErrorDictionary(ErrorCode.MISSING_URL_CODE, ErrorMessage.MISSING_URL_MSG);
    public static final ErrorDictionary EXISTING_CUSTOM_ALIAS_ERROR = new ErrorDictionary(ErrorCode.EXISTING_CUSTOM_ALIAS_CODE, ErrorMessage.EXISTING_CUSTOM_ALIAS_MSG);
    public static final ErrorDictionary URL_ALREADY_EXISTS_ERROR = new ErrorDictionary(ErrorCode.URL_ALREADY_EXISTS_CODE, ErrorMessage.URL_ALREADY_EXISTS_MSG);
    public static final ErrorDictionary MISSING_REQUIRED_PARAMETER_ERROR = new ErrorDictionary(ErrorCode.MISSING_REQUIRED_PARAMETER_CODE, ErrorMessage.MISSING_REQUIRED_PARAMETER_MSG);
    public static final ErrorDictionary UNSUPPORTED_OPERATION_ERROR = new ErrorDictionary(ErrorCode.UNSUPPORTED_OPERATION_CODE, ErrorMessage.UNSUPPORTED_OPERATION_MSG);
    public static final ErrorDictionary UNSUPPORTED_RESPONSE_FORMAT_ERROR = new ErrorDictionary(ErrorCode.UNSUPPORTED_RESPONSE_FORMAT_CODE, ErrorMessage.UNSUPPORTED_RESPONSE_FORMAT_MSG);
    public static final ErrorDictionary INVALID_URL_ERROR = new ErrorDictionary(ErrorCode.INVALID_URL_CODE, ErrorMessage.INVALID_URL_MSG);

    @XmlElement(name = "errorCode")
    public int getErrorCode() {
        return errorCode;
    }

    @XmlElement(name = "errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }
}
