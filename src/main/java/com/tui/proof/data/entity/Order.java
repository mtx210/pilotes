package com.tui.proof.data.entity;

import com.tui.proof.model.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private OrderType orderContent;
    private BigDecimal orderTotal;
    private LocalDateTime createDate;
    private String customerFirstName;
    private String customerLastName;
    private String customerPhoneNumber;
    private String deliveryAddressStreet;
    private String deliveryAddressBuilding;
    private String deliveryAddressApartment;
}