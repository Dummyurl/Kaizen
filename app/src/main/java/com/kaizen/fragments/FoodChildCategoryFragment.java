package com.kaizen.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kaizen.R;
import com.kaizen.models.FoodItem;
import com.kaizen.models.FoodSubcategory;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.utils.ToastUtil;

public class FoodChildCategoryFragment extends Fragment {

    public static final String CHILD_CATEGORY = "CHILD_CATEGORY";
    private FoodItem foodItem;

    public static FoodChildCategoryFragment newInstance(String value) {
        FoodChildCategoryFragment fragment = new FoodChildCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CHILD_CATEGORY, value);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getArguments().getString(CHILD_CATEGORY);

        foodItem = new Gson().fromJson(value, FoodItem.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_sub_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_description = view.findViewById(R.id.tv_description);
        ImageView iv_child_category = view.findViewById(R.id.iv_child_category);
        RelativeLayout rl_content = view.findViewById(R.id.rl_content);
        TextView tv_add_to_cart = view.findViewById(R.id.tv_add_to_cart);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.FOOD_IMAGE_URL + foodItem.getBannerImg()).into(iv_child_category);

        if (foodItem.getDescription().trim().isEmpty()) {
            rl_content.setVisibility(View.GONE);
        } else {
            rl_content.setVisibility(View.VISIBLE);
        }

        if (foodItem.getAliasName() != null) {
            tv_title.setText(Html.fromHtml(foodItem.getAliasName()));
        }

        tv_description.setText(Html.fromHtml(foodItem.getDescription()));

        tv_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = FoodItem.save(foodItem);

                if (id == -1) {
                    ToastUtil.showError(getActivity(),R.string.unable_to_add_to_cart);
                } else {
                    ToastUtil.showSuccess(getActivity(),R.string.item_add_to_cart);
                }
            }
        });
    }
}
