package com.eladrador.common.sound;

import java.util.ArrayList;

class SoundSequenceProfile {

	private int duration;
	private ArrayList<SoundSequenceElement> elements;

	private SoundSequenceProfile() {
		// only to be instantiated by Gson
	}

	double getDuration() {
		return duration;
	}

	ArrayList<SoundSequenceElement> getElements() {
		return elements;
	}

}
