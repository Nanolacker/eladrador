package com.eladrador.common.item;

import org.bukkit.entity.Player;

import com.eladrador.common.Debug;
import com.eladrador.common.ui.AbstractMenu;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonAddress;
import com.eladrador.common.ui.ButtonContainer;
import com.eladrador.common.ui.ButtonToggleEvent;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.ui.UIProfile;

public class GameItemButton extends Button {

	private GameItem item;

	public GameItemButton(GameItem item) {
		super(item.displayName(), item.description(), item.getMaterial());
		setImageSize(item.getCurrentSize());
		this.item = item;
		item.registerButton(this);
	}

	public GameItem getItem() {
		return item;
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
		case TAP_Q_IN_HAND:
			onTapQInHand();
			break;
		default:
			break;
		}
	}

	private void onLeftClickInHand() {
		item.getOnLeftClickInHand().invoke();
	}

	private void onRightClickInHand() {
		item.getOnRightClickInHand().invoke();
	}

	private void onLeftClickInMenu(UIProfile profile, ButtonAddress address) {
		ButtonContainer container = address.getContainer();
		int index = address.getIndex();
		container.setButton(index, null);
		profile.setButtonOnCursor(GameItemButton.this);
	}

	private void onRightClickInMenu(UIProfile profile, ButtonAddress address) {
		ButtonContainer container = address.getContainer();
		int index = address.getIndex();
		int size = item.getCurrentSize();
		if (size == 1) {
			container.setButton(index, null);
			profile.setButtonOnCursor(this);
		} else {
			int amountToTakeOff = (int) Math.ceil(item.getCurrentSize() / 2.0);
			GameItem itemOnCursor = item.split(amountToTakeOff);
			profile.setButtonOnCursor(new GameItemButton(itemOnCursor));
		}
	}

	private void onLeftClickOnCursor(UIProfile profile, ButtonAddress address) {
		boolean discard = address == null;
		if (discard) {
			profile.setButtonOnCursor(null);
			DiscardButtonConfirmMenu confirm = new DiscardButtonConfirmMenu(this, false, profile.getOpenUpperMenu());
			profile.openMenu(confirm);
		} else {
			ButtonContainer container = address.getContainer();
			int index = address.getIndex();

			Button clickedButton = container.getButton(index);
			if (clickedButton instanceof GameItemButton) {
				GameItem clickedItem = ((GameItemButton) clickedButton).getItem();
				if (item.isStackableWith(clickedItem)) {
					// stack
					item.stack(clickedItem);
				} else {
					// swap
					profile.setButtonOnCursor(clickedButton);
					container.setButton(index, this);
				}
			} else {
				profile.setButtonOnCursor(null);
				container.setButton(index, this);
			}
		}
	}

	private void onRightClickOnCursor(UIProfile profile, ButtonAddress address) {
		ButtonContainer container = address.getContainer();
		int index = address.getIndex();

		Button clickedButton = container.getButton(index);
		if (clickedButton == null) {
			container.setButton(index, new GameItemButton(item.split(1)));
		} else {
			if (clickedButton instanceof GameItemButton) {
				GameItem clickedItem = ((GameItemButton) clickedButton).getItem();
				if (clickedItem.isStackableWith(this.item)) {
					// stack
					this.item.stack(clickedItem, 1);
				} else {
					// swap
					profile.setButtonOnCursor(clickedButton);
					container.setButton(index, this);
				}
			}
		}
	}

	private void onTapQInHand() {
		item.getOnSpecialUse().invoke();
	}

}
