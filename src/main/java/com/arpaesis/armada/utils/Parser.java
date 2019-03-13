package com.arpaesis.armada.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import com.arpaesis.armada.Command;
import com.arpaesis.armada.args.Argument;
import com.arpaesis.armada.args.Arguments;
import com.arpaesis.armada.args.ContinuousArgument;
import com.arpaesis.armada.args.GroupArgument;
import com.arpaesis.armada.args.logical.OrArgument;
import com.arpaesis.armada.args.processed.ProcessedArgument;
import com.arpaesis.armada.args.processed.ProcessedGroupArgument;
import com.arpaesis.armada.enumerations.ParamType;
import com.arpaesis.armada.enumerations.Status;

public class Parser {

    public static <T, R> Arguments processInput(Command<T, R> command, T obj, String input) {
	List<Argument> arguments = new ArrayList<>(command.arguments);
	List<String> list = new ArrayList<>();
	Matcher m = command.getStringPattern().matcher(input);
	while (m.find())
	    list.add(m.group(1));

	String[] tempArr = new String[list.size()];
	String[] preArgs = list.toArray(tempArr);

	String[] rawArgs = Arrays.copyOfRange(preArgs, 1, preArgs.length);

	List<ProcessedArgument<?>> processedArguments = new ArrayList<>();

	String branchUsed = "";

	PARSER: for (int i = 0; i < rawArgs.length; i++) {
	    Matcher tm = command.getNumberPattern().matcher(rawArgs[i]);

	    if (!command.isShouldExecute()) {
		break;
	    } else if (rawArgs.length < arguments.size() - command.getOptArgCount()) {
		command.shutdown(obj, Status.FAILED, "The command has an invalid number of parameters!");
		break;
	    }

	    if (arguments.get(i) instanceof ContinuousArgument) {
		if (i != arguments.size() - 1) {
		    command.shutdown(obj, Status.FAILED,
			    "Continuous arguments can only be placed at the end of a command's argument structure!");
		    break;
		}

		for (int j = i; j < rawArgs.length; j++) {
		    ProcessedArgument<Object> arg = new ProcessedArgument<>(rawArgs[j], rawArgs[j]);

		    if (arguments.get(i).getType() == ParamType.STRING) {
			arguments.add(j + 1, new ProcessedArgument<Object>(arguments.get(i).getName(),
				arguments.get(i).getType(), arg.getName(), formatString(rawArgs[j])));
		    } else if (arguments.get(i).getType() == ParamType.CHAR) {
			arguments.add(j + 1, new ProcessedArgument<Object>(arguments.get(i).getName(),
				arguments.get(i).getType(), arg.getName(), rawArgs[j]));
		    } else if (arguments.get(i).getType() == ParamType.BOOLEAN) {
			arguments.add(j + 1, new ProcessedArgument<Object>(arguments.get(i).getName(),
				arguments.get(i).getType(), arg.getName(), formatToBoolean(rawArgs[j])));
		    } else {
			arguments.add(j + 1, new ProcessedArgument<Object>(arguments.get(i).getName(),
				arguments.get(i).getType(), arg.getName(), formatToNumber(rawArgs[j])));
		    }
		}

		arguments.remove(i);
		i--;
		continue;
	    }
	    switch (arguments.get(i).getType()) {
	    case BOOLEAN:
		if (!rawArgs[i].contains(":")) {
		    if (rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1")
			    || rawArgs[i].equals("0")) {
			processedArguments.add(new ProcessedArgument<Boolean>(arguments.get(i).getName(),
				arguments.get(i).getType(), rawArgs[i], formatToBoolean(rawArgs[i])));
		    } else {
			command.shutdown(obj, Status.FAILED,
				"Failed to parse argument " + rawArgs[i] + ", expected a boolean!");
		    }

		} else {
		    String[] split = rawArgs[i].split(":", 2);
		    String tag = split[0];
		    String bool = split[1];

		    if (bool.equals("true") || bool.equals("false") || bool.equals("1") || bool.equals("0")) {

			processedArguments.add(new ProcessedArgument<Boolean>(tag, formatToBoolean(bool)));
		    } else {
			command.shutdown(obj, Status.FAILED,
				"Failed to parse argument " + rawArgs[i] + ", expected a boolean!");
		    }
		}
		break;
	    case CHAR:
		if (!rawArgs[i].contains(":")) {
		    if (rawArgs[i].length() > 1) {
			command.shutdown(obj, Status.FAILED,
				"Failed to parse argument " + rawArgs[i] + ", expected a single character!");
		    }
		} else if (rawArgs[i].contains(":")) {
		    String[] split = rawArgs[i].split(":", 2);
		    String tag = split[0];
		    char cValue = split[1].charAt(0);
		    processedArguments.add(new ProcessedArgument<Character>(arguments.get(i).getName(),
			    arguments.get(i).getType(), tag, cValue));
		} else {
		    processedArguments.add(new ProcessedArgument<Character>(arguments.get(i).getName(),
			    arguments.get(i).getType(), rawArgs[i], rawArgs[i].charAt(0)));
		}
		break;
	    case BYTE:
	    case DOUBLE:
	    case FLOAT:
	    case INT:
	    case LONG:
	    case SHORT:
		try {
			if (tm.find() && !rawArgs[i].contains(":")) {
				rawArgs[i] = rawArgs[i].replaceAll("_", "").replaceAll(",", "");

				if (!rawArgs[i].contains("%")) {
					Number num = formatToNumber(rawArgs[i]);

					rawArgs[i] = MathUtils.clampi(num.intValue(), arguments.get(i));
					processedArguments.add(new ProcessedArgument<Number>(arguments.get(i).getName(),
						arguments.get(i).getType(), rawArgs[i], NumberFormat.getInstance().parse(rawArgs[i])));
					break;

				} else {
					if (arguments.get(i).hasRange()) {
						rawArgs[i] = rawArgs[i].replace("%", "");

						double percentage = formatToNumber(rawArgs[i]).doubleValue() / 100d;
						rawArgs[i] = Integer.toString(MathUtils.getPercentageValue(percentage,
							arguments.get(i).getMin(), arguments.get(i).getMax()));
						processedArguments.add(new ProcessedArgument<Number>(arguments.get(i).getName(),
							arguments.get(i).getType(), rawArgs[i], NumberFormat.getInstance().parse(rawArgs[i])));
						break;
					}
				}
			} else if (rawArgs[i].contains(":")) {
				String[] split = rawArgs[i].split(":", 2);
				Argument optionalArg = command.getArgumentFor(split[0]);
				String tag = split[0];

				if (!rawArgs[i].contains("%")) {
					Number tempNum = NumberFormat.getInstance()
						.parse(MathUtils.clampd(formatToNumber(split[1]).doubleValue(), optionalArg));

					processedArguments.add(new ProcessedArgument<Number>(tag, tempNum));
					break;
				} else {
					if (optionalArg.hasRange()) {
						rawArgs[i] = split[1].replace("%", "");

						double percentage = NumberFormat.getInstance()
							.parse(MathUtils.clampd(formatToNumber(rawArgs[i]).doubleValue(), optionalArg)).intValue()
							/ 100d;
						int temp = MathUtils.getPercentageValue(percentage, optionalArg.getMin(), optionalArg.getMax());
						processedArguments.add(new ProcessedArgument<Number>(split[0], temp));
						break;
						//
					}
				}
			} else {
				command.shutdown(obj, Status.FAILED,
					"Given parameter (" + rawArgs[i] + ") is not a valid integer value!");
			}
		} catch (ParseException e) {
		    e.printStackTrace();
		}
		break;
	    case STRING:

		if (command.arguments.size() == 1 && command.getOptArgCount() == 0
			&& !(command.arguments.get(0) instanceof ContinuousArgument)) {
			StringBuilder concatBuilder = new StringBuilder();
			for(String rawArg : rawArgs) {
				concatBuilder.append(rawArg).append(" ");
			}
		    String concat = formatString(concatBuilder.toString().trim());

		    processedArguments.add(new ProcessedArgument<String>(arguments.get(i).getName(),
			    arguments.get(i).getType(), concat, concat));
		    break PARSER;
		}
		if (!rawArgs[i].contains(":")) {

		    if (rawArgs[i].length() > arguments.get(i).getMax()) {
			command.shutdown(obj, Status.FAILED,
				"A string parameter (" + rawArgs[i] + ") was given that exceed its maximum value of "
					+ arguments.get(i).getMax() + "!");
			break;
		    }

		    rawArgs[i] = formatString(rawArgs[i]);
		    processedArguments.add(new ProcessedArgument<String>(arguments.get(i).getName(),
			    arguments.get(i).getType(), rawArgs[i], rawArgs[i]));
		} else {
		    String[] split = rawArgs[i].split(":", 2);
		    String tag = split[0];
		    Argument optionalArg = command.getArgumentFor(tag);
		    String content = split[1];

		    if (rawArgs[i].length() > optionalArg.getMax()) {
			command.shutdown(obj, Status.FAILED, "A string parameter (" + split[1]
				+ ") was given that exceed its maximum value of " + optionalArg.getMax() + "!");
			break;
		    }

		    content = formatString(content);
		    processedArguments.add(new ProcessedArgument<String>(tag, content));
		}
		break;
	    case GROUP:

		ProcessedGroupArgument group = new ProcessedGroupArgument(arguments.get(i).getName(),
			arguments.get(i).getType(), rawArgs, null);

		GroupArgument groupArg = (GroupArgument) arguments.get(i);

		List<ProcessedArgument<Object>> temp = new ArrayList<>();

		int k = 0;

		for (int j = i; j < groupArg.size() + groupArg.getPosition(); j++) {
		    if (groupArg.getArg(k).getType() == ParamType.STRING) {
			temp.add(new ProcessedArgument<Object>(group.getName(), formatString(rawArgs[j])));
		    } else if (groupArg.getArg(k).getType() == ParamType.CHAR) {
			temp.add(new ProcessedArgument<Object>(group.getName(), rawArgs[j]));
		    } else if (groupArg.getArg(k).getType() == ParamType.BOOLEAN) {
			temp.add(new ProcessedArgument<Object>(group.getName(), formatToBoolean(rawArgs[j])));
		    } else {
			temp.add(new ProcessedArgument<Object>(group.getName(), formatToNumber(rawArgs[j])));
		    }
		    arguments.add(j + 1, groupArg.getArg(k));
		    k++;
		}

		arguments.remove(groupArg.getPosition());
		i++;

		group.setValues(temp);

		processedArguments.add(group);
		break;
	    case OR:
		OrArgument orArg = ((OrArgument) arguments.get(i));

		orArg.setPosition(i);
		Argument usedArgSet = orArg.getLikelyBranch(rawArgs);
		usedArgSet.setPosition(i);
		arguments.set(i, usedArgSet);

		branchUsed = usedArgSet.getName();

		i--; // Go back a step.
		break;
	    default:
		break;
	    }
	}
	return new Arguments(processedArguments, rawArgs).setBranchUsed(branchUsed);
    }

	private static Number formatToNumber(String rawArgs) {
	Number num = 0;
	boolean neg = false;

	if (rawArgs.startsWith("-")) {
	    neg = true;
	    rawArgs = rawArgs.substring(1);
	}

	if (rawArgs.startsWith("+")) {
	    rawArgs = rawArgs.substring(1);
	}

	if (rawArgs.startsWith("0b") || rawArgs.startsWith("0B")) {
	    num = Integer.parseInt(rawArgs.replace("0b", "").replace("0B", ""), 2);
	} else if (rawArgs.startsWith("0x") || rawArgs.startsWith("0X") || rawArgs.startsWith("#")) {
	    num = Integer.parseInt(rawArgs.replace("0x", "").replace("0X", "").replace("#", ""), 16);
	} else if (rawArgs.startsWith("0") && rawArgs.length() != 1) {
	    num = Integer.parseInt(rawArgs.substring(1), 8);
	} else {
	    try {
		num = NumberFormat.getInstance().parse(rawArgs);
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	}

	if (neg) {
	    num = 0 - num.intValue();
	}

	return num;
    }

	private static String formatString(String toFormat) {
	String result = toFormat;

	if (result.startsWith("\"")) {
	    result = result.substring(1);
	}

	if (result.endsWith("\"")) {
	    result = StringUtils.removeLastCharOptional(result);
	}

	return result;
    }

    private static boolean formatToBoolean(String toFormat) {
	return toFormat.equals("true") || toFormat.equals("1");
    }
}
