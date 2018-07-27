package com.kaizen.reterofit;

import com.kaizen.models.ArticleResponse;
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
import com.kaizen.models.SettingsResponse;
import com.kaizen.models.ShopCategoryResponse;
import com.kaizen.models.ShopChildCategoryResponse;
import com.kaizen.models.ShopItemListResponse;
import com.kaizen.models.ShopItemResponse;
import com.kaizen.models.ShopOrder;
import com.kaizen.models.ShopSubCategoryResponse;
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

    @POST(APIUrls.CATEGORY)
    @FormUrlEncoded
    Call<CategoryResponse> getCategories(@Field("languageid") int languageid);

    @POST(APIUrls.SUB_CATEGORY)
    @FormUrlEncoded
    Call<SubcategoryResponse> getSubCategories(@Field("languageid") int languageId, @Query("menuid") String menuid);

    @POST(APIUrls.CHILD_CATEGORY)
    @FormUrlEncoded
    Call<ChildCategoryResponse> getChildCategory(@Field("languageid") int languageId, @Query("menuid") String menuid, @Query("subid") String subid);

    @POST(APIUrls.LIST_CHILD_CATEGORY)
    @FormUrlEncoded
    Call<ListChildCategoryResponse> getListChildCategory(@Field("languageid") int languageId, @Query("menuid") String menuid, @Query("subid") String subid, @Query("childid") String childid);

    @POST(APIUrls.LOGIN)
    @FormUrlEncoded
    Call<UserResponse> login(@Field("languageid") int languageId, @Query("roomno") String roomno, @Query("password") String password);

    @POST(APIUrls.BANNER_LIST)
    @FormUrlEncoded
    Call<BannerResponse> getBanners(@Field("languageid") int languageId, @Query("menuid") String menuid);

    @POST(APIUrls.FEEDBACK)
    @FormUrlEncoded
    Call<RequestResponse> sendFeedBack(@Field("languageid") int languageId, @Query("roomno") String roomno, @Query("name") String name, @Query("description") String description);

    @POST(APIUrls.CHECKOUT)
    @FormUrlEncoded
    Call<RequestResponse> checkoutTime(@Field("languageid") int languageId, @Query("roomno") String roomno, @Query("name") String name, @Query("checktime") String checktime);

    @POST(APIUrls.INTERNET)
    @FormUrlEncoded
    Call<RequestResponse> askForInternet(@Field("languageid") int languageId, @Query("roomno") String roomno, @Query("name") String name);

    @POST(APIUrls.COLLECTTRAY)
    @FormUrlEncoded
    Call<RequestResponse> collectTray(@Field("languageid") int languageId, @Query("roomno") String roomno, @Query("name") String name, @Query("timing") String timing);

    @POST(APIUrls.SENDENQUERY)
    @FormUrlEncoded
    Call<RequestResponse> sendQuery(@Field("languageid") int languageId, @Query("userid") String userid, @Query("catid") String catid, @Query("name") String name, @Query("timeperiod") String timeperiod);

    @POST(APIUrls.BANNER_LINK)
    @FormUrlEncoded
    Call<ReportsResponse> getBanner(@Field("languageid") int languageId, @Query("cid") String cid, @Query("subid") String subid, @Query("subsubid") String subsubid);

    @POST(APIUrls.FOOD_CATEGORY)
    @FormUrlEncoded
    Call<FoodCategoryResponse> getFoodCategory(@Field("languageid") int languageId);

    @POST(APIUrls.FOOD_SUB_CATEGORY)
    @FormUrlEncoded
    Call<FoodSubcategoryResponse> getFoodSubcategory(@Field("languageid") int languageId, @Query("cid") String cid);

    @POST(APIUrls.FOOD_ITEM_LIST)
    @FormUrlEncoded
    Call<FoodItemResponse> getFoodItems(@Field("languageid") int languageId, @Query("cid") String cid, @Query("subid") String subid);

    @POST(APIUrls.FOOD_ITEMS)
    @FormUrlEncoded
    Call<FoodItemListResponse> getFoodItems(@Field("languageid") int languageId, @Query("cid") String cid);

    @POST(APIUrls.ORDER_ITEM)
    @FormUrlEncoded
    Call<RequestResponse> orderItem(@Field("languageid") int languageId, @Query("roomno") String roomno, @Field("data") String items);

    @POST(APIUrls.EMERGENCY)
    @FormUrlEncoded
    Call<RequestResponse> sendEmergency(@Field("languageid") int languageId, @Query("roomno") String roomno, @Query("name") String name, @Query("description") String description);


    @GET(APIUrls.LOCAL_NEWS)
    Call<ArticleResponse> getLocalNews();

    @GET(APIUrls.GLOBAL_NEWS)
    Call<ArticleResponse> getGlobalNews();

    @POST(APIUrls.SETTINGS)
    @FormUrlEncoded
    Call<SettingsResponse> getSettings(@Field("languageid") int languageId);

    @POST(APIUrls.SHOP_CATEGORY)
    @FormUrlEncoded
    Call<ShopCategoryResponse> getShopCategory(@Field("languageid") int languageId);

    @POST(APIUrls.SHOP_SUB_CATEGORY)
    @FormUrlEncoded
    Call<ShopSubCategoryResponse> getShopSubcategory(@Field("languageid") int languageId, @Query("cid") String cid);

    @POST(APIUrls.SHOP_ITEM_LIST)
    @FormUrlEncoded
    Call<ShopItemResponse> getShopItems(@Field("languageid") int languageId, @Query("cid") String cid, @Query("subid") String subid);


    @POST(APIUrls.SHOP_CHILD_CATEGORY)
    @FormUrlEncoded
    Call<ShopChildCategoryResponse> getShopChildSubcategory(@Field("languageid") int languageId, @Query("cid") String cid);

    @POST(APIUrls.SHOP_ITEMS)
    @FormUrlEncoded
    Call<ShopItemListResponse> getShopItems(@Field("languageid") int languageId, @Query("cid") String cid);

    @POST(APIUrls.SHOP_ORDER_ITEM)
    @FormUrlEncoded
    Call<ShopOrder> shoporderItem(@Field("languageid") int languageId, @Query("roomno") String roomno, @Field("data") String items);



}
