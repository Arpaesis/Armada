package com.ccl.args;

import com.ccl.enumerations.ParamType;

public class Argument
{

	private final ParamType type;
	private int min;
	private int max;
	
	public Argument(ParamType type)
	{
		this.type = type;
	}

	public ParamType getType()
	{
		return type;
	}
	
	public Argument setClamp(int min, int max)
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
}
