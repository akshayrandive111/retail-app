package com.retail.retailapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({NoProductFoundException.class})
    public ResponseEntity<Object> handleNoProductFoundException(NoProductFoundException ex) {
        String error = "Provided product id not exist , please recheck product id.";
        ApiError apiError =
                new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ProductPriceExhaustThresholdException.class})
    public ResponseEntity<Object> handleProductPriceExhaustException(ProductPriceExhaustThresholdException ex) {
        String error = "Provided product price exhaust threshold limit";
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({NoApprovalQueueRecordFoundException.class})
    public ResponseEntity<Object> handleNoApprovalQueueRecordFoundException(NoApprovalQueueRecordFoundException ex) {
        String error = "No record found with provided approval id";
        ApiError apiError =
                new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}