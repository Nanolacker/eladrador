package com.eladrador.test.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.eladrador.common.item.GameItem;
import com.eladrador.common.item.GameItemButton;
import com.eladrador.common.item.GameItemQuality;
import com.eladrador.common.item.GameItemType;
import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.ui.UIProfile;
import com.eladrador.common.ui.UpperMenu;

public class MenuTestListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		UIProfile prof = UIProfile.forPlayer(player);

		UpperMenu menu = new UpperMenu("first", 54);

		GameItem item = new GameItem("Test Item", GameItemType.SHORT_SWORD, Material.IRON_SWORD, "A special sword", 1,
				1);

		GameItemButton btn = new GameItemButton(item);

		menu.setButton(0, btn);
		DelayedTask dt = new DelayedTask(1) {

			@Override
			protected void run() {
				prof.openMenu(menu);
			}

		};
		dt.start();
	}

}
