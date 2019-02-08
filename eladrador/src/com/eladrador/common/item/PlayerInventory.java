package com.eladrador.common.item;

import org.bukkit.entity.Player;

import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.ui.LowerMenu;
import com.eladrador.common.ui.UIProfile;

public class PlayerInventory extends GameInventory {

	public PlayerInventory(PlayerCharacter pc) {
		super(findLowerMenu(pc));
	}

	private static LowerMenu findLowerMenu(PlayerCharacter pc) {
		Player player = pc.getBukkitPlayer();
		UIProfile profile = UIProfile.forPlayer(player);
		return profile.getLowerMenu();
	}

}
