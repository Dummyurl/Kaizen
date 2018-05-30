package com.kaizen.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.kaizen.listeners.ISetOnChildClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.Subcategory;
import com.kaizen.reterofit.APIUrls;

public class ChildCategoryAdapter extends CommonRecyclerAdapter<ChildCategory> {

    private Context context;
    private ISetOnChildClickListener iSetOnChildClickListener;
    private Subcategory subcategory;
    private ChildCategory selectedChildCategory;
    private Category category;
    private RequestOptions requestOptions;

    public ChildCategoryAdapter(Category category, Subcategory subcategory, ISetOnChildClickListener iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.subcategory = subcategory;
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
            ChildCategory childCategory = getItem(position);
            tv_child_category.setText(childCategory.getCategoryTitle());
            rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));

            if (selectedChildCategory != null && selectedChildCategory.getId().equals(childCategory.getId())) {
                iSetOnChildClickListener.onChildCategoryClick(category, subcategory, childCategory);
                rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            }

            if (childCategory.getChildcat().size() > 0) {
                String imageName = childCategory.getChildcat().get(0).getMainImage();

                Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.CHILD_CATEGORY_IMAGE_URL + imageName).into(iv_child_category);
            }

        }

        @Override
        public void onClick(View v) {
            ChildCategory childCategory = getItem(getAdapterPosition());
            iSetOnChildClickListener.onChildCategoryClick(category, subcategory, childCategory);
            selectedChildCategory = childCategory;
            rl_background.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
    }
}
