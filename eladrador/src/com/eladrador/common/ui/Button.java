package com.eladrador.common.ui;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Button {

	private String displayName;
	private ArrayList<String> description;
	private Material imageMat;
	private ArrayList<ButtonAddress> addresses;

	/**
	 * 
	 * @param displayName
	 * @param description can be left null
	 * @param imageMat
	 */
	public Button(String displayName, ArrayList<String> description, Material imageMat) {
		this.displayName = displayName;
		this.description = description;
		this.imageMat = imageMat;
		addresses = new ArrayList<ButtonAddress>();
	}

	protected abstract void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked);

	ItemStack getNewImage() {
		ItemStack image = new ItemStack(imageMat);
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

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		updateImages();
	}

	public void setDescription(ArrayList<String> description) {
		this.description = description;
		updateImages();
	}

	public void setImageMaterial(Material imageMat) {
		this.imageMat = imageMat;
		updateImages();
	}

	private void updateImages() {
		for (int i = 0; i < addresses.size(); i++) {
			ButtonAddress address = addresses.get(i);
			AbstractMenu menu = address.getMenu();
			int index = address.getIndex();
			ArrayList<Inventory> menuImages = menu.getImages();
			for (int j = 0; j < menuImages.size(); j++) {
				Inventory menuImage = menuImages.get(j);
				ItemStack buttonImage = getNewImage();
				menuImage.setItem(index, buttonImage);
			}
		}
	}

	void addAddress(ButtonAddress address) {
		addresses.add(address);
	}

	void removeAddress(ButtonAddress address) {
		addresses.remove(address);
	}

	/**
	 * 
	 * @param player
	 * @param slot   must be 41 or less
	 */
	public void addToPlayerInventoryMenu(Player player, int slot) {
		UIProfile profile = UIListener.profiles.get(player);
		PlayerInventoryMenu invMenu = profile.getInventoryMenu();
		invMenu.addButton(this, slot);
	}

}
