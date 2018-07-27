package com.kaizen.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kaizen.R;
import com.kaizen.activities.BaseActivity;
import com.kaizen.adapters.CategoryAdapter;
import com.kaizen.adapters.ChildCategoryPager;
import com.kaizen.fragments.CartFragment;
import com.kaizen.fragments.CategoryFragment;
import com.kaizen.fragments.FoodCategoryFragment;
import com.kaizen.fragments.HomeFragment;
import com.kaizen.fragments.MoreFragment;
import com.kaizen.fragments.ShopCartFragment;
import com.kaizen.fragments.ShopCategoryFragment;
import com.kaizen.listeners.ISetOnCategoryClickListener;
import com.kaizen.listeners.ISetOnChildClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.CategoryResponse;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.ListChildCategoryResponse;
import com.kaizen.models.Subcategory;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.LocaleHelper;
import com.kaizen.utils.PreferenceUtil;

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
        service.getCategories(PreferenceUtil.getLanguage(this)).enqueue(new Callback<CategoryResponse>() {
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

        if (category.getMainCategoryTitle().equalsIgnoreCase("home") || category.getMainCategoryTitle().equalsIgnoreCase("الصفحة الرئيسية")) {
            fT.replace(R.id.frame_layout, HomeFragment.newInstance(category), HomeFragment.class.getSimpleName());
        } else if (category.getMainCategoryTitle().equalsIgnoreCase("room services") || category.getMainCategoryTitle().equalsIgnoreCase("خدمة الغرف")) {
            fT.replace(R.id.frame_layout, FoodCategoryFragment.newInstance(category), FoodCategoryFragment.class.getSimpleName());
        } else if (category.getMainCategoryTitle().equalsIgnoreCase("Gifts & Shopping") || category.getMainCategoryTitle().equalsIgnoreCase("خدمة الغرف")) {
            fT.replace(R.id.frame_layout, ShopCategoryFragment.newInstance(category), ShopCategoryFragment.class.getSimpleName());
        }else if (category.getMainCategoryTitle().equalsIgnoreCase("FoodCart") || category.getMainCategoryTitle().equalsIgnoreCase("عربة التسوق")) {
            fT.replace(R.id.frame_layout, new CartFragment(), CartFragment.class.getSimpleName());
        } else if (category.getMainCategoryTitle().equalsIgnoreCase("ShopCart") || category.getMainCategoryTitle().equalsIgnoreCase("عربة التسوق")) {
            fT.replace(R.id.frame_layout, new ShopCartFragment(), ShopCartFragment.class.getSimpleName());
        }else if (category.getMainCategoryTitle().equalsIgnoreCase("more") || category.getMainCategoryTitle().equalsIgnoreCase("أكثر من")) {
            fT.replace(R.id.frame_layout, MoreFragment.newInstance(category), MoreFragment.class.getSimpleName());
        } else {
            fT.replace(R.id.frame_layout, CategoryFragment.newInstance(category), CategoryFragment.class.getSimpleName());
        }

        fT.commit();
    }


    public void openChildMenu(String catId, String subCatId, String id) {
        for (int i = 0; i < categoryAdapter.getItemCount(); i++) {
            final Category category = categoryAdapter.getItem(i);

            if (category.getId().equals(catId)) {
                CategoryAdapter.CategoryViewHolder categoryViewHolder = (CategoryAdapter.CategoryViewHolder) rv_category.findViewHolderForAdapterPosition(i);

                if (categoryViewHolder != null) {
                    categoryAdapter.setSelectedCategory(category, categoryViewHolder.tv_category);
                }

                CategoryFragment categoryFragment = CategoryFragment.newInstance(category);
                categoryFragment.openMenu(subCatId, id);
                FragmentManager fM = getSupportFragmentManager();
                FragmentTransaction fT = fM.beginTransaction();
                fT.replace(R.id.frame_layout, categoryFragment, CategoryFragment.class.getSimpleName());
                fT.commit();
                break;
            }
        }
    }

    public void updateViews(String languageCode) {
        Context context = LocaleHelper.setLocale(this, languageCode);
        recreate();
    }
}
