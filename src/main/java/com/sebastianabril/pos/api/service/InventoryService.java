package com.sebastianabril.pos.api.service;

import com.sebastianabril.pos.api.entity.Inventory;
import com.sebastianabril.pos.api.entity.Product;
import com.sebastianabril.pos.api.entity.User;
import com.sebastianabril.pos.api.exceptions.NegativeOrZeroQuantityException;
import com.sebastianabril.pos.api.exceptions.NotAdminUserException;
import com.sebastianabril.pos.api.exceptions.ProductNotFoundException;
import com.sebastianabril.pos.api.exceptions.UserNotFoundException;
import com.sebastianabril.pos.api.repository.InventoryRepository;
import com.sebastianabril.pos.api.repository.ProductRepository;
import com.sebastianabril.pos.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private InventoryRepository inventoryRepository;

    public InventoryService(
        UserRepository userRepository,
        ProductRepository productRepository,
        InventoryRepository inventoryRepository
    ) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public Inventory addProductToInventory(Integer userId, Integer productId, Integer quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("user not found"));
        if (user.getRole().getId() != 1) {
            throw new NotAdminUserException("The user must be an admin (userId = 1)");
        }

        Product product = productRepository
            .findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("productId not found"));

        if (quantity <= 0) {
            throw new NegativeOrZeroQuantityException("quantity must be positive number");
        }

        /*
        Optional<Inventory> optionalInventory = inventoryRepository.findByUserAndProduct(user, product);
        Inventory inventory = null;
        if (optionalInventory.isPresent()) {
            inventory = optionalInventory.get();
            inventory.setAmount(inventory.getAmount() + quantity);
        } else {
            inventory = new Inventory(null, user, product, quantity);
        }*/

        //*****************
        Inventory inventory = inventoryRepository
            .findByUserAndProduct(user, product)
            .orElse(new Inventory(null, user, product, 0));

        inventory.setQuantity(inventory.getQuantity() + quantity);

        return inventoryRepository.save(inventory);
    }
}
