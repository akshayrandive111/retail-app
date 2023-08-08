package com.retail.retailapp.exception;

public class NoProductFoundException extends Exception {

    public NoProductFoundException(String errorMessage) {
        super(errorMessage);
    }
}
