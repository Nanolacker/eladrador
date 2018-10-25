package com.eladrador.common.event.character;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.eladrador.common.character.AbstractCharacter;

public class CharacterDamageCharacterEvent extends Event {

	private HandlerList handlers;
	private AbstractCharacter damager, damageRecipient;

	public CharacterDamageCharacterEvent(AbstractCharacter damager, AbstractCharacter damageRecipient) {
		this.damager = damager;
		this.damageRecipient = damageRecipient;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
