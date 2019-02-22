package com.eladrador.common.item;

import org.bukkit.entity.Player;

import com.eladrador.common.Debug;
import com.eladrador.common.GPlugin;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonAddress;
import com.eladrador.common.ui.ButtonContainer;
import com.eladrador.common.ui.ButtonToggleEvent;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.ui.UIProfile;

public final class GameItemStackButton extends Button {

	private GameItemStack itemStack;

	GameItemStackButton(GameItemStack itemStack) {
		super(itemStack.getItem().displayName(), itemStack.getItem().description(), itemStack.getItem().getMaterial());
		setImageSize(itemStack.getSize());
		this.itemStack = itemStack;
	}

	public GameItemStack getItemStack() {
		return itemStack;
	}

	@Override
	protected void onToggle(ButtonToggleEvent toggleEvent) {
		Player player = toggleEvent.getPlayer();
		UIProfile profile = UIProfile.forPlayer(player);
		ButtonAddress address = toggleEvent.getAddress();
		ButtonToggleType toggleType = toggleEvent.getToggleType();
		switch (toggleType) {
		case LEFT_CLICK_IN_HAND:
			onLeftClickInHand();
			break;
		case LEFT_CLICK_IN_MENU:
			onLeftClickInMenu(profile, address);
			break;
		case LEFT_CLICK_ON_CURSOR:
			onLeftClickOnCursor(profile, address);
			break;
		case RIGHT_CLICK_IN_HAND:
			onRightClickInHand();
			break;
		case RIGHT_CLICK_IN_MENU:
			onRightClickInMenu(profile, address);
			break;
		case RIGHT_CLICK_ON_CURSOR:
			onRightClickOnCursor(profile, address);
			break;
		case SHIFT_LEFT_CLICK_IN_MENU:
			onSpeicalUse();
			break;
		case SHIFT_RIGHT_CLICK_IN_MENU:
			onSpeicalUse();
			break;
		case TAP_Q_IN_MAIN_HAND:
			onSpeicalUse();
			break;
		default:
			break;

		}
	}

	private void onLeftClickInHand() {
		itemStack.getItem().getOnLeftClickInHand().invoke();
	}

	private void onRightClickInHand() {
		itemStack.getItem().getOnRightClickInHand().invoke();
	}

	private void onLeftClickInMenu(UIProfile profile, ButtonAddress address) {
		ButtonContainer container = address.getContainer();
		int index = address.getIndex();
		container.setButton(index, null);
		profile.setButtonOnCursor(GameItemStackButton.this);
	}

	private void onRightClickInMenu(UIProfile profile, ButtonAddress address) {
		ButtonContainer container = address.getContainer();
		int index = address.getIndex();
		int size = itemStack.getSize();
		if (size == 1) {
			container.setButton(index, null);
			profile.setButtonOnCursor(this);
		} else {
			int amountToTakeOff = (int) Math.ceil(itemStack.getSize() / 2.0);
			GameItemStack itemStackOnCursor = itemStack.split(amountToTakeOff);
			profile.setButtonOnCursor(itemStackOnCursor.getButton());
		}
	}

	private void onLeftClickOnCursor(UIProfile profile, ButtonAddress address) {
		Debug.log(address.getContainer() == null ? "WOOT BOI" : address.getContainer().getSize());
		if (address.getIndex() == -1) {
			return;
		}
		boolean discard = address.getIndex() == -999;
		if (discard) {
			profile.setButtonOnCursor(null);
			DiscardGameItemStackConfirmMenu discardConfirmMenu = new DiscardGameItemStackConfirmMenu(this,
					profile.getOpenUpperMenu());
			profile.openMenu(discardConfirmMenu);
		} else {
			ButtonContainer container = address.getContainer();
			int index = address.getIndex();
			boolean canBePlacedDown = itemStack.getItem().canBePlacedDown(index);
			if (!canBePlacedDown) {
				return;
			}
			Button clickedButton = container.getButton(index);
			if (clickedButton instanceof GameItemStackButton) {
				GameItemStack clickedItemStack = ((GameItemStackButton) clickedButton).getItemStack();
				if (itemStack.isStackableWith(clickedItemStack)) {
					// stack
					itemStack.stack(clickedItemStack);
				} else {
					// swap
					profile.setButtonOnCursor(clickedButton);
					container.setButton(index, this);
				}
			} else if (clickedButton == null) {
				profile.setButtonOnCursor(null);
				container.setButton(index, this);
			}
		}
	}

	private void onRightClickOnCursor(UIProfile profile, ButtonAddress address) {
		ButtonContainer container = address.getContainer();
		int index = address.getIndex();
		boolean canBePlacedDown = itemStack.getItem().canBePlacedDown(index);
		if (!canBePlacedDown) {
			return;
		}

		Button clickedButton = container.getButton(index);
		if (clickedButton == null) {
			GameItemStack split = itemStack.split(1);
			container.setButton(index, split.getButton());
		} else {
			if (clickedButton instanceof GameItemStackButton) {
				GameItemStack clickedItemStack = ((GameItemStackButton) clickedButton).getItemStack();
				if (clickedItemStack.isStackableWith(this.itemStack)) {
					// stack
					this.itemStack.stack(clickedItemStack, 1);
				} else {
					// swap
					profile.setButtonOnCursor(clickedButton);
					container.setButton(index, this);
				}
			}
		}
	}

	private void onSpeicalUse() {
		itemStack.getItem().getOnSpecialUse().invoke();
	}

}
