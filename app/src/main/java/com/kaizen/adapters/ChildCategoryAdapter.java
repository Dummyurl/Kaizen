package com.kaizen.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaizen.R;
import com.kaizen.listeners.ISetOnChildClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.Subcategory;

public class ChildCategoryAdapter extends CommonRecyclerAdapter<ChildCategory> {

    private Context context;
    private ISetOnChildClickListener iSetOnChildClickListener;
    private Subcategory subcategory;
    private ChildCategory selectedChildCategory;
    private Category category;

    public ChildCategoryAdapter(Category category, Subcategory subcategory, ISetOnChildClickListener iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.subcategory = subcategory;
        this.category = category;
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

        private ChildCategoryViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_child_category = view.findViewById(R.id.tv_child_category);
        }

        private void bindData(int position) {
            ChildCategory childCategory = getItem(position);
            tv_child_category.setText(childCategory.getCategoryTitle());

            if (selectedChildCategory != null && selectedChildCategory.getId().equals(childCategory.getId())) {
                iSetOnChildClickListener.onChildCategoryClick(category, subcategory, childCategory);
            } else {
                if (position == 0) {
                    iSetOnChildClickListener.onChildCategoryClick(category, subcategory, childCategory);
                }
            }
        }

        @Override
        public void onClick(View v) {
            ChildCategory childCategory = getItem(getAdapterPosition());
            iSetOnChildClickListener.onChildCategoryClick(category, subcategory, childCategory);
            selectedChildCategory = childCategory;
        }
    }
}
