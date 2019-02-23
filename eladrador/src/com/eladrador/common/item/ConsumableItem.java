package com.eladrador.common.item;

import org.bukkit.Material;

public abstract class ConsumableItem extends GameItem {

	public ConsumableItem(String id, String name, Material icon, GameItemQuality quality, String flavorText) {
		super(id, name, icon, GameItemType.CONSUMABLE, quality, flavorText);
	}

	public abstract void onUse();

}
