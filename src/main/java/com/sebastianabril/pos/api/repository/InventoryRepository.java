package com.sebastianabril.pos.api.repository;

import com.sebastianabril.pos.api.entity.Inventory;
import com.sebastianabril.pos.api.entity.Product;
import com.sebastianabril.pos.api.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findByUserAndProduct(User user, Product product);
}
