package com.kaizen.listeners;

import com.kaizen.models.Category;
import com.kaizen.models.FoodCategory;
import com.kaizen.models.FoodSubcategory;

public interface ISetOnFoodChildClickListener {
    void onChildCategoryClick(Category category, FoodCategory foodCategory, FoodSubcategory foodSubcategory);
}
