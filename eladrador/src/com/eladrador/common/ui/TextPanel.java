package com.eladrador.common.ui;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.util.Vector;

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
	private static final double MAX_SPAWN_DISTANCE_FROM_PLAYER = 50.0;
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
	 * The maximum allowed offset between a {@code TextPanel}'s location and the
	 * locations of its entities. Sometimes the entities become caught on blocks and
	 * must be freed once they are detected as being more than this distance from
	 * where they should be.
	 */
	private static final double ALLOWED_ENTITY_LOC_OFFSET = 0.1;

	/**
	 * Stores all {@code TextPanels} that are instantiated so that the entities
	 * associated with them can be removed when the server is stopped.
	 */
	private static ArrayList<TextPanel> textPanels = new ArrayList<TextPanel>();

	/**
	 * Whether this {@code TextPanel} is visible and rendering text.
	 */
	private boolean visible;
	/**
	 * The maximum number of characters that will be rendered on this
	 * {@code TextPanel} per line before a new line is started.
	 */
	private int maxCharsPerLine;
	/**
	 * The text rendered by this {@code TextPanel}.
	 */
	private String text;
	/**
	 * The text that has been formatted to be rendered across multiple lines.
	 */
	private ArrayList<String> formattedText;
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

	/**
	 * Constructs a new {@code TextPanel}. Be sure to invoke {@code setVisible} to
	 * make it visible. If this is not done, the text will be invisible.
	 * 
	 * @param text     the text rendered by this {@code TextPanel}
	 * @param location this {@code TextPanel}'s {@code Location}
	 */
	public TextPanel(String text, int maxCharsPerLine, Location location) {
		visible = false;
		this.maxCharsPerLine = maxCharsPerLine;
		setText(text);
		this.location = location;
		entities = new ArrayList<EntityArmorStand>();
		spawned = false;
		initSpawnManageTask();
		textPanels.add(this);
	}

	/**
	 * Constructs a new {@code TextPanel} whose text value defaults to {@code ""}.
	 * Be sure to invoke {@code setVisible} to make it visible. If this is not done,
	 * the text will be invisible.
	 * 
	 * @param location this {@code TextPanel}'s {@code Location}
	 */
	public TextPanel(int maxCharsPerLine, Location location) {
		visible = false;
		this.maxCharsPerLine = maxCharsPerLine;
		setText("");
		this.location = location;
		entities = new ArrayList<EntityArmorStand>();
		spawned = false;
		initSpawnManageTask();
		textPanels.add(this);
	}

	/**
	 * Removes all entities associated with any {@code TextPanel}s to keep worlds
	 * clean.
	 * <p>
	 * <b>Only to be invoked once during runtime, when the server is stopping.</b>
	 */
	public static void removeAllTextPanelEntities() {
		for (int i = 0; i < textPanels.size(); i++) {
			TextPanel textPanel = textPanels.get(i);
			textPanel.setVisible(false);
		}
	}

	/**
	 * Initializes the {@code RepeatingTask} used to manage spawning entities
	 * associated with this {@code TextPanel}.
	 */
	private void initSpawnManageTask() {
		entitySpawnManageTask = new RepeatingTask(SPAWN_MANAGE_TASK_PERIOD) {

			@Override
			protected void run() {
				boolean playerNearby = Players.playerNearby(location, MAX_SPAWN_DISTANCE_FROM_PLAYER,
						MAX_SPAWN_DISTANCE_FROM_PLAYER, MAX_SPAWN_DISTANCE_FROM_PLAYER);
				if (playerNearby) {
					if (spawned) {
						Location idealEntityLoc = getLocation().clone();
						for (int i = 0; i < entities.size(); i++) {
							EntityArmorStand entity = entities.get(i);
							double offsetX = idealEntityLoc.getX() - entity.locX;
							double offsetY = idealEntityLoc.getY() - entity.locY;
							double offsetZ = idealEntityLoc.getZ() - entity.locZ;
							if (offsetX > ALLOWED_ENTITY_LOC_OFFSET || offsetY > ALLOWED_ENTITY_LOC_OFFSET
									|| offsetZ > ALLOWED_ENTITY_LOC_OFFSET) {
								removeSingleEntity(i);
								spawnSingleEntity(i);
							}
							idealEntityLoc.subtract(0, LINE_SEPEARATION_DISTANCE, 0);
						}
					} else {
						spawnEntities();
					}
				} else if (!playerNearby && spawned) {
					removeEntities();
				}
			}

		};
	}

	/**
	 * Returns whether this {@code TextPanel} is visible and rendering text.
	 */
	public boolean getVisible() {
		return visible;
	}

	/**
	 * Sets whether this {@code TextPanel} is visible and rendering text.
	 */
	public void setVisible(boolean visible) {
		boolean redundant = this.visible == visible;
		if (redundant) {
			return;
		}
		this.visible = visible;
		if (visible) {
			entitySpawnManageTask.start();
		} else {
			removeEntities();
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
		formattedText = StrUtils.lineToParagraph(text, maxCharsPerLine);
		if (visible) {
			removeEntities();
			spawnEntities();
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
		if (visible) {
			Vector movement = location.toVector().subtract(temp.toVector());
			for (int i = 0; i < entities.size(); i++) {
				EntityArmorStand entity = entities.get(i);
				entity.move(null, movement.getX(), movement.getY(), movement.getZ());
			}
		}
	}

	/**
	 * Spawns all of the entities entities necessary to display text.
	 */
	private void spawnEntities() {
		int numLines = formattedText.size();
		for (int lineCount = 0; lineCount < numLines; lineCount++) {
			spawnSingleEntity(lineCount);
		}
		spawned = true;
	}

	/**
	 * Spawns a single entity to represent a single line of this {@code TextPanel}.
	 * 
	 * @param lineCount the number of line that the entity will represent
	 */
	private void spawnSingleEntity(int lineCount) {
		Location spawnLoc = new Location(location.getWorld(), location.getX(),
				location.getY() - lineCount * LINE_SEPEARATION_DISTANCE, location.getZ());
		World world = ((CraftWorld) location.getWorld()).getHandle();
		EntityArmorStand entity = new EntityArmorStand(world);
		entity.setPosition(spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ());
		entity.setNoGravity(true);
		entity.setInvisible(true);
		entity.setSmall(true);
		entity.setArms(false);
		entity.setMarker(true);
		String line = formattedText.get(lineCount);
		entity.getBukkitEntity().setCustomName(line);
		entity.setCustomNameVisible(true);
		world.addEntity(entity);
		entities.add(lineCount, entity);
	}

	/**
	 * Removes old entities so that this {@code TextPanel} no longer displays text.
	 */
	private void removeEntities() {
		int numEntities = entities.size();
		for (int i = 0; i < numEntities; i++) {
			removeSingleEntity(0);
		}
		entities.clear();
		spawned = false;
	}

	/**
	 * Removes a single entity.
	 * 
	 * @param lineCount the number of line that the entity to be removed represents.
	 */
	private void removeSingleEntity(int lineCount) {
		World world = ((CraftWorld) location.getWorld()).getHandle();
		EntityArmorStand entity = entities.get(lineCount);
		world.removeEntity(entity);
		entities.remove(entity);
	}

}
