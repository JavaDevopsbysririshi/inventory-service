package com.meru.ecommerce.inventory.inventoryservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meru.ecommerce.inventory.inventoryservice.dao.InventoryRepository;
import com.meru.ecommerce.inventory.inventoryservice.entity.Inventory;

@Service("InventoryService")
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	InventoryRepository ir;

	@Override
	public List<Inventory> getAllProductsInventory() {
		return ir.findAll();
	}

	@Override
	public Inventory getInventoryById(int inventoryId) {
		return ir.findOne(inventoryId);
	}

	@Override
	public Inventory getInventoryByProductId(int productId) {
		return ir.getByProductId(productId);
	}

	@Override
	public boolean createOrUpdateInventory(Inventory inventory) {
		Inventory inv = ir.save(inventory);
		boolean status = false;
		if (null != inv) {
			status = true;
		}
		// also trigger the jms message to the queue using the updated inventory
		// sendMessage("UPDATE", inv);
		return status;
	}

	@Override
	public boolean removeInventory(int inventoryId) {
		Inventory inventory = ir.findOne(inventoryId);
		ir.delete(inventoryId);
		// trigger and update via jms
		if (null != inventory) {
			// sendMessage("DELETE", inventory);
		}
		return true;
	}

	// private void sendMessage(String action, Inventory inventory) {
	// NotificationMessage msg = new NotificationMessage();
	// msg.setNotificationType(NotificationType.UPDATE);
	// if (action.equals("DELETE")) {
	// msg.setNotificationType(NotificationType.DELETE);
	// }
	// msg.setProducerApplication(ProducerApplication.INVENTORY);
	// msg.setMessage(inventory);
	// jmsTemplate.convertAndSend(INVENTORY_UPDATE_QUEUE, msg);
	// }

}
