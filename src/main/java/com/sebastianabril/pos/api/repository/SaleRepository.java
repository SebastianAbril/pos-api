package com.sebastianabril.pos.api.repository;

import com.sebastianabril.pos.api.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {}
