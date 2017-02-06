package com.example.xueyangli.ilovezappos.model;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xueyangli on 2/5/17.
 * Model corresponds to JSON response from the following API:
 * https://api.zappos.com/Search?term=&key=b743e26728e16b81da139182bb2094357c31d331
 * Created by http://www.jsonschema2pojo.org/
 */

public class Product implements Serializable{

    public String brandName;
    public String thumbnailImageUrl;
    public String productId;
    public String originalPrice;
    public String styleId;
    public String colorId;
    public String price;
    public String percentOff;
    public String productUrl;
    public String productName;
}