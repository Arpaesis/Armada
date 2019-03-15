package com.arpaesis.armada.args;

import java.util.ArrayList;
import java.util.List;

import com.arpaesis.armada.enumerations.ParamType;

/**
 * A class used in grouping arguments together.
 * 
 * @author Arpaesis
 *
 */
public class GroupArgument extends Argument
{
	private final List<Argument> arguments = new ArrayList<>();

	public GroupArgument(String argName, Argument enforcedArg1, Argument enforcedArg2, Argument... arguments)
	{
		super(argName, ParamType.GROUP);

		enforcedArg1.setParentName(this.getName());
		this.arguments.add(enforcedArg1);

		enforcedArg2.setParentName(this.getName());
		this.arguments.add(enforcedArg2);

		for (Argument arg : arguments)
		{
			arg.setParentName(this.getName());
			this.arguments.add(arg);
		}
	}

	/**
	 * Retrieves an argument within the group by the given index.
	 * 
	 * @param i
	 *            The index to fetch the argument with.
	 * @return The argument at the given index.
	 */
	public Argument getArg(int i)
	{
		return arguments.get(i);

	}

	/**
	 * @return All arguments within the group as a collection.
	 */
	public List<Argument> getArgs()
	{
		return arguments;
	}

	/**
	 * @return The number of arguments within the group.
	 */
	public int size()
	{
		return arguments.size();
	}
}