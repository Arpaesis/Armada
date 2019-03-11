package com.armada.test;

import com.armada.Command;
import com.armada.args.Arguments;
import com.armada.args.RequiredArgument;
import com.armada.enumerations.ParamType;

public class AddRoleCommand extends Command<String, String> {

    public AddRoleCommand() {
	this.setName("addrole");
	this.setHelp("Adds the specified role to the user.");

	this.addArgument(new RequiredArgument("roleName", ParamType.STRING));
    }

    @Override
    public String onExecute(String obj, Arguments args) {

	String roleName = args.getString();

	System.out.println(roleName);

	return null;
    }
}
