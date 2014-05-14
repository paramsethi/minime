package me.mini.utils;

public class MiniMeException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;

	public MiniMeException() {
		super();
	}

	public MiniMeException(String message) {
		super(message);
		this.message = message;
	}

	public MiniMeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return message;
	}
}
