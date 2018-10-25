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

		addNoise(mainC, 0);
		addNoise(secondaryC, 0);
		addNoise(mainDSharp, 0.5);
		addNoise(secondaryC, 0.5);
		addNoise(mainG, 1);
		addNoise(secondaryC, 1);
		addNoise(mainC, 1.5);
		addNoise(secondaryC, 1.5);
		addNoise(mainDSharp, 2);
		addNoise(secondaryC, 2);
		addNoise(mainG, 2.5);
		addNoise(secondaryC, 2.5);
		addNoise(mainC, 3);
		addNoise(secondaryC, 3);
		addNoise(mainDSharp, 3.5);
		addNoise(secondaryC, 3.5);
		addNoise(mainG, 4);
		addNoise(secondaryC, 4);
		addNoise(mainC, 4.5);
		addNoise(secondaryC, 4.5);
		addNoise(mainDSharp, 5);
		addNoise(secondaryC, 5);
		addNoise(mainG, 5.5);
		addNoise(secondaryC, 5.5);

		addNoise(mainB, 6);
		addNoise(secondaryB, 6);
		addNoise(mainD, 6.5);
		addNoise(secondaryB, 6.5);
		addNoise(mainF, 7);
		addNoise(secondaryB, 7);
		addNoise(mainB, 7.5);
		addNoise(secondaryB, 7.5);
		addNoise(mainD, 8);
		addNoise(secondaryB, 8);
		addNoise(mainF, 8.5);
		addNoise(secondaryB, 8.5);
		addNoise(mainB, 9);
		addNoise(secondaryB, 9);
		addNoise(mainD, 9.5);
		addNoise(secondaryB, 9.5);
		addNoise(mainF, 10);
		addNoise(secondaryB, 10);
		addNoise(mainB, 10.5);
		addNoise(secondaryB, 10.5);
		addNoise(mainD, 11);
		addNoise(secondaryB, 11);
		addNoise(mainF, 11.5);
		addNoise(secondaryB, 11.5);

	}

}
