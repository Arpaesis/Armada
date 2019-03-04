package com.ccl.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import com.ccl.Command;
import com.ccl.args.Argument;
import com.ccl.args.Arguments;
import com.ccl.args.ProcessedArgument;
import com.ccl.enumerations.Result;

public class Parser<T, R>
{
	private Command<T, R> command;
	private T obj;
	private String input;

	public Parser(Command<T, R> command, T obj, String input)
	{
		this.command = command;
		this.obj = obj;
		this.input = input;
	}

	public Arguments processInput()
	{

		List<String> list = new ArrayList<>();
		Matcher m = command.getStringPattern().matcher(input);
		while (m.find())
			list.add(m.group(1));

		String[] tempArr = new String[list.size()];
		String[] preArgs = list.toArray(tempArr);

		String[] rawArgs = Arrays.copyOfRange(preArgs, 1, preArgs.length);

		List<ProcessedArgument<?>> arguments = new ArrayList<>();

		for (int i = 0; i < rawArgs.length; i++)
		{
			Matcher tm = command.getNumberPattern().matcher(rawArgs[i]);

			if (!command.isShouldExecute())
			{
				break;
			}
			else if (rawArgs.length < command.arguments.size() - command.getOptArgCount() || rawArgs.length > command.arguments.size())
			{
				command.shutdown(obj, Result.FAILURE, "The command has an invalid number of parameters!");
				break;
			}

			switch (command.arguments.get(i).getType())
			{
			case BOOLEAN:
				if (rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0"))
				{
					if (rawArgs[i].equals("1"))
					{
						rawArgs[i] = "true";
					}
					else if (rawArgs[i].equals("0"))
					{
						rawArgs[i] = "false";
					}
					arguments.add(new ProcessedArgument<Boolean>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], Boolean.parseBoolean(rawArgs[i])));
				}
				else
				{
					command.shutdown(obj, Result.FAILURE, "Failed to parse argument " + rawArgs[i] + ", expected a boolean!");
				}
				break;
			case BYTE:
				try
				{
					this.parseNumber(arguments, rawArgs, i, tm);
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}
				break;
			case CHAR:
				if (rawArgs[i].length() >= 2)
				{
					command.shutdown(obj, Result.FAILURE, "Failed to parse argument " + rawArgs[i] + ", expected a single character!");
				}
				else if (rawArgs[i].contains(":"))
				{
					String[] split = rawArgs[i].split(":");
					String tag = split[0];
					char cValue = split[1].charAt(0);
					arguments.add(new ProcessedArgument<Character>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), tag, cValue));
				}
				else
				{
					arguments.add(new ProcessedArgument<Character>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], rawArgs[i].charAt(0)));
				}
				break;
			case DOUBLE:
				if (tm.find() && !rawArgs[i].contains(":"))
				{
					double dValue = Double.parseDouble(rawArgs[i]);
					rawArgs[i] = MathUtils.clampd(dValue, command.arguments.get(i));
					arguments.add(new ProcessedArgument<Double>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], Double.parseDouble(rawArgs[i])));
				}
				else if (rawArgs[i].contains(":"))
				{
					String[] split = rawArgs[i].split(":");
					String tag = split[0];
					double dValue = Double.parseDouble(split[1]);
					arguments.add(new ProcessedArgument<Double>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), tag, dValue));
				}
				break;
			case FLOAT:
				if (tm.find() && !rawArgs[i].contains(":"))
				{
					float fValue = Float.parseFloat(rawArgs[i]);
					rawArgs[i] = MathUtils.clampf(fValue, command.arguments.get(i));
					arguments.add(new ProcessedArgument<Float>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], Float.parseFloat(rawArgs[i])));
				}
				else if (rawArgs[i].contains(":"))
				{
					String[] split = rawArgs[i].split(":");
					String tag = split[0];
					float fValue = Float.parseFloat(split[1]);
					arguments.add(new ProcessedArgument<Float>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), tag, fValue));
				}
				break;
			case INT:
			case LONG:
			case SHORT:
				try
				{
					arguments = this.parseNumber(arguments, rawArgs, i, tm);
				}
				catch (ParseException e)
				{
					e.printStackTrace();
				}
				break;
			case STRING:

				if (!rawArgs[i].contains(":"))
				{
					if (rawArgs[i].startsWith("\""))
					{
						rawArgs[i] = rawArgs[i].substring(1);
					}

					if (rawArgs[i].endsWith("\""))
					{
						rawArgs[i] = StringUtils.removeLastCharOptional(rawArgs[i]);
					}
					arguments.add(new ProcessedArgument<String>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], rawArgs[i]));
				}
				else
				{
					String[] split = rawArgs[i].split(":");
					String tag = split[0];
					String content = split[1];
					if (content.startsWith("\""))
					{
						content = content.substring(1);
					}

					if (content.endsWith("\""))
					{
						content = StringUtils.removeLastCharOptional(content);
					}
					arguments.add(new ProcessedArgument<String>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), tag, content));
				}
				break;
			default:
				break;
			}
		}
		return new Arguments(arguments);
	}

	public List<ProcessedArgument<?>> parseNumber(List<ProcessedArgument<?>> arguments, String[] rawArgs, int i, Matcher tm) throws ParseException
	{
		if (tm.find() && !rawArgs[i].contains(":"))
		{
			Number num = 0;

			rawArgs[i] = rawArgs[i].replaceAll("_", "").replaceAll(",", "");

			if (!rawArgs[i].contains("%"))
			{
				num = this.format(rawArgs[i]);

				rawArgs[i] = MathUtils.clampi(num.intValue(), command.arguments.get(i));
				arguments.add(new ProcessedArgument<Number>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], NumberFormat.getInstance().parse(rawArgs[i])));
				return arguments;

			}
			else
			{
				if (command.arguments.get(i).hasRange())
				{
					rawArgs[i] = rawArgs[i].replace("%", "");

					float percentage = this.format(rawArgs[i]).intValue() / 100f;
					rawArgs[i] = Integer.toString(MathUtils.getPercentageValue(percentage, command.arguments.get(i).getMin(), command.arguments.get(i).getMax()));
					arguments.add(new ProcessedArgument<Number>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], NumberFormat.getInstance().parse(rawArgs[i])));
					return arguments;
				}
			}
		}
		else if (rawArgs[i].contains(":"))
		{
			String[] split = rawArgs[i].split(":");
			Argument optionalArg = command.getArgumentFor(split[0]);
			String tag = split[0];

			if (!rawArgs[i].contains("%"))
			{
				Number tempNum = NumberFormat.getInstance().parse(MathUtils.clampi(this.format(split[1]).intValue(), optionalArg));

				arguments.add(new ProcessedArgument<Number>(tag, tempNum));
				return arguments;
			}
			else
			{
				if (optionalArg.hasRange())
				{
					rawArgs[i] = split[1].replace("%", "");

					float percentage = NumberFormat.getInstance().parse(MathUtils.clampi(this.format(rawArgs[i]).intValue(), optionalArg)).intValue() / 100f;
					int temp = MathUtils.getPercentageValue(percentage, optionalArg.getMin(), optionalArg.getMax());
					arguments.add(new ProcessedArgument<Number>(split[0], temp));
					return arguments;
					//
				}
			}
		}
		else
		{
			command.shutdown(this.obj, Result.FAILURE, "Given parameter (" + rawArgs[i] + ") is not a valid integer value!");
		}

		return arguments;
	}

	public Number format(String rawArgs)
	{
		Number num = 0;
		boolean neg = false;

		if (rawArgs.startsWith("-"))
		{
			neg = true;
			rawArgs = rawArgs.substring(1);
		}

		if (rawArgs.startsWith("+"))
		{
			rawArgs = rawArgs.substring(1);
		}

		if (rawArgs.startsWith("0b"))
		{
			num = Integer.parseInt(rawArgs.replace("0b", ""), 2);
		}
		else if (rawArgs.startsWith("0x"))
		{
			num = Integer.parseInt(rawArgs.replace("0x", ""), 16);
		}
		else if (rawArgs.startsWith("0") && rawArgs.length() != 1)
		{
			num = Integer.parseInt(rawArgs.substring(1), 8);
		}
		else
		{
			try
			{
				num = NumberFormat.getInstance().parse(rawArgs);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
		}

		if (neg)
		{
			num = 0 - num.intValue();
		}

		return num;
	}
}
