package com.retail.retailapp.exception;

public class NoApprovalQueueRecordFoundException extends Exception {
    public NoApprovalQueueRecordFoundException(String errorMessage) {
        super(errorMessage);
    }
}