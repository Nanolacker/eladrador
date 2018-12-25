package com.eladrador.test;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonAddress;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.ui.UIProfile;
import com.eladrador.common.ui.UpperMenu;
import com.eladrador.common.ui.UpperMenu.UpperMenuSize;

import net.md_5.bungee.api.ChatColor;

public class MenuTestListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		UIProfile prof = UIProfile.byPlayer(player);

		UpperMenu menu = new UpperMenu("first", UpperMenuSize.FIFTY_FOUR);

		Button addThingToCursor = new Button(ChatColor.GOLD + "Add to Cursor", null, Material.GOLD_INGOT) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				prof.setButtonOnCursor(this);
				if (addressClicked != null) {
					addressClicked.getContainer().removeButton(addressClicked.getIndex());
				}
			}
		};

		menu.addButton(addThingToCursor, 0);
		DelayedTask dt = new DelayedTask(2) {

			@Override
			protected void run() {
				prof.openUpperMenu(menu);
			}

		};
		dt.start();
	}

}
