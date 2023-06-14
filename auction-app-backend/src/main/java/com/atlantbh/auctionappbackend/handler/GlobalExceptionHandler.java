package com.atlantbh.auctionappbackend.handler;

import com.atlantbh.auctionappbackend.exception.*;
import com.atlantbh.auctionappbackend.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.FileNotFoundException;

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

    @ExceptionHandler(FileDeletionException .class)
    public ResponseEntity<ErrorResponse> handleFileDeletionException(FileDeletionException  ex) {
        ErrorResponse errorResponse = new ErrorResponse("FILE_DELETION_FAILED", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFileNotFoundException(FileNotFoundException  ex) {
        ErrorResponse errorResponse = new ErrorResponse("FILE_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(UnprocessableCSVFileException.class)
    public ResponseEntity<ErrorResponse> handleUnprocessableCSVFileException(UnprocessableCSVFileException ex) {
        String[] errorMessage = ex.getMessage().split("\\.", 2);
        ErrorResponse errorResponse = new ErrorResponse("CANT_READ_CSV_FILE", errorMessage[0]);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    @ExceptionHandler(SubcategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> SubcategoryNotFoundException(SubcategoryNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("SUBCATEGORY_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> CategoryNotFoundException(CategoryNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("CATEGORY_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

