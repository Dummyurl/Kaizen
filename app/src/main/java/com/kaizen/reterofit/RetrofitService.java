package com.kaizen.reterofit;

import com.kaizen.models.CategoryResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by FAMILY on 14-12-2017.
 */

public interface RetrofitService {

    @GET(APIUrls.MENU_LIST)
    Call<CategoryResponse> getCategories();
}
