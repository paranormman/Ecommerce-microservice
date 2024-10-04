package com.vestaChrono.ecommerce.inventory_service.service.impl;

import com.vestaChrono.ecommerce.inventory_service.dto.OrderRequestDto;
import com.vestaChrono.ecommerce.inventory_service.dto.OrderRequestItemDto;
import com.vestaChrono.ecommerce.inventory_service.dto.ProductDto;
import com.vestaChrono.ecommerce.inventory_service.entity.Product;
import com.vestaChrono.ecommerce.inventory_service.repository.ProductRepository;
import com.vestaChrono.ecommerce.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    public List<ProductDto> getAllInventory() {
        log.info("Fetching all inventory items");
        List<Product> inventories = productRepository.findAll();
        return inventories.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    @Override
    public ProductDto getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Optional<Product> inventory = productRepository.findById(id);
        return inventory.map(item -> modelMapper.map(item, ProductDto.class))
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    @Override
    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto) {
        log.info("Reducing the stocks");
        Double totalPrice = 0.0;
        for (OrderRequestItemDto orderRequestItemDto: orderRequestDto.getItems()) {
            Long productId = orderRequestItemDto.getProductId();
            Integer quantity = orderRequestItemDto.getQuantity();

//            check if the product exists
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found with Id: " + productId));
//            check if the quantity of ordered product is in the stock
            if (product.getStock() < quantity) {
                throw new RuntimeException("Product cannot be fulfilled for given quantity");
            }
//            reduce the stock
            product.setStock(product.getStock() - quantity);
//            save the product in the repository
            productRepository.save(product);
//            calculate the totalPrice for the quantity of products
            totalPrice += quantity * product.getPrice();
        }
        return totalPrice;
    }
}
