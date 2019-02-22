package com.eladrador.common.item;

import com.eladrador.common.ui.ButtonAddress;
import com.eladrador.common.ui.ButtonContainer;

public class GameItemStack {

	private GameItem item;
	private int size;
	private GameItemStackButton button;

	/**
	 * The size of this item stack will be initialized as 1.
	 */
	public GameItemStack(GameItem item) {
		this(item, 1);
	}

	public GameItemStack(GameItem item, int amount) {
		this.item = item;
		this.size = amount;
		button = new GameItemStackButton(this);
		item.registerItemStack(this);
	}

	public GameItem getItem() {
		return item;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		validateSize(size);
		if (size == 0) {
			delete();
		} else {
			this.size = size;
			updateButton();
		}
	}

	public GameItemStackButton getButton() {
		return button;
	}

	/**
	 * Splits this item, returning a new item that was split from the original. The
	 * current size of this item is also reduced by the specified amount.
	 * 
	 * @param amountToTakeOff the size of the returned item
	 * @return the new item
	 */
	public GameItemStack split(int amountToTakeOff) {
		setSize(size - amountToTakeOff);
		return new GameItemStack(item, amountToTakeOff);
	}

	/**
	 * Stacks this item on the specified item.
	 * 
	 * @param onto the item that this item will be stacked on.
	 */
	public void stack(GameItemStack onto) {
		int howMuch;
		if (size + onto.size <= item.getMaxStackSize()) {
			howMuch = size;
		} else {
			howMuch = item.getMaxStackSize() - onto.size;
		}
		stack(onto, howMuch);
	}

	public void stack(GameItemStack onto, int howMuch) {
		if (!isStackableWith(onto)) {
			throw new IllegalArgumentException("Item is not stackable");
		}
		int ontoCurrentSize = onto.size;
		onto.setSize(ontoCurrentSize + howMuch);
		this.setSize(size - howMuch);
	}

	void updateButton() {
		button.setDisplayName(item.displayName());
		button.setDescription(item.description());
		button.setImageMaterial(item.getMaterial());
		button.setImageSize(size);
	}

	private void delete() {
		ButtonAddress[] addresses = button.getAddresses();
		for (ButtonAddress address : addresses) {
			ButtonContainer container = address.getContainer();
			int index = address.getIndex();
			container.setButton(index, null);
		}
		button = null;
	}

	public boolean isStackableWith(GameItemStack other) {
		return this.item == other.item;
	}

	private void validateSize(int currentSize) {
		int maxSize = item.getMaxStackSize();
		if (currentSize > maxSize) {
			throw new IllegalArgumentException(
					"Specified size (" + currentSize + ") exceeds max size (" + maxSize + ")");
		}
	}

}
