package com.example.xueyangli.ilovezappos.model;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xueyangli on 2/5/17.
 * Model corresponds to JSON response from the following API:
 * https://api.zappos.com/Search?term=&key=b743e26728e16b81da139182bb2094357c31d331
 * Created by http://www.jsonschema2pojo.org/
 */

public class Product {

    private Object originalTerm;
    private String currentResultCount;
    private String totalResultCount;
    private String term;
    private List<ProductResult> results = null;
    private String statusCode;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Object getOriginalTerm() {
        return originalTerm;
    }

    public void setOriginalTerm(Object originalTerm) {
        this.originalTerm = originalTerm;
    }

    public String getCurrentResultCount() {
        return currentResultCount;
    }

    public void setCurrentResultCount(String currentResultCount) {
        this.currentResultCount = currentResultCount;
    }

    public String getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(String totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<ProductResult> getResults() {
        return results;
    }

    public void setResults(List<ProductResult> results) {
        this.results = results;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
