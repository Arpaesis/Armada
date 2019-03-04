package com.ccl.utils;

import com.ccl.args.Argument;

public class MathUtils
{
	public static String clampi(int value, Argument a)
	{
		int temp = value;
		
		if (value < a.getMin())
			temp = a.getMin();

		else if (value > a.getMax())
			temp = a.getMax();
		
		return Integer.toString(temp);
	}
	
	public static String clampf(float value, Argument a)
	{
		float temp = value;
		
		if (value < a.getMin())
			temp = a.getMin();

		else if (value > a.getMax())
			temp = a.getMax();
		
		return Float.toString(temp);
	}
	
	public static String clampd(double value, Argument a)
	{
		double temp = value;
		
		if (value < a.getMin())
			temp = a.getMin();

		else if (value > a.getMax())
			temp = a.getMax();
		
		return Double.toString(temp);
	}
	
	public static String clampl(long value, Argument a)
	{
		long temp = value;
		
		if (value < a.getMin())
			temp = a.getMin();

		else if (value > a.getMax())
			temp = a.getMax();
		
		return Long.toString(temp);
	}
	
	public static String clampb(byte value, Argument a)
	{
		byte temp = value;
		
		if (value < a.getMin())
			temp = (byte) a.getMin();

		else if (value > a.getMax())
			temp = (byte) a.getMax();
		
		return Byte.toString(temp);
	}
	
	public static String clamps(short value, Argument a)
	{
		short temp = value;
		
		if (value < a.getMin())
			temp = (short) a.getMin();

		else if (value > a.getMax())
			temp = (short) a.getMax();
		
		return Short.toString(temp);
	}
	
	public static int getPercentageValue(float percentage, int min, int max)
	{
		return (int) ((max - min) * percentage + min);
	}
}
