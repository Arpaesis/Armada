package com.armada.args.logical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.armada.args.Argument;
import com.armada.args.GroupArgument;
import com.armada.enumerations.ParamType;
import com.armada.utils.Parser;

/**
 * A class representing argument branches.
 * 
 * @author Arpaesis
 *
 */
public class OrArgument extends Argument {
    private static Pattern numberPattern = Pattern.compile("[\\-+]{0,1}[0-9]+[0-9,_]*");
    private static Pattern stringPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

    TreeMap<Argument, Number> sorted;

    public OrArgument(Argument... arguments) {
	sorted = new TreeMap<>();

	for (Argument argument : arguments) {
	    this.sorted.put(argument, 0);
	}
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

	List<String> args = new ArrayList<String>(Arrays.asList(rawArgs));
	Iterator<String> iterator = args.iterator();
	while (iterator.hasNext()) {
	    String current = iterator.next();
	    if (current.contains(":")) {
		iterator.remove();
	    }
	    // other operations
	}

	for (Entry<Argument, Number> entry : this.sorted.entrySet()) {
	    if (entry.getKey() instanceof GroupArgument) {

		if (((GroupArgument) entry.getKey()).getArgs().size() > args.size()) {
		    continue; // Don't bother dealing with the group, it doesn't fit!!!
		}

		for (int i = 0; i < ((GroupArgument) entry.getKey()).getArgs().size(); i++) {
		    Argument tempArg = ((GroupArgument) entry.getKey()).getArgs().get(i);

		    Matcher stringMatcher = stringPattern.matcher(args.get(i));

		    if (this.isNumber(tempArg)) {
			Matcher numberMatcher = numberPattern.matcher(args.get(i));
			if (numberMatcher.matches()) {
			    this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
			}
		    } else if (this.isBoolean(args.get(i)) && entry.getKey().getType() == ParamType.BOOLEAN) {
			this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
		    } else if (this.isChar(args.get(i)) && entry.getKey().getType() == ParamType.CHAR) {
			this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
		    } else if (stringMatcher.matches() && entry.getKey().getType() == ParamType.STRING) {
			this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
		    } else {
			this.sorted.put(entry.getKey(), entry.getValue().intValue());
		    }

		    if (entry.getValue().intValue() > largest) {
			largest = entry.getValue().intValue();
			mostMatchingArg = entry.getKey();
		    }
		}
	    } else {
		int i = this.getPosition();

		Argument tempArg = entry.getKey();

		Matcher stringMatcher = stringPattern.matcher(Parser.formatString(args.get(i)));

		if (this.isNumber(tempArg)) {
		    Matcher numberMatcher = numberPattern.matcher(args.get(i));
		    if (numberMatcher.matches()) {
			this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
		    }
		} else if (this.isBoolean(args.get(i)) && entry.getKey().getType() == ParamType.BOOLEAN) {
		    this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
		} else if (this.isChar(args.get(i)) && entry.getKey().getType() == ParamType.CHAR) {
		    this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
		} else if (stringMatcher.matches() && entry.getKey().getType() == ParamType.STRING) {
		    this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
		} else {
		    this.sorted.put(entry.getKey(), entry.getValue());
		}

		if (entry.getValue().intValue() > largest) {
		    largest = entry.getValue().intValue();
		    mostMatchingArg = entry.getKey();
		}
	    }
	    this.sorted.put(entry.getKey(), 0);
	}

	largest = 0;

	for (Entry<Argument, Number> entry : this.sorted.entrySet()) {
	    this.sorted.put(entry.getKey(), 0);
	}

	return mostMatchingArg;
    }

    /**
     * Gets whether or not the given String is a char.
     * 
     * @param arg The String to check.
     * @return Whether or not the String is a character.
     */
    private boolean isChar(String arg) {
	return arg.length() == 1;
    }

    /**
     * Gets whether or not the given String represents a boolean value.
     * 
     * @param arg The String to check.
     * @return Whether or not the String value is indeed a boolean.
     */
    private boolean isBoolean(String arg) {
	if (arg.equals("true") || arg.equals("false") || arg.equals("1") || arg.equals("0")) {
	    if (arg.equals("1")) {
		arg = "true";
	    } else if (arg.equals("0")) {
		arg = "false";
	    }
	    return true;
	}
	return false;
    }

    /**
     * Gets whether or not the given {@link Argument} is has a numeric {@link ParamType}.
     * @param arg The argument to check.
     * @return Whether or not the argument is of the param type of a number.
     */
    private boolean isNumber(Argument arg) {
	return arg.getType() == ParamType.BYTE || arg.getType() == ParamType.DOUBLE || arg.getType() == ParamType.FLOAT
		|| arg.getType() == ParamType.INT || arg.getType() == ParamType.LONG
		|| arg.getType() == ParamType.SHORT;
    }

    @Override
    public ParamType getType() {
	return ParamType.OR;
    }
}
