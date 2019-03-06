package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.ParamType;

public class AddRoleCommand extends Command<String, String>
{

	public AddRoleCommand()
	{
		this.setName("addrole");
		this.setHelp("Adds the specified role to the user.");

		this.addArgument(new RequiredArgument("roleName", ParamType.STRING));
	}

	@Override
	public String onExecute(String obj, Arguments args)
	{

		String roleName = args.getString();

		System.out.println(roleName);

		return null;
	}
}
