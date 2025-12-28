package com.smartecommerce.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity representing items available in the e-commerce store
 */
public class Product {
    private  int productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private int categoryId;
    private String categoryName; // For display purposes
    private String imageUrl; // Product image URL
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int stockQuantity;

    // Constructors
    public Product() {
    }

    public Product(int productId, String productName, String description, BigDecimal price, int categoryId, String imageUrl, int stockQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl; // Can be null if not provided
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor without imageUrl (defaults to null)
    public Product(int productId, String productName, String description, BigDecimal price, int categoryId, int stockQuantity) {
        this(productId, productName, description, price, categoryId, null, stockQuantity);
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Product product = (Product) obj;
        return productId == product.productId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(productId);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", categoryName='" + categoryName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

