package com.kaizen;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.kaizen.models.Banner;
import com.kaizen.models.BannerResponse;
import com.kaizen.models.Category;
import com.kaizen.models.CategoryResponse;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.ListChildCategory;
import com.kaizen.models.ListChildCategoryResponse;
import com.kaizen.models.Subcategory;
import com.kaizen.models.SubcategoryResponse;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zh.wang.android.yweathergetter4a.WeatherInfo;
import zh.wang.android.yweathergetter4a.YahooWeather;
import zh.wang.android.yweathergetter4a.YahooWeatherInfoListener;

public class MainActivity extends BaseActivity implements ISetOnCategoryClickListener, ISetOnChildClickListener, YahooWeatherInfoListener {

    public static final int RC_LOCATION = 4586;
    private ImageView iv_category;
    private RequestOptions requestOptions;
    private RetrofitService service;
    private ViewPager view_pager;
    private View ll_weather, ll_buttons;
    private TextView tv_time, tv_location, tv_temperature, tv_high_temperature, tv_low_temperature,
            tv_type, tv_tomorrow_high_temperature, tv_tomorrow_low_temperature;

    private ImageView iv_temperature, iv_tomorrow_temperature;
    private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_pager = findViewById(R.id.view_pager);
        TabLayout tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);

        iv_category = findViewById(R.id.iv_category);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

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

        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(getItem(-1), true);
            }
        });

        findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(getItem(+1), true);
            }
        });


        ll_weather = findViewById(R.id.ll_weather);
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            searchByGPS();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), RC_LOCATION, perms);
        }

        ll_buttons = findViewById(R.id.ll_buttons);

        tv_type = findViewById(R.id.tv_type);
        tv_time = findViewById(R.id.tv_time);
        tv_location = findViewById(R.id.tv_location);
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_high_temperature = findViewById(R.id.tv_high_temperature);
        tv_low_temperature = findViewById(R.id.tv_low_temperature);
        tv_tomorrow_high_temperature = findViewById(R.id.tv_tomorrow_high_temperature);
        tv_tomorrow_low_temperature = findViewById(R.id.tv_tomorrow_low_temperature);
        iv_temperature = findViewById(R.id.iv_temperature);
        iv_tomorrow_temperature = findViewById(R.id.iv_tomorrow_temperature);

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_time.setText(new SimpleDateFormat("HH:mm aa", Locale.getDefault()).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);
    }

    private void searchByGPS() {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.GPS);
        mYahooWeather.queryYahooWeatherByGPS(getApplicationContext(), this);
    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }

    @Override
    public void onCategoryClick(Category category) {

        service.getBanners(category.getId()).enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    List<ListChildCategory> listChildCategorys = new ArrayList<>();

                    for (Banner banner : response.body().getReports()) {
                        ListChildCategory listChildCategory = new ListChildCategory();
                        listChildCategory.setBrandName(banner.getMainTitle());
                        listChildCategory.setDescription(banner.getSubTitle());
                        listChildCategory.setMainImage(banner.getBannerImg());
                        listChildCategory.setId(banner.getId());
                        listChildCategorys.add(listChildCategory);
                    }

                    ChildCategoryPager childCategoryPager = new ChildCategoryPager(getSupportFragmentManager(), listChildCategorys);
                    view_pager.setAdapter(childCategoryPager);
                } else {
                    showErrorToast(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                showErrorToast(R.string.something_went_wrong);
            }
        });

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.CATEGORY_IMAGE_URL + category.getCategory_image()).into(iv_category);

        RecyclerView rv_sub_category = findViewById(R.id.rv_sub_category);

        if (category.getMainCategoryTitle().equalsIgnoreCase("home")) {
            ll_weather.setVisibility(View.VISIBLE);
            ll_buttons.setVisibility(View.VISIBLE);
            rv_sub_category.setVisibility(View.GONE);
        } else {
            ll_weather.setVisibility(View.GONE);
            ll_buttons.setVisibility(View.GONE);
            rv_sub_category.setVisibility(View.VISIBLE);
        }

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
    public void onChildCategoryClick(Category category, Subcategory subcategory, final ChildCategory childCategory) {
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

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
        tv_type.setText(weatherInfo.getCurrentText());
        tv_location.setText(weatherInfo.getLocationCity());
        tv_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getCurrentTemp()));
        tv_high_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getForecastInfo1().getForecastTempHigh()));
        tv_low_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getForecastInfo1().getForecastTempLow()));
        tv_tomorrow_high_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getForecastInfo2().getForecastTempLow()));
        tv_tomorrow_low_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getForecastInfo2().getForecastTempLow()));

        iv_temperature.setImageBitmap(weatherInfo.getForecastInfo1().getForecastConditionIcon());
        iv_tomorrow_temperature.setImageBitmap(weatherInfo.getForecastInfo2().getForecastConditionIcon());
    }
}
