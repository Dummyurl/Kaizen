package com.kaizen.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.kaizen.listeners.ISetOnShopChildClickListerner;
import com.kaizen.models.ShopSubCategory;
import com.kaizen.models.ShopCategory;
import com.kaizen.models.ShopItem;
import com.kaizen.reterofit.APIUrls;

public class ShopChildCategoryAdapter extends CommonRecyclerAdapter<ShopItem> {

    private Context context;
    private ISetOnShopChildClickListerner iSetOnChildClickListener;
    private ShopSubCategory shopItemSubcategory;
    private ShopItem selectedShopItem;
    private RequestOptions requestOptions;
    private RelativeLayout rl_selected;
    private String childId;
    private ShopCategory category;

    public ShopChildCategoryAdapter(ShopCategory category, ShopSubCategory shopItemSubcategory, ISetOnShopChildClickListerner iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.shopItemSubcategory = shopItemSubcategory;
        this.category = category;

        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_child_category, parent, false);

        return new ShopChildCategoryViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        ShopChildCategoryViewHolder shopChildCategoryViewHolder = (ShopChildCategoryViewHolder) holder;
        shopChildCategoryViewHolder.bindData(position);
    }

    private class ShopChildCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_child_category;
        ImageView iv_child_category;
        RelativeLayout rl_background;

        private ShopChildCategoryViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_child_category = view.findViewById(R.id.tv_child_category);
            iv_child_category = view.findViewById(R.id.iv_child_category);
            rl_background = view.findViewById(R.id.rl_background);
        }

        private void bindData(int position) {
            ShopItem shopItem = getItem(position);
            tv_child_category.setText(shopItem.getAliasName());
            rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));

            if ((shopItemSubcategory != null && shopItemSubcategory.getId().equals(shopItem.getId()) || (childId != null && childId.equals(shopItem.getId())))) {
                iSetOnChildClickListener.onShopItemClick(shopItem);
                rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                rl_selected = rl_background;
                childId = null;
            }

            Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.SHOP_IMAGE_URL + shopItem.getBannerImg()).into(iv_child_category);
        }

        @Override
        public void onClick(View v) {
            if (rl_selected != null) {
                rl_selected.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
            }
            rl_selected = rl_background;

            ShopItem shopItem = getItem(getAdapterPosition());
            iSetOnChildClickListener.onShopItemClick(shopItem);
            selectedShopItem = shopItem;
            rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }
}