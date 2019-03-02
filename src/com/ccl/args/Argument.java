package com.ccl.args;

import com.ccl.enumerations.ParamType;

public class Argument
{

	private final ParamType type;
	
	public Argument(ParamType type)
	{
		this.type = type;
	}

	public ParamType getType()
	{
		return type;
	}
}
