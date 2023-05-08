package com.sebastianabril.pos.api.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class SaleRequest {
    @NotNull
    @PositiveOrZero(message = "Insert the user(seller) id")
    private Integer userId;

    @NotNull
    @PositiveOrZero(message = "Insert a productId")
    private Integer productId;

    @NotNull(message = "Insert the quantity sold")
    @Min(1)
    private Integer quantitySold;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }
}
