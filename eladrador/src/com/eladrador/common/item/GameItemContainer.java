package com.eladrador.common.item;

import java.io.Serializable;

public class GameItemContainer implements Serializable {

	private static final long serialVersionUID = -1702378942447479050L;
	
	private GameItem[] items;

	public GameItemContainer(int capacity) {
		items = new GameItem[capacity];
	}

	public int getSize() {
		return items.length;
	}

	public void setItem(GameItem itemStack, int index) {
		items[index] = itemStack;
		GameItemAddress itemAddress = itemStack.getAddress();
		itemAddress.setContainer(this);
		itemAddress.setIndex(index);
	}

	public GameItem getItem(int index) {
		return items[index];
	}

	public int amountOf(Class<? extends GameItem> itemClass) {
		int amount = 0;
		for (int i = 0; i < items.length; i++) {
			GameItem itemStack = items[i];
			if (itemStack != null) {
				Class<? extends GameItem> clazz = itemStack.getClass();
				if (itemClass == clazz) {
					amount++;
				}
			}
		}
		return amount;
	}

}
