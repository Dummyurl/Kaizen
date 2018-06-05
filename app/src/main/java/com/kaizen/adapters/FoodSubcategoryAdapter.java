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
import com.kaizen.models.Category;
import com.kaizen.models.FoodCategory;
import com.kaizen.models.FoodSubcategory;
import com.kaizen.reterofit.APIUrls;

public class FoodSubcategoryAdapter  extends CommonRecyclerAdapter<FoodSubcategory> {

    private Context context;
    private ISetOnFoodChildClickListener iSetOnChildClickListener;
    private FoodCategory foodCategory;
    private FoodSubcategory selectedChildCategory;
    private Category category;
    private RequestOptions requestOptions;
    private RelativeLayout rl_selected;
    private String childId;

    public FoodSubcategoryAdapter(Category category, FoodCategory foodCategory, String childId, ISetOnFoodChildClickListener iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.foodCategory = foodCategory;
        this.category = category;
        this.childId = childId;

        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_child_category, parent, false);

        return new ChildCategoryViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        ChildCategoryViewHolder childCategoryViewHolder = (ChildCategoryViewHolder) holder;
        childCategoryViewHolder.bindData(position);
    }

    private class ChildCategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_child_category;
        ImageView iv_child_category;
        RelativeLayout rl_background;

        private ChildCategoryViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_child_category = view.findViewById(R.id.tv_child_category);
            iv_child_category = view.findViewById(R.id.iv_child_category);
            rl_background = view.findViewById(R.id.rl_background);
        }

        private void bindData(int position) {
            FoodSubcategory childCategory = getItem(position);
            tv_child_category.setText(childCategory.getSubCategoryTitle());
            rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));

            if ((selectedChildCategory != null && selectedChildCategory.getId().equals(childCategory.getId()) || (childId != null && childId.equals(childCategory.getId())))) {
                iSetOnChildClickListener.onChildCategoryClick(category, foodCategory, childCategory);
                rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                rl_selected = rl_background;
                childId = null;
            }

            if (childCategory.getChildcat().size() > 0) {
                String imageName = childCategory.getChildcat().get(0).getMainImage();

                Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.CHILD_CATEGORY_IMAGE_URL + imageName).into(iv_child_category);
            }

        }

        @Override
        public void onClick(View v) {
            if (rl_selected != null) {
                rl_selected.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
            }
            rl_selected = rl_background;

            FoodSubcategory foodSubcategory = getItem(getAdapterPosition());
            iSetOnChildClickListener.onChildCategoryClick(category, foodCategory, foodSubcategory);
            selectedChildCategory = foodSubcategory;
            rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }
}