package com.w4ma.soft.tamenly.Utils.NetworkApiPressenter;

import com.w4ma.soft.tamenly.CategoryActivities.Models.CurrencyList;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RetrofitInterface {



    @GET("currency.php")
    Call<CurrencyList> Convert(@QueryMap Map<String,String> ApiInfo);


}
