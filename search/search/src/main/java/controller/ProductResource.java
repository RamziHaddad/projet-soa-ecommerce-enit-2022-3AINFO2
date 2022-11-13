package controller;

import domain.Product;
import service.ProductService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Consumes("application/json")
@Path("/products")
public class ProductResource {

    @Inject
    ProductService productService;

    @POST
    public Response create(Product product) throws IOException {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID().toString());
        }
        productService.create(product);
        return Response.created(URI.create("/products/" + product.getId())).build();
    }

    @GET
    public List<Product> findAll() throws IOException {
        return productService.findAll();
    }

    @GET
    @Path("/{id}")
    public Product get(@PathParam("id") String id) throws IOException {
        return productService.findById(id);
    }

    @GET
    @Path("/search")
    public List<Product> search(@QueryParam("name") String name, @QueryParam("category") String category,
     @QueryParam("description") String desc, @QueryParam("price") Double price) throws IOException {
        if (name != null) {
            return productService.searchByName(name);
        } else if (category != null) {
            return productService.searchByCategory(category);
        } else if ( desc != null){
            return productService.searchByDesc(desc);
        } else if (price != null){
            return productService.searchByPrice(price);   
        } else {
            throw new BadRequestException("Should provide name or category or description or price query parameter");
        }
    }
    
}
