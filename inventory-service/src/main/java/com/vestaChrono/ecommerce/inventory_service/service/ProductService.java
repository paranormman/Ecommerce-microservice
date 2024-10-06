package com.vestaChrono.ecommerce.inventory_service.service;

import com.vestaChrono.ecommerce.inventory_service.dto.OrderRequestDto;
import com.vestaChrono.ecommerce.inventory_service.dto.ProductDto;

import java.util.List;

public interface ProductService {

    List<ProductDto> getAllInventory();

    ProductDto getProductById(Long id);

    Double reduceStocks(OrderRequestDto orderRequestDto);

    void restoreStocks(OrderRequestDto orderRequestDto);
}
