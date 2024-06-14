package com.tui.pilotes.service;

import com.tui.pilotes.data.entity.Order;
import com.tui.pilotes.data.repository.OrderRepository;
import com.tui.pilotes.exception.OrderUpdateTimedOutException;
import com.tui.pilotes.rest.dto.request.OrderRequest;
import com.tui.pilotes.rest.dto.response.CreateOrderResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Spy
    private OrderRepository orderRepository;

    @Test
    void createOrder_shouldThrowException_whenInvalidPilotesAmount() {
        // given
        OrderRequest invalidRequest = OrderRequest.builder()
                .pilotesAmount(3)
                .build();

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(invalidRequest));
    }

    @Test
    void createOrder_shouldSucceed() {
        // given
        ReflectionTestUtils.setField(orderService, "pilotesPrice", 1.33);
        LocalDateTime dateTime = LocalDateTime.now();
        OrderRequest validRequest = OrderRequest.builder()
                .pilotesAmount(10)
                .customerFirstName("john")
                .customerLastName("koval")
                .customerPhoneNumber("666888222")
                .deliveryAddressStreet("sunny")
                .deliveryAddressBuilding("12")
                .deliveryAddressApartment("2")
                .build();

        Order entity = Order.builder()
                .orderId(1L)
                .orderContent("10 PILOTES")
                .orderTotal(BigDecimal.valueOf(13.30))
                .createDate(dateTime)
                .customerFirstName("john")
                .customerLastName("koval")
                .customerPhoneNumber("666888222")
                .deliveryAddressStreet("sunny")
                .deliveryAddressBuilding("12")
                .deliveryAddressApartment("2")
                .build();

        Mockito.when(orderRepository.save(any()))
                .thenReturn(entity);

        // when
        CreateOrderResponse response = orderService.createOrder(validRequest);

        // then
        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.verify(orderRepository).save(argumentCaptor.capture());
        Order value = argumentCaptor.getValue();

        assertEquals(1L, response.getOrderId());
        assertEquals("10 PILOTES", value.getOrderContent());
        assertEquals(BigDecimal.valueOf(13.30).setScale(2, RoundingMode.CEILING), value.getOrderTotal());
        assertEquals("john", value.getCustomerFirstName());
        assertEquals("koval", value.getCustomerLastName());
        assertEquals("666888222", value.getCustomerPhoneNumber());
        assertEquals("sunny", value.getDeliveryAddressStreet());
        assertEquals("12", value.getDeliveryAddressBuilding());
        assertEquals("2", value.getDeliveryAddressApartment());
    }

    @Test
    void updateOrder_shouldThrowException_whenOutOfTimeOrderUpdate() {
        // given
        Long orderId = 111L;
        OrderRequest request = OrderRequest.builder()
                .deliveryAddressApartment("2")
                .build();
        Order entity = Order.builder()
                .orderId(orderId)
                .orderContent("10 PILOTES")
                .orderTotal(BigDecimal.valueOf(13.30))
                .createDate(LocalDateTime.now().minusMinutes(10L))
                .customerFirstName("john")
                .customerLastName("koval")
                .customerPhoneNumber("666888222")
                .deliveryAddressStreet("sunny")
                .deliveryAddressBuilding("12")
                .deliveryAddressApartment("2")
                .build();

        Mockito.when(orderRepository.findById(eq(orderId)))
                .thenReturn(Optional.of(entity));

        // when
        // then
        assertThrows(
                OrderUpdateTimedOutException.class,
                () -> orderService.updateOrder(orderId, request)
        );
    }

    @Test
    void updateOrder_shouldSucceed() {
        // given
        ReflectionTestUtils.setField(orderService, "updateTimeThresholdMinutes", 5);
        Long orderId = 111L;
        OrderRequest request = OrderRequest.builder()
                .deliveryAddressApartment("2")
                .build();
        Order entity = Order.builder()
                .orderId(orderId)
                .orderContent("10 PILOTES")
                .orderTotal(BigDecimal.valueOf(13.30))
                .createDate(LocalDateTime.now().minusMinutes(1L))
                .customerFirstName("john")
                .customerLastName("koval")
                .customerPhoneNumber("666888222")
                .deliveryAddressStreet("sunny")
                .deliveryAddressBuilding("12")
                .deliveryAddressApartment("2")
                .build();

        Mockito.when(orderRepository.findById(eq(orderId)))
                .thenReturn(Optional.of(entity));

        // when
        // then
        assertDoesNotThrow(() -> orderService.updateOrder(orderId, request));
    }

    @Test
    void getOrders_shouldThrowException_whenInvalidArguments() {
        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrders("", "", ""));
    }

    @Test
    void getOrders_shouldSucceed() {
        // given
        Mockito.when(orderRepository.findAll(any(Example.class)))
                .thenReturn(Collections.emptyList());

        // when
        assertDoesNotThrow(() -> orderService.getOrders("john", "", ""));
    }
}