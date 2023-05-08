package com.sebastianabril.pos.api.controller;

import com.sebastianabril.pos.api.controller.dto.AddProductToInventoryRequest;
import com.sebastianabril.pos.api.controller.dto.TransferProductRequest;
import com.sebastianabril.pos.api.entity.Inventory;
import com.sebastianabril.pos.api.entity.InventoryMovement;
import com.sebastianabril.pos.api.service.InventoryMovementsService;
import com.sebastianabril.pos.api.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryMovementsService inventoryMovementsService;

    @PostMapping("/add-product-to-inventory")
    public ResponseEntity<Inventory> addProductToInventory(@RequestBody @Valid AddProductToInventoryRequest request) {
        Inventory inventory = inventoryService.addProductToInventory(
            request.getUserId(),
            request.getProductId(),
            request.getQuantity()
        );

        return ResponseEntity.ok(inventory);
    }

    @PostMapping("/transfer-product")
    public ResponseEntity<InventoryMovement> transferProduct(
        @Valid @RequestBody TransferProductRequest transferredProductRequest
    ) {
        InventoryMovement inventoryMovement = inventoryMovementsService.transferProduct(
            transferredProductRequest.getOriginUserId(),
            transferredProductRequest.getDestinyUserId(),
            transferredProductRequest.getProductId(),
            transferredProductRequest.getQuantityTransferred()
        );
        return ResponseEntity.status(HttpStatus.OK).body(inventoryMovement);
    }
}
