package com.kaizen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.kaizen.activities.BaseActivity;
import com.kaizen.adapters.CategoryAdapter;
import com.kaizen.adapters.ChildCategoryPager;
import com.kaizen.fragments.CategoryFragment;
import com.kaizen.fragments.HomeFragment;
import com.kaizen.listeners.ISetOnCategoryClickListener;
import com.kaizen.listeners.ISetOnChildClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.CategoryResponse;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.ListChildCategoryResponse;
import com.kaizen.models.Subcategory;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kaizen.fragments.HomeFragment.REQUEST_LOCATION;

public class MainActivity extends BaseActivity implements ISetOnCategoryClickListener {


    private RetrofitService service;

    private CategoryAdapter categoryAdapter;
    private RecyclerView rv_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_date = findViewById(R.id.tv_date);
        DateFormat df = new SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault());
        String date = df.format(new Date().getTime());
        tv_date.setText(date);

        rv_category = findViewById(R.id.rv_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_category.setLayoutManager(layoutManager);

        categoryAdapter = new CategoryAdapter(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION) {
            FragmentManager fM = getSupportFragmentManager();
            Fragment fragment = fM.findFragmentByTag(HomeFragment.class.getSimpleName());

            if (fragment != null && fragment instanceof HomeFragment) {
                HomeFragment homeFragment = (HomeFragment) fragment;
                homeFragment.searchByGPS();
            }
        }
    }

    @Override
    public void onCategoryClick(Category category) {
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();

        if (category.getMainCategoryTitle().equalsIgnoreCase("home")) {
            fT.replace(R.id.frame_layout, HomeFragment.newInstance(category), HomeFragment.class.getSimpleName());
        } else {
            fT.replace(R.id.frame_layout, CategoryFragment.newInstance(category), CategoryFragment.class.getSimpleName());
        }

        fT.commit();
    }


    public void openChildMenu(String catId, String subCatId, String id) {
        for (int i = 1; i < categoryAdapter.getItemCount(); i++) {
            Category category = categoryAdapter.getItem(i);

//            if (category.getId().equals(catId)) {
            CategoryAdapter.CategoryViewHolder categoryViewHolder = (CategoryAdapter.CategoryViewHolder) rv_category.findViewHolderForAdapterPosition(i);
            categoryAdapter.setSelectedCategory(category, categoryViewHolder.tv_category);

//            }
            break;
        }
    }
}
