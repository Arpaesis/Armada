package com.arpaesis.armada.args;

import com.arpaesis.armada.enumerations.ParamType;

/**
 * A class representing required arguments.
 * 
 * @author Arpaesis
 *
 */
public class RequiredArgument extends Argument
{

	public RequiredArgument(String argName, ParamType type)
	{
		super(argName, type);
	}

}
