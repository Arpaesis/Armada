package com.arpaesis.armada.utils;

import java.util.Optional;

/**
 * A class with some helper methods to manipulate Strings.
 * 
 * @author Arpaesis
 *
 */
public class StringUtils
{

	/**
	 * Removes the last character of the given String.
	 * 
	 * @param s
	 *            The String to remove the last character of.
	 * @return The String without the last character.
	 */
	public static String removeLastCharOptional(String s)
	{
		return Optional.ofNullable(s).filter(str -> str.length() != 0).map(str -> str.substring(0, str.length() - 1)).orElse(s);
	}
}
