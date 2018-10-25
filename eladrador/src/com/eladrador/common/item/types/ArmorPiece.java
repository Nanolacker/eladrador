package com.eladrador.common.item.types;

import org.bukkit.Material;

import com.eladrador.common.item.ItemQuality;

public abstract class ArmorPiece extends GItemStack {

	public enum ArmorSlot {
		HEAD, CHEST, LEGS, FEET
	}
	
	private ArmorSlot slot;
	
	protected ArmorPiece(String name, ItemQuality quality, Material imageMat, ArmorSlot slot) {
		super(name, quality, imageMat,1);
		this.slot = slot;
	}
	
	public ArmorSlot getSlot() {
		return slot;
	}

}
