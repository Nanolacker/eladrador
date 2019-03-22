package com.eladrador.common.sound;

public class SoundSequenceElement {

	private final double time;
	private final Noise noise;

	public SoundSequenceElement(double time, Noise noise) {
		this.time = time;
		this.noise = noise;
	}

	/**
	 * Returns the time in seconds at which this SoundSequenceElement's Noise will
	 * be played in its parent SoundSequence.
	 */
	public double getTime() {
		return time;
	}

	public Noise getNoise() {
		return noise;
	}

}
