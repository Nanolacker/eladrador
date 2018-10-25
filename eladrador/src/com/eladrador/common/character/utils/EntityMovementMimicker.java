package com.eladrador.common.character.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.eladrador.common.character.AbstractCharacter;
import com.eladrador.common.scheduling.RepeatingTask;

import net.minecraft.server.v1_13_R2.Entity;

public class EntityMovementMimicker {

	private boolean enabled;
	private Location prevEntityLoc;

	public EntityMovementMimicker(Entity toMimic, AbstractCharacter toMove) {
		Runnable r = new Runnable() {

			@Override
			public void run() {

				Vector movement = new Vector();
				toMove.move(movement);
			}

		};
		RepeatingTask task = new RepeatingTask(r, 0.05);
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled) {

		}
		this.enabled = enabled;

	}

}
