package com.eladrador.common.item;

/**
 * An address is assigned to each GItemStack to make them more identifiable and
 * locatable. Each address holds the container (e.g. a player inventory or a
 * loot chest) that the item is held in in addition to their index in said
 * container.
 */
public class ItemAddress {

	private ItemContainer container;
	private int index;

	public ItemAddress() {
		setContainer(null);
		setIndex(0);
	}

	public ItemAddress(ItemContainer container, int index) {
		setContainer(container);
		setIndex(index);
	}

	public ItemContainer getContainer() {
		return container;
	}

	public void setContainer(ItemContainer container) {
		this.container = container;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
