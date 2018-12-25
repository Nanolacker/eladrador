package com.eladrador.common.item.types;

import org.bukkit.Material;

import com.eladrador.common.item.ItemContainer;
import com.eladrador.common.item.ItemQuality;
import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.quest.QuestObjective;
import com.eladrador.common.ui.LowerMenu;

/**
 * An GItemStack whose use is restricted to completing quests.
 */
public abstract class QuestObjectiveItemStack extends GItemStack {

	private QuestObjective objective;

	protected QuestObjectiveItemStack(String name, ItemQuality quality, Material mat, int maxSize,
			QuestObjective objective) {
		super(name, quality, mat, maxSize);
		this.objective = objective;
	}

	public QuestObjective getQuestObjective() {
		return objective;
	}

	@Override
	public void onPickup(PlayerCharacter pc, int amount) {
		int current = objective.getStatusValueFor(pc);
		if (current != objective.getGoal()) {
			objective.setStatusValueFor(pc, current + amount);
		}
	}

	@Override
	public void onDrop(PlayerCharacter pc, int amount) {
		int current = objective.getStatusValueFor(pc);
		ItemContainer inv = pc.getInventory();
		int prevAmount = inv.getAmountOf((Class<? extends QuestObjectiveItemStack>) getClass());
		if (!(prevAmount > objective.getGoal())) {
			objective.setStatusValueFor(pc, current - amount);
		}
	}

}
