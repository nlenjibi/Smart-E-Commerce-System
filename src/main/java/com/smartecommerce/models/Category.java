package com.smartecommerce.models;

/**
 * Category entity for product categorization
 */
public class Category {
    private int categoryId;
    private String categoryName;
    private String description;

    // Constructors
    public Category() {
    }

    public Category(int categoryId, String categoryName, String description) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.description = description;
    }

    // Getters and Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Category category = (Category) obj;
        return categoryId == category.categoryId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(categoryId);
    }


    @Override
    public String toString() {
        return categoryName; // For ComboBox display
    }
}

