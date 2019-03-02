package com.ccl.utils;

import com.ccl.args.Argument;

public class MathUtils
{
	public static String clamp(int value, Argument a)
	{
		int temp = value;
		
		if (value < a.getMin())
			temp = a.getMin();

		else if (value > a.getMax())
			temp = a.getMax();
		
		return Integer.toString(temp);
	}
}
