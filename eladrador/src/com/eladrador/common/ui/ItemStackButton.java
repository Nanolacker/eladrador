package com.eladrador.common.ui;

import org.bukkit.entity.Player;

import com.eladrador.common.character.PlayerCharacter;
import com.eladrador.common.item.types.GItemStack;

public class ItemStackButton extends Button {

	private GItemStack itemStack;

	public ItemStackButton(PlayerCharacter pc, GItemStack itemStack) {
		super(itemStack.getDisplayName(), itemStack.getDescription(pc),
				itemStack.getImageMaterial());
		this.itemStack = itemStack;
	}

	@Override
	protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
		
	}

}
