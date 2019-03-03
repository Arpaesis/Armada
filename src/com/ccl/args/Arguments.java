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
	
	public boolean getBooleanFor(String tag, boolean def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().matches(tag))
			{
				return (boolean) argument.getValue();
			}
		}
		return def;
	}
	
	public byte getByteFor(String tag, byte def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().matches(tag))
			{
				return (byte) argument.getValue();
			}
		}
		return def;
	}
	
	public char getCharacterFor(String tag, char def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().matches(tag))
			{
				return (char) argument.getValue();
			}
		}
		return def;
	}
	
	public double getDoubleFor(String tag, double def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().matches(tag))
			{
				return (double) argument.getValue();
			}
		}
		return def;
	}
	
	public float getFloatFor(String tag, float def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().matches(tag))
			{
				return (float) argument.getValue();
			}
		}
		return def;
	}
	
	public int getIntFor(String tag, int def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().matches(tag))
			{
				return (int) argument.getValue();
			}
		}
		return def;
	}
	
	public long getLongFor(String tag, long def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().matches(tag))
			{
				return (long) argument.getValue();
			}
		}
		return def;
	}
	
	public short getShortFor(String tag, short def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().matches(tag))
			{
				return (short) argument.getValue();
			}
		}
		return def;
	}
	
	public String getStringFor(String tag, String def)
	{
		for(ProcessedArgument<?> argument: arguments)
		{
			if(argument.getRawValue().contains(tag))
			{
				return (String) argument.getValue();
			}
		}
		return def;
	}

	public int size()
	{
		return this.arguments.size();
	}
}
