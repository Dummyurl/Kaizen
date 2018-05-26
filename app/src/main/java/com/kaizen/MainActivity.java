package com.kaizen;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kaizen.activities.BaseActivity;
import com.kaizen.adapters.CategoryAdapter;
import com.kaizen.adapters.ChildCategoryPager;
import com.kaizen.adapters.SubcategoryAdapter;
import com.kaizen.listeners.ISetOnCategoryClickListener;
import com.kaizen.listeners.ISetOnChildClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.CategoryResponse;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.ListChildCategoryResponse;
import com.kaizen.models.Subcategory;
import com.kaizen.models.SubcategoryResponse;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements ISetOnCategoryClickListener, ISetOnChildClickListener {

    private ImageView iv_image;
    private RequestOptions requestOptions;
    private RetrofitService service;
    private ViewPager view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_pager = findViewById(R.id.view_pager);
        TabLayout tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);

        iv_image = findViewById(R.id.iv_category);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);

        TextView tv_date = findViewById(R.id.tv_date);
        DateFormat df = new SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault());
        String date = df.format(new Date().getTime());
        tv_date.setText(date);

        RecyclerView rv_category = findViewById(R.id.rv_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_category.setLayoutManager(layoutManager);
        final CategoryAdapter categoryAdapter = new CategoryAdapter(this);
        rv_category.setAdapter(categoryAdapter);

        service = RetrofitInstance.createService(RetrofitService.class);
        service.getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryAdapter.addItems(response.body().getCategory());
                } else {
                    showErrorToast(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                showErrorToast(R.string.something_went_wrong);
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.IMAGE_URL + category.getCategory_image()).into(iv_image);

        RecyclerView rv_sub_category = findViewById(R.id.rv_sub_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_sub_category.setLayoutManager(layoutManager);
        final SubcategoryAdapter subcategoryAdapter = new SubcategoryAdapter(category, this);
        rv_sub_category.setAdapter(subcategoryAdapter);

        service.getSubCategories(category.getId()).enqueue(new Callback<SubcategoryResponse>() {
            @Override
            public void onResponse(Call<SubcategoryResponse> call, Response<SubcategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    subcategoryAdapter.addItems(response.body().getSubcategory());
                } else {
                    showErrorToast(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<SubcategoryResponse> call, Throwable t) {
                showErrorToast(R.string.something_went_wrong);
            }
        });
    }

    @Override
    public void onChildCategoryClick(Category category, Subcategory subcategory, ChildCategory childCategory) {
        service.getListChildCategory(category.getId(), subcategory.getId(), childCategory.getId()).enqueue(new Callback<ListChildCategoryResponse>() {
            @Override
            public void onResponse(Call<ListChildCategoryResponse> call, Response<ListChildCategoryResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    ChildCategoryPager childCategoryPager = new ChildCategoryPager(getSupportFragmentManager(), response.body().getListchildcategory());
                    view_pager.setAdapter(childCategoryPager);
                } else {
                    showErrorToast(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<ListChildCategoryResponse> call, Throwable t) {
                showErrorToast(R.string.something_went_wrong);
            }
        });
    }
}
