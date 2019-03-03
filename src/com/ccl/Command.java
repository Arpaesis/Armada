package com.ccl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ccl.args.Argument;
import com.ccl.args.Arguments;
import com.ccl.args.OptionalArgument;
import com.ccl.args.ProcessedArgument;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.Result;
import com.ccl.utils.MathUtils;
import com.ccl.utils.StringUtils;

public abstract class Command<T extends Object, R extends Object>
{

	public final List<Argument> parameters = new ArrayList<>();
	int reqArgCount = 0;
	int optArgCount = 0;

	private String name = "";
	private String help = "";
	private String[] aliases;

	private int globalGlobalCooldown = 0;
	private long lastUsage = 0;
	private int timesUsed = 0;
	private int maxUsage = -1;

	private int level = 0;

	private Pattern numberPattern = Pattern.compile("[\\-+]{0,1}[0-9]+[0-9,_]*");
	private Pattern stringPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	private boolean shouldExecute = true;
	
	private CommandManager<T, R> manager;

	public Command()
	{
	}
	
	public Command(CommandManager<T, R> manager)
	{
		this.manager = manager;
	}

	public abstract R onExecute(T obj, Arguments in);

	public R execute(T obj, String in)
	{
		Arguments processedInput = this.processInput(obj, in);

		R result = null;

		if (this.shouldExecute && this.isGlobalCooldownReady() && this.timesUsed != maxUsage)
		{
			result = this.onExecute(obj, processedInput);

			timesUsed++;
			this.lastUsage = System.currentTimeMillis();
			this.shutdown(obj, Result.SUCCESS, "The command has successfully been executed.");
		}
		else if (!this.isGlobalCooldownReady())
		{
			this.shutdown(obj, Result.FAILURE, "The command is currently under cooldown!");
		}
		else if (this.timesUsed == maxUsage)
		{
			this.shutdown(obj, Result.FAILURE, "The has reached the maximum amount of uses!");
		}

		// reset the command for usage.
		this.shouldExecute = true;

		return result;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getHelp()
	{
		return help;
	}

	public void setHelp(String help)
	{
		this.help = help;
	}

	public boolean isShouldExecute()
	{
		return shouldExecute;
	}

	public String[] getAliases()
	{
		return aliases;
	}

	public void setAliases(String[] aliases)
	{
		this.aliases = aliases;
	}

	public int getGlobalCooldown()
	{
		return globalGlobalCooldown;
	}

	public void setGlobalCooldown(int cooldown)
	{
		this.globalGlobalCooldown = cooldown;
	}

	public boolean isGlobalCooldownReady()
	{
		long currentTime = System.currentTimeMillis();

		if (((currentTime - lastUsage) / 1000) >= this.globalGlobalCooldown)
		{
			return true;
		}

		return false;
	}

	public void result(T obj, Result result, String response)
	{

	}

	private void shutdown(T obj, Result result, String response)
	{
		this.result(obj, result, response);
		this.result(result, response);
		this.shouldExecute = false;
	}

	public void result(Result result, String response)
	{

	}

	public int getMaxUsage()
	{
		return maxUsage;
	}

	public void setMaxUsage(int maxUsage)
	{
		this.maxUsage = maxUsage;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public void addArgument(Argument argument)
	{
		if (argument instanceof RequiredArgument)
			this.reqArgCount++;
		if (argument instanceof OptionalArgument)
			this.optArgCount++;

		this.parameters.add(argument);
	}

	private Arguments processInput(T obj, String input)
	{

		List<String> list = new ArrayList<>();
		Matcher m = stringPattern.matcher(input);
		while (m.find())
			list.add(m.group(1));

		String[] tempArr = new String[list.size()];
		String[] preArgs = list.toArray(tempArr);

		String[] rawArgs = Arrays.copyOfRange(preArgs, 1, preArgs.length);

		List<ProcessedArgument<?>> arguments = new ArrayList<>();

		for (int i = 0; i < rawArgs.length; i++)
		{
			try
			{
				if (!this.shouldExecute)
				{
					break;
				}
				else if (rawArgs.length < parameters.size() - this.optArgCount || rawArgs.length > this.parameters.size())
				{
					this.shutdown(obj, Result.FAILURE, "The command has an invalid number of parameters!");
					break;
				}

				switch (this.parameters.get(i).getType())
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
						arguments.add(new ProcessedArgument<Boolean>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), Boolean.parseBoolean(rawArgs[i])));
					}
					else
					{
						this.shutdown(obj, Result.FAILURE, "Failed to parse argument " + rawArgs[i] + ", expected a boolean!");
					}
					break;
				case BYTE:
					byte bValue = Byte.parseByte(rawArgs[i]);
					rawArgs[i] = MathUtils.clampb(bValue, parameters.get(i));
					arguments.add(new ProcessedArgument<Byte>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), Byte.parseByte(rawArgs[i])));
					break;
				case CHAR:
					if (rawArgs[i].length() >= 2)
					{
						this.shutdown(obj, Result.FAILURE, "Failed to parse argument " + rawArgs[i] + ", expected a single character!");
					}
					else
					{
						arguments.add(new ProcessedArgument<Character>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), rawArgs[i].charAt(0)));
					}
					break;
				case DOUBLE:
					double dValue = Double.parseDouble(rawArgs[i]);
					rawArgs[i] = MathUtils.clampd(dValue, parameters.get(i));
					arguments.add(new ProcessedArgument<Double>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), Double.parseDouble(rawArgs[i])));
					break;
				case FLOAT:
					float fValue = Float.parseFloat(rawArgs[i]);
					rawArgs[i] = MathUtils.clampf(fValue, parameters.get(i));
					arguments.add(new ProcessedArgument<Float>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), Float.parseFloat(rawArgs[i])));
					break;
				case INT:
					int iValue = 0;

					boolean neg = false;

					Matcher tm = numberPattern.matcher(rawArgs[i]);

					if (tm.find())
					{
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

						rawArgs[i] = MathUtils.clampi(iValue, parameters.get(i));
						arguments.add(new ProcessedArgument<Integer>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), Integer.parseInt(rawArgs[i])));
					}
					else
					{
						this.shutdown(obj, Result.FAILURE, "Given parameter (" + rawArgs[i] + ") is not a valid integer value!");
					}
					break;
				case LONG:
					long lValue = Long.parseLong(rawArgs[i]);
					rawArgs[i] = MathUtils.clampl(lValue, parameters.get(i));
					arguments.add(new ProcessedArgument<Long>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), Long.parseLong(rawArgs[i])));
					break;
				case SHORT:
					short sValue = Short.parseShort(rawArgs[i]);
					rawArgs[i] = MathUtils.clamps(sValue, parameters.get(i));
					arguments.add(new ProcessedArgument<Short>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), Short.parseShort(rawArgs[i])));
					break;
				case STRING:
					if (rawArgs[i].startsWith("\""))
					{
						rawArgs[i] = rawArgs[i].substring(1);
					}

					if (rawArgs[i].endsWith("\""))
					{
						rawArgs[i] = StringUtils.removeLastCharOptional(rawArgs[i]);
					}
					arguments.add(new ProcessedArgument<String>(this.parameters.get(i).getArgName(), this.parameters.get(i).getType(), rawArgs[i]));
					break;
				default:
					break;
				}

			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
				this.shutdown(obj, Result.FAILURE, "Expected a numerical value from a parameter " + rawArgs[i] + ", but the given value was not a number!");
			}
		}
		return new Arguments(arguments);
	}
}
