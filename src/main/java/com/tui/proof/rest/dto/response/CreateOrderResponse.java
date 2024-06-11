package com.tui.proof.rest.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateOrderResponse {

    private final String message = "Order created!";
    private final Long orderId;
}