package com.eladrador.test.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.BoundingBox;

import com.eladrador.common.Debug;
import com.eladrador.common.item.GameItem;
import com.eladrador.common.item.GameItemQuality;
import com.eladrador.common.item.GameItemStack;
import com.eladrador.common.item.GameItemType;
import com.eladrador.common.item.PlayerInventory;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.zone.Zone;

public class MenuTestListener implements Listener {

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

				GameItem item1 = new GameItem("Holy Boots of the North", GameItemType.ARMOR_FEET,
						Material.DIAMOND_BOOTS, GameItemQuality.EPIC,
						"These boots are very powerful, very holy, very epic.", 1);

				GameItem item2 = new GameItem("Holy Breastplate of the East", GameItemType.ARMOR_HEAD,
						Material.DIAMOND_CHESTPLATE, GameItemQuality.EPIC,
						"This breastplate is very powerful, very holy, very epic.", 64);

				GameItem item3 = new GameItem("Crap", GameItemType.CONSUMABLE, Material.COCOA_BEANS,
						GameItemQuality.COMMON, "Ew", 64);
				item3.getOnSpecialUse().addListener(new Runnable() {
					public void run() {
						Debug.log("crap");
					}
				});

				PlayerInventory inventory = pc.getInventory();

				GameItemStack itemStack1 = new GameItemStack(item1, 1);
				GameItemStack itemStack2 = new GameItemStack(item2, 1);
				GameItemStack itemStack3 = new GameItemStack(item3, 64);

				inventory.setItem(2, itemStack1);
				inventory.setItem(3, itemStack2);
				inventory.setItem(4, itemStack3);
			}

		}.start();
	}

}
