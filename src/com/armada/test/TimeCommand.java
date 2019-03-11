package com.armada.test;

import com.armada.Command;
import com.armada.args.Arguments;
import com.armada.args.RequiredArgument;
import com.armada.args.logical.OrArgument;
import com.armada.enumerations.ParamType;
import com.armada.enumerations.Status;

public class TimeCommand extends Command<String, String> {

    public TimeCommand() {
	this.setName("time");

	this.addArgument(new RequiredArgument("func", ParamType.STRING));

	this.addArgument(
		new OrArgument(new RequiredArgument("tickCount", ParamType.LONG).setRange(0, Integer.MAX_VALUE),
			new RequiredArgument("word", ParamType.STRING)));
    }

    @Override
    public String onExecute(String obj, Arguments args) {

	System.out.println("executing...");

	String func = args.getString();

	System.out.println(args.getBranchUsed());
	if (args.getBranchUsed().equalsIgnoreCase("tickCount")) {
	    System.out.println("tickCount");
	    int ticks = args.getInt();

	    System.out.println(ticks);

	    switch (func) {
	    case "add":
		System.out.println("adding...");
		break;

	    default:

	    }
	} else if (args.getBranchUsed().equalsIgnoreCase("word")) {
	    String keyword = args.getString();
	}
	return obj;
    }

    @Override
    public void result(Status result, String response) {
	System.out.println(response);
	super.result(result, response);
    }
}
