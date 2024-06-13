package com.tui.pilotes.rest.dto.request;

import com.tui.pilotes.rest.SpecificIntegerValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@RequiredArgsConstructor
@Getter
@Setter
public class OrderRequest {

    @NotNull
    @SpecificIntegerValue(value = {5, 10, 15}, message = "invalid pilotes amount, accepted values: 5, 10, 15")
    private Integer pilotesAmount;
    @NotNull
    @Pattern(regexp = "^\\p{L}+$", message = "first name must contain letters only")
    private String customerFirstName;
    @NotNull
    @Pattern(regexp = "^\\p{L}+$", message = "last name must contain letters only")
    private String customerLastName;
    @NotNull
    @Length(min = 9, max = 12, message = "invalid phone number")
    private String customerPhoneNumber;
    @NotNull
    @Length(min = 1, max = 50, message = "invalid address street")
    private String deliveryAddressStreet;
    @NotNull
    @Length(min = 1, max = 10, message = "invalid address building")
    private String deliveryAddressBuilding;
    private String deliveryAddressApartment;
}