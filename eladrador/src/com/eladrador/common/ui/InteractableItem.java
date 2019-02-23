package com.eladrador.common.ui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class InteractableItem {

	private final String id;
	private final ItemStack itemStack;

	public InteractableItem(String id, ItemStack itemStack) {
		this.id = id;
		this.itemStack = itemStack;
	}

	public String getId() {
		return id;
	}

	public ItemStack itemStack() {
		return itemStack.clone();
	}

	public ItemStack itemStack(int amount) {
		ItemStack out = itemStack.clone();
		out.setAmount(amount);
		return out;
	}

	public void onClickInInventory(InventoryClickEvent event) {
	}

	public void onClickOnCursor(InventoryClickEvent event) {
	}

	public void onDragOnCursor(InventoryDragEvent event) {
	}

	public void onDrop(PlayerDropItemEvent event) {
	}

	public void onHoldInHand(PlayerItemHeldEvent event) {
	}

}
