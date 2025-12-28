package com.smartecommerce.models;

import java.time.LocalDateTime;

import static com.smartcommerce.utils.ValidationUtil.validateRating;

/**
 * Review entity for product reviews and ratings
 */
public class Review {
    private int reviewId;
    private int productId;
    private int userId;
    private String username; // For display
    private int rating; // 1-5
    private String comment;
    private LocalDateTime createdAt;
    private boolean isVerifiedPurchase;



    // Constructors
    public Review() {
    }

    public Review(int reviewId, int productId, int userId, int rating, String comment, boolean isVerifiedPurchase) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
        this.isVerifiedPurchase = isVerifiedPurchase;
    }

    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

//    public void setRating(int rating) {
//        this.rating = rating;
//    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    // Setters (with validation)
    public void setRating(int rating) {
        validateRating(rating);

        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", productId=" + productId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

