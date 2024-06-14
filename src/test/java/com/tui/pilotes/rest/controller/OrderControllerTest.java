package com.tui.pilotes.rest.controller;

import com.tui.pilotes.rest.dto.request.OrderRequest;
import com.tui.pilotes.rest.dto.response.CreateOrderResponse;
import com.tui.pilotes.rest.dto.response.UpdateOrderResponse;
import com.tui.pilotes.service.OrderService;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;
    @LocalServerPort
    private Integer port;
    private final String HOST = "http://localhost:";

    @ParameterizedTest
    @MethodSource("createOrder_contractTestArguments")
    void createOrder_contractTest(int expectedStatusCode, String expectedBody, OrderRequest orderRequest) {
        when(orderService.createOrder(any()))
                .thenReturn(new CreateOrderResponse(1L));

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(orderRequest)
                .post(buildUrl(ApiEndpoint.CREATE_ORDER))
                .then()
                .statusCode(expectedStatusCode)
                .body(containsString(expectedBody));
    }

    private static Stream<Arguments> createOrder_contractTestArguments() {
        return Stream.of(
                Arguments.of(
                        400, "invalid pilotes amount, accepted values: 5, 10, 15",
                        OrderRequest.builder().pilotesAmount(9).build()
                ),
                Arguments.of(
                        400, "invalid pilotes amount, accepted values: 5, 10, 15",
                        OrderRequest.builder().pilotesAmount(-3).build()
                ),
                Arguments.of(
                        400, "pilotesAmount: must not be null",
                        OrderRequest.builder().build()
                ),
                Arguments.of(
                        400, "first name must contain letters only",
                        OrderRequest.builder().customerFirstName("john1").build()
                ),
                Arguments.of(
                        400, "customerFirstName: must not be null",
                        OrderRequest.builder().build()
                ),
                Arguments.of(
                        400, "last name must contain letters only",
                        OrderRequest.builder().customerLastName("koval0").build()
                ),
                Arguments.of(
                        400, "customerLastName: must not be null",
                        OrderRequest.builder().build()
                ),
                Arguments.of(
                        400, "invalid phone number",
                        OrderRequest.builder().customerPhoneNumber("+48 111").build()
                ),
                Arguments.of(
                        400, "invalid phone number",
                        OrderRequest.builder().customerPhoneNumber("+48 111555888999").build()
                ),
                Arguments.of(
                        400, "customerPhoneNumber: must not be null",
                        OrderRequest.builder().build()
                ),
                Arguments.of(
                        400, "invalid address street",
                        OrderRequest.builder().deliveryAddressStreet("").build()
                ),
                Arguments.of(
                        400, "invalid address street",
                        OrderRequest.builder().deliveryAddressStreet("aabbccddeeaabbccddeeaabbccddeeaabbccddeeaabbccddeexx").build()
                ),
                Arguments.of(
                        400, "deliveryAddressStreet: must not be null",
                        OrderRequest.builder().build()
                ),
                Arguments.of(
                        400, "invalid address building",
                        OrderRequest.builder().deliveryAddressBuilding("").build()
                ),
                Arguments.of(
                        400, "invalid address building",
                        OrderRequest.builder().deliveryAddressBuilding("aabbccddeeaabbccddeeaabbccddeeaabbccddeeaabbccddeexx").build()
                ),
                Arguments.of(
                        400, "deliveryAddressBuilding: must not be null",
                        OrderRequest.builder().build()
                ),
                Arguments.of(
                        201, "order created!\",\"orderId\":1",
                        OrderRequest.builder()
                                .pilotesAmount(5)
                                .customerFirstName("john")
                                .customerLastName("koval")
                                .customerPhoneNumber("+48666555444")
                                .deliveryAddressStreet("sunny")
                                .deliveryAddressBuilding("11")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("updateOrder_contractTestArguments")
    void updateOrder_contractTest(int expectedStatusCode, String expectedBody, Long orderId, OrderRequest orderRequest) {
        when(orderService.updateOrder(any(), any()))
                .thenReturn(new UpdateOrderResponse());

        given()
                .contentType(ContentType.JSON)
                .when()
                .body(orderRequest)
                .pathParams("orderId", orderId)
                .patch(buildUrl(ApiEndpoint.UPDATE_ORDER))
                .then()
                .statusCode(expectedStatusCode)
                .body(containsString(expectedBody));
    }

    private static Stream<Arguments> updateOrder_contractTestArguments() {
        return Stream.of(
                Arguments.of(200, "order updated!", 1L,
                        OrderRequest.builder()
                                .pilotesAmount(10)
                                .deliveryAddressApartment("3")
                                .build()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("getOrders_contractTestArguments")
    void getOrders_contractTest(int expectedStatusCode, String expectedBody, Header header,
                                String name, String surname, String phone
    ) {
        Map<String, String> queryParams = Map.of(
                "name", name,
                "surname", surname,
                "phone", phone
        );

        when(orderService.getOrders(any(), any(), any()))
                .thenReturn(Collections.emptyList());

        given()
                .contentType(ContentType.JSON)
                .when()
                .queryParams(queryParams)
                .header(header)
                .get(buildUrl(ApiEndpoint.GET_ORDERS))
                .then()
                .statusCode(expectedStatusCode)
                .body(containsString(expectedBody));
    }

    private static Stream<Arguments> getOrders_contractTestArguments() {
        Header authHeader = new Header("X-API-KEY", "ultrasecret");

        return Stream.of(
                Arguments.of(401, "", new Header("X-API-KEY", ""), "john", "", ""),
                Arguments.of(200, "", authHeader, "john", "", ""),
                Arguments.of(200, "", authHeader, "", "koval", ""),
                Arguments.of(200, "", authHeader, "", "", "111222333"),
                Arguments.of(200, "", authHeader, "john", "koval", "111222333")
        );
    }

    @RequiredArgsConstructor
    @Getter
    private enum ApiEndpoint {
        CREATE_ORDER("/api/order"),
        UPDATE_ORDER("/api/order/{orderId}"),
        GET_ORDERS("/api/order");

        private final String path;
    }

    private String buildUrl(ApiEndpoint apiEndpoint) {
        return HOST + port + apiEndpoint.getPath();
    }
}