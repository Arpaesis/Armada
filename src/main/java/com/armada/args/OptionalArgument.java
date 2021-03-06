package com.armada.args;

import com.armada.enumerations.ParamType;

/**
 * 
 * A class used to represent optional arguments.
 * 
 * @author Arpaesis
 *
 */
public class OptionalArgument extends Argument
{

	public OptionalArgument(String argName, ParamType type)
	{
		super(argName, type);
	}

}
