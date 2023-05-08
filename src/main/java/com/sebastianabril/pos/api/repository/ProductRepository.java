package com.sebastianabril.pos.api.repository;

import com.sebastianabril.pos.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {}
