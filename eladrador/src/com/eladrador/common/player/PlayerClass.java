package com.eladrador.common.player;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.Listener;

public class PlayerClass implements Listener {

	/**
	 * Keys are class names.
	 */
	private static final HashMap<String, PlayerClass> classNameMap = new HashMap<>();

	private final String name;
	private final Material image;
	private final ArrayList<Skill> skills;
	/**
	 * The first index corresponds to the ordinal of the AbilityScoreType enum
	 * value. The second corresponds to the level of a PlayerCharacter.
	 */
	private final int[][] baseAbilityScores;

	public PlayerClass(String name, Material image) {
		this.name = name;
		this.image = image;
		skills = new ArrayList<Skill>();
		classNameMap.put(name, this);
		baseAbilityScores = new int[0][0];
	}

	public static PlayerClass forName(String name) {
		return classNameMap.get(name);
	}

	public String getName() {
		return name;
	}

	/**
	 * The material to display for this PlayerClass in a menu.
	 */
	public Material getImageMaterial() {
		return image;
	}

	public int getBaseAbilityScore(AbilityScoreType type, int level) {
		return baseAbilityScores[type.ordinal()][level - 1];
	}

	public void registerSkill(Skill skill) {
		skill.playerClass = this;
		skills.add(skill);
	}

}
