package com.example.xueyangli.ilovezappos.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit interface to consume ZapposAPI
 *
 * Created by xueyangli on 2/9/17.
 */

public interface ZapposAPI {
    @GET("/Search")
    Call<ProductListResponse> searchProducts(@Query("term") String tags, @Query("key") String key);
}
