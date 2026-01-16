package com.smartecommerce.dao;

import com.smartecommerce.models.Inventory;
import com.smartecommerce.utils.JdbcUtils.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smartecommerce.utils.AppUtils.*;
import static com.smartecommerce.utils.JdbcUtils.executePreparedQuery;

/**
 * InventoryDAO handles all database operations for Inventory entity
 */
public class InventoryDAO {

    public boolean create(Inventory inventory) {
        String sql = "INSERT INTO Inventory (product_id, quantity_available, reorder_level) VALUES (?, ?, ?)";
        QueryResult insertResult = executePreparedQuery(
                sql,
                inventory.getProductId(),
                inventory.getQuantityAvailable(),
                inventory.getReorderLevel());

        if (insertResult.hasError()) {
            printE("Error creating inventory: " + insertResult.getError());
            return false;
        }

        Long generatedId = insertResult.getGeneratedKey();
        if (generatedId != null) {
            inventory.setInventoryId(generatedId.intValue());
            return true;
        }

        Integer affectedRows = insertResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }



}
