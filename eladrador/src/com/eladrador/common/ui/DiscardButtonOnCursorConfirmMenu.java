package com.eladrador.common.ui;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.eladrador.common.utils.StrUtils;

/**
 * A menu to be opened when a player closes all other menus and a button is
 * present on their cursor. It confirms that the user wishes to proceed in
 * closing their menus and destroy the button on their cursor.
 */
public class DiscardButtonOnCursorConfirmMenu extends UpperMenu {

	public DiscardButtonOnCursorConfirmMenu(Button toDiscard) {
		super(ChatColor.BLACK + "Discard " + ChatColor.RESET + toDiscard.getDisplayName() + ChatColor.RESET + "?",
				UpperMenuSize.NINE);

		ArrayList<String> yesDesc = StrUtils.lineToParagraph(
				ChatColor.GREEN + "Discard " + ChatColor.RESET + toDiscard.getDisplayName(),
				StrUtils.IDEAL_CHARACTERS_PER_LINE);
		Button yes = new Button(ChatColor.GREEN + "Yes", yesDesc, Material.GREEN_WOOL) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				UIProfile prof = UIProfile.byPlayer(player);
				prof.setButtonOnCursor(null);
				prof.closeMenus();
			}

		};
		addButton(yes, 2);

		ArrayList<String> noDesc = StrUtils.lineToParagraph(
				ChatColor.RED + "Do not discard " + ChatColor.RESET + toDiscard.getDisplayName(),
				StrUtils.IDEAL_CHARACTERS_PER_LINE);
		Button no = new Button(ChatColor.RED + "No", noDesc, Material.RED_WOOL) {

			@Override
			protected void onToggle(Player player, ButtonToggleType toggleType, ButtonAddress addressClicked) {
				UIProfile profile = UIProfile.byPlayer(player);
				profile.setButtonOnCursor(toDiscard);
				player.sendMessage("not deleted");
			}

		};
		addButton(no, 6);
	}

}
