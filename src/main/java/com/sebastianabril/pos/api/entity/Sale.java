package com.sebastianabril.pos.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "sales")
@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity_sold")
    private Integer quantitySold;

    private LocalDate date;
    private LocalTime time;

    @Column(name = "total_value")
    private Double price;

    public Sale() {}

    public Sale(
        Integer id,
        User user,
        Product product,
        Integer quantitySold,
        LocalDate date,
        LocalTime time,
        Double price
    ) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.quantitySold = quantitySold;
        this.date = date;
        this.time = time;
        this.price = price;
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

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return (
            "Sale{" +
            "id=" +
            id +
            ", user=" +
            user +
            ", product=" +
            product +
            ", quantitySold=" +
            quantitySold +
            ", date=" +
            date +
            ", time=" +
            time +
            ", price=" +
            price +
            '}'
        );
    }
}
