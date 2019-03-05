package com.ccl.args;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ccl.enumerations.ParamType;

public class GroupArgument extends Argument
{
	private List<Argument> arguments = new ArrayList<>();

	public GroupArgument(String argName, Argument... arguments)
	{
		super(argName, ParamType.GROUP);
		this.arguments.addAll(Arrays.asList(arguments));
	}

	public Argument getNextArg(int i)
	{
		return arguments.get(i);

	}

	public int size()
	{
		return arguments.size();
	}
}