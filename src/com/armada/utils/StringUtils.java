package com.armada.utils;

import java.util.Optional;

public class StringUtils {

    public static String removeLastCharOptional(String s) {
	return Optional.ofNullable(s).filter(str -> str.length() != 0).map(str -> str.substring(0, str.length() - 1))
		.orElse(s);
    }
}
