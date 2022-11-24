package kafka;

import com.google.gson.Gson;
import domain.Product;
import service.ProductService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class SearchConsumer {

    @Inject
    ProductService productService;

    @Incoming("catalog-insert")
    public void index(String productJson) throws IOException {

        Gson gson = new Gson();    
        Product product = gson.fromJson(productJson, Product.class);

        Product productDB = productService.findById(product.getId());

        if (productDB == null){
            // insert new product
            productService.index(product);
        } else {
            // update product
            productService.deleteIndex(productDB);
            productService.index(product);
        }

    }

    @Incoming("catalog-delete")
    public void delete(String productJson) throws IOException {

        Gson gson = new Gson();    
        Product product = gson.fromJson(productJson, Product.class);

        Product productDB = productService.findById(product.getId());

        if (productDB != null) {
            productService.deleteIndex(productDB);
        } 

    }
    
}
