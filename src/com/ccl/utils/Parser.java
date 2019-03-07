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
import com.ccl.args.ContinuousArgument;
import com.ccl.args.GroupArgument;
import com.ccl.args.logical.OrArgument;
import com.ccl.args.processed.ProcessedArgument;
import com.ccl.args.processed.ProcessedGroupArgument;
import com.ccl.enumerations.ParamType;
import com.ccl.enumerations.Status;

public class Parser<T, R>
{
	private Command<T, R> command;
	private T obj;
	private String input;
	private List<Argument> arguments;

	public Parser(Command<T, R> command, T obj, String input)
	{
		this.command = command;
		this.obj = obj;
		this.input = input;
		this.arguments = new ArrayList<>(command.arguments);
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

		String branchUsed = "";

		PARSER: for (int i = 0; i < rawArgs.length; i++)
		{
			Matcher tm = command.getNumberPattern().matcher(rawArgs[i]);

			if (!command.isShouldExecute())
			{
				break;
			}
			else if (rawArgs.length < this.arguments.size() - command.getOptArgCount())
			{
				command.shutdown(obj, Status.FAILED, "The command has an invalid number of parameters!");
				break;
			}

			if (this.arguments.get(i) instanceof ContinuousArgument)
			{
				if(i != this.arguments.size() - 1)
				{
					command.shutdown(obj, Status.FAILED, "Continuous arguments can only be placed at the end of a command's argument structure!");
					break;
				}
				
				for (int j = i; j < rawArgs.length; j++)
				{
					ProcessedArgument<Object> arg = new ProcessedArgument<>(rawArgs[j], rawArgs[j]);

					if (this.arguments.get(i).getType() == ParamType.STRING)
					{
						this.arguments.add(j + 1, new ProcessedArgument<Object>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), arg.getName(), this.formatString(rawArgs[j])));
					}
					else if (this.arguments.get(i).getType() == ParamType.CHAR)
					{
						this.arguments.add(j + 1, new ProcessedArgument<Object>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), arg.getName(), rawArgs[j]));
					}
					else if (this.arguments.get(i).getType() == ParamType.BOOLEAN)
					{
						this.arguments.add(j + 1, new ProcessedArgument<Object>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), arg.getName(), this.formatToBoolean(rawArgs[j])));
					}
					else
					{
						this.arguments.add(j + 1, new ProcessedArgument<Object>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), arg.getName(), this.formatToNumber(rawArgs[j])));
					}
				}

				this.arguments.remove(i);
				i--;
				continue;
			}
			switch (this.arguments.get(i).getType())
			{
			case BOOLEAN:
				if (!rawArgs[i].contains(":"))
				{
					if (rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0"))
					{
						arguments.add(new ProcessedArgument<Boolean>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), rawArgs[i], this.formatToBoolean(rawArgs[i])));
					}
					else
					{
						command.shutdown(obj, Status.FAILED, "Failed to parse argument " + rawArgs[i] + ", expected a boolean!");
					}

				}
				else
				{
					String[] split = rawArgs[i].split(":", 2);
					String tag = split[0];
					String bool = split[1];

					if (bool.equals("true") || bool.equals("false") || bool.equals("1") || bool.equals("0"))
					{

						arguments.add(new ProcessedArgument<Boolean>(tag, this.formatToBoolean(bool)));
					}
					else
					{
						command.shutdown(obj, Status.FAILED, "Failed to parse argument " + rawArgs[i] + ", expected a boolean!");
					}
				}
				break;
			case CHAR:
				if (!rawArgs[i].contains(":"))
				{
					if (rawArgs[i].length() > 1)
					{
						command.shutdown(obj, Status.FAILED, "Failed to parse argument " + rawArgs[i] + ", expected a single character!");
					}
				}
				else if (rawArgs[i].contains(":"))
				{
					String[] split = rawArgs[i].split(":", 2);
					String tag = split[0];
					char cValue = split[1].charAt(0);
					arguments.add(new ProcessedArgument<Character>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), tag, cValue));
				}
				else
				{
					arguments.add(new ProcessedArgument<Character>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), rawArgs[i], rawArgs[i].charAt(0)));
				}
				break;
			case BYTE:
			case DOUBLE:
			case FLOAT:
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

				if (this.command.arguments.size() == 1 && this.command.getOptArgCount() == 0 && !(command.arguments.get(0) instanceof ContinuousArgument))
				{
					String concat = "";
					for (int j = 0; j < rawArgs.length; j++)
					{
						concat += rawArgs[j] + " ";
					}
					concat = concat.trim();

					concat = this.formatString(concat);

					arguments.add(new ProcessedArgument<String>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), concat, concat));
					break PARSER;
				}
				if (!rawArgs[i].contains(":"))
				{

					if (rawArgs[i].length() > this.arguments.get(i).getMax())
					{
						command.shutdown(obj, Status.FAILED, "A string parameter (" + rawArgs[i] + ") was given that exceed its maximum value of " + this.arguments.get(i).getMax() + "!");
						break;
					}

					rawArgs[i] = this.formatString(rawArgs[i]);
					arguments.add(new ProcessedArgument<String>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), rawArgs[i], rawArgs[i]));
				}
				else
				{
					String[] split = rawArgs[i].split(":", 2);
					String tag = split[0];
					Argument optionalArg = command.getArgumentFor(tag);
					String content = split[1];

					if (rawArgs[i].length() > optionalArg.getMax())
					{
						command.shutdown(obj, Status.FAILED, "A string parameter (" + split[1] + ") was given that exceed its maximum value of " + optionalArg.getMax() + "!");
						break;
					}

					content = this.formatString(content);
					arguments.add(new ProcessedArgument<String>(tag, content));
				}
				break;
			case GROUP:

				ProcessedGroupArgument group = new ProcessedGroupArgument(this.arguments.get(i).getName(), this.arguments.get(i).getType(), rawArgs, null);

				GroupArgument groupArg = (GroupArgument) this.arguments.get(i);

				List<ProcessedArgument<Object>> temp = new ArrayList<>();

				int k = 0;

				for (int j = i; j < groupArg.size() + groupArg.getPosition(); j++)
				{
					if (groupArg.getArg(k).getType() == ParamType.STRING)
					{
						temp.add(new ProcessedArgument<Object>(group.getName(), this.formatString(rawArgs[j])));
					}
					else if (groupArg.getArg(k).getType() == ParamType.CHAR)
					{
						temp.add(new ProcessedArgument<Object>(group.getName(), rawArgs[j]));
					}
					else if (groupArg.getArg(k).getType() == ParamType.BOOLEAN)
					{
						temp.add(new ProcessedArgument<Object>(group.getName(), this.formatToBoolean(rawArgs[j])));
					}
					else
					{
						temp.add(new ProcessedArgument<Object>(group.getName(), this.formatToNumber(rawArgs[j])));
					}
					this.arguments.add(j + 1, groupArg.getArg(k));
					k++;
				}

				this.arguments.remove(groupArg.getPosition());
				i++;

				group.setValues(temp);

				arguments.add(group);
				break;
			case OR:
				OrArgument orArg = ((OrArgument) this.arguments.get(i));

				orArg.setPosition(i);
				Argument usedArgSet = orArg.getLikelyBranch(rawArgs);
				usedArgSet.setPosition(i);
				this.arguments.set(i, usedArgSet);

				branchUsed = usedArgSet.getName();

				i--; // Go back a step.
				break;
			default:
				break;
			}
		}
		return new Arguments(arguments).setBranchUsed(branchUsed);
	}

	public List<ProcessedArgument<?>> parseNumber(List<ProcessedArgument<?>> arguments, String[] rawArgs, int i, Matcher tm) throws ParseException
	{
		if (tm.find() && !rawArgs[i].contains(":"))
		{
			Number num = 0;

			rawArgs[i] = rawArgs[i].replaceAll("_", "").replaceAll(",", "");

			if (!rawArgs[i].contains("%"))
			{
				num = this.formatToNumber(rawArgs[i]);

				rawArgs[i] = MathUtils.clampi(num.intValue(), this.arguments.get(i));
				arguments.add(new ProcessedArgument<Number>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), rawArgs[i], NumberFormat.getInstance().parse(rawArgs[i])));
				return arguments;

			}
			else
			{
				if (this.arguments.get(i).hasRange())
				{
					rawArgs[i] = rawArgs[i].replace("%", "");

					double percentage = this.formatToNumber(rawArgs[i]).doubleValue() / 100d;
					rawArgs[i] = Integer.toString(MathUtils.getPercentageValue(percentage, this.arguments.get(i).getMin(), this.arguments.get(i).getMax()));
					arguments.add(new ProcessedArgument<Number>(this.arguments.get(i).getName(), this.arguments.get(i).getType(), rawArgs[i], NumberFormat.getInstance().parse(rawArgs[i])));
					return arguments;
				}
			}
		}
		else if (rawArgs[i].contains(":"))
		{
			String[] split = rawArgs[i].split(":", 2);
			Argument optionalArg = command.getArgumentFor(split[0]);
			String tag = split[0];

			if (!rawArgs[i].contains("%"))
			{
				Number tempNum = NumberFormat.getInstance().parse(MathUtils.clampd(this.formatToNumber(split[1]).doubleValue(), optionalArg));

				arguments.add(new ProcessedArgument<Number>(tag, tempNum));
				return arguments;
			}
			else
			{
				if (optionalArg.hasRange())
				{
					rawArgs[i] = split[1].replace("%", "");

					double percentage = NumberFormat.getInstance().parse(MathUtils.clampd(this.formatToNumber(rawArgs[i]).doubleValue(), optionalArg)).intValue() / 100d;
					int temp = MathUtils.getPercentageValue(percentage, optionalArg.getMin(), optionalArg.getMax());
					arguments.add(new ProcessedArgument<Number>(split[0], temp));
					return arguments;
					//
				}
			}
		}
		else
		{
			command.shutdown(this.obj, Status.FAILED, "Given parameter (" + rawArgs[i] + ") is not a valid integer value!");
		}

		return arguments;
	}

	public Number formatToNumber(String rawArgs)
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
		else if (rawArgs.startsWith("0x") || rawArgs.startsWith("#"))
		{
			num = Integer.parseInt(rawArgs.replace("0x", "").replace("#", ""), 16);
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

	public String formatString(String toFormat)
	{
		String result = toFormat;

		if (result.startsWith("\""))
		{
			result = result.substring(1);
		}

		if (result.endsWith("\""))
		{
			result = StringUtils.removeLastCharOptional(result);
		}

		return result;
	}

	public boolean formatToBoolean(String toFormat)
	{
		if (toFormat.equals("true") || toFormat.equals("false") || toFormat.equals("1") || toFormat.equals("0"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
