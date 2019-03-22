package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ItemInteractionListener implements Listener {

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.setCanPickupItems(false);
	}

	@EventHandler
	private void onPlayerDisconnect(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		player.getInventory().clear();
	}

	@EventHandler
	private void onItemClickInInventory(InventoryClickEvent event) {
		if (event.getClick() == ClickType.NUMBER_KEY) {
			event.setCancelled(true);
			return;
		}
		ItemStack clickedItem = event.getCurrentItem();
		InteractableItem interactable = InteractableItemRegistry.forItemStack(clickedItem);
		if (interactable != null) {
			interactable.onClickInInventory(event);
		}
	}

	@EventHandler
	private void onItemClickOnCursor(InventoryClickEvent event) {
		ItemStack cursor = event.getCursor();
		InteractableItem interactable = InteractableItemRegistry.forItemStack(cursor);
		if (interactable != null) {
			interactable.onClickOnCursor(event);
		}
	}

	@EventHandler
	private void onItemDrag(InventoryDragEvent event) {
		ItemStack oldCursor = event.getOldCursor();
		InteractableItem interactable = InteractableItemRegistry.forItemStack(oldCursor);
		if (interactable != null) {
			interactable.onDragOnCursor(event);
		}
	}

	@EventHandler
	private void onItemClickInHand(PlayerInteractEvent event) {
		Action action = event.getAction();
		if (action == Action.PHYSICAL) {
			return;
		}
		ItemStack inHand = event.getItem();
		InteractableItem interactable = InteractableItemRegistry.forItemStack(inHand);
		if (interactable != null) {
			interactable.onClickInHand(event);
		}
	}

	@EventHandler
	private void onItemDrop(PlayerDropItemEvent event) {
		ItemStack droppedItem = event.getItemDrop().getItemStack();
		InteractableItem interactable = InteractableItemRegistry.forItemStack(droppedItem);
		if (interactable != null) {
			interactable.onDrop(event);
		}
	}

	@EventHandler
	private void onItemHoldInHand(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		int slot = event.getNewSlot();
		ItemStack heldItem = inventory.getItem(slot);
		InteractableItem interactable = InteractableItemRegistry.forItemStack(heldItem);
		if (interactable != null) {
			interactable.onHoldInHand(event);
		}
	}

}
