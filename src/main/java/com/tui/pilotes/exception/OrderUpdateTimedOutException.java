package com.tui.pilotes.exception;

public class OrderUpdateTimedOutException extends RuntimeException {

    public OrderUpdateTimedOutException() {
        super("Cannot update order after 5 minutes since creation passed");
    }
}