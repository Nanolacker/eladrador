package com.eladrador.common.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.eladrador.common.event.Event;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonToggleEvent;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.utils.StrUtils;

public class GameItem {

	private String name;
	private GameItemType type;
	private Material material;
	private GameItemQuality quality;
	private String flavorText;
	private int currentSize;
	private int maxSize;
	private GameItemAddress address;
	private Event onLeftClickInHand;
	private Event onRightClickInHand;
	private Event onSpecialUse;

	private ArrayList<Button> buttons;

	public GameItem(String name, GameItemType type, Material material, String flavorText, int currentSize,
			int maxSize) {
		this.name = name;
		this.type = type;
		this.material = material;
		this.flavorText = flavorText;
		this.currentSize = currentSize;
		this.maxSize = maxSize;
		address = new GameItemAddress();
		onLeftClickInHand = new Event();
		onRightClickInHand = new Event();
		onSpecialUse = new Event();
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
		this.currentSize = currentSize;
		updateButtons();
	}

	public int getMaxSize() {
		return maxSize;
	}

	public GameItemAddress getAddress() {
		return address;
	}

	public Event getOnLeftClickInHand() {
		return onLeftClickInHand;
	}

	public Event getOnRightClickInHand() {
		return onRightClickInHand;
	}

	public Event getOnSpecialUse() {
		return onSpecialUse;
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
				&& flavorText.equals(other.flavorText) && currentSize == other.currentSize && maxSize == other.maxSize;
	}

	public String displayName() {
		return quality.getColor() + name;
	}

	public ArrayList<String> description() {
		ArrayList<String> description = new ArrayList<String>();
		description.add(ChatColor.WHITE + quality.toString());
		description.add("");
		description.addAll(StrUtils.lineToParagraph(flavorText));
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

}
