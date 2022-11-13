package domain;

import lombok.Data;

@Data
public class Product {
   
    private String id;
    private String name;
    private String category;
    private String description;
    private Double price;
    
}
