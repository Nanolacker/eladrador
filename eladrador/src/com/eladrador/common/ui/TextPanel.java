package com.eladrador.common.ui;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;

import com.eladrador.common.Debug;
import com.eladrador.common.scheduling.RepeatingTask;
import com.eladrador.common.utils.Players;
import com.eladrador.common.utils.StrUtils;

import net.minecraft.server.v1_13_R2.EntityArmorStand;
import net.minecraft.server.v1_13_R2.World;

/**
 * Displays text on a panel in a Minecraft world.
 */
public class TextPanel {

	/**
	 * The maximum distance from a {@code Player} that a {@code TextPanel} can be
	 * before its associated entities despawn.
	 */
	private static final int MAX_SPAWN_DISTANCE_FROM_PLAYER = 50;
	/**
	 * The maximum distance in blocks over which entities of {@code TextPanel}s will
	 * be relocated. If a {@code TextPanel} is moved across distance greater than
	 * this value, its entity will be deleted and reconstructed instead of simply
	 * being relocated. The reconstruction of entities like this prevents them from
	 * getting stuck on blocks during movement.
	 */
	private static final double MAX_SAFE_MOVE_DISTANCE = 2.0;
	/**
	 * The distance between two consecutive lines in a multi-lined
	 * {@code TextPanel}.
	 */
	private static final double LINE_SEPEARATION_DISTANCE = 0.25;
	/**
	 * The period of each {@code TextPanel}'s {@code spawnManageTask}.
	 */
	private static final double SPAWN_MANAGE_TASK_PERIOD = 1.0;

	/**
	 * Stores all {@code TextPanels} that are instantiated so that the entities
	 * associated with them can be removed when the server is stopped.
	 */
	private static ArrayList<TextPanel> textPanels;

	/**
	 * whether this {@code TextPanel} is enabled and rendering textF
	 */
	private boolean enabled;
	/**
	 * the text rendered by this {@code TextPanel}
	 */
	private String text;
	/**
	 * The maximum number of characters that will be rendered on this
	 * {@code TextPanel} per line before a new line is started.
	 */
	private int maxCharsPerLine;
	/**
	 * this {@code TextPanel}'s {@code Location}
	 */
	private Location location;
	/**
	 * the Minecraft entities used to display this {@code TextPanel}
	 */
	private ArrayList<EntityArmorStand> entities;
	/**
	 * A {@code RepeatingTask} which manages spawning and despawning the entities
	 * associated with this {@code TextPanel}.
	 */
	private RepeatingTask entitySpawnManageTask;
	/**
	 * Whether the entities associate with this {@code TextPanel} are spawned.
	 */
	private boolean spawned;

	static {
		textPanels = new ArrayList<TextPanel>();
	}

	/**
	 * Constructs a new {@code TextPanel}. Be sure to use {@code setEnabled} to
	 * enable it. If left disabled, text will not be rendered.
	 * 
	 * @param text     the text rendered by this {@code TextPanel}
	 * @param location this {@code TextPanel}'s {@code Location}
	 */
	public TextPanel(String text, int maxCharsPerLine, Location location) {
		enabled = false;
		this.text = text;
		this.maxCharsPerLine = maxCharsPerLine;
		this.location = location;
		entities = new ArrayList<EntityArmorStand>();
		spawned = false;
		initSpawnManageTask();
		textPanels.add(this);
	}

	/**
	 * Initializes the {@code RepeatingTask} used to manage spawning entities
	 * associated with this {@code TextPanel}.
	 */
	private void initSpawnManageTask() {
		Runnable spawnCheck = new Runnable() {

			@Override
			public void run() {
				boolean playerNearby = Players.playerNearby(location, MAX_SPAWN_DISTANCE_FROM_PLAYER,
						MAX_SPAWN_DISTANCE_FROM_PLAYER, MAX_SPAWN_DISTANCE_FROM_PLAYER);
				if (playerNearby && !spawned) {
					constructNewEntities();
				} else if (!playerNearby && spawned) {
					removeOldEntities();
				}
			}

		};
		entitySpawnManageTask = new RepeatingTask(spawnCheck, SPAWN_MANAGE_TASK_PERIOD);
		// task not started until later
	}

	/**
	 * Removes all entities associated with any {@code TextPanel}s to keep worlds
	 * clean. ONLY TO BE USED ONCE WHEN THE SERVER IS STOPPING.
	 */
	public static void removeAllTextPanelEntities() {
		for (int i = 0; i < textPanels.size(); i++) {
			TextPanel textPanel = textPanels.get(i);
			textPanel.setEnabled(false);
		}
	}

	/**
	 * Returns whether this {@code TextPanel} is enabled and rendering text.
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * Sets whether this {@code TextPanel} is enabled and rendering text.
	 */
	public void setEnabled(boolean enabled) {
		boolean redundant = this.enabled == enabled;
		if (redundant) {
			return;
		}
		this.enabled = enabled;
		if (enabled) {
			entitySpawnManageTask.start();
		} else {
			removeOldEntities();
			entitySpawnManageTask.stop();
		}
	}

	/**
	 * Returns the text rendered by this {@code TextPanel}.
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text rendered by this {@code TextPanel}.
	 */
	public void setText(String text) {
		this.text = text;
		if (enabled) {
			// updates text
			removeOldEntities();
			constructNewEntities();
		}
	}

	/**
	 * Returns this {@code TextPanel}'s {@code Location}.
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets this {@code TextPanel}'s {@code Location}.
	 */
	public void setLocation(Location location) {
		Location temp = this.location;
		this.location = location;
		if (enabled) {
			double translateX = temp.getX() - location.getX();
			double translateY = temp.getY() - location.getY();
			double translateZ = temp.getZ() - location.getZ();
			boolean underSafeMoveDistance = translateX <= MAX_SAFE_MOVE_DISTANCE && translateY <= MAX_SAFE_MOVE_DISTANCE
					&& translateZ <= MAX_SAFE_MOVE_DISTANCE;
			if (underSafeMoveDistance) {
				for (int i = 0; i < entities.size(); i++) {
					EntityArmorStand entity = entities.get(i);
					entity.setPosition(location.getX(), location.getY(), location.getZ());
				}
			} else {
				removeOldEntities();
				constructNewEntities();
			}
		}
	}

	/**
	 * Spawns new entities that displays text.
	 */
	private void constructNewEntities() {
		World world = ((CraftWorld) location.getWorld()).getHandle();
		ArrayList<String> lines = StrUtils.stringToParagraph(getText(), maxCharsPerLine);
		int numLines = lines.size();
		for (int lineCount = 0; lineCount < numLines; lineCount++) {
			Location spawnLoc = new Location(location.getWorld(), location.getX(),
					location.getY() - lineCount * LINE_SEPEARATION_DISTANCE, location.getZ());
			EntityArmorStand entity = new EntityArmorStand(world);
			entity.setPosition(spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ());
			entity.setNoGravity(true);
			/*
			 * For some odd reason, setting the entity invisible also makes it undamageable,
			 * so there is no need to explicitly set it invulnerable.
			 */
			entity.setInvisible(true);
			String line = lines.get(lineCount);
			entity.getBukkitEntity().setCustomName(line);
			entity.setCustomNameVisible(true);
			world.addEntity(entity);
			entities.add(entity);
		}
		spawned = true;
	}

	/**
	 * Removes old entities so that this {@code TextPanel} no longer displays text.
	 */
	private void removeOldEntities() {
		World world = ((CraftWorld) location.getWorld()).getHandle();
		for (int i = 0; i < entities.size(); i++) {
			EntityArmorStand entity = entities.get(i);
			world.removeEntity(entity);
		}
		entities.clear();
		spawned = false;
	}

}
