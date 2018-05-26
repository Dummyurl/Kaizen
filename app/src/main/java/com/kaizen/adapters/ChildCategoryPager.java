package com.kaizen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.google.gson.Gson;
import com.kaizen.fragments.ChildCategoryFragment;
import com.kaizen.models.ListChildCategory;

import java.util.List;

public class ChildCategoryPager extends FragmentPagerAdapter {
    private List<ListChildCategory> listChildCategories;

    public ChildCategoryPager(FragmentManager fm, List<ListChildCategory> listChildCategories) {
        super(fm);
        this.listChildCategories = listChildCategories;
    }

    @Override
    public Fragment getItem(int position) {
        ListChildCategory listChildCategory = listChildCategories.get(0);
        String value = new Gson().toJson(listChildCategory);
        return ChildCategoryFragment.newInstance(value);
    }

    @Override
    public int getCount() {
        return listChildCategories.size();
    }
}
