package com.kaizen.listeners;

import com.kaizen.models.Category;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.Subcategory;

public interface ISetOnChildClickListener {
    void onChildCategoryClick(Category category, Subcategory subcategory, ChildCategory childCategory);

    void onChildCategoryClick(Category category);
}
