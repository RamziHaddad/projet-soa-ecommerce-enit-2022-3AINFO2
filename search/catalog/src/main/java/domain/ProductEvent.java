package domain;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.debezium.outbox.quarkus.ExportedEvent;

public class ProductEvent implements ExportedEvent<String, JsonNode> {
    
    private static final String TYPE = "Product";
    //private static final String EVENT_TYPE = "ProductCreated";

    private static ObjectMapper mapper = new ObjectMapper();

   // private UUID id;
    private UUID productId;
    private JsonNode payLoad;
    private Instant timestamp;
    private String eventType;

    public ProductEvent(){}

    /*public ProductEvent(UUID id){
        this.id = id;
    }*/

    public ProductEvent(UUID productId, JsonNode payLoad, String eventType) {
        this.productId = productId;
        this.timestamp = Instant.now();
        this.payLoad = payLoad;
        this.eventType = eventType;
    }

    public ProductEvent of(Product p, String type) {
        ObjectNode asJson = mapper.createObjectNode()
                .put("id", p.getId().toString())  
                .put("name", p.getName())
                .put("category", p.getCategory())
                .put("brand", p.getBrand())
                .put("description", p.getDescription())
                .put("price", p.getPrice().toString());
        return new ProductEvent(p.getId(), asJson, type);
    }


    @Override
    public String getAggregateId() {
        return String.valueOf(productId);
    }

    @Override
    public String getAggregateType() {
        return TYPE;
    }

    @Override
    public JsonNode getPayload() {
        return payLoad;
    }

   
    public String getType() {
        return this.eventType;
    }

   
    public void setType(String type){
        this.eventType = type;
    }


    @Override
    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public Map<String, Object> getAdditionalFieldValues() {
        // no additional fields
        return Collections.emptyMap();
    }

    @Override
    public String toString() {
        return "Outbox [ Event=" + productId +   ", payload=" + payLoad
                + " event-type=" + this.getType() + ", createdAt=" + timestamp + "]";
    }

}
