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
import com.kaizen.models.ShopCategory;
import com.kaizen.models.ShopChildCategory;
import com.kaizen.models.ShopChildCategoryResponse;
import com.kaizen.models.ShopItemListResponse;
import com.kaizen.models.ShopItemResponse;
import com.kaizen.models.ShopSubCategory;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopSubCategoryAdapter extends CommonRecyclerAdapter<ShopSubCategory> {

    private Context context;
    private ISetOnShopChildClickListerner iSetOnChildClickListener;
    private ShopSubCategory selectedShopCategory;
    private TextView tv_selected;
    private ShopCategory shopCategory;
    private RecyclerView rv_selected;
    private String subId;
    private String childId;

    public ShopSubCategoryAdapter(ShopCategory category, ShopCategory shopCategory, String childId, ISetOnShopChildClickListerner iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.shopCategory = shopCategory;
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
        ShopCategoryViewHolder childCategoryViewHolder = (ShopCategoryViewHolder) holder;
        childCategoryViewHolder.bindData(position);
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
            ShopSubCategory shopCategory = getItem(position);
            tv_sub_category.setText(shopCategory.getSubCategoryTitle());

            if ((tv_selected != null && selectedShopCategory.getId().equals(shopCategory.getId()) || (subId != null && subId.equals(shopCategory.getId())))) {
                tv_selected = tv_sub_category;
                selectedShopCategory = shopCategory;
                showRecyclerView(shopCategory);
            } else {
                rv_child_category.setVisibility(View.GONE);
            }
        }

        private void showRecyclerView(ShopSubCategory shopSubcategory) {
            if (rv_selected != null) {
                rv_selected.setVisibility(View.GONE);
            }

            rv_child_category.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            rv_child_category.setLayoutManager(layoutManager);
            rv_selected = rv_child_category;

            final ShopChildCategoryAdapter childCategoryAdapter = new ShopChildCategoryAdapter(shopCategory, shopSubcategory, iSetOnChildClickListener);
            rv_child_category.setAdapter(childCategoryAdapter);

            RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
            service.getShopItems(PreferenceUtil.getLanguage(context),shopCategory.getId(), shopSubcategory.getId()).enqueue(new Callback<ShopItemResponse>() {
                @Override
                public void onResponse(Call<ShopItemResponse> call, Response<ShopItemResponse> response) {
                    childCategoryAdapter.addItems(response.body().getShopitemslist());
                    subId = null;
                    childId = null;
                }

                @Override
                public void onFailure(Call<ShopItemResponse> call, Throwable t) {
                    ToastUtil.showError((Activity) context, R.string.something_went_wrong);
                }
            });
        }

        @Override
        public void onClick(View v)
        {
            if (rv_child_category.getVisibility() == View.GONE)
            {
                ShopSubCategory subcategory = getItem(getAdapterPosition());
                tv_selected = tv_sub_category;
                selectedShopCategory = subcategory;
                showRecyclerView(subcategory);
                iSetOnChildClickListener.onSubCategoryClick(shopCategory);
            }
            else
            {
                tv_selected = null;
                selectedShopCategory = null;
                rv_child_category.setVisibility(View.GONE);
            }
        }
    }
}