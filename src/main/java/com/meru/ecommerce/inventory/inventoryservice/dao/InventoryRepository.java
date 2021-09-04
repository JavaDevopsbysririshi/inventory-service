package com.meru.ecommerce.inventory.inventoryservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.meru.ecommerce.inventory.inventoryservice.entity.Inventory;

@Repository("InventoryRepository")
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
   
	@Query("select inv from Inventory inv where inv.productId=:productId")
    public Inventory getByProductId(@Param("productId") int productId);
}
