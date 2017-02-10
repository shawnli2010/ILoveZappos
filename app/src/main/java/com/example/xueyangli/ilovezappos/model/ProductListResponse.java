package com.example.xueyangli.ilovezappos.model;

import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Response Model class, which will hold the data converted from JSON
 *
 * Created by xueyangli on 2/9/17.
 */

public class ProductListResponse {
    List<Product> results  = new ArrayList<>();

    public List<Product> getProducts() {
        return results;
    }

}
