package repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import domain.Product;

@ApplicationScoped
public class ProductRepo {

    private List<Product> products = new ArrayList<Product>();

    public List<Product> findAll() {
        return products;
    }

    public Product findById(UUID id) throws IOException {
        Product p = new Product(id, "", "", "", 0.0, "");
        if (products.contains(p)) {
            int pos = products.indexOf(p);
            return products.get(pos);
        }
        throw new IOException("cannot find product");
    }

    public Product insert(Product p) throws IOException {
        if (p.getId() == null) {
            p.setId(UUID.randomUUID());
            products.add(p);
            return p;
        }
        throw new IOException("product already exists");
    }

    public Product update(Product p) throws IOException {
        if (products.contains(p)) {
            int pos = products.indexOf(p);
            products.get(pos).setName(p.getName());
            products.get(pos).setCategory(p.getCategory());
            products.get(pos).setBrand(p.getBrand());
            products.get(pos).setDescription(p.getDescription());
            products.get(pos).setPrice(p.getPrice());
            return p;
        }
        throw new IOException("cannot find product");
    }

    public void delete(Product p) {
        products.remove(p);
    }

    public void delete(UUID id) {
        Product p = new Product(id, "", "", "", 0.0, "");
        delete(p);
    }

}
