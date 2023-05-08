package com.sebastianabril.pos.api.repository;

import com.sebastianabril.pos.api.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementsRepository extends JpaRepository<InventoryMovement, Integer> {}
