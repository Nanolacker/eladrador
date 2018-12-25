package com.eladrador.common.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.eladrador.common.Debug;
import com.eladrador.common.quest.persistence.QuestState;
import com.google.common.io.Files;

/**
 * This class will need to be heavily refactored in the future to support MySQL
 * instead of serializing to files.
 *
 */
final class PlayerCharacterPersistence {

	private static final String SAVES_DIR_PATH = "C:\\Users\\conno\\Eladrador Server\\plugins\\player_character_saves";

	/**
	 * Returns the stored {@code PlayerCharacter} that corresponds to the specified
	 * {@code Player} and save slot.
	 */
	static PlayerCharacterSaveData retrieveData(Player bukkitPlayer, int saveSlot) {
		try {
			File saveFile = getSaveFile(bukkitPlayer.getName(), saveSlot);
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile));
			PlayerCharacterSaveData data = (PlayerCharacterSaveData) in.readObject();
			in.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Stores the specified {@code PlayerCharacter} to be retrieved later.
	 */
	static void storeData(PlayerCharacterSaveData data) {
		try {
			File saveFile = getSaveFile(data.name, data.saveSlot);
			saveFile.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile));
			out.writeObject(data);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void deleteData(Player bukkitPlayer, int saveSlot) {
		File saveFile = getSaveFile(bukkitPlayer.getName(), saveSlot);
		saveFile.delete();
	}

	static boolean saveExists(Player bukkitPlayer, int saveSlot) {
		File saveFile = getSaveFile(bukkitPlayer.getName(), saveSlot);
		return saveFile.exists();
	}

	static File getSaveFile(String playerName, int saveSlot) {
		return new File(SAVES_DIR_PATH + "\\" + playerName + "_" + saveSlot);
	}

}
