package com.tui.pilotes.rest.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiErrorResponse { //TODO

    private final String errorReason;
}