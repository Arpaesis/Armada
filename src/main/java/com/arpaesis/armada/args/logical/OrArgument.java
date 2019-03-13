package com.arpaesis.armada.args.logical;

import com.arpaesis.armada.args.Argument;
import com.arpaesis.armada.args.GroupArgument;
import com.arpaesis.armada.enumerations.ParamType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A class representing argument branches.
 * 
 * @author Arpaesis
 *
 */
public class OrArgument extends Argument {
    private static final Pattern numberPattern = Pattern.compile("[\\-+]?[0-9]+[0-9,_]*");
    private static final Pattern stringPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

    private final List<Argument> arguments;

    public OrArgument(Argument... arguments) {
    this.arguments = Arrays.asList(arguments);
    this.arguments.sort(Comparator.naturalOrder());
    }

    /**
     * Gets the likely branch used from given input.
     * 
     * @param rawArgs The raw arguments used to determine the likely branch.
     * @return The argument that is likely being used in the input.
     */
    public Argument getLikelyBranch(String[] rawArgs) {

	int largest = 0;
	Argument mostMatchingArg = null;

	List<String> args = new ArrayList<>(Arrays.asList(rawArgs));
	args.removeIf(current -> current.contains(":"));

	for (Argument argument : this.arguments) {
		int weight = 0;

	    if (argument instanceof GroupArgument) {

	    List<Argument> groupArguments = ((GroupArgument) argument).getArgs();
	    int groupArgumentsSize = groupArguments.size();
		if (groupArgumentsSize > args.size()) {
		    continue; // Don't bother dealing with the group, it doesn't fit!!!
		}

		for (int i = 0; i < groupArgumentsSize; i++) {
		    weight = this.handleArg(weight, groupArguments.get(i).getType(), args.get(i));

		    if (weight > largest) {
			largest = weight;
			mostMatchingArg = argument;
		    }
		}
	    } else {

	    weight = this.handleArg(weight, argument.getType(), args.get(this.getPosition()));

		if (weight > largest) {
		    largest = weight;
		    mostMatchingArg = argument;
		}
	    }
	}

	return mostMatchingArg;
    }

    private int handleArg(int weight, ParamType type, String argValue) {
	    switch(type) {
		    case BYTE:
		    case SHORT:
		    case INT:
		    case LONG:
		    case FLOAT:
		    case DOUBLE:
			    if (numberPattern.matcher(argValue).matches()) {
				    return weight + 1;
			    }
			    break;
		    case BOOLEAN:
			    if (argValue.equals("true") || argValue.equals("false") || argValue.equals("1") || argValue.equals("0")) {
				    return weight + 1;
			    }
			    break;
		    case CHAR:
			    if (argValue.length() == 1) {
				    return weight + 1;
			    }
			    break;
		    case STRING:
			    if (stringPattern.matcher(argValue).matches()) {
				    return weight + 1;
			    }
	    }
	    return weight;
    }

    @Override
    public ParamType getType() {
	return ParamType.OR;
    }
}
