package com.armada.test;

import com.armada.Command;
import com.armada.args.Arguments;
import com.armada.args.RequiredArgument;
import com.armada.enumerations.ParamType;

public class ResponsiveCommand extends Command<String, String> {

    public ResponsiveCommand() {
	this.setName("buy");
	this.setHelp("???");

	this.addArgument(new RequiredArgument("buy", ParamType.INT));
    }

    @Override
    public String onExecute(String obj, Arguments args) {

	int buyAmount = args.getInt();

	System.out
		.println("Are you sure you want to buy for $" + buyAmount + "? Type yes to confirm, or no to cancel.");

	this.onResponse((obj1, input) -> {
	    if (input.matches("yes")) {
		System.out.println("are you sure?");

		this.onResponse((obj2, input2) -> {
		    if (input2.matches("yes")) {
			System.out.println("Splendid! Thank you for your purchase!");
		    } else if (input2.matches("no")) {
			System.out.println("Changed your mind, huh? Well, alright.");
		    } else {
			return false; // The response isn't the desired response, so we await the valid response.
		    }

		    return true;
		});
	    } else if (input.matches("no")) {
		System.out.println("Aw... well please come again some time soon!");
	    } else {
		return false; // The response isn't the desired response, so we await the valid response.
	    }

	    return true;
	});

	return null;
    }
}
