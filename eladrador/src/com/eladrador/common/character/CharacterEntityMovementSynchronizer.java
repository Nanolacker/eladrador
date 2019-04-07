package com.eladrador.common.character;

import org.bukkit.entity.Entity;

import com.eladrador.common.scheduling.RepeatingTask;

/**
 * Allows the movement of an entity and a character to be synchronized. Whenever
 * one moves, the other will move as well, thus keeping their locations
 * synchronized.
 */
public class CharacterEntityMovementSynchronizer {

	/**
	 * The mode by which an {@code CharacterEntityMovementSynchronizer} will
	 * operate.
	 */
	public static enum MovementSynchronizeMode {
	/**
	 * The character of this {@code CharacterEntityMovementSynchronizer} will follow
	 * the movement of the entity of this
	 * {@code CharacterEntityMovementSynchronizer}.
	 */
	CHARACTER_FOLLOWS_ENTITY,
	/**
	 * The entity of this {@code CharacterEntityMovementSynchronizer} will follow
	 * the movement of the character of this
	 * {@code CharacterEntityMovementSynchronizer}.
	 */
	ENTITY_FOLLOWS_CHARACTER;

	}

	private static final double SYNC_TASK_PERIOD = 0.03;

	private boolean enabled;
	/**
	 * The mode by which this {@code CharacterEntityMovementSynchronizer} will
	 * operate.
	 */
	private GameCharacter character;
	private Entity entity;
	private final MovementSynchronizeMode mode;
	private RepeatingTask syncTask;

	/**
	 * Be sure to invoke {@code setEnabled} to enable this
	 * {@code CharacterEntityMovementSynchronizer}.
	 */
	public CharacterEntityMovementSynchronizer(GameCharacter character, Entity entity,
			MovementSynchronizeMode mode) {
		this.character = character;
		this.entity = entity;
		this.mode = mode;
		initSyncTask();
	}

	private void initSyncTask() {
		syncTask = new RepeatingTask(SYNC_TASK_PERIOD) {

			@Override
			protected void run() {
				switch (mode) {
				case CHARACTER_FOLLOWS_ENTITY:
					character.setLocation(entity.getLocation());
					break;
				case ENTITY_FOLLOWS_CHARACTER:
					entity.teleport(character.getLocation());
					break;
				default:
					break;
				}
			}

		};
	}

	public void setEnabled(boolean enabled) {
		boolean redundant = this.enabled == enabled;
		if (redundant) {
			return;
		}
		this.enabled = enabled;
		if (enabled) {
			syncTask.start();
		} else {
			syncTask.stop();
		}
	}

}
