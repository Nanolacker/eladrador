package com.eladrador.common.item;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.eladrador.common.event.Event;
import com.eladrador.common.utils.StrUtils;

public final class GameItem {

	private String name;
	private GameItemType type;
	private Material material;
	private GameItemQuality quality;
	private String flavorText;
	private int maxStackSize;
	private Event onLeftClickInHand;
	private Event onRightClickInHand;
	private Event onSpecialUse;
	private ArrayList<GameItemStack> itemStacks;

	public GameItem(String name, GameItemType type, Material material, GameItemQuality quality, String flavorText,
			int maxStackSize) {
		this.name = name;
		this.type = type;
		this.material = material;
		this.quality = quality;
		this.flavorText = flavorText;
		this.maxStackSize = maxStackSize;
		onLeftClickInHand = new Event();
		onRightClickInHand = new Event();
		onSpecialUse = new Event();
		itemStacks = new ArrayList<GameItemStack>();
	}

	public String getName() {
		return name;
	}

	public String getFlavorText() {
		return flavorText;
	}

	public void setFlavorText(String text) {
		flavorText = text;
		updateItemStackButtons();
	}

	public GameItemType getType() {
		return type;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
		updateItemStackButtons();
	}

	public GameItemQuality getQuality() {
		return quality;
	}

	public int getMaxStackSize() {
		return maxStackSize;
	}

	public Event getOnLeftClickInHand() {
		if (!type.canBeInMainHand()) {
			throw new IllegalStateException("This item (type " + type + ") cannot be held in the main hand.");
		}
		return onLeftClickInHand;
	}

	public Event getOnRightClickInHand() {
		if (!type.canBeInMainHand()) {
			throw new IllegalStateException("This item (type " + type + ") cannot be held in the main hand.");
		}
		return onRightClickInHand;
	}

	public Event getOnSpecialUse() {
		if (!type.canHaveSpecialUse()) {
			throw new IllegalStateException("This item (type " + type + ") cannot have a special use.");
		}
		return onSpecialUse;
	}

	public String displayName() {
		return quality.getColor() + name;
	}

	public ArrayList<String> description() {
		ArrayList<String> description = new ArrayList<String>();
		description.add(ChatColor.RESET + type.toString());
		description.add(quality.getColor() + quality.toString());
		description.add("");
		description.addAll(StrUtils.lineToParagraph(ChatColor.WHITE + flavorText));
		return description;
	}

	void registerItemStack(GameItemStack itemStack) {
		itemStacks.add(itemStack);
	}

	void unregisterItemStack(GameItemStack itemStack) {
		itemStacks.remove(itemStack);
	}

	private void updateItemStackButtons() {
		for (GameItemStack itemStack : itemStacks) {
			itemStack.updateButton();
		}
	}

	/**
	 * Returns whether this item can be placed on the specified slot in an
	 * inventory.
	 */
	public boolean canBePlacedDown(int inventorySlot) {
		// 0: main hand
		// 40: off hand

		// 36: feet
		// 37: legs
		// 38: chest
		// 39: head
		switch (inventorySlot) {
		case 0:
			return type.canBeInMainHand();
		case 40:
			return type.canBeUsedInOffHand();
		case 36:
			return type == GameItemType.ARMOR_FEET;
		case 37:
			return type == GameItemType.ARMOR_LEGS;
		case 38:
			return type == GameItemType.ARMOR_CHEST;
		case 39:
			return type == GameItemType.ARMOR_HEAD;
		default:
			return true;
		}
	}

}
