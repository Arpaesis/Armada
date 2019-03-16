package com.armada.args;

import com.armada.enumerations.ParamType;

/**
 * The base class class for all arguments.
 * 
 * @author Arpaesis
 *
 */
public abstract class Argument implements Comparable<Argument>
{

	private final String argName;
	private final ParamType type;
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE;

	protected int position = 0;

	private String parentName = "";

	private boolean hasRange = false;

	public Argument(String argName, ParamType type)
	{
		this.argName = argName;
		this.type = type;
	}

	public Argument()
	{
		this.argName = null;
		this.type = null;
	}

	/**
	 * @return The expected type of the argument.
	 */
	public ParamType getType()
	{
		return type;
	}

	/**
	 * Sets the range of the argument, if it is numeric. For Strings, this will set the maximum length the String can be.
	 * 
	 * @param min
	 *            The minimum value the number can be.
	 * @param max
	 *            The maximum value the number can be, or the maximum length the String can be.
	 * @return The argument the range is being set for.
	 */
	public Argument setRange(int min, int max)
	{
		if (this.getType() != ParamType.STRING)
		{
			this.min = min;
			this.max = max;
		}
		else
		{
			this.min = 0;
			this.max = max;
		}

		if (min != Integer.MIN_VALUE || max != Integer.MAX_VALUE)
			this.hasRange = true;

		return this;
	}

	/**
	 * @return The minimum value this argument can be.
	 */
	public int getMin()
	{
		return min;
	}

	/**
	 * @return The maximum value this argument can be.
	 */
	public int getMax()
	{
		return max;
	}

	/**
	 * @return The proper name of the argument.
	 */
	public String getName()
	{
		return argName;
	}

	/**
	 * @return Whether or not this argument has a range defined.
	 */
	public boolean hasRange()
	{
		return hasRange;
	}

	/**
	 * @return The parent name of the argument that contains this argument, if this argument is nested. May return null.
	 */
	public String getParentName()
	{
		return parentName;
	}

	/**
	 * Sets the parent name for the argument.
	 * 
	 * @param parentName
	 *            The name of the parent to set.
	 * @return The argument.
	 */
	public Argument setParentName(String parentName)
	{
		this.parentName = parentName;
		return this;
	}

	/**
	 * @return The position of argument in the raw argument input.
	 */
	public int getPosition()
	{
		return this.position;
	}

	/**
	 * Sets the position of the argument within the raw argument input.
	 * 
	 * @param position
	 *            The position of the argument within the raw argument input.
	 * @return The argument.
	 */
	public Argument setPosition(int position)
	{
		this.position = position;
		return this;
	}

	@Override
	public int compareTo(Argument o)
	{
		return Integer.compare(this.type.ordinal(), o.type.ordinal());
	}
}
