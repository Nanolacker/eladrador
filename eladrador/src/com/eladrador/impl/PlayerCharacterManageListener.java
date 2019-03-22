package com.eladrador.impl;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.eladrador.common.MMORPGPlugin;
import com.eladrador.common.character.PlayerCharacterOLD;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.PlayerCharacterPersistence;
import com.eladrador.common.player.PlayerCharacterSaveData;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.sound.Noise;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.InteractableItemRegistry;
import com.eladrador.common.utils.StrUtils;
import com.eladrador.common.zone.Zone;

public class PlayerCharacterManageListener implements Listener {

	private static final World CHARACTER_SELECT_WORLD = Bukkit.createWorld(new WorldCreator("world_character_select"));
	private static final Location CHARACTER_SELECT_LOC = new Location(CHARACTER_SELECT_WORLD, 0, 75, 0);

	private Button openPcSelectMenuButton;
	private Button openPcDeleteMenuButton;
	private Button cancelPcDeleteButton;
	private Button openGameMenuButton;
	private Button openQuestLogButton;
	private Button openSkillTreeButton;
	private Button returnToCharacterSelectButton;

	private Noise clickNoise = new Noise(Sound.BLOCK_STONE_BUTTON_CLICK_ON);
	private Noise deleteNoise = new Noise(Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED);

	/*
	 * TEST STUFF
	 */
	World spawnWorld = MMORPGPlugin.getGameManager().worldForName("world");
	Location spawn = new Location(spawnWorld, 46.5, 69, 236.5);
	Zone testZone = new Zone(spawnWorld, "Melcher", ChatColor.GREEN, 1);
	PlayerBackground background = new PlayerBackground("Dustbell", testZone, spawn);
	PlayerClass playerClass = new PlayerClass("Warrior", Material.IRON_AXE);

	public PlayerCharacterManageListener() {
		openPcSelectMenuButton = openPcSelectMenuButton();
		openPcDeleteMenuButton = openPcDeleteMenuButton();
		cancelPcDeleteButton = cancelPcDeleteButton();
		openGameMenuButton = openGameMenuButton();
		openQuestLogButton = openQuestLogButton();
		openSkillTreeButton = openSkillTreeButton();
		returnToCharacterSelectButton = returnToCharacterSelectButton();

		InteractableItemRegistry.register(openPcSelectMenuButton);
		InteractableItemRegistry.register(openPcDeleteMenuButton);
		InteractableItemRegistry.register(cancelPcDeleteButton);
		InteractableItemRegistry.register(openGameMenuButton);
		InteractableItemRegistry.register(openQuestLogButton);
		InteractableItemRegistry.register(openSkillTreeButton);
		InteractableItemRegistry.register(returnToCharacterSelectButton);
	}

	private Button openPcSelectMenuButton() {
		ItemStack itemStack = new ItemStack(Material.EMERALD);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName("Character Select");
		itemStack.setItemMeta(itemMeta);

		return new Button("OPEN_CHARACTER_SELECT_MENU_BUTTON", itemStack) {
			@Override
			public void onToggle(Player player) {
				clickNoise.play(player);
				Inventory pcSelectMenu = pcSelectMenu(player);
				player.openInventory(pcSelectMenu);
			}
		};
	}

	private Button openPcDeleteMenuButton() {
		ItemStack itemStack = new ItemStack(Material.BARRIER);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.RED + "Delete a character");
		itemStack.setItemMeta(itemMeta);

		return new Button("OPEN_CHARACTER_DELETE_MENU_BUTTON", itemStack) {
			@Override
			public void onToggle(Player player) {
				clickNoise.play(player);
				Inventory pcDeleteMenuButton = pcDeleteMenu(player);
				player.openInventory(pcDeleteMenuButton);
			}
		};
	}

	private Button cancelPcDeleteButton() {
		ItemStack itemStack = new ItemStack(Material.BARRIER);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.RED + "Cancel");
		itemStack.setItemMeta(itemMeta);

		return new Button("CANCEL_CHARACTER_DELETE", itemStack) {
			@Override
			protected void onToggle(Player player) {
				clickNoise.play(player);
				Inventory pcSelectMenu = pcSelectMenu(player);
				player.openInventory(pcSelectMenu);
			}
		};
	}

	private Button openGameMenuButton() {
		ItemStack itemStack = new ItemStack(Material.EMERALD);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GREEN + "Menu");
		ArrayList<String> lore = StrUtils.lineToParagraph(ChatColor.GREEN + "Access stuff");
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);

		Button button = new Button("OPEN_GAME_MENU_BUTTON", itemStack) {
			@Override
			protected void onToggle(Player player) {
				clickNoise.play(player);
				Inventory gameMenu = gameMenu(player);
				player.openInventory(gameMenu);
			}
		};
		button.setToggleableByHold(true);
		return button;
	}

	private Inventory pcSelectMenu(Player player) {
		Inventory menu = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Select a character");
		for (int i = 1; i <= 4; i++) {
			int invIndex = (i - 1) * 2;
			Button button = pcSelectButton(player, i);
			InteractableItemRegistry.register(button);
			menu.setItem(invIndex, button.itemStack());
		}
		menu.setItem(8, openPcDeleteMenuButton.itemStack());
		return menu;
	}

	private Inventory pcDeleteMenu(Player player) {
		Inventory menu = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Delete a character");
		for (int i = 1; i <= 4; i++) {
			int invIndex = (i - 1) * 2;
			Button button = pcDeleteButton(player, i);
			InteractableItemRegistry.register(button);
			menu.setItem(invIndex, button.itemStack());
		}
		menu.setItem(8, cancelPcDeleteButton.itemStack());
		return menu;
	}

	private Inventory gameMenu(Player player) {
		Inventory menu = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Menu");
		menu.setItem(2, openSkillTreeButton.itemStack());
		menu.setItem(6, returnToCharacterSelectButton.itemStack());
		return menu;
	}

	private Button pcSelectButton(Player player, int saveSlot) {
		ArrayList<String> pcDescription;
		Material buttonIcon;
		boolean pcCreated = PlayerCharacterPersistence.saveExists(player, saveSlot);
		if (pcCreated) {
			PlayerCharacterSaveData data = PlayerCharacterPersistence.retrieveData(player, saveSlot);
			PlayerBackground background = PlayerBackground.forName(data.getPlayerBackgroundName());
			int level = PlayerCharacterOLD.levelForXP(data.getXp());
			PlayerClass playerClass = PlayerClass.forName(data.getPlayerClassName());
			Zone zone = Zone.forName(data.getZoneName());
			pcDescription = new ArrayList<String>();
			pcDescription.add(ChatColor.GREEN + background.getName());
			pcDescription.add(ChatColor.GOLD + "Lv. " + level + " " + ChatColor.WHITE + playerClass.getName());
			pcDescription.add(zone.getDisplayColor() + zone.getName());
			buttonIcon = playerClass.getImageMaterial();
		} else {
			pcDescription = StrUtils.lineToParagraph(ChatColor.GREEN + "Create a new character");
			buttonIcon = Material.GLASS_PANE;
		}

		ItemStack itemStack = new ItemStack(buttonIcon);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BLUE + "Slot " + saveSlot);
		itemMeta.setLore(pcDescription);
		itemStack.setItemMeta(itemMeta);

		return new Button("SELECT_CHARACTER_BUTTON_" + player.getName() + "_" + saveSlot, itemStack) {
			@Override
			protected void onToggle(Player player) {
				clickNoise.play(player);
				if (!pcCreated) {
					PlayerCharacterOLD.createNew(player, saveSlot, background, playerClass);
				}
				enterWorld(player, saveSlot);
				InteractableItemRegistry.unregister(this);
			}
		};
	}

	private Button pcDeleteButton(Player player, int saveSlot) {
		String unformattedDescription;
		if (PlayerCharacterPersistence.saveExists(player, saveSlot)) {
			unformattedDescription = ChatColor.RED + "Delete this character";
		} else {
			unformattedDescription = ChatColor.GRAY + "No save found";
		}
		ArrayList<String> formattedDescription = StrUtils.lineToParagraph(unformattedDescription);
		boolean pcCreated = PlayerCharacterPersistence.saveExists(player, saveSlot);

		Material buttonIcon = pcCreated ? Material.BARRIER : Material.GLASS_PANE;
		ItemStack itemStack = new ItemStack(buttonIcon);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.BLUE + "Slot " + saveSlot);
		itemMeta.setLore(formattedDescription);
		itemStack.setItemMeta(itemMeta);

		String buttonId = "DELETE_CHARACTER_BUTTON_" + player.getName();
		Button button = new Button(buttonId + "_" + saveSlot, itemStack) {
			@Override
			protected void onToggle(Player player) {
				if (pcCreated) {
					PlayerCharacterPersistence.deleteData(player, saveSlot);
					deleteNoise.play(player);
					player.sendMessage(ChatColor.RED + "Character deleted");
					Inventory pcSelectMenu = pcSelectMenu(player);
					player.openInventory(pcSelectMenu);
				}
			}
		};
		return button;
	}

	private Button openQuestLogButton() {
		ItemStack itemStack = new ItemStack(Material.COMPASS);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GREEN + "Quest Log");
		itemStack.setItemMeta(itemMeta);

		Button button = new Button("OPEN_QUEST_LOG_BUTTON", itemStack) {
			@Override
			protected void onToggle(Player player) {
				clickNoise.play(player);
				PlayerCharacterOLD pc = PlayerCharacterOLD.forBukkitPlayer(player);
				pc.addXp(10);
				// pc.openQuestLog();
			}
		};
		button.setToggleableByHold(true);
		return button;
	}

	private Button openSkillTreeButton() {
		ItemStack itemStack = new ItemStack(Material.DIAMOND);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GREEN + "Skill Tree");
		itemStack.setItemMeta(itemMeta);

		return new Button("OPEN_SKILL_TREE_BUTTON", itemStack) {
			@Override
			protected void onToggle(Player player) {
				clickNoise.play(player);
				PlayerCharacterOLD pc = PlayerCharacterOLD.forBukkitPlayer(player);
				// pc.openSkillTree();
			}
		};
	}

	private Button returnToCharacterSelectButton() {
		ItemStack itemStack = new ItemStack(Material.BARRIER);
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GREEN + "Save and Quit");
		itemStack.setItemMeta(itemMeta);

		return new Button("RETURN_TO_CHARACTER_SELECT_BUTTON", itemStack) {
			@Override
			protected void onToggle(Player player) {
				clickNoise.play(player);
				PlayerCharacterOLD pc = PlayerCharacterOLD.forBukkitPlayer(player);
				pc.saveData();
				setup(player);
			}
		};
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		setup(player);
	}

	@EventHandler
	private void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Player player = event.getPlayer();
		PlayerCharacterOLD pc = (PlayerCharacterOLD) PlayerCharacterOLD.forBukkitPlayer(player);
		if (pc != null) {
			pc.saveData();
		}
	}

	/**
	 * Sets up a player to select a character.
	 */
	private void setup(Player player) {
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.teleport(CHARACTER_SELECT_LOC);
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setItem(0, openPcSelectMenuButton.itemStack());
	}

	private void enterWorld(Player player, int saveSlot) {
		PlayerCharacterOLD pc = PlayerCharacterOLD.retrieve(player, saveSlot);
		pc.setBukkitPlayer(player);
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setItem(7, openQuestLogButton.itemStack());
		inventory.setItem(8, openGameMenuButton.itemStack());
	}

}
