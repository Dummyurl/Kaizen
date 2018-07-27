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


import com.kaizen.listeners.ISetOnShopChildClickListerner;
import com.kaizen.models.Category;
import com.kaizen.models.ShopCategory;
import com.kaizen.models.ShopSubCategoryResponse;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopCategoryAdapter extends CommonRecyclerAdapter<ShopCategory> {
    private Context context;
    private ISetOnShopChildClickListerner iSetOnChildClickListener;
    private ShopCategory selectedShopCategory;
    private TextView tv_selected;
    private Category category;
    private RecyclerView rv_selected;
    private String subId;
    private String childId;

    public ShopCategoryAdapter(Category category, String subId, String childId, ISetOnShopChildClickListerner iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.category = category;
        this.subId = subId;
        this.childId = childId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_shop_sub_category, parent, false);

        return new ShopCategoryViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        ShopCategoryViewHolder shopCategoryViewHolder = (ShopCategoryViewHolder) holder;
        shopCategoryViewHolder.bindData(position);
    }

    private class ShopCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_sub_category;
        private RecyclerView rv_child_category;

        private ShopCategoryViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_sub_category = view.findViewById(R.id.tv_sub_category);
            rv_child_category = view.findViewById(R.id.rv_child_category);
        }

        private void bindData(int position) {
            ShopCategory shopCategory = getItem(position);
            tv_sub_category.setText(shopCategory.getMainCategoryTitle());

            if ((tv_selected != null && selectedShopCategory.getId().equals(shopCategory.getId()) || (subId != null && subId.equals(shopCategory.getId())))) {
                tv_selected = tv_sub_category;
                selectedShopCategory = shopCategory;
                showRecyclerView(shopCategory);
            } else {
                rv_child_category.setVisibility(View.GONE);
            }
        }

        private void showRecyclerView(ShopCategory shopCategory) {
            if (rv_selected != null) {
                rv_selected.setVisibility(View.GONE);
            }

            rv_child_category.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            rv_child_category.setLayoutManager(layoutManager);
            rv_selected = rv_child_category;

            final ShopSubCategoryAdapter childCategoryAdapter = new ShopSubCategoryAdapter(shopCategory, shopCategory, childId, iSetOnChildClickListener);
            rv_child_category.setAdapter(childCategoryAdapter);

            RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
            service.getShopSubcategory(PreferenceUtil.getLanguage(context), shopCategory.getId()).enqueue(new Callback<ShopSubCategoryResponse>() {
                @Override
                public void onResponse(Call<ShopSubCategoryResponse> call, Response<ShopSubCategoryResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        childCategoryAdapter.addItems(response.body().getShopsubcategorylist());
                        subId = null;
                        childId = null;
                    }
                }

                @Override
                public void onFailure(Call<ShopSubCategoryResponse> call, Throwable t) {
                    ToastUtil.showError((Activity) context, R.string.something_went_wrong);
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (rv_child_category.getVisibility() == View.GONE) {
                ShopCategory subcategory = getItem(getAdapterPosition());
                tv_selected = tv_sub_category;
                selectedShopCategory = subcategory;
                showRecyclerView(subcategory);
                iSetOnChildClickListener.onSubCategoryClick(subcategory);
            } else {
                tv_selected = null;
                selectedShopCategory = null;
                rv_child_category.setVisibility(View.GONE);
            }
        }
    }
}