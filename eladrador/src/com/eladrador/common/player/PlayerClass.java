package com.eladrador.common.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.Listener;

public class PlayerClass implements Listener {

	/**
	 * Keys are PlayerClass IDs.
	 */
	private static final HashMap<Integer, PlayerClass> CLASS_MAP = new HashMap<Integer, PlayerClass>();

	private String name;
	private int id;
	private Material image;
	private ArrayList<Skill> skills;
	/**
	 * The first index corresponds to the ordinal of the AbilityScoreType enum
	 * value. The second corresponds to the level of a PlayerCharacter.
	 */
	private int[][] baseAbilityScores;

	protected PlayerClass(String name, int id, Material image) {
		this.name = name;
		this.id = id;
		this.image = image;
		skills = new ArrayList<Skill>();
		CLASS_MAP.put(id, this);
	}

	public static PlayerClass byID(int id) {
		return CLASS_MAP.get(id);
	}

	public String getName() {
		return name;
	}

	public int getID() {
		return id;
	}

	/**
	 * The material to display for this PlayerClass in a menu.
	 */
	public Material getImageMaterial() {
		return image;
	}

	public int getBaseAbilityScore(AbilityScoreType type, int level) {
		return baseAbilityScores[type.ordinal()][level];
	}

	protected void registerSkill(Skill skill) {
		skill.playerClass = this;
		skills.add(skill);
	}

}
