package com.sebastianabril.pos.api.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class TransferProductRequest {
    @PositiveOrZero(message = "Insert origin user's id")
    private Integer originUserId;

    @PositiveOrZero(message = "Insert destiny user's id")
    private Integer destinyUserId;

    @PositiveOrZero(message = "Insert a productId")
    private Integer productId;

    @NotNull(message = "Insert a quantity")
    @Min(1)
    private Integer quantityTransferred;

    public Integer getOriginUserId() {
        return originUserId;
    }

    public void setOriginUserId(Integer originUserId) {
        this.originUserId = originUserId;
    }

    public Integer getDestinyUserId() {
        return destinyUserId;
    }

    public void setDestinyUserId(Integer destinyUserId) {
        this.destinyUserId = destinyUserId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantityTransferred() {
        return quantityTransferred;
    }

    public void setQuantityTransferred(Integer quantityTransferred) {
        this.quantityTransferred = quantityTransferred;
    }
}
