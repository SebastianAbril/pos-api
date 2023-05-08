package com.sebastianabril.pos.api.controller;

import com.sebastianabril.pos.api.controller.dto.SaleRequest;
import com.sebastianabril.pos.api.entity.Sale;
import com.sebastianabril.pos.api.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SaleController {
    @Autowired
    private SaleService saleService;

    @PostMapping("/api/sale")
    public ResponseEntity<Sale> save(@Valid @RequestBody SaleRequest saleRequest) {
        Sale sale = saleService.save(
            saleRequest.getUserId(),
            saleRequest.getProductId(),
            saleRequest.getQuantitySold()
        );
        return new ResponseEntity<>(sale, HttpStatus.CREATED);
    }
}
