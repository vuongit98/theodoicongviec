package com.theodoilamviec.Account.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.theodoilamviec.Account.listeners.ChooseCategoryListener;
import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.theodoilamviec.models.Category;

import java.util.List;

public class ChooseCategoryAdapter extends RecyclerView.Adapter<ChooseCategoryAdapter.CategoryViewHolder> {

    private final List<Category> categories;
    private final ChooseCategoryListener chooseCategoryListener;

    public ChooseCategoryAdapter(List<Category> categories, ChooseCategoryListener chooseCategoryListener) {
        this.categories = categories;
        this.chooseCategoryListener = chooseCategoryListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.set_category(categories.get(position));

        // category onclick
        holder.layout.setOnClickListener(v -> chooseCategoryListener.onCategoryClicked(categories.get(position), position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout layout;

        TextView categoryTitle;

        CategoryViewHolder(@NonNull View category_view) {
            super(category_view);

            layout = category_view.findViewById(R.id.item_category_layout);
            categoryTitle = category_view.findViewById(R.id.item_category_title);
        }

        void set_category(Category category) {
            categoryTitle.setText(category.getCategory_title());
        }

    }

}
