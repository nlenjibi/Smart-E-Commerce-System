package com.smartecommerce.daos;

import com.smartcommerce.model.Review;
import com.smartcommerce.utils.JdbcUtils.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smartcommerce.utils.AppUtils.*;
import static com.smartcommerce.utils.JdbcUtils.executePreparedQuery;

/**
 * ReviewDAO handles all database operations for Review entity
 */
public class ReviewDAO {

    public boolean create(Review review) {
        String sql = "INSERT INTO Reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
        QueryResult insertResult = executePreparedQuery(
                sql,
                review.getProductId(),
                review.getUserId(),
                review.getRating(),
                review.getComment());

        if (insertResult.hasError()) {
            printE("Error creating review: " + insertResult.getError());
            return false;
        }

        Long generatedId = insertResult.getGeneratedKey();
        if (generatedId != null) {
            review.setReviewId(generatedId.intValue());
            return true;
        }

        Integer affectedRows = insertResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    public List<Review> findByProductId(int productId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, u.username FROM Reviews r " +
                "JOIN Users u ON r.user_id = u.user_id " +
                "WHERE r.product_id = ? ORDER BY r.created_at DESC";
        QueryResult queryResult = executePreparedQuery(sql, productId);

        if (queryResult.hasError()) {
            printE("Error finding reviews: " + queryResult.getError());
            return reviews;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return reviews;
        }

        for (Map<String, Object> row : rows) {
            Review mappedReview = mapRow(row);
            if (mappedReview != null) {
                reviews.add(mappedReview);
            }
        }
        return reviews;
    }

    public double getAverageRating(int productId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM Reviews WHERE product_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, productId);

        if (queryResult.hasError()) {
            printE("Error getting average rating: " + queryResult.getError());
            return 0.0;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null || rows.isEmpty()) {
            return 0.0;
        }

        Object avgValue = rows.get(0).get("avg_rating");
        if (avgValue instanceof Number) {
            return ((Number) avgValue).doubleValue();
        }
        return 0.0;
    }

    /**
     * Convert a row map into a Review instance
     */
    private Review mapRow(Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            return null;
        }

        Review review = new Review();
        review.setReviewId(asInt(row.get("review_id")));
        review.setProductId(asInt(row.get("product_id")));
        review.setUserId(asInt(row.get("user_id")));
        review.setUsername(asString(row.get("username")));
        review.setRating(asInt(row.get("rating")));
        review.setComment(asString(row.get("comment")));
        review.setCreatedAt(asLocalDateTime(row.get("created_at")));
        return review;
    }


}
