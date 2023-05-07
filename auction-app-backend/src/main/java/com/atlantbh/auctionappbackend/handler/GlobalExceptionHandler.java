package com.atlantbh.auctionappbackend.handler;

import com.atlantbh.auctionappbackend.exception.*;
import com.atlantbh.auctionappbackend.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AppUserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAppUserNotFoundException(AppUserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("USER_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateAppUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateAppUserException(DuplicateAppUserException ex) {
        ErrorResponse errorResponse = new ErrorResponse("USER_ALREADY_EXISTS", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse errorResponse = new ErrorResponse("INVALID_CREDENTIALS", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(BidAmountException.class)
    public ResponseEntity<ErrorResponse> handleWrongBidAmount(BidAmountException ex) {
        ErrorResponse errorResponse = new ErrorResponse("WRONG_BID_AMOUNT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("PRODUCT_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(ImageIndexNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleImageIndexNotFoundException(ImageIndexNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("IMAGE_INDEX_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

