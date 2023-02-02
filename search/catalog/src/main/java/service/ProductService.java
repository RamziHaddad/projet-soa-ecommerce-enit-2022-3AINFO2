package service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import domain.Product;
import domain.ProductEvent;
import io.debezium.outbox.quarkus.ExportedEvent;
import repository.ProductRepo;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepo productRepo;

    @Inject
    EntityManager em;

    /*@Inject
    ProductEventRepo productEventRepo;*/

    @Inject
    Event<ExportedEvent<?, ?>> event;

    public List<Product> findAll() {
        return productRepo.findAll();
    }
    public Product findById(UUID id) throws IOException {
        return productRepo.findById(id);
    }
@Transactional
    public void create(Product p) throws IOException {
        
        em.persist(p);
        ProductEvent productEvent = new ProductEvent().of(p, "ProductCreated");
        event.fire(productEvent);
        //productEventRepo.insert(productEvent);
        System.out.println(productEvent.toString());
       
    }
    public Product update(Product p) throws IOException {
        return productRepo.update(p);      
    }

    public void remove(UUID id) {
        productRepo.delete(id);
    }

    
}
