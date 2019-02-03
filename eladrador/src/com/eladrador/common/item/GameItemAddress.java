package com.eladrador.common.item;

/**
 * An address is assigned to each {@link GameItemStack} to make them more
 * identifiable and locatable. Each address holds the container (e.g. a player
 * inventory or a loot chest) of the item and its index in the container.
 */
public class GameItemAddress {

	private GameItemContainer container;
	private int index;

	public GameItemAddress() {
		setContainer(null);
		setIndex(0);
	}

	public GameItemAddress(GameItemContainer container, int index) {
		setContainer(container);
		setIndex(index);
	}

	public GameItemContainer getContainer() {
		return container;
	}

	public void setContainer(GameItemContainer container) {
		this.container = container;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
