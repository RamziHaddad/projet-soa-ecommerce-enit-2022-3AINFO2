package controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import com.google.gson.Gson;

import domain.Product;
import service.ProductService;

@Path("/catalog")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @Inject @Channel("catalog-insert") Emitter<String> productAddEmitter;

    //@Inject @Channel("catalog-delete") Emitter<String> productDeleteEmitter;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    public List<Product> findall() {
        return  productService.findAll();
    }

    @GET
    @Path("/{id}")
    public Product findById(@PathParam("id") UUID id) throws IOException {
        return  productService.findById(id);
    }

    @POST
    public void create(Product p) throws IOException {
        productService.create(p);

        Gson gson = new Gson();    
        String json = gson.toJson(p);

        //Send to Topic kafka
        productAddEmitter.send(json);

       
    }

    @PUT
    public Product update(Product p) throws IOException{
        Gson gson = new Gson();    
        String json = gson.toJson(p);

        //Send to Topic kafka
        productAddEmitter.send(json);

        return  productService.update(p);
    }

    /*@DELETE
    public void remove(Product p) throws IOException {

        Gson gson = new Gson();    
        String json = gson.toJson(p);

        //Send to Topic kafka
        productDeleteEmitter.send(json);

        productService.remove(p.getId());

    }*/
    
}
