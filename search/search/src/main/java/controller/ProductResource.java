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
    public List<Product> search(@QueryParam("term") String term) throws IOException{
        if (term != null){
            return productService.searchItem(term);
        } else {
            throw new BadRequestException("Should provide the name or category or brand of the product as a query parameter");
        }

    }

    @GET
    @Path("/search/filterByPrice")
    public List<Product> filterByPrice(@QueryParam("gte") Integer gte, @QueryParam("lte") Integer lte) throws IOException{
        if (gte != null || lte != null ){
            return productService.filterByPrice(gte, lte);
        } else {
            throw new BadRequestException("Should provide the name or category or brand of the product as a query parameter");
        }

    }
    /*public List<Product> search(@QueryParam("name") String name, @QueryParam("category") String category,
     @QueryParam("brand") String brand) throws IOException {
        if (name != null) {
            return productService.searchByName(name);
        } 
        if (category != null) {
            return productService.searchByCategory(category);
        } 
        if ( brand != null){
            return productService.searchByBrand(brand);
        } 
        if (brand == null && name == null && category == null){
            throw new BadRequestException("Should provide the name or category or brand of the product as a query parameter");
        }
        return null;
    }*/
    
}
