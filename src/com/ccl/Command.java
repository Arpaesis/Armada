package com.ccl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ccl.args.Argument;
import com.ccl.args.OptionalArgument;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.Result;
import com.ccl.utils.MathUtils;

public abstract class Command<T extends Object>
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

	private boolean shouldExecute = true;

	public Command()
	{
	}

	public void onExecute(T obj, String[] in)
	{
	}

	public void execute(T obj, String in)
	{
		String[] processedInput = this.processInput(in);

		if (this.shouldExecute && this.isGlobalCooldownReady() && this.timesUsed != maxUsage)
		{
			this.onExecute(obj, processedInput);

			timesUsed++;
			this.lastUsage = System.currentTimeMillis();
			this.shutdown(Result.SUCCESS, "The command has successfully been executed.");
		}
		else if (!this.isGlobalCooldownReady())
		{
			this.shutdown(Result.FAILURE, "The command is currently under cooldown!");
		}
		else if (this.timesUsed == maxUsage)
		{
			this.shutdown(Result.FAILURE, "The has reached the maximum amount of uses!");
		}

		// reset the command for usage.
		this.shouldExecute = true;
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

	public void result(Result result, String response)
	{

	}

	private void shutdown(Result result, String response)
	{
		this.result(result, response);
		this.shouldExecute = false;
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

	private String[] processInput(String input)
	{

		String[] rawArgs = Arrays.copyOfRange(input.split(" "), 1, input.split(" ").length);

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
					this.shutdown(Result.FAILURE, "The command has an invalid number of parameters!");
					break;
				}

				switch (this.parameters.get(i).getType())
				{
				case BOOLEAN:
					if (rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0"))
					{
					}
					else
					{
						this.shutdown(Result.FAILURE, "Failed to parse argument " + rawArgs[i] + ", expected a boolean!");
					}
					break;
				case BYTE:
					byte bValue = Byte.parseByte(rawArgs[i]);
					rawArgs[i] = MathUtils.clampb(bValue, parameters.get(i));
					break;
				case CHAR:
					if (rawArgs[i].length() >= 2)
					{
						this.shutdown(Result.FAILURE, "Failed to parse argument " + rawArgs[i] + ", expected a single character!");
					}
					break;
				case DOUBLE:
					double dValue = Double.parseDouble(rawArgs[i]);
					rawArgs[i] = MathUtils.clampd(dValue, parameters.get(i));
					break;
				case FLOAT:
					float fValue = Float.parseFloat(rawArgs[i]);
					rawArgs[i] = MathUtils.clampf(fValue, parameters.get(i));
					break;
				case INT:
					int iValue = Integer.parseInt(rawArgs[i]);
					rawArgs[i] = MathUtils.clampi(iValue, parameters.get(i));
					break;
				case LONG:
					long lValue = Long.parseLong(rawArgs[i]);
					rawArgs[i] = MathUtils.clampl(lValue, parameters.get(i));
					break;
				case SHORT:
					short sValue = Short.parseShort(rawArgs[i]);
					rawArgs[i] = MathUtils.clamps(sValue, parameters.get(i));
					break;
				default:
					break;
				}

			}
			catch (NumberFormatException e)
			{
				e.printStackTrace();
				this.shutdown(Result.FAILURE, "Expected a numerical value from a parameter " + rawArgs[i] + ", but the given value was not a number!");
			}
		}
		return rawArgs;
	}
}
