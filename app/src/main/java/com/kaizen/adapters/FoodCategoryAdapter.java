package com.kaizen.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaizen.R;
import com.kaizen.fragments.FoodFragment;
import com.kaizen.listeners.ISetOnFoodChildClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.FoodCategory;
import com.kaizen.models.FoodSubcategoryResponse;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodCategoryAdapter extends CommonRecyclerAdapter<FoodCategory> {
    private Context context;
    private ISetOnFoodChildClickListener iSetOnChildClickListener;
    private FoodCategory selectedFoodCategory;
    private TextView tv_selected;
    private Category category;
    private RecyclerView rv_selected;
    private String subId;
    private String childId;

    public FoodCategoryAdapter(Category category, String subId, String childId, ISetOnFoodChildClickListener iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.category = category;
        this.subId = subId;
        this.childId = childId;
    }

    public FoodCategoryAdapter(FoodFragment foodFragment) {
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_food_sub_category, parent, false);

        return new FoodCategoryViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        FoodCategoryViewHolder foodCategoryViewHolder = (FoodCategoryViewHolder) holder;
        foodCategoryViewHolder.bindData(position);
    }

    private class FoodCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_sub_category;
        private RecyclerView rv_child_category;

        private FoodCategoryViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_sub_category = view.findViewById(R.id.tv_sub_category);
            rv_child_category = view.findViewById(R.id.rv_child_category);
        }

        private void bindData(int position) {
            FoodCategory foodCategory = getItem(position);
            tv_sub_category.setText(foodCategory.getMainCategoryTitle());

            if ((tv_selected != null && selectedFoodCategory.getId().equals(foodCategory.getId()) || (subId != null && subId.equals(foodCategory.getId())))) {
                tv_selected = tv_sub_category;
                selectedFoodCategory = foodCategory;
                showRecyclerView(foodCategory);
            } else {
                rv_child_category.setVisibility(View.GONE);
            }
        }

        private void showRecyclerView(FoodCategory foodCategory) {
            if (rv_selected != null) {
                rv_selected.setVisibility(View.GONE);
            }

            rv_child_category.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            rv_child_category.setLayoutManager(layoutManager);
            rv_selected = rv_child_category;

            final FoodSubcategoryAdapter childCategoryAdapter = new FoodSubcategoryAdapter(foodCategory, foodCategory, childId, iSetOnChildClickListener);
            rv_child_category.setAdapter(childCategoryAdapter);

            RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
            service.getFoodSubcategory(PreferenceUtil.getLanguage(context),foodCategory.getId()).enqueue(new Callback<FoodSubcategoryResponse>() {
                @Override
                public void onResponse(Call<FoodSubcategoryResponse> call, Response<FoodSubcategoryResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        childCategoryAdapter.addItems(response.body().getFoodsubcategorylist());
                        subId = null;
                        childId = null;
                    }
                }

                @Override
                public void onFailure(Call<FoodSubcategoryResponse> call, Throwable t) {
                    ToastUtil.showError((Activity) context, R.string.something_went_wrong);
                }
            });
        }

        @Override
        public void onClick(View v) {
            FoodCategory subcategory = getItem(getAdapterPosition());
            tv_selected = tv_sub_category;
            selectedFoodCategory = subcategory;
            showRecyclerView(subcategory);
            iSetOnChildClickListener.onSubCategoryClick(subcategory);
        }
    }
}