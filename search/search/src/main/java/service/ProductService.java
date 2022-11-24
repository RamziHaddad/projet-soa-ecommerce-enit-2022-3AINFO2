package service;

import domain.Product;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import org.elasticsearch.index.query.QueryBuilders;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Product Service
 * Create, get and search (by attribute) a {@link Product}
 */
@Slf4j
@ApplicationScoped
public class ProductService {

    private static final String PRODUCT_INDEX_NAME= "products";

    @Inject
    RestHighLevelClient restHighLevelClient;



    public void index(Product product) throws IOException {
        log.debug("Indexing PRODUCT [{}] in ES", product.getName());
        IndexRequest request = new IndexRequest(PRODUCT_INDEX_NAME);
        request.id(product.getId());
        request.source(JsonObject.mapFrom(product).toString(), XContentType.JSON);
        restHighLevelClient.index(request, RequestOptions.DEFAULT);
        log.debug("PRODUCT [{}] indexed ", product.getName());
    }

    public void deleteIndex(Product product) throws IOException {
        
        DeleteRequest request = new DeleteRequest(PRODUCT_INDEX_NAME, product.getId());  
        request.id(product.getId());
        restHighLevelClient.delete(request, RequestOptions.DEFAULT);
        log.debug("PRODUCT [{}] deleted ", product.getName());

    }



    public Product findById(String id) throws IOException {
        log.debug("Find PRODUCT by ID : [{}]", id);
        GetRequest getRequest = new GetRequest(PRODUCT_INDEX_NAME, id);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            String sourceAsString = getResponse.getSourceAsString();
            log.debug("OK => Product:{}", sourceAsString);
            JsonObject json = new JsonObject(sourceAsString);
            return json.mapTo(Product.class);
        }
        log.debug("PRODUCT [{}] not found!", id);
        return null;
    }

    public List<Product> findAll() throws IOException {
        log.debug("Get All PRODUCTS");
        SearchRequest searchRequest = new SearchRequest(PRODUCT_INDEX_NAME);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return Arrays.stream(searchResponse.getHits().getHits())
                .map(SearchHit::getSourceAsString)
                .map(s -> new JsonObject(s).mapTo(Product.class))
                .collect(Collectors.toList());
    }
    
    public List<Product> searchByName(String name) throws IOException {
        return search("name", name);
    }

    public List<Product> searchByCategory(String category) throws IOException {
        return search("category", category);
    }

    public List<Product> searchByDesc(String desc) throws IOException {
        return search("description", desc);
    }

    public List<Product> searchByPrice(Double price) throws IOException {
        String p = price.toString();
        return search("price", p);
    }

    public List<Product> searchByBrand(String brand) throws IOException {
        return search("brand", brand);
    }

    public List<Product> searchItem(String term) throws IOException{
        SearchRequest searchRequest = new SearchRequest(PRODUCT_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(term, "name","brand","category"));
        searchRequest.source(searchSourceBuilder);

        log.debug("ES query = {}", Json.encode(searchRequest));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.debug("ES response = {}", Json.encode(searchResponse));
        SearchHits hits = searchResponse.getHits();
        List<Product> results = new ArrayList<>(hits.getHits().length);
        log.debug("  {} hit found !", hits.getHits().length);

        for (SearchHit hit : hits.getHits()) {
            String sourceAsString = hit.getSourceAsString();
            JsonObject json = new JsonObject(sourceAsString);
            results.add(json.mapTo(Product.class));
        }
        return results;
    }

    private List<Product> search(String term, String match) throws IOException {
        log.debug("Search PRODUCT by {}: [{}]", term, match);
        // prepare request
        SearchRequest searchRequest = new SearchRequest(PRODUCT_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(term, match));
        searchRequest.source(searchSourceBuilder);

        // call ES
        log.debug("ES query = {}", Json.encode(searchRequest));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.debug("ES response = {}", Json.encode(searchResponse));
        SearchHits hits = searchResponse.getHits();
        List<Product> results = new ArrayList<>(hits.getHits().length);
        log.debug("  {} hit found !", hits.getHits().length);

        // map JSON response to model & return
        for (SearchHit hit : hits.getHits()) {
            String sourceAsString = hit.getSourceAsString();
            JsonObject json = new JsonObject(sourceAsString);
            results.add(json.mapTo(Product.class));
        }
        return results;
    }

    public List<Product> filterByPrice(String term, Integer min, Integer max) throws IOException {
        SearchRequest searchRequest = new SearchRequest(PRODUCT_INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery().filter(QueryBuilders.rangeQuery("price")
                                                .from(min)
                                                .to(max).
                                                includeLower(true)
                                                .includeUpper(false))
                                                .must(QueryBuilders.multiMatchQuery(term,"name","brand","category")) 
                                                );
        searchRequest.source(searchSourceBuilder);

        log.debug("ES query = {}", Json.encode(searchRequest));
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        log.debug("ES response = {}", Json.encode(searchResponse));
        SearchHits hits = searchResponse.getHits();
        List<Product> results = new ArrayList<>(hits.getHits().length);
        log.debug("  {} hit found !", hits.getHits().length);

        for (SearchHit hit : hits.getHits()) {
            String sourceAsString = hit.getSourceAsString();
            JsonObject json = new JsonObject(sourceAsString);
            results.add(json.mapTo(Product.class));
        }
        return results;
    }

   
   

}
