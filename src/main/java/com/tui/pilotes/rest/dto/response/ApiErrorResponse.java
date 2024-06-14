package com.tui.pilotes.rest.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ApiErrorResponse {

    private final String errorReason;
}