package com.eladrador.common.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.eladrador.common.Debug;
import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonToggleEvent;
import com.eladrador.common.ui.ButtonToggleType;
import com.eladrador.common.ui.UIProfile;
import com.eladrador.common.ui.UpperMenu;
import com.eladrador.common.utils.StrUtils;

/**
 * A menu to be opened when a player closes all other menus and a button is
 * present on their cursor. It confirms that the user wishes to proceed in
 * closing their menus and destroy the button on their cursor.
 */
public class DiscardGameItemStackConfirmMenu extends UpperMenu {

	private static final Button PLACEHOLDER_BUTTON = new Button("", null, Material.GLASS_PANE) {
		@Override
		protected void onToggle(ButtonToggleEvent toggleEvent) {
		}
	};

	private int howMuchToDiscard;

	public DiscardGameItemStackConfirmMenu(GameItemStackButton toDiscard, UpperMenu upperMenu) {
		super(ChatColor.BLACK + "Discard " + ChatColor.RESET + toDiscard.getDisplayName() + ChatColor.RESET + "?", 9);

		howMuchToDiscard = toDiscard.getItemStack().getSize();

		ArrayList<String> confirmDesc = StrUtils
				.lineToParagraph(ChatColor.GREEN + "Discard " + ChatColor.RESET + toDiscard.getDisplayName());
		Button confirmButton = new Button(ChatColor.GREEN + "Confirm", confirmDesc, Material.GREEN_WOOL) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
				UIProfile profile = UIProfile.forPlayer(player);
				profile.openMenu(upperMenu);
				GameItemStack itemStack = toDiscard.getItemStack();
				itemStack.setSize(itemStack.getSize() - howMuchToDiscard);
				profile.setButtonOnCursor(itemStack.getButton());
				Debug.log(itemStack.getSize());
			}

		};

		ArrayList<String> cancelDesc = StrUtils
				.lineToParagraph(ChatColor.RED + "Do not discard " + ChatColor.RESET + toDiscard.getDisplayName());
		Button cancelButton = new Button(ChatColor.RED + "Cancel", cancelDesc, Material.RED_WOOL) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
				UIProfile profile = UIProfile.forPlayer(player);
				profile.openMenu(upperMenu);
				profile.setButtonOnCursor(toDiscard);
			}

		};

		ArrayList<String> counterDesc = StrUtils.lineToParagraph(ChatColor.GREEN + "Left click: " + ChatColor.GREEN
				+ "↑\n" + ChatColor.GREEN + "Right click: " + ChatColor.YELLOW + "↓");
		Button counterButton = new Button(ChatColor.AQUA + "Discard how much?", counterDesc,
				toDiscard.getItemStack().getItem().getMaterial()) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				ButtonToggleType toggleType = toggleEvent.getToggleType();
				if (toggleType == ButtonToggleType.LEFT_CLICK_IN_MENU) {
					if (howMuchToDiscard < toDiscard.getItemStack().getSize()) {
						howMuchToDiscard++;
						setImageSize(howMuchToDiscard);
					}
				} else if (toggleType == ButtonToggleType.RIGHT_CLICK_IN_MENU) {
					if (howMuchToDiscard > 1) {
						howMuchToDiscard--;
						setImageSize(howMuchToDiscard);
					}
				}
			}
		};
		counterButton.setImageSize(howMuchToDiscard);

		setButton(0, PLACEHOLDER_BUTTON);
		setButton(1, confirmButton);
		setButton(2, PLACEHOLDER_BUTTON);
		setButton(3, PLACEHOLDER_BUTTON);
		setButton(4, counterButton);
		setButton(5, PLACEHOLDER_BUTTON);
		setButton(6, PLACEHOLDER_BUTTON);
		setButton(7, cancelButton);
		setButton(8, PLACEHOLDER_BUTTON);

	}

}
