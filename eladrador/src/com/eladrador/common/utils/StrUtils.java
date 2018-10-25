package com.eladrador.common.utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;

public final class StrUtils {

	/**
	 * The ideal amount of characters per line to display in the lore of an ItemStack.
	 */
	public static final int LORE_CHARS_PER_LINE = 18;

	private StrUtils() {
		// not to be instantiated
	}
	
	public static ArrayList<String> stringToParagraph(String str, int maxCharsPerLine) {
		ArrayList<String> parag = new ArrayList<String>();
		String prevChatColor = ChatColor.RESET.toString();
		boolean complete = false;
		while (!complete) {
			int currentLineTotalCharCount = 0;
			int currentLineVisibleCharCount = 0;
			for (int i = 0; i < str.length() && currentLineVisibleCharCount < maxCharsPerLine; i++) {
				if (str.charAt(i) == '§') {
					currentLineTotalCharCount += 2;
					i++; // skips over next ChatColor associated char
				} else {
					currentLineTotalCharCount += 1;
					currentLineVisibleCharCount += 1;
				}
			}
			String currentLine = str.substring(0, currentLineTotalCharCount);
			boolean lastLine = str.length() == currentLineTotalCharCount;
			if (lastLine) {
				complete = true;
			} else {
				boolean severedWord = str.charAt(currentLineTotalCharCount) != ' '; // if a word was split when the String was cut
				if (severedWord) {
					int lastSpaceIndex = currentLine.lastIndexOf(' ');
					if (lastSpaceIndex != -1) {
						currentLine = currentLine.substring(0, lastSpaceIndex);
						prevChatColor = ChatColor.getLastColors(currentLine);
						str = prevChatColor + str.substring(lastSpaceIndex + 1); // +1 for ' '
					} else {
						prevChatColor = ChatColor.getLastColors(currentLine);
						str = prevChatColor + str.substring(currentLineTotalCharCount);
					}
				} else {
					prevChatColor = ChatColor.getLastColors(currentLine);
					str = prevChatColor + str.substring(currentLineTotalCharCount + 1); // +1 for following ' '
				}
			}
			parag.add(currentLine);
		}
		return parag;
	}

}
