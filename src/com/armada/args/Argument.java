package com.armada.args;

import com.armada.enumerations.ParamType;

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

	public ParamType getType()
	{
		return type;
	}

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

	public int getMin()
	{
		return min;
	}

	public int getMax()
	{
		return max;
	}

	public String getName()
	{
		return argName;
	}

	public boolean hasRange()
	{
		return hasRange;
	}

	public String getParentName()
	{
		return parentName;
	}

	public Argument setParentName(String parentName)
	{
		this.parentName = parentName;
		return this;
	}

	public int getPosition()
	{
		return this.position;
	}

	public Argument setPosition(int position)
	{
		this.position = position;
		return this;
	}
	
	@Override
	public int compareTo(Argument o)
	{
		return this.type.ordinal() - o.type.ordinal();
	}
}
