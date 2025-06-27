package com.drcita.user.models.fliter;


public class FilterCategoryModel {

    private String categoryName;
    private String categoryId;
    private int selectedCount;
    private boolean isSelected;

    public FilterCategoryModel(String categoryName,String categoryId,boolean isSelected) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.selectedCount = 0;
        this.isSelected = isSelected;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public void setSelectedCount(int selectedCount) {
        this.selectedCount = selectedCount;
    }

    public void incrementSelectedCount() {
        this.selectedCount++;
    }

    public void decrementSelectedCount() {
        if (this.selectedCount > 0) {
            this.selectedCount--;
        }
    }

    public void clearSelection() {
        this.selectedCount = 0;
    }
}
