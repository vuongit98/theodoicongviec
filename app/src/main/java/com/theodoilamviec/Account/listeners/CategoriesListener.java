package com.theodoilamviec.Account.listeners;

import com.theodoilamviec.theodoilamviec.models.Category;

public interface CategoriesListener {
    void onCategoryDeleteClicked(Category category, int position);

    void onCategoryEditClicked(Category category, int position);
}
