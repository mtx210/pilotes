package com.tui.pilotes.rest.controller;

import com.tui.pilotes.data.entity.Order;
import com.tui.pilotes.rest.dto.request.OrderRequest;
import com.tui.pilotes.rest.dto.response.CreateOrderResponse;
import com.tui.pilotes.rest.dto.response.UpdateOrderResponse;
import com.tui.pilotes.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(
            @RequestBody @Valid OrderRequest orderRequest
    ) {
        return new ResponseEntity<>(
                orderService.createOrder(orderRequest),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<UpdateOrderResponse> updateOrder(
            @PathVariable @NotNull Long orderId,
            @RequestBody OrderRequest orderRequest
    ) {
        return new ResponseEntity<>(
                orderService.updateOrder(orderId, orderRequest),
                HttpStatus.OK
        );
    }

    @GetMapping
    public List<Order> getOrders(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false) String phone
    ) {
        return orderService.getOrders(name, surname, phone);
    }
}