package com.smartecommerce.dao;

import com.smartecommerce.models.Category;
import com.smartecommerce.utils.JdbcUtils.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smartecommerce.utils.AppUtils.*;
import static com.smartecommerce.utils.JdbcUtils.executePreparedQuery;

/**
 * CategoryDAO handles all database operations for Category entity
 */
public class CategoryDAO {

    /**
     * Create a new category
     */
    public boolean create(Category category) {
        String sql = "INSERT INTO Categories (category_name, description) VALUES (?, ?)";
        QueryResult insertResult = executePreparedQuery(
                sql,
                category.getCategoryName(),
                category.getDescription());

        if (insertResult.hasError()) {
            printE("Error creating category: " + insertResult.getError());
            return false;
        }

        Long generatedId = insertResult.getGeneratedKey();
        if (generatedId != null) {
            category.setCategoryId(generatedId.intValue());
            return true;
        }

        Integer affectedRows = insertResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Read category by ID
     */
    public Category findById(int categoryId) {
        String sql = "SELECT * FROM Categories WHERE category_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, categoryId);

        if (queryResult.hasError()) {
            printE("Error finding category: " + queryResult.getError());
            return null;
        }

        return mapSingleCategory(queryResult);
    }

    /**
     * Get all categories
     */
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories ORDER BY category_name";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error retrieving categories: " + queryResult.getError());
            return categories;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return categories;
        }

        for (Map<String, Object> row : rows) {
            Category mappedCategory = mapRow(row);
            if (mappedCategory != null) {
                categories.add(mappedCategory);
            }
        }
        return categories;
    }

    /**
     * Update category
     */
    public boolean update(Category category) {
        String sql = "UPDATE Categories SET category_name = ?, description = ? WHERE category_id = ?";
        QueryResult updateResult = executePreparedQuery(
                sql,
                category.getCategoryName(),
                category.getDescription(),
                category.getCategoryId());

        if (updateResult.hasError()) {
            printE("Error updating category: " + updateResult.getError());
            return false;
        }

        Integer affectedRows = updateResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    /**
     * Delete category
     */
    public boolean delete(int categoryId) {
        String sql = "DELETE FROM Categories WHERE category_id = ?";
        QueryResult deleteResult = executePreparedQuery(sql, categoryId);

        if (deleteResult.hasError()) {
            printE("Error deleting category: " + deleteResult.getError());
            return false;
        }

        Integer affectedRows = deleteResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }



    /**
     * Map the first row from a query result to a Category
     */
    private Category mapSingleCategory(QueryResult queryResult) {
        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        return mapRow(rows.get(0));
    }

    /**
     * Convert a row map into a Category instance
     */
    private Category mapRow(Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            return null;
        }

        Category category = new Category();
        category.setCategoryId(asInt(row.get("category_id")));
        category.setCategoryName(asString(row.get("category_name")));
        category.setDescription(asString(row.get("description")));
        return category;
    }

}
