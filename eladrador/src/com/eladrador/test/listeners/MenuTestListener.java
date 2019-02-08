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
import com.eladrador.common.item.GameItemButton;
import com.eladrador.common.item.GameItemQuality;
import com.eladrador.common.item.GameItemType;
import com.eladrador.common.item.PlayerInventory;
import com.eladrador.common.player.PlayerBackground;
import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.player.PlayerClass;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.ui.LowerMenu;
import com.eladrador.common.ui.UIProfile;
import com.eladrador.common.ui.UpperMenu;
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

				GameItem item1 = new GameItem("Test Item", GameItemType.SHORT_SWORD, Material.ACACIA_WOOD,
						GameItemQuality.EPIC, "A special sword", 40, 64);
				Runnable listener = new Runnable() {
					public void run() {
						Debug.log("You are very special");
					}
				};
				item1.getOnSpecialUse().addListener(listener);

				GameItem item2 = new GameItem("FUR", GameItemType.SHORT_SWORD, Material.BLACK_WOOL,
						GameItemQuality.EPIC, "The big wool", 20, 64);
				PlayerInventory inventory = pc.getInventory();
				inventory.setItem(5, item1);
				inventory.setItem(4, item2);
			}

		}.start();
	}

}
