package com.tui.proof.rest.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class CustomerSearchRequest {

    private String customerFirstName;
    private String customerLastName;
    private String customerPhoneNumber;
}