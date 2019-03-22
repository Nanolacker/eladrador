package com.eladrador.test.songs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.eladrador.common.sound.SoundSequence;
import com.eladrador.common.sound.SoundSequencePlayer;

public class SongCommands implements CommandExecutor {

	private SoundSequence song;

	public SongCommands() {
		song = new TestSong();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		SoundSequencePlayer soundPlayer = new SoundSequencePlayer(song);
		if (label.equals("play")) {
			soundPlayer.play(p);
		} else if (label.equals("pause")) {
			soundPlayer.pause();
		} else if (label.equals("stop")) {
			soundPlayer.stop();
		} else if (label.equals("sequencetime")) {
			p.sendMessage(soundPlayer.getSequenceTime() + "");
		} else if (label.equals("duration")) {
			p.sendMessage(song.getDuration() + "");
		} else if (label.equals("playing")) {
			p.sendMessage(soundPlayer.getPlaying() + "");
		}
		return true;
	}

}
