package api;

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

import domain.Product;
import services.ProductService;

@Path("/catalog")
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    public List<Product> findall() {
        return productService.findAll();
    }

    @GET
    @Path("/{id}")
    public Product findById(@PathParam("id") UUID id) throws IOException {
        return productService.findById(id);
    }

    @POST
    public Product create(Product p) throws IOException {
        return productService.create(p);

    }

    @PUT
    public Product update(Product p) throws IOException {

        return productService.update(p);
    }

    @DELETE
    public void remove(Product p) throws IOException {

        productService.remove(p.getId());

    }

}
