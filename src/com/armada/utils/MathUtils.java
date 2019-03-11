package com.armada.utils;

import com.armada.args.Argument;

/**
 * A helper class for some math.
 * 
 * @author Arpaesis
 *
 */
public class MathUtils {

    /**
     * Clamps a given argument's value between its range values.
     * 
     * @param value The value given.
     * @param a     The argument to be used.
     * @return The clamped value as a String.
     */
    public static String clampi(int value, Argument a) {
	int temp = value;

	if (value < a.getMin())
	    temp = a.getMin();

	else if (value > a.getMax())
	    temp = a.getMax();

	return Integer.toString(temp);
    }

    /**
     * Clamps a given argument's value between its range values.
     * 
     * @param value The value given.
     * @param a     The argument to be used.
     * @return The clamped value as a String.
     */
    public static String clampf(float value, Argument a) {
	float temp = value;

	if (value < a.getMin())
	    temp = a.getMin();

	else if (value > a.getMax())
	    temp = a.getMax();

	return Float.toString(temp);
    }

    /**
     * Clamps a given argument's value between its range values.
     * 
     * @param value The value given.
     * @param a     The argument to be used.
     * @return The clamped value as a String.
     */
    public static String clampd(double value, Argument a) {
	double temp = value;

	if (value < a.getMin())
	    temp = a.getMin();

	else if (value > a.getMax())
	    temp = a.getMax();

	return Double.toString(temp);
    }

    /**
     * Clamps a given argument's value between its range values.
     * 
     * @param value The value given.
     * @param a     The argument to be used.
     * @return The clamped value as a String.
     */
    public static String clampl(long value, Argument a) {
	long temp = value;

	if (value < a.getMin())
	    temp = a.getMin();

	else if (value > a.getMax())
	    temp = a.getMax();

	return Long.toString(temp);
    }

    /**
     * Clamps a given argument's value between its range values.
     * 
     * @param value The value given.
     * @param a     The argument to be used.
     * @return The clamped value as a String.
     */
    public static String clampb(byte value, Argument a) {
	byte temp = value;

	if (value < a.getMin())
	    temp = (byte) a.getMin();

	else if (value > a.getMax())
	    temp = (byte) a.getMax();

	return Byte.toString(temp);
    }

    /**
     * Clamps a given argument's value between its range values.
     * 
     * @param value The value given.
     * @param a     The argument to be used.
     * @return The clamped value as a String.
     */
    public static String clamps(short value, Argument a) {
	short temp = value;

	if (value < a.getMin())
	    temp = (short) a.getMin();

	else if (value > a.getMax())
	    temp = (short) a.getMax();

	return Short.toString(temp);
    }

    /**
     * Gets the percentage value of a range.
     * 
     * @param percentage The percentage value out of 1.
     * @param min        The minimum value of the range.
     * @param max        The maximum value of the range.
     * @return The calculated percentage value.
     */
    public static int getPercentageValue(double percentage, int min, int max) {
	return (int) ((max - min) * percentage + min);
    }
}
