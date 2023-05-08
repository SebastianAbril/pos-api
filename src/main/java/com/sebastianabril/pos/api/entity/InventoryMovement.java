package com.sebastianabril.pos.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "inventory_movements")
@Entity
public class InventoryMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "origin_user_id")
    private User originUser;

    @ManyToOne
    @JoinColumn(name = "destiny_user_id")
    private User destinyUser;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity_transferred")
    private Integer quantityTransferred;

    private LocalDate date;

    private LocalTime hour;

    public InventoryMovement() {}

    public InventoryMovement(
        Integer id,
        User originUser,
        User destinyUser,
        Product product,
        Integer quantityTransferred,
        LocalDate date,
        LocalTime hour
    ) {
        this.id = id;
        this.originUser = originUser;
        this.destinyUser = destinyUser;
        this.product = product;
        this.quantityTransferred = quantityTransferred;
        this.date = date;
        this.hour = hour;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getOriginUser() {
        return originUser;
    }

    public void setOriginUser(User originUser) {
        this.originUser = originUser;
    }

    public User getDestinyUser() {
        return destinyUser;
    }

    public void setDestinyUser(User destinyUser) {
        this.destinyUser = destinyUser;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantityTransferred() {
        return quantityTransferred;
    }

    public void setQuantityTransferred(Integer amountTransferred) {
        this.quantityTransferred = amountTransferred;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHour() {
        return hour;
    }

    public void setHour(LocalTime hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return (
            "InventoryMovements{" +
            "id=" +
            id +
            ", originUser=" +
            originUser +
            ", destinyUser=" +
            destinyUser +
            ", product=" +
            product +
            ", quantityTransferred=" +
            quantityTransferred +
            ", date=" +
            date +
            ", hour=" +
            hour +
            '}'
        );
    }
}
