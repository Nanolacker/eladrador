package com.eladrador.common.sound;

import java.util.ArrayList;

/**
 * A playable, pausable, and stoppable sequence of sounds. Useful for music and
 * dialogue.
 */
public class SoundSequence {

	private double duration;
	private ArrayList<SoundSequenceElement> elements;

	public SoundSequence(double duration) {
		this.duration = duration;
		elements = new ArrayList<SoundSequenceElement>();
	}

	public double getDuration() {
		return duration;
	}

	public ArrayList<SoundSequenceElement> getElements() {
		return elements;
	}

	public void addNoise(Noise noise, double time) {
		if (time > duration) {
			throw new IllegalArgumentException(
					"Cannot add a Noise at a time greater than this SoundSequence's duration");
		} else if (time < 0.0) {
			throw new IllegalArgumentException("Cannot add a Noise to play at a negative time");
		}
		SoundSequenceElement element = new SoundSequenceElement(noise, time);
		elements.add(element);
	}

}
