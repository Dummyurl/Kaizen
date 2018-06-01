package com.kaizen.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaizen.R;
import com.kaizen.listeners.ISetOnCategoryClickListener;
import com.kaizen.models.Category;

public class CategoryAdapter extends CommonRecyclerAdapter<Category> {
    private TextView tv_selected;
    private Context context;
    private Category selectedCategory;
    private ISetOnCategoryClickListener iSetOnCategoryClickListener;

    public CategoryAdapter(ISetOnCategoryClickListener iSetOnCategoryClickListener) {
        this.iSetOnCategoryClickListener = iSetOnCategoryClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        categoryViewHolder.bindData(position);
    }

    public void setSelectedCategory(Category category, TextView tv_category) {
        iSetOnCategoryClickListener.onCategoryClick(category);

        if (tv_selected != null) {
            tv_selected.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rectangle_blue));
        }

        tv_category.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rectangle_orange));
        tv_selected = tv_category;
        selectedCategory = category;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_category;

        private CategoryViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_category = view.findViewById(R.id.tv_category);
        }

        public void bindData(int position) {
            Category category = getItem(position);
            tv_category.setText(category.getMainCategoryTitle());

            if (tv_selected != null && selectedCategory.getId().equals(category.getId())) {
                tv_selected.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rectangle_blue));
                tv_category.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rectangle_orange));
                tv_selected = tv_category;
                selectedCategory = category;
            } else {
                int drawableId = R.drawable.ic_rectangle_blue;

                if (position == 0) {
                    drawableId = R.drawable.ic_rectangle_orange;
                    iSetOnCategoryClickListener.onCategoryClick(category);
                    tv_selected = tv_category;
                    selectedCategory = category;
                }

                tv_category.setBackground(ContextCompat.getDrawable(context, drawableId));
            }
        }

        @Override
        public void onClick(View v) {
            Category category = getItem(getAdapterPosition());
            iSetOnCategoryClickListener.onCategoryClick(category);

            if (tv_selected != null) {
                tv_selected.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rectangle_blue));
            }

            tv_category.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_rectangle_orange));
            tv_selected = tv_category;
            selectedCategory = category;
        }
    }
}
