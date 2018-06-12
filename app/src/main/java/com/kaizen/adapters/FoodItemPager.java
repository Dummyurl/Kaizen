package com.kaizen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.kaizen.fragments.FoodChildCategoryFragment;
import com.kaizen.models.FoodItem;
import com.kaizen.models.FoodSubcategory;

import java.util.List;

public class FoodItemPager extends FragmentStatePagerAdapter {
    private List<FoodItem> foodItems;

    public FoodItemPager(FragmentManager fm, List<FoodItem> foodItems) {
        super(fm);
        this.foodItems = foodItems;
    }

    @Override
    public Fragment getItem(int position) {
        FoodItem foodItem = foodItems.get(position);

        FoodChildCategoryFragment fragment = FoodChildCategoryFragment.newInstance(new Gson().toJson(foodItem));
        return fragment;
    }

    @Override
    public int getCount() {
        return foodItems.size();
    }

    public int getFoodItem(long id) {

        for (int i = 0; i < foodItems.size(); i++) {
            FoodItem foodItem = foodItems.get(i);

            if (id == foodItem.getId()) {
                return i;
            }
        }

        return 0;
    }
}
