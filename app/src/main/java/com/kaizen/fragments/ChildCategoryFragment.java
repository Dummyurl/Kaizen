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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kaizen.R;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.ListChildCategory;
import com.kaizen.reterofit.APIUrls;

public class ChildCategoryFragment extends Fragment {

    public static final String CHILD_CATEGORY = "CHILD_CATEGORY";
    private ListChildCategory listChildCategory;

    public static ChildCategoryFragment newInstance(String value) {
        ChildCategoryFragment fragment = new ChildCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CHILD_CATEGORY, value);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getArguments().getString(CHILD_CATEGORY);

        listChildCategory = new Gson().fromJson(value, ListChildCategory.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_description = view.findViewById(R.id.tv_description);
        ImageView iv_child_category = view.findViewById(R.id.iv_child_category);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.IMAGE_URL + listChildCategory.getCoverImage()).into(iv_child_category);

        tv_title.setText(Html.fromHtml(listChildCategory.getBrandName()));
        tv_description.setText(Html.fromHtml(listChildCategory.getDescription()));
    }
}
