package com.kaizen.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
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
import com.kaizen.utils.PreferenceUtil;
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


        ImageView iv_child_category = view.findViewById(R.id.iv_child_category);
        RelativeLayout rl_content = view.findViewById(R.id.rl_content);
        TextView tv_add_to_cart = view.findViewById(R.id.tv_add_to_cart);

        if (PreferenceUtil.getLanguage(getContext()) == 1) {
            tv_add_to_cart.setText(R.string.add_to_cart);
        } else {
            tv_add_to_cart.setText(R.string.add_to_cart_arabic);
        }

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



        tv_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodItem.setQuantity(1);
                long id = FoodItem.save(foodItem);

                if (id == -1) {
                    ToastUtil.showError(getActivity(), R.string.unable_to_add_to_cart);
                } else {
                    ToastUtil.showSuccess(getActivity(), R.string.item_add_to_cart);
                }
            }
        });

        TextView tv_price = view.findViewById(R.id.tv_price);
        tv_price.setPaintFlags(tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        TextView tv_discount_price = view.findViewById(R.id.tv_discount_price);

        if (TextUtils.isEmpty(foodItem.getFood_discount_price())) {
            tv_discount_price.setText(String.format("SR %s", foodItem.getFood_discount_price()));
            tv_price.setVisibility(View.GONE);
        } else {
            tv_price.setVisibility(View.VISIBLE);
            tv_price.setText(String.format("(SR %s)", foodItem.getFood_price()));
            tv_discount_price.setText(String.format("SR %s", foodItem.getFood_discount_price()));
        }
    }
}
