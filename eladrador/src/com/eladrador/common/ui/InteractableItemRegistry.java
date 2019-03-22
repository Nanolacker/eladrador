package com.eladrador.common.ui;

import java.util.HashMap;

import org.bukkit.inventory.ItemStack;

public class InteractableItemRegistry {

	private static final HashMap<String, InteractableItem> idMap = new HashMap<>();
	private static final HashMap<ItemStack, InteractableItem> itemStackMap = new HashMap<>();

	private InteractableItemRegistry() {
	}

	public static void register(InteractableItem interactableItem) {
		String id = interactableItem.getId();
		ItemStack itemStack = interactableItem.itemStack();
		idMap.put(id, interactableItem);
		itemStackMap.put(itemStack, interactableItem);
	}

	public static void unregister(InteractableItem interactableItem) {
		String id = interactableItem.getId();
		ItemStack itemStack = interactableItem.itemStack();
		idMap.remove(id);
		itemStackMap.remove(itemStack);
	}

	public static InteractableItem forId(String id) {
		if (id == null) {
			return null;
		}
		return idMap.get(id);
	}

	public static InteractableItem forItemStack(ItemStack itemStack) {
		if (itemStack == null) {
			return null;
		}
		ItemStack reducedSize = itemStack.clone();
		reducedSize.setAmount(1);
		return itemStackMap.get(reducedSize);
	}

	public static void printRegistry() {
		for (String id : idMap.keySet()) {
			System.out.println(id);
		}
	}

}
