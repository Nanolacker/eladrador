package com.eladrador.common.item;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.eladrador.common.Debug;
import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.ui.InteractableItem;
import com.eladrador.common.utils.StrUtils;

public class GameItem extends InteractableItem {

	private String name;
	private GameItemType type;
	private GameItemQuality quality;

	public GameItem(String id, String name, Material icon, GameItemType type, GameItemQuality quality,
			String flavorText) {
		super(id, itemStack(name, icon, type, quality, flavorText));
		this.name = name;
		this.type = type;
		this.quality = quality;
	}

	private static ItemStack itemStack(String name, Material icon, GameItemType type, GameItemQuality quality,
			String flavorText) {
		ItemStack itemStack = new ItemStack(icon);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(quality.getColor() + name);

		itemMeta.setUnbreakable(true);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

		ArrayList<String> lore = new ArrayList<>();
		lore.add(ChatColor.RESET + type.toString());
		lore.add(quality.getColor() + quality.toString());
		lore.add("");
		lore.addAll(StrUtils.lineToParagraph(ChatColor.WHITE + flavorText));
		itemMeta.setLore(lore);

		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}

	public String getName() {
		return name;
	}

	public GameItemType getType() {
		return type;
	}

	public GameItemQuality getQuality() {
		return quality;
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

	@Override
	public void onClickInInventory(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		int slot = event.getSlot();
		ItemStack cursor = event.getCursor();
		if (slot == 0) {
			if (cursor.getType() == Material.AIR) {
				PlayerCharacter pc = PlayerCharacter.forBukkitPlayer(player);
				pc.setMainHand(null);
			}
		}
	}

	@Override
	public void onClickOnCursor(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		int slot = event.getSlot();
		if (slot == 0) {
			if (!type.canBeInMainHand()) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "That can't go there!");
			} else {
				PlayerCharacter pc = PlayerCharacter.forBukkitPlayer(player);
				pc.setMainHand((MainHandItem) this);
			}
		}
	}

	@Override
	public void onDragOnCursor(InventoryDragEvent event) {
		Player player = (Player) event.getWhoClicked();
		Set<Integer> slots = event.getInventorySlots();
		if (slots.contains(0)) {
			if (!type.canBeInMainHand()) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "That can't go there!");
			} else {
				PlayerCharacter pc = PlayerCharacter.forBukkitPlayer(player);
				pc.setMainHand((MainHandItem) this);
			}
		}
	}

}
