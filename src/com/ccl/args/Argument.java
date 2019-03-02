package com.ccl.args;

import com.ccl.enumerations.ParamType;

public class Argument
{

	private final String argName;
	private final ParamType type;
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;
	
	public Argument(String argName, ParamType type)
	{
		this.argName = argName;
		this.type = type;
	}

	public ParamType getType()
	{
		return type;
	}
	
	public Argument setRange(int min, int max)
	{
		this.min = min;
		this.max = max;
		return this;
	}

	public int getMin()
	{
		return min;
	}

	public int getMax()
	{
		return max;
	}

	public String getArgName()
	{
		return argName;
	}
}
