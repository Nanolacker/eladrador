package com.eladrador.common.item;

import java.util.ArrayList;

import org.bukkit.Material;

import com.eladrador.common.character.CharacterEffect;
import com.eladrador.common.character.PlayerCharacterOLD;

import character.PlayerCharacter;

public class EquipmentItem extends GameItem {

	private ArrayList<CharacterEffect> onEquipEffects;

	public EquipmentItem(String id, String name, Material icon, GameItemType type, GameItemQuality quality,
			String flavorText) {
		super(id, name, icon, type, quality, flavorText);
		onEquipEffects = new ArrayList<CharacterEffect>();
	}

	public void addOnEquipEffect(CharacterEffect onEquipEffect) {
		onEquipEffects.add(onEquipEffect);
	}

	public void onEquip(PlayerCharacter pc) {
		for (CharacterEffect effect : onEquipEffects) {
			effect.onApply(pc);
		}
	}

	public void onUnequip(PlayerCharacter pc) {
		for (CharacterEffect effect : onEquipEffects) {
			effect.onRemove(pc);
		}
	}

}
