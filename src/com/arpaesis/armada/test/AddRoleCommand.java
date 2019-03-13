package com.arpaesis.armada.test;

import com.arpaesis.armada.Command;
import com.arpaesis.armada.args.Arguments;
import com.arpaesis.armada.args.RequiredArgument;
import com.arpaesis.armada.enumerations.ParamType;

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
