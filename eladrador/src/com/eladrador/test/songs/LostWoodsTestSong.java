package com.eladrador.test.songs;

import org.bukkit.Sound;

import com.eladrador.common.sound.MusicNote;
import com.eladrador.common.sound.Noise;
import com.eladrador.common.sound.SoundSequence;

public class LostWoodsTestSong extends SoundSequence {

	private static float noteTime = 0.4f;

	public LostWoodsTestSong() {
		super(16 * noteTime);

		// Sound instrument = Sound.BLOCK_NOTE_BLOCK_FLUTE;
		Sound instrument = Sound.ENTITY_CAT_DEATH;

		Noise lowB = new Noise(instrument, 1, MusicNote.B_6.getPitch());
		Noise lowC = new Noise(instrument, 1, MusicNote.C_7.getPitch());
		Noise lowD = new Noise(instrument, 1, MusicNote.D_9.getPitch());
		Noise lowE = new Noise(instrument, 1, MusicNote.E_11.getPitch());
		Noise lowDSharp = new Noise(instrument, 1, MusicNote.D_SHARP_10.getPitch());
		Noise lowF = new Noise(instrument, 1, MusicNote.F_12.getPitch());
		Noise highG = new Noise(instrument, 1, MusicNote.G_14.getPitch());
		Noise highA = new Noise(instrument, 1, MusicNote.A_16.getPitch());
		Noise highB = new Noise(instrument, 1, MusicNote.B_18.getPitch());
		Noise highC = new Noise(instrument, 1, MusicNote.C_19.getPitch());
		Noise highCSharp = new Noise(instrument, 1, MusicNote.C_SHARP_20.getPitch());
		Noise highD = new Noise(instrument, 1, MusicNote.D_21.getPitch());
		Noise highE = new Noise(instrument, 1, MusicNote.E_23.getPitch());

		addSound(0, lowF);
		addSound(0.5 * noteTime, highA);
		addSound(1 * noteTime, highB);

		addSound(2 * noteTime, lowF);
		addSound(2.5 * noteTime, highA);
		addSound(3 * noteTime, highB);

		addSound(4 * noteTime, lowF);
		addSound(4.5 * noteTime, highA);
		addSound(5 * noteTime, highB);
		addSound(5.5 * noteTime, highE);
		addSound(6 * noteTime, highD);
		addSound(7 * noteTime, highB);
		addSound(7.5 * noteTime, highC);

		addSound(8 * noteTime, highB);
		addSound(8.5 * noteTime, highG);
		addSound(9 * noteTime, lowE);

		addSound(11.5 * noteTime, lowD);
		addSound(12 * noteTime, lowE);
		addSound(12.5 * noteTime, highG);
		addSound(13 * noteTime, lowE);

	}

}
