package com.eladrador.common.item;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.eladrador.common.Debug;
import com.eladrador.common.player.PlayerCharacter;
import com.eladrador.common.ui.AbstractMenu;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonAddress;
import com.eladrador.common.ui.ButtonToggleEvent;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.ui.UIProfile;

public class GameItemButton extends Button {

	private GameItem item;

	public GameItemButton(GameItem item) {
		super(item.displayName(), item.description(), item.getMaterial());
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
		ButtonToggleType toggleType = toggleEvent.getToggleType();
		ButtonAddress address = toggleEvent.getAddress();
		if (toggleType == ButtonToggleType.LEFT_CLICK_IN_MENU || toggleType == ButtonToggleType.RIGHT_CLICK_IN_MENU) {
			AbstractMenu menu = address.getMenu();
			int index = address.getIndex();
			menu.removeButton(index);
			profile.setButtonOnCursor(GameItemButton.this);
		} else if (toggleType == ButtonToggleType.LEFT_CLICK_ON_CURSOR
				|| toggleType == ButtonToggleType.RIGHT_CLICK_ON_CURSOR) {
			boolean discard = address == null;
			if (discard) {
				profile.setButtonOnCursor(null);
				DiscardButtonConfirmMenu confirm = new DiscardButtonConfirmMenu(this, false,
						profile.getOpenUpperMenu());
				profile.openMenu(confirm);
			} else {
				AbstractMenu menu = address.getMenu();
				int index = address.getIndex();

				Button clickedButton = menu.getButton(index);
				if (clickedButton instanceof GameItemButton) {
					GameItem clickedItem = ((GameItemButton) clickedButton).getItem();
					if (item.getClass() == clickedItem.getClass()) {
						Debug.log("Stack em");
						int newCursorItemSize = 0;
						int newClickedItemSize = item.getCurrentSize() + clickedItem.getCurrentSize();
						clickedItem.setCurrentSize(newClickedItemSize);
						if (newCursorItemSize == 0) {

						} else {
							item.setCurrentSize(newCursorItemSize);
						}
					} else {
						Debug.log("Swap em");
						profile.setButtonOnCursor(clickedButton);
						menu.setButton(index, this);
					}
				} else {
					profile.setButtonOnCursor(null);
					menu.setButton(index, this);
				}
			}
		}
	}

}
