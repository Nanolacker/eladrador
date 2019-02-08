package com.eladrador.common.ui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.eladrador.common.Debug;

/**
 * Buttons represent graphical objects which can be clicked on by players to
 * execute a certain task. Buttons have a Minecraft item stack counterpart which
 * allows players to interact with buttons. This item stack is referred to as a
 * button's "image".
 */
public abstract class Button {

	/**
	 * The display name of this button's images.
	 */
	private String displayName;
	/**
	 * The description of this button. The lore of its images.
	 */
	private List<String> description;
	/**
	 * The material of this button's image.
	 */
	private Material imageMat;
	/**
	 * The size of this button's images.
	 */
	private int imageSize;
	/**
	 * The {@link ButtonAddress}es of this button.
	 */
	private ArrayList<ButtonAddress> addresses;

	/**
	 * Constructs a new {@code Button}.
	 * 
	 * @param displayName the name to be displayed to players on this button's image
	 * @param description leave null if there is no description
	 * @param imageMat    the material of this button's image
	 */
	public Button(String displayName, List<String> description, Material imageMat) {
		this.displayName = displayName;
		this.description = description;
		this.imageMat = imageMat;
		imageSize = 1;
		addresses = new ArrayList<ButtonAddress>();
	}

	/**
	 * Returns the display name of this button.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name of this button and updates the names of all of its
	 * images accordingly.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		updateImages();
	}

	/**
	 * Sets the description of this button and updates the lore of all of its images
	 * accordingly.
	 */
	public void setDescription(List<String> description) {
		this.description = description;
		updateImages();
	}

	/**
	 * Sets the image material of this button and updates the material of all of its
	 * images.
	 */
	public void setImageMaterial(Material imageMat) {
		this.imageMat = imageMat;
		updateImages();
	}

	/**
	 * Sets the size of this button's images and updates them accordingly.
	 */
	public void setImageSize(int size) {
		imageSize = size;
		updateImages();
	}

	/**
	 * Returns a new image of this button.
	 */
	ItemStack image() {
		ItemStack image = new ItemStack(imageMat, imageSize);
		ItemMeta meta = image.getItemMeta();
		meta.setDisplayName(displayName);
		if (description != null) {
			meta.setLore(description);
		}
		meta.setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		image.setItemMeta(meta);
		return image;
	}

	/**
	 * Registers a new {@link ButtonAddress} with this button.
	 */
	void registerAddress(ButtonAddress address) {
		addresses.add(address);
	}

	/**
	 * Unregisters a {@link ButtonAddress} with this button.
	 */
	void unregisterAddress(ButtonAddress address) {
		addresses.remove(address);
	}

	/**
	 * Updates all images associated with this button so that they accurately
	 * reflect this button's properties. These properties include the display name,
	 * description, and image material of this button.
	 */
	private void updateImages() {
		for (ButtonAddress address : addresses) {
			ButtonContainer container = address.getContainer();
			int index = address.getIndex();
			container.updateButtonImage(index);
		}
	}

	/**
	 * Removes this button from all containers.
	 */
	public void delete() {
		for (ButtonAddress address : addresses) {
			ButtonContainer container = address.getContainer();
			int index = address.getIndex();
			container.setButton(index, null);
		}
	}

	/**
	 * Invoked when this button is toggled by a player.
	 * 
	 * @param toggleEvent describes how this button was toggled
	 */
	protected abstract void onToggle(ButtonToggleEvent toggleEvent);

}
