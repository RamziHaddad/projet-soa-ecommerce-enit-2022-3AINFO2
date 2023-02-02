package domain;

import java.util.UUID;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String category;
    private String brand;
    private Double price;
    private String description;

    public Product() {

    }

    public Product(UUID id, String name, String category, String brand, Double price, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.brand = brand;
        this.price = price;
        this.description = description;
    }

}
