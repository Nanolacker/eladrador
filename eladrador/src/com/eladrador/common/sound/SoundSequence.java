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
		elements = new ArrayList<>();
	}

	public double getDuration() {
		return duration;
	}

	public ArrayList<SoundSequenceElement> getElements() {
		return elements;
	}

	public void addSound(double time, Noise noise) {
		if (time > duration) {
			throw new IllegalArgumentException(
					"Cannot add a Noise at a time greater than this SoundSequence's duration");
		} else if (time < 0.0) {
			throw new IllegalArgumentException("Cannot add a Noise to play at a negative time");
		}
		SoundSequenceElement element = new SoundSequenceElement(time, noise);
		elements.add(element);
	}

}
