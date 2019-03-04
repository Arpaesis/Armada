package com.ccl.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.ProcessedArgument;
import com.ccl.enumerations.Result;

public class Parser<T, R>
{

	public Arguments processInput(Command<T, R> command, T obj, String input)
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
				if (tm.find() && !rawArgs[i].contains(":"))
				{
					byte bValue = Byte.parseByte(rawArgs[i]);
					rawArgs[i] = MathUtils.clampb(bValue, command.arguments.get(i));
					arguments.add(new ProcessedArgument<Byte>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], Byte.parseByte(rawArgs[i])));
				}
				else if (rawArgs[i].contains(":"))
				{
					String[] split = rawArgs[i].split(":");
					String tag = split[0];
					byte bValue = Byte.parseByte(split[1]);
					arguments.add(new ProcessedArgument<Byte>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), tag, bValue));
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

				if (tm.find() && !rawArgs[i].contains(":"))
				{
					int iValue = 0;

					boolean neg = false;

					rawArgs[i] = rawArgs[i].replaceAll("_", "").replaceAll(",", "");

					if (rawArgs[i].startsWith("-"))
					{
						neg = true;
						rawArgs[i] = rawArgs[i].substring(1);
					}

					if (rawArgs[i].startsWith("+"))
					{
						rawArgs[i] = rawArgs[i].substring(1);
					}

					if (rawArgs[i].startsWith("0b"))
					{
						iValue = Integer.parseInt(rawArgs[i].replace("0b", ""), 2);
					}
					else if (rawArgs[i].startsWith("0x"))
					{
						iValue = Integer.parseInt(rawArgs[i].replace("0x", ""), 16);
					}
					else if (rawArgs[i].startsWith("0") && rawArgs[i].length() != 1)
					{
						iValue = Integer.parseInt(rawArgs[i].substring(1), 8);
					}
					else
					{
						iValue = Integer.parseInt(rawArgs[i]);
					}

					if (neg)
					{
						iValue = 0 - iValue;
					}

					rawArgs[i] = MathUtils.clampi(iValue, command.arguments.get(i));
					arguments.add(new ProcessedArgument<Integer>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], Integer.parseInt(rawArgs[i])));
				}
				else if (rawArgs[i].contains(":"))
				{
					String[] split = rawArgs[i].split(":");
					String tag = split[0];
					int iValue = Integer.parseInt(split[1]);
					arguments.add(new ProcessedArgument<Integer>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), tag, iValue));
				}
				else
				{
					command.shutdown(obj, Result.FAILURE, "Given parameter (" + rawArgs[i] + ") is not a valid integer value!");
				}
				break;
			case LONG:
				if (tm.find() && !rawArgs[i].contains(":"))
				{
					long lValue = Long.parseLong(rawArgs[i]);
					rawArgs[i] = MathUtils.clampl(lValue, command.arguments.get(i));
					arguments.add(new ProcessedArgument<Long>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], Long.parseLong(rawArgs[i])));
				}
				else if (rawArgs[i].contains(":"))
				{
					String[] split = rawArgs[i].split(":");
					String tag = split[0];
					long lValue = Long.parseLong(split[1]);
					arguments.add(new ProcessedArgument<Long>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), tag, lValue));
				}
				break;
			case SHORT:
				if (tm.find() && !rawArgs[i].contains(":"))
				{
					short sValue = Short.parseShort(rawArgs[i]);
					rawArgs[i] = MathUtils.clamps(sValue, command.arguments.get(i));
					arguments.add(new ProcessedArgument<Short>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), rawArgs[i], Short.parseShort(rawArgs[i])));
				}
				else if (rawArgs[i].contains(":"))
				{
					String[] split = rawArgs[i].split(":");
					String tag = split[0];
					short sValue = Short.parseShort(split[1]);
					arguments.add(new ProcessedArgument<Short>(command.arguments.get(i).getName(), command.arguments.get(i).getType(), tag, sValue));
				}
				break;
			case STRING: // TODO: Make command not ass.

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
}
