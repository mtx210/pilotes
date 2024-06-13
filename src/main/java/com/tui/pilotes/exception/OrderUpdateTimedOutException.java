package com.tui.pilotes.exception;

public class OrderUpdateTimedOutException extends RuntimeException {

    public OrderUpdateTimedOutException() {
        super("cannot update order after 5 minutes since creation passed");
    }
}