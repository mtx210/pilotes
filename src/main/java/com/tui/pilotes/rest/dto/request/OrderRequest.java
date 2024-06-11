package com.tui.pilotes.rest.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@RequiredArgsConstructor
@Getter
@Setter
public class OrderRequest {

    @NotNull @Range(min = 5, max = 15)
    private Integer pilotesAmount;
    @NotNull
    private String customerFirstName;
    @NotNull
    private String customerLastName;
    @NotNull
    private String customerPhoneNumber;
    @NotNull
    private String deliveryAddressStreet;
    @NotNull
    private String deliveryAddressBuilding;
    private String deliveryAddressApartment;
}