package com.eladrador.common.item;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.eladrador.common.ui.Button;
import com.eladrador.common.ui.ButtonToggleEvent;
import com.eladrador.common.ui.UIProfile;
import com.eladrador.common.ui.UpperMenu;
import com.eladrador.common.utils.StrUtils;

/**
 * A menu to be opened when a player closes all other menus and a button is
 * present on their cursor. It confirms that the user wishes to proceed in
 * closing their menus and destroy the button on their cursor.
 */
public class DiscardButtonConfirmMenu extends UpperMenu {

	public DiscardButtonConfirmMenu(Button toDiscard, boolean closeMenusAfterDiscard, UpperMenu upperMenu) {
		super(ChatColor.BLACK + "Discard " + ChatColor.RESET + toDiscard.getDisplayName() + ChatColor.RESET + "?", 9);

		ArrayList<String> yesDesc = StrUtils.lineToParagraph(
				ChatColor.GREEN + "Discard " + ChatColor.RESET + toDiscard.getDisplayName(),
				StrUtils.IDEAL_CHARACTERS_PER_LINE);
		Button yes = new Button(ChatColor.GREEN + "Yes", yesDesc, Material.GREEN_WOOL) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
				UIProfile prof = UIProfile.forPlayer(player);
				prof.setButtonOnCursor(null);
				if (closeMenusAfterDiscard) {
					prof.closeMenus();
				} else {
					prof.openMenu(upperMenu);
				}
			}

		};
		setButton(2, yes);

		ArrayList<String> noDesc = StrUtils.lineToParagraph(
				ChatColor.RED + "Do not discard " + ChatColor.RESET + toDiscard.getDisplayName(),
				StrUtils.IDEAL_CHARACTERS_PER_LINE);
		Button no = new Button(ChatColor.RED + "No", noDesc, Material.RED_WOOL) {

			@Override
			protected void onToggle(ButtonToggleEvent toggleEvent) {
				Player player = toggleEvent.getPlayer();
				UIProfile profile = UIProfile.forPlayer(player);
				profile.setButtonOnCursor(toDiscard);
				player.sendMessage("not deleted");
			}

		};
		setButton(6, no);
	}

}
