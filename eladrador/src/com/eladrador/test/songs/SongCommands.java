package com.eladrador.test.songs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.eladrador.common.sound.SoundSequence;

public class SongCommands implements CommandExecutor {

	private SoundSequence song;

	public SongCommands() {
		song = new TestSong();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (label.equals("play")) {
			song.play(p, true);
		} else if (label.equals("pause")) {
			song.pause();
		} else if (label.equals("stop")) {
			song.stop();
		} else if (label.equals("sequencetime")) {
			p.sendMessage(song.getSequenceTime() + "");
		} else if (label.equals("duration")) {
			p.sendMessage(song.getDuration() + "");
		} else if (label.equals("playing")) {
			p.sendMessage(song.getPlaying() + "");
		}
		return true;
	}

}
