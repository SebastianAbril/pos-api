package com.sebastianabril.pos.api.entity;

import com.sebastianabril.pos.api.exceptions.NotEnoughQuantityException;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    public Inventory() {}

    public Inventory(Integer id, User user, Product product, Integer quantity) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public void increaseQuantity(Integer increase) {
        this.quantity += increase;
    }

    public void decreaseQuantity(Integer decrease) {
        if (this.quantity - decrease < 0) {
            throw new NotEnoughQuantityException(
                "The quantity must be 0 or greater (The user does not have enough product to sell)"
            );
        }
        this.quantity -= decrease;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Inventory{" + "id=" + id + ", user=" + user + ", product=" + product + ", quantity=" + quantity + '}';
    }
}
