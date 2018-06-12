package com.kaizen.reterofit;

import com.kaizen.models.Banner;
import com.kaizen.models.BannerResponse;
import com.kaizen.models.CategoryResponse;
import com.kaizen.models.ChildCategoryResponse;
import com.kaizen.models.FoodCategoryResponse;
import com.kaizen.models.FoodItemListResponse;
import com.kaizen.models.FoodItemResponse;
import com.kaizen.models.FoodSubcategoryResponse;
import com.kaizen.models.ListChildCategoryResponse;
import com.kaizen.models.ReportsResponse;
import com.kaizen.models.RequestResponse;
import com.kaizen.models.SubcategoryResponse;
import com.kaizen.models.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by FAMILY on 14-12-2017.
 */

public interface RetrofitService {

    @GET(APIUrls.CATEGORY)
    Call<CategoryResponse> getCategories();

    @GET(APIUrls.SUB_CATEGORY)
    Call<SubcategoryResponse> getSubCategories(@Query("menuid") String menuid);

    @GET(APIUrls.CHILD_CATEGORY)
    Call<ChildCategoryResponse> getChildCategory(@Query("menuid") String menuid, @Query("subid") String subid);

    @GET(APIUrls.LIST_CHILD_CATEGORY)
    Call<ListChildCategoryResponse> getListChildCategory(@Query("menuid") String menuid, @Query("subid") String subid, @Query("childid") String childid);

    @GET(APIUrls.LOGIN)
    Call<UserResponse> login(@Query("roomno") String roomno, @Query("password") String password);

    @GET(APIUrls.BANNER_LIST)
    Call<BannerResponse> getBanners(@Query("menuid") String menuid);

    @GET(APIUrls.FEEDBACK)
    Call<RequestResponse> sendFeedBack(@Query("roomno") String roomno, @Query("name") String name, @Query("description") String description);

    @GET(APIUrls.CHECKOUT)
    Call<RequestResponse> checkoutTime(@Query("roomno") String roomno, @Query("name") String name, @Query("checktime") String checktime);

    @GET(APIUrls.INTERNET)
    Call<RequestResponse> askForInternet(@Query("roomno") String roomno, @Query("name") String name);

    @GET(APIUrls.COLLECTTRAY)
    Call<RequestResponse> collectTray(@Query("roomno") String roomno, @Query("name") String name, @Query("timing") String timing);

    @GET(APIUrls.SENDENQUERY)
    Call<RequestResponse> sendQuery(@Query("userid") String userid, @Query("catid") String catid, @Query("name") String name, @Query("timeperiod") String timeperiod);

    @GET(APIUrls.BANNER_LINK)
    Call<ReportsResponse> getBanner(@Query("cid") String cid, @Query("subid") String subid, @Query("subsubid") String subsubid);

    @GET(APIUrls.FOOD_CATEGORY)
    Call<FoodCategoryResponse> getFoodCategory();

    @GET(APIUrls.FOOD_SUB_CATEGORY)
    Call<FoodSubcategoryResponse> getFoodSubcategory(@Query("cid") String cid);

    @GET(APIUrls.FOOD_ITEM_LIST)
    Call<FoodItemResponse> getFoodItems(@Query("cid") String cid,@Query("subid") String subid);

    @GET(APIUrls.FOOD_ITEMS)
    Call<FoodItemListResponse> getFoodItems(@Query("cid") String cid);

    @POST(APIUrls.ORDER_ITEM)
    @FormUrlEncoded
    Call<RequestResponse> orderItem(@Query("roomno") String roomno, @Field("data") String items);

    @GET(APIUrls.EMERGENCY)
    Call<RequestResponse> sendEmergency(@Query("roomno") String roomno);
}
