package com.eladrador.common.player;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.Listener;

public class PlayerClass implements Listener {

	private String name;
	private Material image;
	private ArrayList<Skill> skills;
	/**
	 * The first index corresponds to the ordinal of the AbilityScoreType enum
	 * value. The second corresponds to the level of a PlayerCharacter.
	 */
	private int[][] baseAbilityScores;

	protected PlayerClass(String name, Material image) {
		this.name = name;
		this.image = image;
		skills = new ArrayList<Skill>();
	}

	public String getName() {
		return name;
	}

	/**
	 * The material to display for this PlayerClass in an inventory menu.
	 */
	public Material getImage() {
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
