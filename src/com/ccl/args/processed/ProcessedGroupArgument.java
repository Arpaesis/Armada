package com.ccl.args.processed;

import java.util.List;

import com.ccl.enumerations.ParamType;

public class ProcessedGroupArgument<T> extends ProcessedArgument<T>
{

	private List<ProcessedArgument<T>> values;
	private String[] rawValues;
	
	private int counter = 0;

	public ProcessedGroupArgument(String argName, ParamType type, String[] rawValues, List<ProcessedArgument<T>> values)
	{
		super(argName, type, argName, null); //TODO: Solve how to not pass in null here.
		this.rawValues = rawValues;
		this.values = values;
	}

	public List<ProcessedArgument<T>> getValues()
	{
		return values;
	}

	public String[] getRawValues()
	{
		return rawValues;
	}
	
	public ProcessedArgument<T> getNextArg()
	{
		return values.get(counter++);
	}
	
	public void setValues(List<ProcessedArgument<T>> values)
	{
		this.values = values;
	}
}