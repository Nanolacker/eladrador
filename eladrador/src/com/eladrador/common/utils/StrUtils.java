package com.eladrador.common.utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.eladrador.common.Debug;

public final class StrUtils {

	/**
	 * The ideal amount of characters per line to display in the {@code lore} of an
	 * {@code ItemStack}.
	 */
	public static final int LORE_CHARS_PER_LINE = 18;

	/**
	 * Hides constructor.
	 */
	private StrUtils() {
		// not to be instantiated
	}

	/**
	 * Converts a String into a paragraph by splitting it into multiple lines.
	 * Anytime 'n' is present in the passed in String, a new line will be started in
	 * this paragraph.
	 * 
	 * @param str             the String to be converted
	 * @param maxCharsPerLine the maximum number of visible characters to be present
	 *                        in a line of this paragraph before a new line is
	 *                        started
	 */
	public static ArrayList<String> stringToParagraph(String str, int maxCharsPerLine) {
		ArrayList<String> parag = new ArrayList<String>();
		String prevChatColor;
		boolean complete = false;
		while (!complete) {
			int currentLineTotalCharCount = 0;
			int currentLineVisibleCharCount = 0; // number of chars excluding ChatColor related chars
			for (int i = 0; i < str.length() && currentLineVisibleCharCount < maxCharsPerLine; i++) {
				char c = str.charAt(i);
				if (c == '§') {
					currentLineTotalCharCount += 2;
					i++; // skips over next ChatColor associated char
				} else if (c == '\n') {
					currentLineTotalCharCount += 1;
					break;
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
				// if a word was split when the String was cut
				char lastChar = str.charAt(currentLineTotalCharCount - 1);
				boolean severedWord = lastChar != ' ' && lastChar != '\n';
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
					// +1 for following' '
					str = prevChatColor + str.substring(currentLineTotalCharCount + (lastChar == ' ' ? 1 : 0));
				}
			}
			parag.add(currentLine);
		}
		return parag;
	}

}
