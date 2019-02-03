package com.eladrador.common.event;

import java.util.ArrayList;

public class Event {

	private ArrayList<Runnable> listeners;

	public Event() {
		listeners = new ArrayList<Runnable>();
	}

	/**
	 * Adds a new listener to this event.
	 */
	public void addListener(Runnable listener) {
		listeners.add(listener);
	}

	/**
	 * Invokes the event, calling all listeners.
	 */
	public void invoke() {
		for (Runnable listener : listeners) {
			listener.run();
		}
	}

}
