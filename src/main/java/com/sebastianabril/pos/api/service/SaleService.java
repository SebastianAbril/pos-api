package com.sebastianabril.pos.api.service;

import com.sebastianabril.pos.api.entity.Inventory;
import com.sebastianabril.pos.api.entity.Product;
import com.sebastianabril.pos.api.entity.Sale;
import com.sebastianabril.pos.api.entity.User;
import com.sebastianabril.pos.api.exceptions.InventoryNotFoundException;
import com.sebastianabril.pos.api.exceptions.NegativeOrZeroQuantityException;
import com.sebastianabril.pos.api.exceptions.ProductNotFoundException;
import com.sebastianabril.pos.api.exceptions.UserNotFoundException;
import com.sebastianabril.pos.api.repository.InventoryRepository;
import com.sebastianabril.pos.api.repository.ProductRepository;
import com.sebastianabril.pos.api.repository.SaleRepository;
import com.sebastianabril.pos.api.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {
    SaleRepository saleRepository;

    UserRepository userRepository;

    InventoryRepository inventoryRepository;
    ProductRepository productRepository;

    public SaleService(
        SaleRepository saleRepository,
        UserRepository userRepository,
        InventoryRepository inventoryRepository,
        ProductRepository productRepository
    ) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Sale save(Integer userId, Integer productId, Integer quantitySold) {
        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFoundException("The user with id " + userId + " does not exist"));
        Product product = productRepository
            .findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("The product with id " + productId + " does not exist"));

        if (quantitySold <= 0) {
            throw new NegativeOrZeroQuantityException("The quantity must be greater or equal than 1");
        }

        Double price = product.getPrice() * quantitySold;

        Inventory inventory = inventoryRepository
            .findByUserAndProduct(user, product)
            .orElseThrow(() -> new InventoryNotFoundException("The user does not have an inventory of the product"));

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        inventory.decreaseQuantity(quantitySold);
        inventoryRepository.save(inventory);

        Sale sale = new Sale(null, user, product, quantitySold, date, time, price);

        return saleRepository.save(sale);
    }
}
