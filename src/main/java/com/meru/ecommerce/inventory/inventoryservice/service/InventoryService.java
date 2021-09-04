package com.meru.ecommerce.inventory.inventoryservice.service;

import java.util.List;

import com.meru.ecommerce.inventory.inventoryservice.entity.Inventory;

public interface InventoryService {
    public List<Inventory> getAllProductsInventory();

    public Inventory getInventoryById(int inventoryId);

    public Inventory getInventoryByProductId(int productId);

    public boolean createOrUpdateInventory(Inventory inventory);

    public boolean removeInventory(int inventoryId);
}
