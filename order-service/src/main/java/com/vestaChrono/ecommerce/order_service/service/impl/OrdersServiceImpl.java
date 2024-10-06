package com.vestaChrono.ecommerce.order_service.service.impl;

import com.vestaChrono.ecommerce.order_service.clients.InventoryFeignClient;
import com.vestaChrono.ecommerce.order_service.dto.OrderRequestDto;
import com.vestaChrono.ecommerce.order_service.entity.OrderItem;
import com.vestaChrono.ecommerce.order_service.entity.OrderStatus;
import com.vestaChrono.ecommerce.order_service.entity.Orders;
import com.vestaChrono.ecommerce.order_service.repository.OrdersRepository;
import com.vestaChrono.ecommerce.order_service.service.OrdersService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final InventoryFeignClient inventoryFeignClient;


    @Override
    public List<OrderRequestDto> getAllOrders() {
        log.info("Fetching all orders");
        List<Orders> orders = ordersRepository.findAll();
        return orders.stream().map(order -> modelMapper.map(order, OrderRequestDto.class)).toList();
    }

    @Override
    public OrderRequestDto getOrderById(Long id) {
        log.info("Fetching order with id: {}", id);
        Orders orders = ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        return modelMapper.map(orders, OrderRequestDto.class);
    }

    @Override
//    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
//    @RateLimiter(name = "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Calling the createOrder method");
        Double totalPrice = inventoryFeignClient.reduceStocks(orderRequestDto);

//        Create the order and save in the orderDB, map to entity
        Orders orders = modelMapper.map(orderRequestDto, Orders.class);
//        set the same orderID to multiple orderItems
        for (OrderItem orderItem: orders.getItems()) {
            orderItem.setOrder(orders);
        }
//        set the order details
        orders.setTotalPrice(totalPrice);
        orders.setOrderStatus(OrderStatus.CONFIRMED);

//        save the orders
        Orders savedOrder = ordersRepository.save(orders);
        return modelMapper.map(savedOrder, OrderRequestDto.class);
    }

    @Override
    public void deleteOrder(Long orderId) {

        log.info("Deleting the order with Id {}", orderId);
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with Id: "+ orderId));

//        creating DTO to restore stock
        OrderRequestDto orderRequestDto = modelMapper.map(order, OrderRequestDto.class);
//        calling the inventory service to restore the stock
        inventoryFeignClient.restoreStocks(orderRequestDto);
//        delete the order from the repository
        ordersRepository.delete(order);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable){
        log.info("Fallback occurred due to : {}", throwable.getMessage());
        return new OrderRequestDto();
    }
}
