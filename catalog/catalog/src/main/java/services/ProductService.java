package services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import domain.Product;
import repository.ProductRepo;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepo productRepo;

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public Product findById(UUID id) throws IOException {
        return productRepo.findById(id);
    }

    public Product create(Product p) throws IOException {

        return productRepo.insert(p);

    }

    public Product update(Product p) throws IOException {
        return productRepo.update(p);
    }

    public void remove(UUID id) {
        productRepo.delete(id);
    }

}
