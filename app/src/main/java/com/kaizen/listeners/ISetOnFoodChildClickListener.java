package com.kaizen.listeners;

import com.kaizen.models.Category;
import com.kaizen.models.FoodCategory;
import com.kaizen.models.FoodItem;
import com.kaizen.models.FoodSubcategory;

public interface ISetOnFoodChildClickListener {
    void onSubCategoryClick(FoodCategory foodCategory);
    void onFoodItemClick(FoodItem foodItem);
}
