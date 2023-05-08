package com.sebastianabril.pos.api.service;

import com.sebastianabril.pos.api.entity.Inventory;
import com.sebastianabril.pos.api.entity.InventoryMovement;
import com.sebastianabril.pos.api.entity.Product;
import com.sebastianabril.pos.api.entity.User;
import com.sebastianabril.pos.api.exceptions.InventoryNotFoundException;
import com.sebastianabril.pos.api.exceptions.NotEnoughQuantityException;
import com.sebastianabril.pos.api.exceptions.ProductNotFoundException;
import com.sebastianabril.pos.api.exceptions.UserNotFoundException;
import com.sebastianabril.pos.api.repository.InventoryMovementsRepository;
import com.sebastianabril.pos.api.repository.InventoryRepository;
import com.sebastianabril.pos.api.repository.ProductRepository;
import com.sebastianabril.pos.api.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryMovementsService {
    private UserRepository userRepository;
    private ProductRepository productRepository;

    private InventoryRepository inventoryRepository;
    private InventoryMovementsRepository inventoryMovementsRepository;

    public InventoryMovementsService(
        UserRepository userRepository,
        ProductRepository productRepository,
        InventoryRepository inventoryRepository,
        InventoryMovementsRepository inventoryMovementsRepository
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.inventoryMovementsRepository = inventoryMovementsRepository;
    }

    @Transactional
    public InventoryMovement transferProduct(
        Integer originUserId,
        Integer destinyUserId,
        Integer productId,
        Integer quantity
    ) {
        User originUser = userRepository
            .findById(originUserId)
            .orElseThrow(() -> new UserNotFoundException("Origin user not found"));
        User destinyUser = userRepository
            .findById(destinyUserId)
            .orElseThrow(() -> new UserNotFoundException("Destiny user not found"));
        Product product = productRepository
            .findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        Inventory originUserInventory = inventoryRepository
            .findByUserAndProduct(originUser, product)
            .orElseThrow(() -> new InventoryNotFoundException("The inventory for the origin user does not exist"));

        Inventory destinyUserInventory = inventoryRepository
            .findByUserAndProduct(destinyUser, product)
            .orElse(new Inventory(null, destinyUser, product, 0));

        Integer originUserProductAmount = originUserInventory.getQuantity();
        Integer destinyUserProductAmount = destinyUserInventory.getQuantity();

        if (originUserProductAmount < quantity) {
            throw new NotEnoughQuantityException("The origin user does not have that quantity, try less");
        }

        originUserProductAmount -= quantity;
        destinyUserProductAmount += quantity;

        originUserInventory.setQuantity(originUserProductAmount);
        destinyUserInventory.setQuantity(destinyUserProductAmount);

        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        InventoryMovement inventoryMovement = new InventoryMovement(
            null,
            originUser,
            destinyUser,
            product,
            quantity,
            date,
            time
        );

        inventoryRepository.save(originUserInventory);
        inventoryRepository.save(destinyUserInventory);

        return inventoryMovementsRepository.save(inventoryMovement);
    }
}
