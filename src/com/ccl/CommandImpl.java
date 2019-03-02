package com.ccl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ccl.enumerations.ParamType;
import com.ccl.enumerations.Result;

public abstract class CommandImpl<T extends Object>
{

	public final List<ParamType> requiredParams = new ArrayList<>();
	public final List<ParamType> optionalParams = new ArrayList<>();

	private String name = "";
	private String help = "";
	private String[] aliases;

	private int cooldown = 0;
	private long lastUsage = 0;

	private int timesUsed = 0;

	private boolean shouldExecute = true;

	public CommandImpl()
	{
	}

	public void onExecute(T obj, String[] in)
	{
	}

	public void execute(T obj, String in)
	{
		this.processInput(in);

		if (this.shouldExecute && this.isCooldownReady())
		{
			String[] temp = in.split(" ");
			String[] rawArgs = Arrays.copyOfRange(temp, 1, temp.length);
			this.onExecute(obj, rawArgs);

			timesUsed++;
			this.lastUsage = System.currentTimeMillis();
			this.shutdown(Result.SUCCESS);
		}
		else
		{
			this.shutdown(Result.FAILURE);
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

	public int getCooldown()
	{
		return cooldown;
	}

	public void setCooldown(int cooldown)
	{
		this.cooldown = cooldown;
	}

	public boolean isCooldownReady()
	{
		long currentTime = System.currentTimeMillis();

		if (((currentTime - lastUsage) / 1000) >= this.cooldown)
		{
			return true;
		}

		return false;
	}

	public void result(Result result)
	{

	}

	private void shutdown(Result result)
	{
		this.result(result);
		this.shouldExecute = false;
	}

	private void processInput(String input)
	{
		try
		{
			String[] rawInput = input.split(" ");
			String[] rawArgs = Arrays.copyOfRange(rawInput, 1, rawInput.length);

			for (int i = 0; i < rawArgs.length; i++)
			{
				if (!this.shouldExecute || i > requiredParams.size() - 1 || rawArgs.length < requiredParams.size())
				{
					this.shutdown(Result.FAILURE);
					break;
				}

				switch (requiredParams.get(i))
				{
				case BOOLEAN:
					if (!(rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0")))
					{
						this.shutdown(Result.FAILURE);
					}
					break;
				case BYTE:
					Byte.parseByte(rawArgs[i]);
					break;
				case CHAR:
					if (rawArgs[i].length() >= 2)
					{
						this.shutdown(Result.FAILURE);
					}
					break;
				case DOUBLE:
					Double.parseDouble(rawArgs[i]);
					break;
				case FLOAT:
					Float.parseFloat(rawArgs[i]);
					break;
				case INT:
					Integer.parseInt(rawArgs[i]);

					break;
				case LONG:
					Long.parseLong(rawArgs[i]);
					break;
				case SHORT:
					Short.parseShort(rawArgs[i]);
					break;
				default:
					break;
				}
			}

			for (int i = this.requiredParams.size() - 1; i < rawArgs.length - 1 && rawArgs.length - requiredParams.size() != 0; i++)
			{
				if (!this.shouldExecute || i > (optionalParams.size() + requiredParams.size() - 1))
				{
					break;
				}

				switch (optionalParams.get(rawArgs.length - i - 2))
				{
				case BOOLEAN:
					if (rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0"))
					{
						break;
					}
					else
					{
						this.shutdown(Result.FAILURE);
						break;
					}
				case BYTE:
					Byte.parseByte(rawArgs[i]);
					break;
				case CHAR:
					if (rawArgs[i].length() >= 2)
					{
						this.shutdown(Result.FAILURE);
					}
					break;
				case DOUBLE:
					Double.parseDouble(rawArgs[i]);
					break;
				case FLOAT:
					Float.parseFloat(rawArgs[i]);
					break;
				case INT:
					Integer.parseInt(rawArgs[i]);
					break;
				case LONG:
					Long.parseLong(rawArgs[i]);
					break;
				case SHORT:
					Short.parseShort(rawArgs[i]);
					break;
				default:
					break;
				}
			}
		}
		catch (Exception e)
		{
			this.shutdown(Result.FAILURE);
		}
	}
}
