package com.ccl.args;

import java.util.List;

public class Arguments
{

	private final List<ProcessedArgument<?>> arguments;
	
	private int counter;
	
	public Arguments(List<ProcessedArgument<?>> arguments)
	{
		this.arguments = arguments;
	}

	public List<ProcessedArgument<?>> getArguments()
	{
		return arguments;
	}
	
	public boolean isEmpty()
	{
		return this.arguments.isEmpty();
	}
	
	public boolean getBoolean()
	{
		return (boolean) arguments.get(counter++).getValue();
	}
	
	public byte getByte()
	{
		return (byte) arguments.get(counter++).getValue();
	}
	
	public char getCharacter()
	{
		return (char) arguments.get(counter++).getValue();
	}
	
	public double getDouble()
	{
		return (double) arguments.get(counter++).getValue();
	}
	
	public float getFloat()
	{
		return (float) arguments.get(counter++).getValue();
	}
	
	public int getInt()
	{
		return (int) arguments.get(counter++).getValue();
	}
	
	public long getLong()
	{
		return (long) arguments.get(counter++).getValue();
	}
	
	public short getShort()
	{
		return (short) arguments.get(counter++).getValue();
	}
	
	public String getString()
	{
		return (String) arguments.get(counter++).getValue();
	}

	public int size()
	{
		return this.arguments.size();
	}
}
