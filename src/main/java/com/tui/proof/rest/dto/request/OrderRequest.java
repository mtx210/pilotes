package com.tui.proof.rest.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class OrderRequest {

    private Integer pilotesAmount;
    private String customerFirstName;
    private String customerLastName;
    private String customerPhoneNumber;
    private String deliveryAddressStreet;
    private String deliveryAddressBuilding;
    private String deliveryAddressApartment;
}