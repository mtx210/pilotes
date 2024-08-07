package com.tui.pilotes.rest.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateOrderResponse {

    private final String message = "order created!";
    private final Long orderId;
}