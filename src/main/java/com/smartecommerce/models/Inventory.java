package com.smartecommerce.models;

/**
 * Inventory entity for tracking product stock levels
 */
public class Inventory {
    private int inventoryId;
    private int productId;
    private int quantityAvailable;
    private int reorderLevel;

    // Constructors
    public Inventory() {

    }

    public Inventory(int inventoryId, int productId, int quantityAvailable, int reorderLevel) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.quantityAvailable = quantityAvailable;
        this.reorderLevel = reorderLevel;


    }

    // Getters and Setters
    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public boolean needsReorder() {
        return quantityAvailable <= reorderLevel;
    }




    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", productId=" + productId +
                ", quantityAvailable=" + quantityAvailable +
                ", reorderLevel=" + reorderLevel +
                '}';
    }
}

