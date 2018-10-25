package com.eladrador.common.sound;

public class SoundSequenceElement {

	private Noise noise;
	private double time;

	public SoundSequenceElement(Noise noise, double time) {
		this.noise = noise;
		this.time = time;
	}

	public Noise getNoise() {
		return noise;
	}

	/**
	 * Returns the time in seconds at which this SoundSequenceElement's Noise will
	 * be played in its parent SoundSequence.
	 */
	public double getTime() {
		return time;
	}

}
