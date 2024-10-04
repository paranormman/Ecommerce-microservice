package com.vestaChrono.ecommerce.order_service.service;

import com.vestaChrono.ecommerce.order_service.dto.OrderRequestDto;

import java.util.List;

public interface OrdersService {

    List<OrderRequestDto> getAllOrders();

    OrderRequestDto getOrderById(Long id);

    OrderRequestDto createOrder(OrderRequestDto orderRequestDto);
}
