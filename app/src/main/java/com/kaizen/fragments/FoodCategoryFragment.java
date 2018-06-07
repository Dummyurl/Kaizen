package com.kaizen.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kaizen.activities.MainActivity;
import com.kaizen.R;
import com.kaizen.adapters.ChildCategoryPager;
import com.kaizen.adapters.FoodSubcategoryAdapter;
import com.kaizen.adapters.FoodSubcategoryPager;
import com.kaizen.listeners.ISetOnFoodChildClickListener;
import com.kaizen.models.Banner;
import com.kaizen.models.BannerResponse;
import com.kaizen.models.Category;
import com.kaizen.models.FoodCategory;
import com.kaizen.models.FoodCategoryResponse;
import com.kaizen.models.FoodItemResponse;
import com.kaizen.models.FoodSubcategory;
import com.kaizen.models.ListChildCategory;
import com.kaizen.models.SubcategoryResponse;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodCategoryFragment extends Fragment implements ISetOnFoodChildClickListener {

    private static final String CATEGORY = "CATEGORY";
    private ImageView iv_category;
    private RequestOptions requestOptions;
    private ViewPager view_pager;
    private RetrofitService service;
    private Category category;
    private String subCatId, childId;

    public static FoodCategoryFragment newInstance(Category category) {
        FoodCategoryFragment categoryFragment = new FoodCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, new Gson().toJson(category));
        categoryFragment.setArguments(bundle);

        return categoryFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String value = getArguments().getString(CATEGORY);
        category = new Gson().fromJson(value, Category.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view_pager = view.findViewById(R.id.view_pager);
        TabLayout tab_layout = view.findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);

        iv_category = view.findViewById(R.id.iv_category);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        view.findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(getItem(-1), true);
            }
        });

        view.findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(getItem(+1), true);
            }
        });

        service = RetrofitInstance.createService(RetrofitService.class);
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
                        listChildCategory.setEnquiry(banner.isEnquiry());
                        listChildCategory.setEnableClick(true);
                        listChildCategory.setCatId(banner.getCatId());
                        listChildCategory.setMainCatId(banner.getMainCatId());
                        listChildCategory.setSubCatId(banner.getSubCatId());
                        listChildCategorys.add(listChildCategory);
                    }

                    if (getActivity() != null && !getActivity().isFinishing()) {
                        ChildCategoryPager childCategoryPager = new ChildCategoryPager(getChildFragmentManager(), listChildCategorys);
                        view_pager.setAdapter(childCategoryPager);
                    }
                } else {
                    ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
            }
        });

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.CATEGORY_IMAGE_URL + category.getCategory_image()).into(iv_category);

        RecyclerView rv_sub_category = view.findViewById(R.id.rv_sub_category);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_sub_category.setLayoutManager(layoutManager);
        final FoodSubcategoryAdapter foodSubcategoryAdapter = new FoodSubcategoryAdapter(category, subCatId, childId, this);
        rv_sub_category.setAdapter(foodSubcategoryAdapter);

        service.getFoodCategory().enqueue(new Callback<FoodCategoryResponse>() {
            @Override
            public void onResponse(Call<FoodCategoryResponse> call, Response<FoodCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodSubcategoryAdapter.addItems(response.body().getFoodmaincategory());
                } else {
                    ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<FoodCategoryResponse> call, Throwable t) {
                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
            }
        });

        service.getSubCategories(category.getId()).enqueue(new Callback<SubcategoryResponse>() {
            @Override
            public void onResponse(Call<SubcategoryResponse> call, Response<SubcategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                } else {
                    ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<SubcategoryResponse> call, Throwable t) {
                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
            }
        });

        setupAutoPager();

        iv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null && !mainActivity.isFinishing()) {
                    mainActivity.openChildMenu(category.getMainCatId(), category.getSubCatId(), category.getCatId());
                }
            }
        });
    }

    private void setupAutoPager() {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {

                if (view_pager != null) {
                    int currentPage = view_pager.getCurrentItem();

                    int count = 0;

                    if (view_pager.getAdapter() != null) {
                        count = view_pager.getAdapter().getCount();
                    }

                    if (currentPage == count - 1) {
                        currentPage = 0;
                    } else {
                        currentPage++;
                    }

                    view_pager.setCurrentItem(currentPage);
                }
            }
        };


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 5000, 5000);
    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }

    public void openMenu(String subCatId, String childId) {
        this.subCatId = subCatId;
        this.childId = childId;
    }

    @Override
    public void onChildCategoryClick(Category category, FoodCategory foodCategory, FoodSubcategory foodSubcategory) {
        try {
            service.getFoodItems(foodCategory.getId(), foodCategory.getId()).enqueue(new Callback<FoodItemResponse>() {
                @Override
                public void onResponse(Call<FoodItemResponse> call, Response<FoodItemResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        FoodSubcategoryPager childCategoryPager = new FoodSubcategoryPager(getChildFragmentManager(), response.body().getFooditemslist());
                        view_pager.setAdapter(childCategoryPager);
                    } else {
                        ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                    }
                }

                @Override
                public void onFailure(Call<FoodItemResponse> call, Throwable t) {
                    ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        childId = null;
        subCatId = null;
    }
}

