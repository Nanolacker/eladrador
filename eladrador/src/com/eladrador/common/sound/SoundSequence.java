package com.eladrador.common.sound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.scheduling.GClock;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

/**
 * A playable, pausable, and stoppable sequence of sounds. Useful for music and
 * dialogue.
 */
public class SoundSequence {

	private ArrayList<SoundSequenceElement> elements;
	private double duration;
	private ArrayList<DelayedTask> playTasks;
	private boolean playing;
	private double globalStartTime;
	private double sequenceStartTime;
	private boolean looping;

	public SoundSequence(double duration) {
		elements = new ArrayList<SoundSequenceElement>();
		this.duration = duration;
		playTasks = new ArrayList<DelayedTask>();
		globalStartTime = 0;
		sequenceStartTime = 0;
		playing = false;
		looping = false;
	}

	private SoundSequence(SoundSequenceProfile profile) {
		duration = profile.getDuration();
		elements = profile.getElements();
		playTasks = new ArrayList<DelayedTask>();
		globalStartTime = 0;
		sequenceStartTime = 0;
		playing = false;
		looping = false;
	}

	public static SoundSequence fromJson(String jsonFileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(jsonFileName));
			Gson gson = new Gson();
			SoundSequenceProfile profile = gson.fromJson(reader, SoundSequenceProfile.class);
			return new SoundSequence(profile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

	}

	public void addNoise(Noise noise, double time) {
		if (playing) {
			throw new IllegalStateException("Cannot add a Noise to a SoundSequence that is currently playing");
		}
		if (time <= duration) {
			if (time >= 0) {
				SoundSequenceElement element = new SoundSequenceElement(noise, time);
				elements.add(element);
			} else {
				throw new IllegalArgumentException("Cannot add a Noise to play at a negative time");
			}
		} else {
			throw new IllegalArgumentException(
					"Cannot add a Noise at a time greater than this SoundSequence's duration");
		}
	}

	public double getDuration() {
		return duration;
	}

	public boolean getPlaying() {
		return playing;
	}

	/**
	 * 
	 * @return the amount of time in seconds through the sequence that has played
	 */
	public double getSequenceTime() {
		return sequenceStartTime + (playing ? GClock.getTime() - globalStartTime : 0);
	}

	public void play(boolean loop) {
		if (playing) {
			stop();
		}
		playing = true;
		globalStartTime = GClock.getTime();
		looping = loop;
		for (int i = 0; i < elements.size(); i++) {
			SoundSequenceElement element = elements.get(i);
			double noiseTime = element.getTime();
			if (noiseTime > sequenceStartTime) {
				Noise noise = element.getNoise();
				Runnable r = new Runnable() {

					@Override
					public void run() {
						noise.play();
					}

				};
				DelayedTask playNoise = new DelayedTask(r, noiseTime - sequenceStartTime);
				playTasks.add(playNoise);
				playNoise.start();
			}
		}
		Runnable r = new Runnable() {

			@Override
			public void run() {
				stop();
				if (looping) {
					play(true);
				}
			}

		};
		DelayedTask finish = new DelayedTask(r, duration - sequenceStartTime);
		playTasks.add(finish);
		finish.start();
	}

	/**
	 * Plays to a specific player only.
	 * 
	 * @param p the player
	 */
	public void play(Player p, boolean loop) {
		if (playing) {
			stop();
		}
		playing = true;
		globalStartTime = GClock.getTime();
		looping = loop;
		for (int i = 0; i < elements.size(); i++) {
			SoundSequenceElement element = elements.get(i);
			double noiseTime = element.getTime();
			if (noiseTime >= sequenceStartTime) {
				Noise noise = element.getNoise();
				Runnable r = new Runnable() {

					@Override
					public void run() {
						noise.play(p);
					}

				};
				DelayedTask playNoise = new DelayedTask(r, noiseTime - sequenceStartTime);
				playTasks.add(playNoise);
				playNoise.start();
			}
		}
		Runnable r = new Runnable() {

			@Override
			public void run() {
				stop();
				if (looping) {
					play(p, true);
				}
			}

		};
		DelayedTask finish = new DelayedTask(r, duration - sequenceStartTime);
		playTasks.add(finish);
		finish.start();
	}

	public void pause() {
		if (!playing) {
			throw new IllegalStateException("Cannot pause a sequence that is not playing");
		} else {
			playing = false;
			sequenceStartTime += GClock.getTime() - globalStartTime;
			for (int i = 0; i < playTasks.size(); i++) {
				DelayedTask playNoise = playTasks.get(i);
				if (playNoise.getActive()) {
					double taskExeTime = playNoise.getExeTime();
					if (taskExeTime >= GClock.getTime()) {
						playNoise.stop();
						for (int c = i + 1; c < playTasks.size(); c++) {
							DelayedTask followingTask = playTasks.get(c);
							followingTask.stop();
						}
						break;
					}
				}
			}
			playTasks.clear();
		}
	}

	public void stop() {
		if (playing) {
			playing = false;
			sequenceStartTime = 0;
			for (int i = 0; i < playTasks.size(); i++) {
				DelayedTask playNoise = playTasks.get(i);
				if (playNoise.getActive()) {
					double taskExeTime = playNoise.getExeTime();
					if (taskExeTime > GClock.getTime()) {
						playNoise.stop();
						for (int c = i + 1; c < playTasks.size(); c++) {
							DelayedTask followingTask = playTasks.get(c);
							followingTask.stop();
						}
						break;
					}
				}
			}
			playTasks.clear();
		} else {
			throw new IllegalStateException("Cannot stop a sequence that is not playing");
		}
	}

}
