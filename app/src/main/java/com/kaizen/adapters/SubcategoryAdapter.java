package com.kaizen.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaizen.R;
import com.kaizen.listeners.ISetOnChildClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.ChildCategoryResponse;
import com.kaizen.models.Subcategory;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubcategoryAdapter extends CommonRecyclerAdapter<Subcategory> {
    private Context context;
    private ISetOnChildClickListener iSetOnChildClickListener;
    private Subcategory selectedSubcategory;
    private TextView tv_selected;
    private Category category;
    private RecyclerView rv_selected;

    public SubcategoryAdapter(Category category, ISetOnChildClickListener iSetOnChildClickListener) {
        this.iSetOnChildClickListener = iSetOnChildClickListener;
        this.category = category;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_sub_category, parent, false);

        return new SubcategoryViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        SubcategoryViewHolder subcategoryViewHolder = (SubcategoryViewHolder) holder;
        subcategoryViewHolder.bindData(position);
    }

    private class SubcategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_sub_category;
        private RecyclerView rv_child_category;

        private SubcategoryViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            tv_sub_category = view.findViewById(R.id.tv_sub_category);
            rv_child_category = view.findViewById(R.id.rv_child_category);
        }

        private void bindData(int position) {
            Subcategory subcategory = getItem(position);
            tv_sub_category.setText(subcategory.getSubCategoryTitle());

            if (tv_selected != null && selectedSubcategory.getId().equals(subcategory.getId())) {
                tv_selected = tv_sub_category;
                selectedSubcategory = subcategory;
                showRecyclerView(subcategory);
            } else {

                if (position == 0) {
                    tv_selected = tv_sub_category;
                    selectedSubcategory = subcategory;

                    showRecyclerView(subcategory);
                } else {
                    rv_child_category.setVisibility(View.GONE);
                }
            }
        }

        private void showRecyclerView(Subcategory subcategory) {
            if (rv_selected != null) {
                rv_selected.setVisibility(View.GONE);
            }

            rv_child_category.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            rv_child_category.setLayoutManager(layoutManager);
            rv_selected = rv_child_category;

            final ChildCategoryAdapter childCategoryAdapter = new ChildCategoryAdapter(category, subcategory, iSetOnChildClickListener);
            rv_child_category.setAdapter(childCategoryAdapter);

            RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
            service.getChildCategory(category.getId(), subcategory.getId()).enqueue(new Callback<ChildCategoryResponse>() {
                @Override
                public void onResponse(Call<ChildCategoryResponse> call, Response<ChildCategoryResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        childCategoryAdapter.addItems(response.body().getChildcategory());
                    }
                }

                @Override
                public void onFailure(Call<ChildCategoryResponse> call, Throwable t) {

                }
            });
        }

        @Override
        public void onClick(View v) {
            Subcategory subcategory = getItem(getAdapterPosition());
            tv_selected = tv_sub_category;
            selectedSubcategory = subcategory;
            showRecyclerView(subcategory);
        }
    }
}
