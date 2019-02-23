package com.eladrador.common.sound;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.eladrador.common.scheduling.DelayedTask;
import com.eladrador.common.scheduling.GClock;

public class SoundSequencePlayer {

	private final SoundSequence soundSequence;
	private ArrayList<DelayedTask> playSoundTasks;
	private boolean playing;
	private boolean looping;
	private double globalStartTime;
	private double sequenceTime;

	public SoundSequencePlayer(SoundSequence soundSequence) {
		this.soundSequence = soundSequence;
		playSoundTasks = new ArrayList<DelayedTask>();
		playing = false;
		looping = false;
		globalStartTime = 0.0;
		sequenceTime = 0.0;
	}

	public boolean getPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public boolean getLooping() {
		return looping;
	}

	public void setLooping(boolean looping) {
		this.looping = looping;
	}

	/**
	 * 
	 * @return the amount of time in seconds through the sequence that has played
	 */
	public double getSequenceTime() {
		return sequenceTime + (playing ? GClock.getTime() - globalStartTime : 0);
	}

	public void play(Location source) {
		playImpl(null, source);
	}

	public void play(Player player) {
		playImpl(player, null);
	}

	public void play(Player player, Location source) {
		playImpl(player, source);
	}

	private void playImpl(Player player, Location source) {
		if (playing) {
			stop();
		}
		playing = true;
		globalStartTime = GClock.getTime();
		ArrayList<SoundSequenceElement> elements = soundSequence.getElements();
		for (int i = 0; i < elements.size(); i++) {
			SoundSequenceElement element = elements.get(i);
			double noiseTime = element.getTime();
			if (noiseTime > sequenceTime) {
				Noise noise = element.getNoise();
				DelayedTask playNoise = new DelayedTask(noiseTime - sequenceTime) {

					@Override
					protected void run() {
						if (player == null) {
							noise.play(source);
						} else {
							if (source == null) {
								noise.play(player);
							} else {
								noise.play(player, source);
							}
						}
					}

				};
				playSoundTasks.add(playNoise);
				playNoise.start();
			}
		}
		double duration = soundSequence.getDuration();
		DelayedTask finish = new DelayedTask(duration - sequenceTime) {

			@Override
			public void run() {
				stop();
				if (looping) {
					play(source);
				}
			}

		};
		playSoundTasks.add(finish);
		finish.start();
	}

	public void pause() {
		if (!playing) {
			throw new IllegalStateException("Cannot pause a sequence that is not playing");
		} else {
			playing = false;
			sequenceTime += GClock.getTime() - globalStartTime;
			for (int i = 0; i < playSoundTasks.size(); i++) {
				DelayedTask playNoise = playSoundTasks.get(i);
				if (playNoise.getActive()) {
					double taskExeTime = playNoise.getExeTime();
					if (taskExeTime >= GClock.getTime()) {
						playNoise.stop();
						for (int j = i + 1; j < playSoundTasks.size(); j++) {
							DelayedTask followingTask = playSoundTasks.get(j);
							followingTask.stop();
						}
						break;
					}
				}
			}
			playSoundTasks.clear();
		}
	}

	public void stop() {
		if (playing) {
			playing = false;
			sequenceTime = 0.0;
			for (int i = 0; i < playSoundTasks.size(); i++) {
				DelayedTask playNoise = playSoundTasks.get(i);
				if (playNoise.getActive()) {
					double taskExeTime = playNoise.getExeTime();
					if (taskExeTime > GClock.getTime()) {
						playNoise.stop();
						for (int j = i + 1; j < playSoundTasks.size(); j++) {
							DelayedTask followingTask = playSoundTasks.get(j);
							followingTask.stop();
						}
						break;
					}
				}
			}
			playSoundTasks.clear();
		} else {
			throw new IllegalStateException("Cannot stop a sequence that is not playing");
		}
	}
}