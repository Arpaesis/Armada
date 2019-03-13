package com.arpaesis.armada.test;

import com.arpaesis.armada.Command;
import com.arpaesis.armada.args.Arguments;
import com.arpaesis.armada.args.OptionalArgument;
import com.arpaesis.armada.enumerations.ParamType;
import com.arpaesis.armada.enumerations.Status;

public class ToggleDownfallCommand extends Command<String, String> {

    boolean isRaining;

    public ToggleDownfallCommand() {
	this.setName("toggleDownfall");
	this.setAliases(new String[] { "isRaining" });
	this.setGlobalCooldown(3);
	this.addArgument(new OptionalArgument("flag", ParamType.STRING)); // is raining
	Categories.WORLD.addToCategory(this);
    }

    @Override
    public String onExecute(String obj, Arguments args) {

	if (!args.isEmpty()) {
		String flag = args.getString();
		switch(flag) {
			case "on":
			case "1":
				this.isRaining = true;
				break;
			case "off":
			case "0":
				this.isRaining = false;
				break;
			default:
				this.isRaining = Boolean.parseBoolean(flag);
		}
	}

	else {
	    this.isRaining = !this.isRaining;
	}

	System.out.println(isRaining);

	return null;
    }

    @Override
    public void result(String obj, Status result, String response) {
	if (result == Status.FAILED)
	    System.err.println(response);
    }
}
