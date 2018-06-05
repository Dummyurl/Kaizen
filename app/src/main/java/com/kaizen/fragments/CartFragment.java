package com.kaizen.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaizen.R;
import com.kaizen.adapters.CartAdapter;
import com.kaizen.models.FoodItem;

import java.util.List;

public class CartFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_empty = view.findViewById(R.id.tv_empty);
        RecyclerView rv_cart = view.findViewById(R.id.rv_cart);
        rv_cart.setLayoutManager(new GridLayoutManager(getContext(), 2));

        CartAdapter cartAdapter = new CartAdapter();
        rv_cart.setAdapter(cartAdapter);
        List<FoodItem> foodItems = FoodItem.listAll(FoodItem.class);

        if (foodItems.size() > 0) {
            cartAdapter.addItems(foodItems);
            tv_empty.setVisibility(View.GONE);
        } else {
            tv_empty.setVisibility(View.VISIBLE);
        }
    }
}
