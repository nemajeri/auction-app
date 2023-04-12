package com.atlantbh.auctionappbackend.exception;

public class DuplicateAppUserException extends RuntimeException {

    public DuplicateAppUserException()
    {
        super("Cannot register or login user because user already exists");
    }
}
