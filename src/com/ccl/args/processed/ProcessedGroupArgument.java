package com.ccl.args.processed;

import java.util.List;

import com.ccl.enumerations.ParamType;

public class ProcessedGroupArgument<T> extends ProcessedArgument<T>
{

	private List<ProcessedArgument<T>> arguments;
	private String[] rawValues;
	
	private int counter = 0;

	public ProcessedGroupArgument(String argName, ParamType type, String[] rawValues, List<ProcessedArgument<T>> values)
	{
		super(argName, type, argName, null); //TODO: Solve how to not pass in null here.
		this.rawValues = rawValues;
		this.arguments = values;
	}

	public List<ProcessedArgument<T>> getValues()
	{
		return arguments;
	}

	public String[] getRawValues()
	{
		return rawValues;
	}
	
	public boolean getBoolean()
	{
		return (boolean) arguments.get(counter++).getValue();
	}

	public byte getByte()
	{
		return ((Number) arguments.get(counter++).getValue()).byteValue();
	}

	public char getCharacter()
	{
		return (char) arguments.get(counter++).getValue();
	}

	public double getDouble()
	{
		return ((Number) arguments.get(counter++).getValue()).doubleValue();
	}

	public float getFloat()
	{
		return ((Number) arguments.get(counter++).getValue()).floatValue();
	}

	public int getInt()
	{
		return ((Number) arguments.get(counter++).getValue()).intValue();
	}

	public long getLong()
	{
		return ((Number) arguments.get(counter++).getValue()).longValue();
	}

	public short getShort()
	{
		return ((Number) arguments.get(counter++).getValue()).shortValue();
	}

	public String getString()
	{
		return (String) arguments.get(counter++).getValue();
	}

	public boolean getBooleanFor(String tag, boolean def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().matches(tag))
			{
				return (boolean) argument.getValue();
			}
		}
		return def;
	}

	public byte getByteFor(String tag, byte def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().matches(tag))
			{
				return ((Number) argument.getValue()).byteValue();
			}
		}
		return def;
	}

	public char getCharacterFor(String tag, char def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().matches(tag))
			{
				return (char) argument.getValue();
			}
		}
		return def;
	}

	public double getDoubleFor(String tag, double def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().matches(tag))
			{
				return ((Number) argument.getValue()).doubleValue();
			}
		}
		return def;
	}

	public float getFloatFor(String tag, float def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().matches(tag))
			{
				return ((Number) argument.getValue()).floatValue();
			}
		}
		return def;
	}

	public int getIntFor(String tag, int def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().matches(tag))
			{
				return ((Number) argument.getValue()).intValue();
			}
		}
		return def;
	}

	public long getLongFor(String tag, long def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().matches(tag))
			{
				return ((Number) argument.getValue()).longValue();
			}
		}
		return def;
	}

	public short getShortFor(String tag, short def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().matches(tag))
			{
				return ((Number) argument.getValue()).shortValue();
			}
		}
		return def;
	}

	public String getStringFor(String tag, String def)
	{
		for (ProcessedArgument<?> argument : arguments)
		{
			if (argument.getRawValue().contains(tag))
			{
				return (String) argument.getValue();
			}
		}
		return def;
	}
	
	public void setValues(List<ProcessedArgument<T>> values)
	{
		this.arguments = values;
	}
}