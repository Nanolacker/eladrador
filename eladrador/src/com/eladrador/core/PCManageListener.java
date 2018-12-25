package com.eladrador.core;

import java.util.ArrayList;

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

import com.eladrador.common.GPlugin;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.sound.Noise;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonAddress;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.ui.LowerMenu;
import com.eladrador.common.ui.UIProfile;
import com.eladrador.common.ui.UpperMenu;
import com.eladrador.common.ui.UpperMenu.UpperMenuSize;
import com.eladrador.common.utils.StrUtils;
import com.eladrador.common.zone.Zone;

public class PCManageListener implements Listener {

	public static PCManageListener instance;

	private static final World CHARACTER_SELECT_WORLD = Bukkit.createWorld(new WorldCreator("world_character_select"));
	private static final Location CHARACTER_SELECT_LOC = new Location(CHARACTER_SELECT_WORLD, 0, 75, 0);

	private Button openPCSelectMenuButton;
	private Button deletePCButton;
	private Button cancelDeleteButton;
	private Button deleteConfirmButton;
	private Button deleteDenyButton;

	private Noise clickNoise = new Noise(Sound.BLOCK_STONE_BUTTON_CLICK_ON);
	private Noise deleteNoise = new Noise(Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED);

	/*
	 * TEST STUFF
	 */
	World spawnWorld = GPlugin.getGameManager().worldByName("world");
	Location startLoc = new Location(spawnWorld, 0, 70, 0);
	Zone testZone = new Zone(spawnWorld, "test", 1, ChatColor.BLACK, 1) {
	};
	PlayerBackground background = new PlayerBackground("test background", 0, testZone, startLoc) {
	};
	PlayerClass playerClass = new PlayerClass("Test Class", 0, Material.IRON_AXE) {
	};

	PCManageListener() {
		openPCSelectMenuButton = new Button(ChatColor.BLUE + "Select a Character", null, Material.EMERALD) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				UpperMenu pcSelectMenu = pcSelectMenu(player);
				UIProfile uiProf = UIProfile.byPlayer(player);
				uiProf.openUpperMenu(pcSelectMenu);
				clickNoise.play(player);
			}

		};
		deletePCButton = new Button(ChatColor.RED + "Delete a character", null, Material.BARRIER) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				UpperMenu pcDeleteMenu = pcDeleteMenu(player);
				UIProfile uiProf = UIProfile.byPlayer(player);
				uiProf.openUpperMenu(pcDeleteMenu);
				clickNoise.play(player);
			}

		};
		cancelDeleteButton = new Button(ChatColor.RED + "Cancel", null, Material.REDSTONE) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				UpperMenu pcSelectMenu = pcSelectMenu(player);
				UIProfile uiProf = UIProfile.byPlayer(player);
				uiProf.openUpperMenu(pcSelectMenu);
				clickNoise.play(player);
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
		PlayerCharacter pc = (PlayerCharacter) PlayerCharacter.byBukkitPlayer(player);
		if (pc != null) {
			pc.saveData();
		}
	}

	/**
	 * Sets up a player to select a character.
	 */
	private void setup(Player player) {
		player.teleport(CHARACTER_SELECT_LOC);
		UIProfile uiProf = UIProfile.byPlayer(player);
		LowerMenu lowerMenu = uiProf.getLowerMenu();
		lowerMenu.addButton(openPCSelectMenuButton, 0);
	}

	private void enterWorld(Player player, int saveSlot) {
		PlayerCharacter pc = PlayerCharacter.retrieve(player, saveSlot);
		pc.setBukkitPlayer(player);
	}

	private UpperMenu pcSelectMenu(Player player) {
		UpperMenu menu = new UpperMenu(ChatColor.BLACK + "Select a character", UpperMenuSize.NINE);
		for (int i = 1; i <= 4; i++) {
			int menuIndex = (i - 1) * 2;
			menu.addButton(pcSelectButton(player, i), menuIndex);
		}
		menu.addButton(deletePCButton, 8);
		return menu;
	}

	private UpperMenu pcDeleteMenu(Player player) {
		UpperMenu menu = new UpperMenu(ChatColor.BLACK + "Delete a character", UpperMenuSize.NINE);
		for (int i = 1; i <= 4; i++) {
			int menuIndex = (i - 1) * 2;
			menu.addButton(pcDeleteButton(player, i), menuIndex);
		}
		menu.addButton(cancelDeleteButton, 8);
		return menu;
	}

	private Button pcSelectButton(Player player, int saveSlot) {
		String text;
		if (PlayerCharacter.saveExists(player, saveSlot)) {
			text = ChatColor.GREEN + "Save exists";
		} else {
			text = ChatColor.GREEN + "Create a new character";
		}
		ArrayList<String> description = StrUtils.lineToParagraph(text);
		boolean pcCreated = PlayerCharacter.saveExists(player, saveSlot);
		Material mat = pcCreated ? Material.IRON_SWORD : Material.GLASS_PANE;
		Button button = new Button(ChatColor.BLUE + "Slot " + saveSlot, description, mat) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				clickNoise.play(player);
				if (!pcCreated) {
					PlayerCharacter.createNew(player, saveSlot, background, playerClass);
				}
				enterWorld(player, saveSlot);
			}

		};
		return button;
	}

	private Button pcDeleteButton(Player player, int saveSlot) {
		String unformattedDescription;
		if (PlayerCharacter.saveExists(player, saveSlot)) {
			unformattedDescription = ChatColor.RED + "Delete this character";
		} else {
			unformattedDescription = ChatColor.GRAY + "No save found";
		}
		ArrayList<String> formattedDescription = StrUtils.lineToParagraph(unformattedDescription);
		boolean pcCreated = PlayerCharacter.saveExists(player, saveSlot);
		Material mat = pcCreated ? Material.BARRIER : Material.GLASS_PANE;
		Button button = new Button(ChatColor.BLUE + "Slot " + saveSlot, formattedDescription, mat) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				if (pcCreated) {
					PlayerCharacter.deleteData(player, saveSlot);
					deleteNoise.play(player);
					player.sendMessage(ChatColor.RED + "Character deleted");
					UpperMenu pcSelectMenu = pcSelectMenu(player);
					UIProfile uiProf = UIProfile.byPlayer(player);
					uiProf.openUpperMenu(pcSelectMenu);
				}
			}

		};
		return button;
	}

}
