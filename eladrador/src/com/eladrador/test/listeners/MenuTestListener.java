package com.eladrador.test.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.BoundingBox;

import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.character.CharacterEffect;
import com.eladrador.common.item.GameItemQuality;
import com.eladrador.common.item.GameItemType;
import com.eladrador.common.item.MainHandItem;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.ui.InteractableItemRegistry;
import com.eladrador.common.zone.Zone;

public class MenuTestListener implements Listener {

	public MenuTestListener() {
		MainHandItem shoes = new MainHandItem("SWORD", "Sword", Material.DIAMOND_SWORD, GameItemType.SHORT_SWORD,
				GameItemQuality.EPIC, "This sword is very powerful.");
		CharacterEffect effect = new CharacterEffect() {

			@Override
			public void onRemove(AbstractCharacter character) {
				((PlayerCharacter) character).sendMessage("Removed!");
			}

			@Override
			public void onApply(AbstractCharacter character) {
				((PlayerCharacter) character).sendMessage("Equipped!");
			}
		};
		shoes.addOnEquipEffect(effect);
		InteractableItemRegistry.register(shoes);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		new DelayedTask(1.0) {

			@Override
			protected void run() {
				Player player = e.getPlayer();
				World world = player.getWorld();
				Location location = new Location(world, 54, 69, 240);
				Zone zone = new Zone(world, "Zone", 1, ChatColor.BLACK, 1, new BoundingBox(0, 0, 0, 1, 1, 1)) {
				};
				PlayerBackground background = new PlayerBackground("", 1, zone, location) {
				};
				PlayerClass clazz = new PlayerClass("Class", 1, Material.ICE) {
				};
				PlayerCharacter.createNew(player, 0, background, clazz);
				PlayerCharacter pc = PlayerCharacter.retrieve(player, 0);
				pc.setBukkitPlayer(player);

				MainHandItem sword = (MainHandItem) InteractableItemRegistry.forId("SWORD");

				PlayerInventory inventory = pc.getInventory();

				inventory.setItem(2, sword.itemStack());
			}

		}.start();
	}

}
