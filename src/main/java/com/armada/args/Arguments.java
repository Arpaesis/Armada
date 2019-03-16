package com.armada.args;

import java.util.List;

import com.armada.args.processed.ProcessedArgument;
import com.armada.args.processed.ProcessedGroupArgument;

/**
 * A wrapper class to make managing arguments easier.
 * 
 * @author Arpaesis
 *
 */
public class Arguments
{

	private String branchUsed;

	private final List<ProcessedArgument<?>> arguments;
	private final String[] rawArgs;

	private int counter;

	public Arguments(List<ProcessedArgument<?>> arguments, String[] rawArgs)
	{
		this.arguments = arguments;
		this.rawArgs = rawArgs;
	}

	/**
	 * @return Whether or not there is another argument in the arguments chain.
	 */
	public boolean hasNext()
	{
		return counter < this.arguments.size();
	}

	/**
	 * @return The name of the branch that is used in the argument chain.
	 */
	public String getBranchUsed()
	{
		return branchUsed;
	}

	/**
	 * Sets the branch that is used for the argument chain.
	 * 
	 * @param branchUsed
	 *            The name of the branch used.
	 * @return The argument chain.
	 */
	public Arguments setBranchUsed(String branchUsed)
	{
		this.branchUsed = branchUsed;
		return this;
	}

	/**
	 * @return The collection of processed arguments in the chain.
	 */
	public List<ProcessedArgument<?>> getArguments()
	{
		return arguments;
	}

	/**
	 * @return Returns whether or not the arguments chain is empty.
	 */
	public boolean isEmpty()
	{
		return this.arguments.isEmpty();
	}

	/**
	 * @return The next {@link ProcessedGroupArgument} in the argument chain.
	 */
	public <T> ProcessedGroupArgument getGroup()
	{
		return (ProcessedGroupArgument) arguments.get(counter++);
	}

	/**
	 * @return The next boolean in the argument chain.
	 */
	public boolean getBoolean()
	{
		return (boolean) arguments.get(counter++).getValue();
	}

	/**
	 * @return The next byte in the argument chain.
	 */
	public byte getByte()
	{
		return ((Number) arguments.get(counter++).getValue()).byteValue();
	}

	/**
	 * @return The next char in the argument chain.
	 */
	public char getCharacter()
	{
		return (char) arguments.get(counter++).getValue();
	}

	/**
	 * @return The next double in the argument chain.
	 */
	public double getDouble()
	{
		return ((Number) arguments.get(counter++).getValue()).doubleValue();
	}

	/**
	 * @return The next float in the argument chain.
	 */
	public float getFloat()
	{
		return ((Number) arguments.get(counter++).getValue()).floatValue();
	}

	/**
	 * @return The next int in the argument chain.
	 */
	public int getInt()
	{
		return ((Number) arguments.get(counter++).getValue()).intValue();
	}

	/**
	 * @return The next long in the argument chain.
	 */
	public long getLong()
	{
		return ((Number) arguments.get(counter++).getValue()).longValue();
	}

	/**
	 * @return The next short in the argument chain.
	 */
	public short getShort()
	{
		return ((Number) arguments.get(counter++).getValue()).shortValue();
	}

	/**
	 * @return The next String in the argument chain.
	 */
	public String getString()
	{
		return (String) arguments.get(counter++).getValue();
	}

	/**
	 * Fetches a boolean argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return A boolean value from the argument with the game given tag name.
	 */
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

	/**
	 * Fetches a byte argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return A byte value from the argument with the game given tag name.
	 */
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

	/**
	 * Fetches a char argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return A char value from the argument with the game given tag name.
	 */
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

	/**
	 * Fetches a double argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return A double value from the argument with the game given tag name.
	 */
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

	/**
	 * Fetches a float argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return A float value from the argument with the game given tag name.
	 */
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

	/**
	 * Fetches a int argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return An int value from the argument with the game given tag name.
	 */
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

	/**
	 * Fetches a long argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return A long value from the argument with the game given tag name.
	 */
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

	/**
	 * Fetches a short argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return A short value from the argument with the game given tag name.
	 */
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

	/**
	 * Fetches a String argument in the argument chain with the given tag.
	 * 
	 * @param tag
	 *            The tag to search with.
	 * @param def
	 *            The default value to fall back on if the argument could not be found.
	 * @return A String value from the argument with the game given tag name.
	 */
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

	/**
	 * @return The size of the argument chain.
	 */
	public int size()
	{
		return this.arguments.size();
	}

	/**
	 * @return The argument chain as a raw String array.
	 */
	public String[] getRawArguments()
	{
		return rawArgs;
	}
}
