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
import com.kaizen.listeners.ISetOnFoodChildClickListener;
import com.kaizen.models.FoodCategory;
import com.kaizen.models.FoodItem;
import com.kaizen.models.FoodSubcategory;
import com.kaizen.reterofit.APIUrls;

public class FoodChildCategoryAdapter extends CommonRecyclerAdapter<FoodItem> {

    private Context context;
    private ISetOnFoodChildClickListener iSetOnChildClickListener;
    private FoodSubcategory foodItemSubcategory;
    private FoodItem selectedFoodItem;
    private RequestOptions requestOptions;
    private RelativeLayout rl_selected;
    private String childId;
    private FoodCategory category;

    public FoodChildCategoryAdapter(FoodCategory category, FoodSubcategory foodItemSubcategory, ISetOnFoodChildClickListener iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.foodItemSubcategory = foodItemSubcategory;
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

        return new FoodChildCategoryViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        FoodChildCategoryViewHolder foodChildCategoryViewHolder = (FoodChildCategoryViewHolder) holder;
        foodChildCategoryViewHolder.bindData(position);
    }

    private class FoodChildCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_child_category;
        ImageView iv_child_category;
        RelativeLayout rl_background;

        private FoodChildCategoryViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_child_category = view.findViewById(R.id.tv_child_category);
            iv_child_category = view.findViewById(R.id.iv_child_category);
            rl_background = view.findViewById(R.id.rl_background);
        }

        private void bindData(int position) {
            FoodItem foodItem = getItem(position);
            tv_child_category.setText(foodItem.getAliasName());
            rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));

            if ((foodItemSubcategory != null && foodItemSubcategory.getId().equals(foodItem.getId()) || (childId != null && childId.equals(foodItem.getId())))) {
                iSetOnChildClickListener.onFoodItemClick(foodItem, position);
                rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                rl_selected = rl_background;
                childId = null;
            }

            Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.FOOD_IMAGE_URL + foodItem.getBannerImg()).into(iv_child_category);
        }

        @Override
        public void onClick(View v) {
            if (rl_selected != null) {
                rl_selected.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
            }
            rl_selected = rl_background;

            FoodItem foodItem = getItem(getAdapterPosition());
            iSetOnChildClickListener.onFoodItemClick(foodItem,getAdapterPosition());
            selectedFoodItem = foodItem;
            rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }
}