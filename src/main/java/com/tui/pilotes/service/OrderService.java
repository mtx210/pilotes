package com.tui.pilotes.service;

import com.tui.pilotes.data.entity.Order;
import com.tui.pilotes.data.repository.OrderRepository;
import com.tui.pilotes.exception.OrderUpdateTimedOutException;
import com.tui.pilotes.model.OrderType;
import com.tui.pilotes.rest.dto.request.OrderRequest;
import com.tui.pilotes.rest.dto.response.CreateOrderResponse;
import com.tui.pilotes.rest.dto.response.UpdateOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
    @Value("${order.single-pilotes-price}")
    private double pilotesPrice;
    @Value("${order.update-time-threshold-minutes}")
    private int updateTimeThresholdMinutes;
    private static final Map<Integer, OrderType> ALLOWED_PILOTES_AMOUNT_TYPE_MAP = Map.of(
            5, OrderType.PILOTES_5,
            10, OrderType.PILOTES_10,
            15, OrderType.PILOTES_15
    );
    private static final ExampleMatcher SEARCH_MATCHER = ExampleMatcher.matchingAll()
            .withMatcher("customerFirstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher("customerLastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher("customerPhoneNumber", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

    public CreateOrderResponse createOrder(OrderRequest orderRequest) {
        validateRequest(orderRequest);

        Order createdOrder = orderRepository.save(mapRequestToEntity(orderRequest));
        return new CreateOrderResponse(createdOrder.getOrderId());
    }

    public UpdateOrderResponse updateOrder(Long orderId, OrderRequest orderRequest) {
        validateRequest(orderRequest);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found!"));

        if (!canUpdateOrder(order.getCreateDate())) {
            throw new OrderUpdateTimedOutException();
        }

        updateEntity(order, orderRequest);
        orderRepository.save(order);
        return new UpdateOrderResponse();
    }

    public List<Order> getOrders(String name, String surname, String phone) {
        validateRequest(name, surname, phone);

        return orderRepository.findAll(Example.of(Order.builder()
                .customerFirstName(name)
                .customerLastName(surname)
                .customerPhoneNumber(phone)
                .build(), SEARCH_MATCHER)
        );
    }

    private void validateRequest(OrderRequest orderRequest) {
        if (orderRequest.getPilotesAmount() != null && !ALLOWED_PILOTES_AMOUNT_TYPE_MAP.containsKey(orderRequest.getPilotesAmount())) {
            throw new IllegalArgumentException("invalid pilotes amount, accepted values: 5, 10, 15");
        }
    }

    private void validateRequest(String name, String surname, String phone) {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(surname) && StringUtils.isBlank(phone)) {
            throw new IllegalArgumentException("no search arguments were provided");
        }
    }

    private Order mapRequestToEntity(OrderRequest request) {
        int pilotesAmount = request.getPilotesAmount();

        return Order.builder()
                .orderContent(ALLOWED_PILOTES_AMOUNT_TYPE_MAP.get(pilotesAmount).getName())
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
        return orderCreateDate.plusMinutes(updateTimeThresholdMinutes).isAfter(LocalDateTime.now());
    }

    private BigDecimal getOrderTotal(int pilotesAmount) {
        return BigDecimal.valueOf(pilotesPrice).multiply(BigDecimal.valueOf(pilotesAmount));
    }

    private void updateEntity(Order entity, OrderRequest request) {
        if (request.getPilotesAmount() != null) {
            entity.setOrderContent(ALLOWED_PILOTES_AMOUNT_TYPE_MAP.get(request.getPilotesAmount()).getName());
            entity.setOrderTotal(getOrderTotal(request.getPilotesAmount()));
        }
        if (StringUtils.isNotBlank(request.getCustomerFirstName())) {
            entity.setCustomerFirstName(request.getCustomerFirstName());
        }
        if (StringUtils.isNotBlank(request.getCustomerLastName())) {
            entity.setCustomerLastName(request.getCustomerLastName());
        }
        if (StringUtils.isNotBlank(request.getCustomerPhoneNumber())) {
            entity.setCustomerPhoneNumber(request.getCustomerPhoneNumber());
        }
        if (StringUtils.isNotBlank(request.getDeliveryAddressStreet())) {
            entity.setDeliveryAddressStreet(request.getDeliveryAddressStreet());
        }
        if (StringUtils.isNotBlank(request.getDeliveryAddressBuilding())) {
            entity.setDeliveryAddressBuilding(request.getDeliveryAddressBuilding());
        }
        if (StringUtils.isNotBlank(request.getDeliveryAddressApartment())) {
            entity.setDeliveryAddressApartment(request.getDeliveryAddressApartment());
        }
    }
}