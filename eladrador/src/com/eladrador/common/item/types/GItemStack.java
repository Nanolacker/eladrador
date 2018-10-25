package com.eladrador.common.item.types;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.item.ItemAddress;
import com.eladrador.common.item.ItemQuality;
import com.eladrador.common.utils.StrUtils;

/**
 * Represents an item that is more intriquate and far more specialized than the
 * vanilla Minecraft ItemStack. In game, GItemStacks should ALWAYS be used
 * instead of manually using Minecraft ItemStacks. Capable of displaying unique
 * descriptions and effects for each PlayerCharacter owner, while not needing to
 * be completely reconstructed or tediously modified when transferred between
 * PlayerCharacters. A GItemStack's image refers to the Minecraft ItemStack that
 * will be displayed to players in an inventory view.
 *
 */
public abstract class GItemStack implements Cloneable {

	private ItemAddress address;
	private String name;
	private ItemQuality quality;
	private Material imageMat;
	private int maxSize;
	private int currentSize;

	protected GItemStack(String name, ItemQuality quality, Material imageMat, int maxSize) {
		address = new ItemAddress();
		this.name = name;
		this.quality = quality;
		this.imageMat = imageMat;
		this.maxSize = maxSize;
		currentSize = 0;
	}

	public ItemAddress getAddress() {
		return address;
	}

	public ItemQuality getQuality() {
		return quality;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public int getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int amount) {
		if (this.currentSize > maxSize) {
			try {
				throw new IllegalArgumentException("amount must not be greater than maxSize.");
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		} else {
			this.currentSize = amount;
		}
	}

	public String getDisplayName() {
		return quality + name;
	}

	public Material getImageMaterial() {
		return imageMat;
	}

	public ArrayList<String> getDescription(PlayerCharacter owner) {
		ArrayList<String> description = new ArrayList<String>();
		description.add(quality.toString());
		description.addAll(StrUtils.stringToParagraph(getFlavorText(owner), StrUtils.LORE_CHARS_PER_LINE));
		if (this instanceof ItemActionable) {
			String actionDescription = ((ItemActionable) this).getActionDescription(owner);
			description.addAll(StrUtils.stringToParagraph(actionDescription, StrUtils.LORE_CHARS_PER_LINE));
		}
		return description;
	}

	/**
	 * Override to add flavor text to be displayed on this GItemStack's image.
	 * 
	 * @param owner
	 *            the PlayerCharacter owner which this GItemStack's flavor text is
	 *            tailored to
	 * @return the flavor text to be displayed on this GItemStack's image (will be
	 *         formatted automatically)
	 */
	protected String getFlavorText(PlayerCharacter owner) {
		return "";
	}

	/**
	 * Called when this GItemStack is added to a PlayerCharacter's inventory, by any
	 * means.
	 * 
	 * @param pc
	 *            the PlayerCharacter that this item is being added to the inventory
	 *            of
	 * @param amount
	 *            the amount being added
	 * 
	 */
	public void onPickup(PlayerCharacter pc, int amount) {
		// override to add effects
	}

	/**
	 * Called when this GItemStack is subtracted from a PlayerCharacter's inventory,
	 * by any means.
	 * 
	 * @param pc
	 *            the PlayerCharacter that this item is being subtracted from the
	 *            inventory of
	 * @param amount
	 *            the amount being subtracted
	 */
	public void onDrop(PlayerCharacter pc, int amount) {
		// override to add effects
	}

	@Override
	public GItemStack clone() {
		try {
			return (GItemStack) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
