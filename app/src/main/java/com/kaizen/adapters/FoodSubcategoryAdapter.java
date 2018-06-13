package com.kaizen.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kaizen.R;
import com.kaizen.listeners.ISetOnFoodChildClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.FoodCategory;
import com.kaizen.models.FoodItemResponse;
import com.kaizen.models.FoodSubcategory;
import com.kaizen.models.FoodSubcategoryResponse;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodSubcategoryAdapter extends CommonRecyclerAdapter<FoodSubcategory> {

    private Context context;
    private ISetOnFoodChildClickListener iSetOnChildClickListener;
    private FoodSubcategory selectedFoodCategory;
    private TextView tv_selected;
    private FoodCategory foodCategory;
    private RecyclerView rv_selected;
    private String subId;
    private String childId;

    public FoodSubcategoryAdapter(FoodCategory category, FoodCategory foodCategory, String childId, ISetOnFoodChildClickListener iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.foodCategory = foodCategory;
        this.childId = childId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_sub_category, parent, false);

        return new FoodCategoryViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        FoodCategoryViewHolder childCategoryViewHolder = (FoodCategoryViewHolder) holder;
        childCategoryViewHolder.bindData(position);
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
            FoodSubcategory foodCategory = getItem(position);
            tv_sub_category.setText(foodCategory.getSubCategoryTitle());

            if ((tv_selected != null && selectedFoodCategory.getId().equals(foodCategory.getId()) || (subId != null && subId.equals(foodCategory.getId())))) {
                tv_selected = tv_sub_category;
                selectedFoodCategory = foodCategory;
                showRecyclerView(foodCategory);
            } else {
                rv_child_category.setVisibility(View.GONE);
            }
        }

        private void showRecyclerView(FoodSubcategory foodSubcategory) {
            if (rv_selected != null) {
                rv_selected.setVisibility(View.GONE);
            }

            rv_child_category.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            rv_child_category.setLayoutManager(layoutManager);
            rv_selected = rv_child_category;

            final FoodChildCategoryAdapter childCategoryAdapter = new FoodChildCategoryAdapter(foodCategory, foodSubcategory, iSetOnChildClickListener);
            rv_child_category.setAdapter(childCategoryAdapter);

            RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
            service.getFoodItems(PreferenceUtil.getLanguage(context),foodCategory.getId(), foodSubcategory.getId()).enqueue(new Callback<FoodItemResponse>() {
                @Override
                public void onResponse(Call<FoodItemResponse> call, Response<FoodItemResponse> response) {
                    childCategoryAdapter.addItems(response.body().getFooditemslist());
                    subId = null;
                    childId = null;
                }

                @Override
                public void onFailure(Call<FoodItemResponse> call, Throwable t) {
                    ToastUtil.showError((Activity) context, R.string.something_went_wrong);
                }
            });
        }

        @Override
        public void onClick(View v) {
            FoodSubcategory subcategory = getItem(getAdapterPosition());
            tv_selected = tv_sub_category;
            selectedFoodCategory = subcategory;
            showRecyclerView(subcategory);
        }
    }
}