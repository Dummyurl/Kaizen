package com.kaizen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.gson.Gson;
import com.kaizen.fragments.ShopChildCategoryFragment;
import com.kaizen.models.ShopItem;

import java.util.List;

public class ShopItemPager extends FragmentStatePagerAdapter {
    private List<ShopItem> shopItems;

    public ShopItemPager(FragmentManager fm, List<ShopItem> shopItems) {
        super(fm);
        this.shopItems = shopItems;
    }

    @Override
    public Fragment getItem(int position) {
        ShopItem shopItem = shopItems.get(position);

        ShopChildCategoryFragment fragment = ShopChildCategoryFragment.newInstance(new Gson().toJson(shopItem));
        return fragment;
    }

    @Override
    public int getCount() {
        return shopItems.size();
    }

    public int getShopItem(long id) {

        for (int i = 0; i < shopItems.size(); i++) {
            ShopItem shopItem = shopItems.get(i);


        }

        return 0;
    }
}
