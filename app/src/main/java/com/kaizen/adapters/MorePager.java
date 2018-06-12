package com.kaizen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.kaizen.fragments.ChildCategoryFragment;
import com.kaizen.fragments.MoreCategoryFragment;
import com.kaizen.models.ListChildCategory;

import java.util.List;

public class MorePager extends FragmentStatePagerAdapter {
    private List<ListChildCategory> listChildCategories;

    public MorePager(FragmentManager fm, List<ListChildCategory> listChildCategories) {
        super(fm);
        this.listChildCategories = listChildCategories;
    }

    @Override
    public Fragment getItem(int position) {
        ListChildCategory listChildCategory = listChildCategories.get(position);

        MoreCategoryFragment fragment = MoreCategoryFragment.newInstance(new Gson().toJson(listChildCategory));
        return fragment;
    }

    @Override
    public int getCount() {
        return listChildCategories.size();
    }
}
