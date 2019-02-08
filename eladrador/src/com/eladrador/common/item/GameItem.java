package com.eladrador.common.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.eladrador.common.Debug;
import com.eladrador.common.event.Event;
import com.eladrador.common.ui.Button;
import com.eladrador.common.utils.StrUtils;

public final class GameItem {

	private String name;
	private GameItemType type;
	private Material material;
	private GameItemQuality quality;
	private String flavorText;
	private int currentSize;
	private int maxSize;
	private Event onLeftClickInHand;
	private Event onRightClickInHand;
	private Event onSpecialUse;
	private ArrayList<Button> buttons;

	public GameItem(String name, GameItemType type, Material material, GameItemQuality quality, String flavorText,
			int currentSize, int maxSize) {
		this.name = name;
		this.type = type;
		this.material = material;
		this.quality = quality;
		this.flavorText = flavorText;
		this.currentSize = currentSize;
		this.maxSize = maxSize;
		onLeftClickInHand = new Event();
		onRightClickInHand = new Event();
		onSpecialUse = new Event();
		buttons = new ArrayList<Button>();
	}

	public String getName() {
		return name;
	}

	public String getFlavorText() {
		return flavorText;
	}

	public void setFlavorText(String text) {
		flavorText = text;
		updateButtons();
	}

	public GameItemType getType() {
		return type;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
		updateButtons();
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		validateCurrentSize(currentSize);
		if (currentSize == 0) {
			delete();
		} else {
			this.currentSize = currentSize;
			updateButtons();
		}
	}

	public int getMaxSize() {
		return maxSize;
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

	/**
	 * Splits this item, returning a new item that was split from the original. The
	 * current size of this item is also reduced by the specified amount.
	 * 
	 * @param amountToTakeOff the size of the returned item
	 * @return the new item
	 */
	public GameItem split(int amountToTakeOff) {
		setCurrentSize(currentSize - amountToTakeOff);
		return new GameItem(name, type, material, quality, flavorText, amountToTakeOff, maxSize);
	}

	public void stack(GameItem onto) {
		if (!onto.isStackableWith(onto)) {
			throw new IllegalArgumentException("Item is not stackable");
		}
		stack(onto, currentSize);
	}

	public void stack(GameItem onto, int howMuch) {
		if (!onto.isStackableWith(onto)) {
			throw new IllegalArgumentException("Item is not stackable");
		}
		int ontoCurrentSize = onto.currentSize;
		onto.setCurrentSize(ontoCurrentSize + howMuch);
		this.setCurrentSize(currentSize - howMuch);
	}

	/**
	 * Whether this item can be stacked with the other specified item. This method
	 * does not take into account sizes and can still return true if this item's
	 * size is maxed.
	 * 
	 * @param other the other item
	 * @return true if the items can be stacked
	 */
	public boolean isStackableWith(GameItem other) {
		return name.equals(other.name) && type == other.type && material == other.material && quality == other.quality
				&& flavorText.equals(other.flavorText) && maxSize == other.maxSize;
	}

	public String displayName() {
		return quality.getColor() + name;
	}

	public ArrayList<String> description() {
		ArrayList<String> description = new ArrayList<String>();
		description.add(ChatColor.WHITE + quality.toString());
		description.add("");
		description.addAll(StrUtils.lineToParagraph(ChatColor.WHITE + flavorText));
		return description;
	}

	void registerButton(GameItemButton button) {
		buttons.add(button);
	}

	void unregisterButton(GameItemButton button) {
		buttons.remove(button);
	}

	private void updateButtons() {
		for (Button button : buttons) {
			button.setDisplayName(displayName());
			button.setDescription(description());
			button.setImageMaterial(material);
			button.setImageSize(currentSize);
		}
	}

	private void validateCurrentSize(int currentSize) {
		if (currentSize > maxSize) {
			throw new IllegalArgumentException(
					"Specified size (" + currentSize + ") exceeds max size (" + maxSize + ")");
		}
	}

	private void delete() {
		for (Button button : buttons) {
			button.delete();
		}
	}

}
