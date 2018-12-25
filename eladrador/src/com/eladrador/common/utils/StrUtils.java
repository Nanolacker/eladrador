package com.eladrador.common.utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import com.eladrador.common.Debug;

public final class StrUtils {

	/**
	 * The ideal amount of characters per line to display in the {@code lore} of an
	 * {@code ItemStack}.
	 */
	public static final int IDEAL_CHARACTERS_PER_LINE = 18;

	/**
	 * Hides constructor.
	 */
	private StrUtils() {
		// not to be instantiated
	}

	/**
	 * Converts a line {@code String} into a paragraph by splitting it into multiple
	 * lines. Any time '\n' is present in the passed in String, a new line will be
	 * started in this paragraph. Each line in the paragraph can be a maximum of
	 * {@link StrUtils#IDEAL_CHARACTERS_PER_LINE} visible characters long.
	 * 
	 * @param line the line {@code String} to be converted
	 */
	public static ArrayList<String> lineToParagraph(String line) {
		return lineToParagraph(line, IDEAL_CHARACTERS_PER_LINE);
	}

	/**
	 * Converts a line {@code String} into a paragraph by splitting it into multiple
	 * lines. Any time '\n' is present in the passed in String, a new line will be
	 * started in this paragraph.
	 * 
	 * @param line            the line {@code String} to be converted
	 * @param maxCharsPerLine the maximum number of visible characters to be present
	 *                        in a line of this paragraph before a new line is
	 *                        started
	 * @throws IllegalArgumentException if maxCharsPerLine is 0
	 */
	public static ArrayList<String> lineToParagraph(String line, int maxCharsPerLine) {
		if (maxCharsPerLine == 0) {
			throw new IllegalArgumentException("maxCharsPerLine cannot be 0");
		}
		ArrayList<String> parag = new ArrayList<String>();
		String prevChatColor;
		boolean complete = false;
		while (!complete) {
			int currentLineTotalCharCount = 0;
			int currentLineVisibleCharCount = 0; // number of chars excluding ChatColor related chars
			for (int i = 0; i < line.length() && currentLineVisibleCharCount < maxCharsPerLine; i++) {
				char c = line.charAt(i);
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
			String currentLine = line.substring(0, currentLineTotalCharCount);
			if (line.length() == currentLineTotalCharCount) {
				complete = true;
			} else {
				char lastChar = line.charAt(currentLineTotalCharCount - 1);
				// if a word was split when the String was cut
				boolean severedWord = lastChar != ' ' && lastChar != '\n';
				if (severedWord) {
					int lastSpaceIndex = currentLine.lastIndexOf(' ');
					if (lastSpaceIndex != -1) {
						currentLine = currentLine.substring(0, lastSpaceIndex);
						prevChatColor = ChatColor.getLastColors(currentLine);
						line = prevChatColor + line.substring(lastSpaceIndex + 1); // +1 for ' '
					} else {
						prevChatColor = ChatColor.getLastColors(currentLine);
						line = prevChatColor + line.substring(currentLineTotalCharCount);
					}
				} else {
					prevChatColor = ChatColor.getLastColors(currentLine);
					// +1 for following' '
					line = prevChatColor + line.substring(currentLineTotalCharCount + (lastChar == ' ' ? 1 : 0));
				}
			}
			parag.add(currentLine);
		}
		return parag;
	}

}
