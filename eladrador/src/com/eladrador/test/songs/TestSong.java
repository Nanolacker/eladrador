package com.eladrador.test.songs;

import org.bukkit.Sound;

import com.eladrador.common.sound.Noise;
import com.eladrador.common.sound.MusicNote;
import com.eladrador.common.sound.SoundSequence;

public class TestSong extends SoundSequence {

	public TestSong() {
		super(12);
		Sound instrumentMain = Sound.BLOCK_NOTE_BLOCK_GUITAR;
		Sound instrumentSecondary = Sound.BLOCK_NOTE_BLOCK_XYLOPHONE;

		Noise mainB = new Noise(instrumentMain, 1, MusicNote.B_6.getPitch());
		Noise mainC = new Noise(instrumentMain, 1, MusicNote.C_7.getPitch());
		Noise mainD = new Noise(instrumentMain, 1, MusicNote.D_9.getPitch());
		Noise mainDSharp = new Noise(instrumentMain, 1, MusicNote.D_SHARP_10.getPitch());
		Noise mainF = new Noise(instrumentMain, 1, MusicNote.F_12.getPitch());
		Noise mainG = new Noise(instrumentMain, 1, MusicNote.G_14.getPitch());

		Noise secondaryB = new Noise(instrumentSecondary, 0.5f, MusicNote.B_6.getPitch());
		Noise secondaryC = new Noise(instrumentSecondary, 0.5f, MusicNote.C_7.getPitch());
		Noise secondaryD = new Noise(instrumentSecondary, 0.5f, MusicNote.D_9.getPitch());

		addSound(0, mainC);
		addSound(0, secondaryC);
		addSound(0.5, mainDSharp);
		addSound(0.5, secondaryC);
		addSound(1, mainG);
		addSound(1, secondaryC);
		addSound(1.5, mainC);
		addSound(1.5, secondaryC);
		addSound(2, mainDSharp);
		addSound(2, secondaryC);
		addSound(2.5, mainG);
		addSound(2.5, secondaryC);
		addSound(3, mainC);
		addSound(3, secondaryC);
		addSound(3.5, mainDSharp);
		addSound(3.5, secondaryC);
		addSound(4, mainG);
		addSound(4, secondaryC);
		addSound(4.5, mainC);
		addSound(4.5, secondaryC);
		addSound(5, mainDSharp);
		addSound(5, secondaryC);
		addSound(5.5, mainG);
		addSound(5.5, secondaryC);

		addSound(6, mainB);
		addSound(6, secondaryB);
		addSound(6.5, mainD);
		addSound(6.5, secondaryB);
		addSound(7, mainF);
		addSound(7, secondaryB);
		addSound(7.5, mainB);
		addSound(7.5, secondaryB);
		addSound(8, mainD);
		addSound(8, secondaryB);
		addSound(8.5, mainF);
		addSound(8.5, secondaryB);
		addSound(9, mainB);
		addSound(9, secondaryB);
		addSound(9.5, mainD);
		addSound(9.5, secondaryB);
		addSound(10, mainF);
		addSound(10, secondaryB);
		addSound(10.5, mainB);
		addSound(10.5, secondaryB);
		addSound(11, mainD);
		addSound(11, secondaryB);
		addSound(11.5, mainF);
		addSound(11.5, secondaryB);
	}

}
