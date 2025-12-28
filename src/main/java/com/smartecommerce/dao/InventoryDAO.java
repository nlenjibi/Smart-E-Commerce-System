package com.smartecommerce.dao;

import com.smartcommerce.model.Inventory;
import com.smartcommerce.utils.JdbcUtils.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.smartcommerce.utils.AppUtils.*;
import static com.smartcommerce.utils.JdbcUtils.executePreparedQuery;

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

    public Inventory findByProductId(int productId) {
        String sql = "SELECT * FROM Inventory WHERE product_id = ?";
        QueryResult queryResult = executePreparedQuery(sql, productId);

        if (queryResult.hasError()) {
            printE("Error finding inventory: " + queryResult.getError());
            return null;
        }

        return mapSingleInventory(queryResult);
    }

    public boolean updateQuantity(int productId, int quantity) {
        String sql = "UPDATE Inventory SET quantity_available = ? WHERE product_id = ?";
        QueryResult updateResult = executePreparedQuery(sql, quantity, productId);

        if (updateResult.hasError()) {
            printE("Error updating inventory: " + updateResult.getError());
            return false;
        }

        Integer affectedRows = updateResult.getAffectedRows();
        return affectedRows != null && affectedRows > 0;
    }

    public List<Inventory> findLowStock() {
        List<Inventory> inventoryList = new ArrayList<>();
        String sql = "SELECT * FROM Inventory WHERE quantity_available <= reorder_level ORDER BY quantity_available";
        QueryResult queryResult = executePreparedQuery(sql);

        if (queryResult.hasError()) {
            printE("Error finding low stock: " + queryResult.getError());
            return inventoryList;
        }

        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null) {
            return inventoryList;
        }

        for (Map<String, Object> row : rows) {
            Inventory mappedInventory = mapRow(row);
            if (mappedInventory != null) {
                inventoryList.add(mappedInventory);
            }
        }
        return inventoryList;
    }

    /**
     * Map the first row from a query result to an Inventory
     */
    private Inventory mapSingleInventory(QueryResult queryResult) {
        List<Map<String, Object>> rows = queryResult.getResultSet();
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        return mapRow(rows.get(0));
    }

    /**
     * Convert a row map into an Inventory instance
     */
    private Inventory mapRow(Map<String, Object> row) {
        if (row == null || row.isEmpty()) {
            return null;
        }

        Inventory inventory = new Inventory();
        inventory.setInventoryId(asInt(row.get("inventory_id")));
        inventory.setProductId(asInt(row.get("product_id")));
        inventory.setQuantityAvailable(asInt(row.get("quantity_available")));
        inventory.setReorderLevel(asInt(row.get("reorder_level")));
        return inventory;
    }

}
