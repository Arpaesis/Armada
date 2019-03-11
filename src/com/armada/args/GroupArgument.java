package com.armada.args;

import java.util.ArrayList;
import java.util.List;

import com.armada.enumerations.ParamType;

public class GroupArgument extends Argument {
    private List<Argument> arguments = new ArrayList<>();

    public GroupArgument(String argName, Argument enforcedArg1, Argument enforcedArg2, Argument... arguments) {
	super(argName, ParamType.GROUP);

	enforcedArg1.setParentName(this.getName());
	this.arguments.add(enforcedArg1);

	enforcedArg2.setParentName(this.getName());
	this.arguments.add(enforcedArg2);

	for (Argument arg : arguments) {
	    arg.setParentName(this.getName());
	    this.arguments.add(arg);
	}
    }

    public Argument getArg(int i) {
	return arguments.get(i);

    }

    public List<Argument> getArgs() {
	return arguments;
    }

    public int size() {
	return arguments.size();
    }
}