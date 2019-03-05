package com.ccl.args;

import java.util.ArrayList;
import java.util.List;

import com.ccl.enumerations.ParamType;

public class GroupArgument extends Argument
{
	private List<Argument> arguments = new ArrayList<>();

	public GroupArgument(String argName, Argument... arguments)
	{
		super(argName, ParamType.GROUP);

		for (Argument arg : arguments)
		{
			arg.setParentName(this.getName());
			this.arguments.add(arg);
		}
	}

	public Argument getArg(int i)
	{
		return arguments.get(i);

	}

	public List<Argument> getArgs()
	{
		return arguments;
	}

	public int size()
	{
		return arguments.size();
	}
}