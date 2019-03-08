package com.armada.args;

import java.util.List;

import com.armada.args.processed.ProcessedArgument;
import com.armada.args.processed.ProcessedGroupArgument;

public class Arguments
{

	private String branchUsed;

	private final List<ProcessedArgument<?>> arguments;

	private int counter;

	public Arguments(List<ProcessedArgument<?>> arguments)
	{
		this.arguments = arguments;
	}

	public boolean hasNext()
	{
		return counter < this.arguments.size();
	}

	public String getBranchUsed()
	{
		return branchUsed;
	}

	public Arguments setBranchUsed(String branchUsed)
	{
		this.branchUsed = branchUsed;
		return this;
	}

	public List<ProcessedArgument<?>> getArguments()
	{
		return arguments;
	}

	public boolean isEmpty()
	{
		return this.arguments.isEmpty();
	}

	public <T> ProcessedGroupArgument getGroup()
	{
		return (ProcessedGroupArgument) arguments.get(counter++);
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

	public int size()
	{
		return this.arguments.size();
	}
}
