package com.eladrador.core;

import java.util.ArrayList;

import org.bukkit.Bukkit;
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

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.GPlugin;
import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.sound.Noise;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonAddress;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.ui.TopMenu;
import com.eladrador.common.ui.TopMenu.TopMenuSize;
import com.eladrador.common.utils.StrUtils;

import net.md_5.bungee.api.ChatColor;

public class PcManageListener implements Listener {

	public static PcManageListener instance;

	private static final World SPAWN_WORLD = Bukkit.createWorld(new WorldCreator("world_character_select"));
	private static final Location SPAWN_LOC = new Location(SPAWN_WORLD, 0, 75, 0);

	private Button openPCSelectMenuButton;
	private Button deletePCButton;
	private Button cancelButton;
	private Button deleteConfirmButton;
	private Button deleteDenyButton;

	private Noise clickNoise = new Noise(Sound.BLOCK_STONE_BUTTON_CLICK_ON);
	private Noise deleteNoise = new Noise(Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED);

	PcManageListener() {
		openPCSelectMenuButton = new Button(ChatColor.BLUE + "Select a Character", null, Material.EMERALD) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				clickNoise.play(player);
				TopMenu pcSelectMenu = getNewPCSelectMenu();
				pcSelectMenu.open(player);
			}

		};
		deletePCButton = new Button(ChatColor.RED + "Delete a character", null, Material.BARRIER) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				TopMenu pcDeleteMenu = getNewPCDeleteMenu();
				pcDeleteMenu.open(player);
				clickNoise.play(player);
			}

		};
		cancelButton = new Button(ChatColor.RED + "Cancel", null, Material.BARRIER) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				clickNoise.play(player);
			}

		};

	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
		Player player = event.getPlayer();
		setup(player);
		player.getInventory().setItem(1, null);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		Player player = event.getPlayer();
		AbstractGameManager gameManager = GPlugin.getGameManager();
		PlayerCharacter pc = (PlayerCharacter) gameManager.charaByEntity(player);
		if (pc != null) {
			// pc.saveData();
		}
	}

	private TopMenu getNewPCSelectMenu() {
		TopMenu menu = new TopMenu(ChatColor.BLACK + "Select a character", TopMenuSize.NINE);
		for (int i = 0; i < 4; i++) {
			int menuIndex = i * 2;
			menu.addButton(getNewPCSelectButton(i), menuIndex);
		}
		menu.addButton(deletePCButton, 8);
		return menu;
	}

	private TopMenu getNewPCDeleteMenu() {
		TopMenu menu = new TopMenu(ChatColor.BLACK + "Delete a character", TopMenuSize.NINE);
		for (int i = 0; i < 4; i++) {
			int menuIndex = i * 2;
			menu.addButton(getNewPCDeleteButton(i), menuIndex);
		}
		menu.addButton(cancelButton, 8);
		return menu;
	}

	private Button getNewPCSelectButton(int slot) {
		ArrayList<String> description = StrUtils.stringToParagraph(ChatColor.GRAY + "Create a new character",
				StrUtils.LORE_CHARS_PER_LINE);
		Button button = new Button(ChatColor.GREEN + "Slot " + (slot + 1), description, Material.IRON_SWORD) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				clickNoise.play(player);
			}

		};
		return button;
	}

	private Button getNewPCDeleteButton(int slot) {
		ArrayList<String> description = StrUtils.stringToParagraph(ChatColor.RED + "Delete this character",
				StrUtils.LORE_CHARS_PER_LINE);
		Button button = new Button(ChatColor.GREEN + "Slot " + (slot + 1), description, Material.IRON_SWORD) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				deleteNoise.play(player);
				player.sendMessage(ChatColor.RED + "Character deleted");
				TopMenu pcSelectMenu = getNewPCSelectMenu();
				pcSelectMenu.open(player);
			}

		};
		return button;
	}

	public void setup(Player player) {
		openPCSelectMenuButton.addToPlayerInventoryMenu(player, 0);
	}
}
