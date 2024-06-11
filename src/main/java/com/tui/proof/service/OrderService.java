package com.tui.proof.service;

import com.tui.proof.data.entity.Order;
import com.tui.proof.data.repository.OrderRepository;
import com.tui.proof.exception.OrderUpdateTimedOutException;
import com.tui.proof.model.OrderType;
import com.tui.proof.rest.dto.request.CustomerSearchRequest;
import com.tui.proof.rest.dto.request.OrderRequest;
import com.tui.proof.rest.dto.response.CreateOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final Map<Integer, OrderType> ALLOWED_PILOTES_AMOUNT_TYPE_MAP = Map.of(
            5, OrderType.PILOTES_5,
            10, OrderType.PILOTES_10,
            15, OrderType.PILOTES_15
    );
    @Value("${order.single-pilotes-price}")
    private double pilotesPrice;

    public CreateOrderResponse createOrder(OrderRequest orderRequest) {
        validateRequest(orderRequest);

        Order createdOrder = orderRepository.save(mapRequestToEntity(orderRequest));
        return new CreateOrderResponse(createdOrder.getOrderId());
    }

    public Order updateOrder(Long orderId, OrderRequest orderRequest) {
        validateRequest(orderRequest);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found!"));

        if (!canUpdateOrder(order.getCreateDate())) {
            throw new OrderUpdateTimedOutException();
        }

        updateEntity(order, orderRequest);
        return orderRepository.save(order);
    }

    public List<Order> getOrders(CustomerSearchRequest customerSearchRequest) {
        validateRequest(customerSearchRequest);

        return orderRepository.findAll(Example.of(Order.builder()
                .customerFirstName(customerSearchRequest.getCustomerFirstName())
                .customerLastName(customerSearchRequest.getCustomerLastName())
                .customerPhoneNumber(customerSearchRequest.getCustomerPhoneNumber())
                .build())
        );
    }

    private void validateRequest(OrderRequest orderRequest) {
        if (!ALLOWED_PILOTES_AMOUNT_TYPE_MAP.containsKey(orderRequest.getPilotesAmount())) {
            throw new IllegalArgumentException("Invalid pilotes amount");
        }
    }

    private void validateRequest(CustomerSearchRequest customerSearchRequest) {
        if (StringUtils.isBlank(customerSearchRequest.getCustomerFirstName()) &&
                StringUtils.isBlank(customerSearchRequest.getCustomerFirstName()) &&
                StringUtils.isBlank(customerSearchRequest.getCustomerFirstName())) {
            throw new IllegalArgumentException("No search arguments were provided");
        }
    }

    private Order mapRequestToEntity(OrderRequest request) {
        int pilotesAmount = request.getPilotesAmount();

        return Order.builder()
                .orderContent(ALLOWED_PILOTES_AMOUNT_TYPE_MAP.get(pilotesAmount))
                .orderTotal(getOrderTotal(pilotesAmount))
                .createDate(LocalDateTime.now())
                .customerFirstName(request.getCustomerFirstName())
                .customerLastName(request.getCustomerLastName())
                .customerPhoneNumber(request.getCustomerPhoneNumber())
                .deliveryAddressStreet(request.getDeliveryAddressStreet())
                .deliveryAddressBuilding(request.getDeliveryAddressBuilding())
                .deliveryAddressApartment(request.getDeliveryAddressApartment())
                .build();
    }

    private boolean canUpdateOrder(LocalDateTime orderCreateDate) {
        return orderCreateDate.plusMinutes(5).isAfter(LocalDateTime.now());
    }

    private BigDecimal getOrderTotal(int pilotesAmount) {
        return BigDecimal.valueOf(pilotesPrice).multiply(BigDecimal.valueOf(pilotesAmount));
    }

    private void updateEntity(Order entity, OrderRequest request) {
        if (request.getPilotesAmount() != null) {
            entity.setOrderContent(ALLOWED_PILOTES_AMOUNT_TYPE_MAP.get(request.getPilotesAmount()));
            entity.setOrderTotal(getOrderTotal(request.getPilotesAmount()));
        }
        if (request.getCustomerFirstName() != null) {
            entity.setCustomerFirstName(request.getCustomerFirstName());
        }
        if (request.getCustomerLastName() != null) {
            entity.setCustomerLastName(request.getCustomerLastName());
        }
        if (request.getCustomerPhoneNumber() != null) {
            entity.setCustomerPhoneNumber(request.getCustomerPhoneNumber());
        }
        if (request.getDeliveryAddressStreet() != null) {
            entity.setDeliveryAddressStreet(request.getDeliveryAddressStreet());
        }
        if (request.getDeliveryAddressBuilding() != null) {
            entity.setDeliveryAddressBuilding(request.getDeliveryAddressBuilding());
        }
        if (request.getDeliveryAddressApartment() != null) {
            entity.setDeliveryAddressApartment(request.getDeliveryAddressApartment());
        }
    }
}