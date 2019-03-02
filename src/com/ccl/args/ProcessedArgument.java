package com.ccl.args;

import com.ccl.enumerations.ParamType;

public class ProcessedArgument<T> extends Argument
{

	T value;

	public ProcessedArgument(String argName, ParamType type, T value)
	{
		super(argName, type);
		this.value = value;
	}

	public T getValue()
	{
		return value;
	}
}
