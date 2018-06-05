package com.kaizen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kaizen.R;
import com.kaizen.models.FoodItem;
import com.kaizen.reterofit.APIUrls;

public class CartAdapter extends CommonRecyclerAdapter<FoodItem> {
    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        CartViewHolder cartViewHolder = (CartViewHolder) holder;
        cartViewHolder.bindData(position);
    }

    private class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tv_food;
        ImageView iv_food;

        private CartViewHolder(View view) {
            super(view);
            tv_food = view.findViewById(R.id.tv_food);
            iv_food = view.findViewById(R.id.iv_food);
        }

        private void bindData(int position) {
            FoodItem foodItem = getItem(position);
            tv_food.setText(foodItem.getAliasName());

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true);

            Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.FOOD_IMAGE_URL + foodItem.getBannerImg()).into(iv_food);

        }
    }
}
