package com.example.xueyangli.ilovezappos;

import com.example.xueyangli.ilovezappos.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xueyangli on 2/9/17.
 */

public class ProductListResponse {
    List<Product> results  = new ArrayList<>();

    public List<Product> getProducts() {
        return results;
    }

}
