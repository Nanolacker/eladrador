package com.eladrador.common.item;

import java.io.Serializable;

import com.eladrador.common.item.types.GItemStack;

/**
 * Superclass for all GItemStack-storing objects.
 * 
 */
public class ItemContainer implements Serializable {

	private static final long serialVersionUID = -1702378942447479050L;
	
	private GItemStack[] items;

	public ItemContainer(int capacity) {
		items = new GItemStack[capacity];
	}

	public int getSize() {
		return items.length;
	}

	public void setItem(GItemStack itemStack, int index) {
		items[index] = itemStack;
		ItemAddress itemAddress = itemStack.getAddress();
		itemAddress.setContainer(this);
		itemAddress.setIndex(index);
	}

	public GItemStack getItem(int index) {
		return items[index];
	}

	public int getAmountOf(Class<? extends GItemStack> itemClass) {
		int amount = 0;
		for (int i = 0; i < items.length; i++) {
			GItemStack itemStack = items[i];
			if (itemStack != null) {
				Class<? extends GItemStack> clazz = itemStack.getClass();
				if (itemClass == clazz) {
					amount++;
				}
			}
		}
		return amount;
	}

}
