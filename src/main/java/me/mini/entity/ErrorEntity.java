package me.mini.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import me.mini.utils.AppConstants;
import me.mini.utils.Utils;

/**
 * Error Entity for error response
 * 
 * @author parampreetsethi
 * 
 */
@XmlRootElement(name="error")
public class ErrorEntity {

	@XmlElement(name = "state")
	private String state = "error";

	public ErrorEntity() {
		this(AppConstants.GENERIC_ERROR);
	}

	public ErrorEntity(String errorMessage) {
		this(errorMessage, AppConstants.GENERIC_ERROR_CODE);
	}

	public ErrorEntity(String errorMessage, int errorCode) {
		this.setErrorMessage(errorMessage);
		this.errorCode = errorCode;
	}

	private int errorCode;
	private String errorMessage;

	/**
	 * @return the errorMessage
	 */
	@XmlElement(name = "errorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		if (Utils.isStringNullOrEmpty(errorMessage)) {
			errorMessage = AppConstants.GENERIC_ERROR;
		}
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the errorCode
	 */
	@XmlElement(name = "errorCode")
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
