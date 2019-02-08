package com.eladrador.core.playerCharacterManagement;

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
import com.eladrador.common.player.PlayerCharacterPersistence;
import com.eladrador.common.player.PlayerCharacterSaveData;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.sound.Noise;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonToggleEvent;
import com.eladrador.common.ui.LowerMenu;
import com.eladrador.common.ui.UIProfile;
import com.eladrador.common.ui.UpperMenu;
import com.eladrador.common.utils.StrUtils;
import com.eladrador.common.zone.Zone;

public class PlayerCharacterManageListener implements Listener {

	public static PlayerCharacterManageListener instance;

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
	World spawnWorld = GPlugin.getGameManager().worldForName("world");
	Location startLoc = new Location(spawnWorld, 0, 70, 0);
	Zone testZone = new Zone(spawnWorld, "Melcher", 1, ChatColor.GREEN, 1) {
	};
	PlayerBackground background = new PlayerBackground("Dustbell", 0, testZone, startLoc) {
	};
	PlayerClass playerClass = new PlayerClass("Warrior", 0, Material.IRON_AXE) {
	};

	public PlayerCharacterManageListener() {
		openPCSelectMenuButton = new Button(ChatColor.BLUE + "Select a Character", null, Material.EMERALD) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
				UpperMenu pcSelectMenu = pcSelectMenu(player);
				UIProfile uiProf = UIProfile.forPlayer(player);
				uiProf.openMenu(pcSelectMenu);
				clickNoise.play(player);
			}

		};
		deletePCButton = new Button(ChatColor.RED + "Delete a character", null, Material.BARRIER) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
				UpperMenu pcDeleteMenu = pcDeleteMenu(player);
				UIProfile uiProf = UIProfile.forPlayer(player);
				uiProf.openMenu(pcDeleteMenu);
				clickNoise.play(player);
			}

		};
		cancelDeleteButton = new Button(ChatColor.RED + "Cancel", null, Material.REDSTONE) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
				UpperMenu pcSelectMenu = pcSelectMenu(player);
				UIProfile uiProf = UIProfile.forPlayer(player);
				uiProf.openMenu(pcSelectMenu);
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
		PlayerCharacter pc = (PlayerCharacter) PlayerCharacter.forBukkitPlayer(player);
		if (pc != null) {
			pc.saveData();
		}
	}

	/**
	 * Sets up a player to select a character.
	 */
	private void setup(Player player) {
		player.teleport(CHARACTER_SELECT_LOC);
		UIProfile uiProf = UIProfile.forPlayer(player);
		LowerMenu lowerMenu = uiProf.getLowerMenu();
		lowerMenu.setButton(0, openPCSelectMenuButton);
	}

	private void enterWorld(Player player, int saveSlot) {
		PlayerCharacter pc = PlayerCharacter.retrieve(player, saveSlot);
		pc.setBukkitPlayer(player);
	}

	private UpperMenu pcSelectMenu(Player player) {
		UpperMenu menu = new UpperMenu(ChatColor.BLACK + "Select a character", 9);
		for (int i = 1; i <= 4; i++) {
			int menuIndex = (i - 1) * 2;
			menu.setButton(menuIndex, pcSelectButton(player, i));
		}
		menu.setButton(8, deletePCButton);
		return menu;
	}

	private UpperMenu pcDeleteMenu(Player player) {
		UpperMenu menu = new UpperMenu(ChatColor.BLACK + "Delete a character", 9);
		for (int i = 1; i <= 4; i++) {
			int menuIndex = (i - 1) * 2;
			menu.setButton(menuIndex, pcDeleteButton(player, i));
		}
		menu.setButton(8, cancelDeleteButton);
		return menu;
	}

	private Button pcSelectButton(Player player, int saveSlot) {
		ArrayList<String> pcDescription;
		Material buttonMat;
		boolean pcCreated = PlayerCharacterPersistence.saveExists(player, saveSlot);
		if (pcCreated) {
			PlayerCharacterSaveData data = PlayerCharacterPersistence.retrieveData(player, saveSlot);
			PlayerBackground background = PlayerBackground.byID(data.playerBackgroundID);
			int level = PlayerCharacter.levelFromXP(data.xp);
			PlayerClass playerClass = PlayerClass.byID(data.playerClassID);
			Zone zone = Zone.byID(data.zoneID);
			pcDescription = new ArrayList<String>();
			pcDescription.add(ChatColor.GREEN + background.getName());
			pcDescription.add(ChatColor.GOLD + "Lv. " + level + " " + ChatColor.WHITE + playerClass.getName());
			pcDescription.add(zone.getDisplayColor() + zone.getName());
			buttonMat = playerClass.getImageMaterial();
		} else {
			pcDescription = StrUtils.lineToParagraph(ChatColor.GREEN + "Create a new character");
			buttonMat = Material.GLASS_PANE;
		}
		Button button = new Button(ChatColor.BLUE + "Slot " + saveSlot, pcDescription, buttonMat) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
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
		if (PlayerCharacterPersistence.saveExists(player, saveSlot)) {
			unformattedDescription = ChatColor.RED + "Delete this character";
		} else {
			unformattedDescription = ChatColor.GRAY + "No save found";
		}
		ArrayList<String> formattedDescription = StrUtils.lineToParagraph(unformattedDescription);
		boolean pcCreated = PlayerCharacterPersistence.saveExists(player, saveSlot);
		Material mat = pcCreated ? Material.BARRIER : Material.GLASS_PANE;
		Button button = new Button(ChatColor.BLUE + "Slot " + saveSlot, formattedDescription, mat) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
				if (pcCreated) {
					PlayerCharacterPersistence.deleteData(player, saveSlot);
					deleteNoise.play(player);
					player.sendMessage(ChatColor.RED + "Character deleted");
					UpperMenu pcSelectMenu = pcSelectMenu(player);
					UIProfile uiProf = UIProfile.forPlayer(player);
					uiProf.openMenu(pcSelectMenu);
				}
			}

		};
		return button;
	}

}
