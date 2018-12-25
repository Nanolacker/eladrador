package com.eladrador.test;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.eladrador.common.AbstractGameManager;
import com.eladrador.common.Debug;
import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonAddress;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.ui.UIProfile;
import com.eladrador.common.ui.UpperMenu;
import com.eladrador.common.ui.UpperMenu.UpperMenuSize;

public class TestGameManager extends AbstractGameManager {

	@Override
	public void onEnable() {
		DelayedTask dt = new DelayedTask(5) {
			public void run() {
				menuTest();
			}
		};
		dt.start();

	}

	private void menuTest() {
		Debug.log("Testing");
		Player player = Debug.getFirstPlayerOnline();
		if (player == null) {
			Debug.log("Player not found");
			return;
		}
		Debug.log("Player found");
		UIProfile prof = UIProfile.byPlayer(player);

		UpperMenu menu = new UpperMenu("Test", UpperMenuSize.NINE);

		UpperMenu newMenu = new UpperMenu("hello there", UpperMenuSize.EIGHTEEN);

		UpperMenu evenNewerMenu = new UpperMenu("done", UpperMenuSize.TWENTY_SEVEN);

		Button openNewMenuButton = new Button("click me", null, Material.WOODEN_AXE) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				prof.openUpperMenu(newMenu);
			}

		};
		Button openEvenNewerMenuButton = new Button("The last one", null, Material.DIAMOND) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				prof.openUpperMenu(evenNewerMenu);
			}

		};
		menu.addButton(openNewMenuButton, 0);

		prof.openUpperMenu(menu);

		newMenu.addButton(openEvenNewerMenuButton, 8);
	}

	@Override
	public void onDisable() {
	}

}
