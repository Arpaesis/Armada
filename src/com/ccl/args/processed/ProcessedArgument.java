package com.ccl.args.processed;

import com.ccl.args.Argument;
import com.ccl.enumerations.ParamType;

public class ProcessedArgument<T> extends Argument
{

	private final T value;
	private final String rawValue;

	public ProcessedArgument(String argName, ParamType type, String rawValue, T value)
	{
		super(argName, type);
		this.rawValue = rawValue;
		this.value = value;
	}
	
	public ProcessedArgument(String rawValue, T value)
	{
		this.rawValue = rawValue;
		this.value = value;
	}

	public T getValue()
	{
		return value;
	}

	public String getRawValue()
	{
		return rawValue;
	}
}
