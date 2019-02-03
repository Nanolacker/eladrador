package com.eladrador.common.ui;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class ButtonToggleEvent {

	private final Player player;
	private final ButtonToggleType toggleType;
	private final ButtonAddress address;
	private final Event bukkitEvent;

	ButtonToggleEvent(Player player, ButtonToggleType toggleType, ButtonAddress address, Event bukkitEvent) {
		this.player = player;
		this.toggleType = toggleType;
		this.address = address;
		this.bukkitEvent = bukkitEvent;
	}

	/**
	 * The player who toggled the button.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * How this button was triggered.
	 */
	public ButtonToggleType getToggleType() {
		return toggleType;
	}

	/**
	 * The address that the button was at when it was toggled.
	 */
	public ButtonAddress getAddress() {
		return address;
	}

	/**
	 * The Bukkit {@link Event} that corresponds to this {@code ButtonToggleEvent}.
	 * You probably won't need to use this, but it may be necessary for very special
	 * cases.
	 */
	public Event getBukkitEvent() {
		return bukkitEvent;
	}

}
