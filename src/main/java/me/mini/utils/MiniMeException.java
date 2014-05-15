package me.mini.utils;

import me.mini.bean.ErrorDictionary;

public class MinimeException extends Exception {

    private static final long serialVersionUID = 1L;
    private String message;

    public MinimeException() {
        super();
    }

    public MinimeException(String message) {
        super(message);
    }

    public MinimeException(Throwable cause) {
        super(cause);
    }

    public MinimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MinimeException(ErrorDictionary error) {
        this(error.getErrorMessage());
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
