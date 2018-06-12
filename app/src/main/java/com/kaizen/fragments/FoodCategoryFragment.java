package com.kaizen.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kaizen.activities.MainActivity;
import com.kaizen.R;
import com.kaizen.adapters.ChildCategoryPager;
import com.kaizen.adapters.FoodCategoryAdapter;
import com.kaizen.adapters.FoodItemPager;
import com.kaizen.listeners.DateTimeSetListener;
import com.kaizen.listeners.ISetOnFoodChildClickListener;
import com.kaizen.models.Banner;
import com.kaizen.models.BannerResponse;
import com.kaizen.models.Category;
import com.kaizen.models.FoodCategory;
import com.kaizen.models.FoodCategoryResponse;
import com.kaizen.models.FoodItem;
import com.kaizen.models.FoodItemListResponse;
import com.kaizen.models.FoodItemResponse;
import com.kaizen.models.FoodSubcategory;
import com.kaizen.models.ListChildCategory;
import com.kaizen.models.RequestResponse;
import com.kaizen.models.Settings;
import com.kaizen.models.SettingsResponse;
import com.kaizen.models.SubcategoryResponse;
import com.kaizen.models.User;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.DateTimeUtil;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodCategoryFragment extends Fragment implements ISetOnFoodChildClickListener, View.OnClickListener {

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
        final FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(category, subCatId, childId, this);
        rv_sub_category.setAdapter(foodCategoryAdapter);

        service.getFoodCategory().enqueue(new Callback<FoodCategoryResponse>() {
            @Override
            public void onResponse(Call<FoodCategoryResponse> call, Response<FoodCategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    foodCategoryAdapter.addItems(response.body().getFoodmaincategory());
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

        final TextView tv_service_time = view.findViewById(R.id.tv_service_time);
        view.findViewById(R.id.tv_feed_back).setOnClickListener(this);
        view.findViewById(R.id.tv_collect_tray).setOnClickListener(this);

        service.getSettings().enqueue(new Callback<SettingsResponse>() {
            @Override
            public void onResponse(Call<SettingsResponse> call, Response<SettingsResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    SettingsResponse settingsResponse = response.body();

                    if (settingsResponse.getSettings().size() > 0) {
                        Settings settings = settingsResponse.getSettings().get(0);
                        tv_service_time.setText("Service Time: " + settings.getName());
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingsResponse> call, Throwable t) {

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
    public void onFoodItemClick(FoodItem foodItem) {
        try {
            FoodItemPager childCategoryPager = (FoodItemPager) view_pager.getAdapter();

            if (childCategoryPager != null) {
                int position = childCategoryPager.getFoodItem(foodItem.getId());
                view_pager.setCurrentItem(position);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        childId = null;
        subCatId = null;
    }

    @Override
    public void onSubCategoryClick(FoodCategory foodCategory) {
        service.getFoodItems(foodCategory.getId()).enqueue(new Callback<FoodItemListResponse>() {
            @Override
            public void onResponse(Call<FoodItemListResponse> call, Response<FoodItemListResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    FoodItemPager childCategoryPager = new FoodItemPager(getChildFragmentManager(), response.body().getFooditemlist());
                    view_pager.setAdapter(childCategoryPager);
                } else {
                    ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<FoodItemListResponse> call, Throwable t) {
                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_feed_back:
                sendFeedBack();
                break;
            case R.id.tv_collect_tray:
                collectTray();
                break;
        }
    }

    private void sendFeedBack() {
        final User user = PreferenceUtil.getUser(getContext());

        View feedBackView = getLayoutInflater().inflate(R.layout.dialog_feedback, null);
        final EditText et_name = feedBackView.findViewById(R.id.et_name);
        final EditText et_description = feedBackView.findViewById(R.id.et_description);

        AlertDialog.Builder feedBackBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.feedback)
                .setView(feedBackView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String description = et_description.getText().toString().trim();

                        if (name.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_name);
                        } else if (description.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_description);
                        } else {
                            service.sendFeedBack(user.getRoomno(), name, description).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            ToastUtil.showSuccess(getActivity(), requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            ToastUtil.showError(getActivity(), requestResponse.getError());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RequestResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        feedBackBuilder.create().show();
    }

    private void collectTray() {
        final User user = PreferenceUtil.getUser(getContext());
        View collectTrayView = getLayoutInflater().inflate(R.layout.dialog_collect_tray, null);
        final EditText et_name = collectTrayView.findViewById(R.id.et_name);
        final EditText et_time = collectTrayView.findViewById(R.id.et_time);

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimeUtil().datePicker(getContext(), new DateTimeSetListener() {
                    @Override
                    public void onDateTimeSet(String date) {
                        et_time.setText(date);
                    }
                });
            }
        });

        AlertDialog.Builder collectTrayBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.collect_tray)
                .setView(collectTrayView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String timing = et_time.getText().toString().trim();

                        if (name.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_name);
                        } else if (timing.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_date_time);
                        } else {
                            service.collectTray(user.getRoomno(), name, timing).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            ToastUtil.showSuccess(getActivity(), requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            ToastUtil.showError(getActivity(), requestResponse.getError());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RequestResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        collectTrayBuilder.create().show();
    }
}

