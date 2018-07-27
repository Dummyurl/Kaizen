package com.kaizen.listeners;

import com.kaizen.models.ShopCategory;
import com.kaizen.models.ShopItem;

public interface ISetOnShopChildClickListerner {
    void onSubCategoryClick(ShopCategory shopCategory);
    void onShopItemClick(ShopItem shopItem);
}
