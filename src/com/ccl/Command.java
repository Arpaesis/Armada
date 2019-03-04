package com.ccl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.ccl.args.Argument;
import com.ccl.args.Arguments;
import com.ccl.args.OptionalArgument;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.Result;
import com.ccl.schedule.Task;
import com.ccl.utils.Parser;

public abstract class Command<T, R>
{

	public final List<Argument> parameters = new ArrayList<>();
	private int reqArgCount = 0;
	private int optArgCount = 0;

	private String name = "";
	private String help = "";
	private String[] aliases;

	private int globalGlobalCooldown = 0;
	private long lastUsage = 0;
	private int timesUsed = 0;
	private int maxUsage = -1;

	private int level = 0;

	private long delay = 0;

	private Pattern numberPattern = Pattern.compile("[\\-+]{0,1}[0-9]+[0-9,_]*");
	private Pattern stringPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	private boolean shouldExecute = true;

	private boolean isDisabled = false;

	private CommandManager<T, R> manager;

	private Parser<T, R> parser = new Parser<>();

	public Command()
	{
	}

	public abstract R onExecute(T obj, Arguments in);

	public R execute(T obj, String in)
	{
		R result = null;

		if (!this.isDisabled)
		{
			Arguments processedInput = this.processInput(obj, in);

			if (this.shouldExecute && this.isGlobalCooldownReady() && this.timesUsed != maxUsage && !this.hasDelay())
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
			else if (this.hasDelay())
			{
				this.getCommandManager().getScheduler().addTask(new Task<T, R>(obj, in, this.getDelay(), TimeUnit.SECONDS));
			}

			// reset the command for usage.
			this.shouldExecute = true;
		}

		return result;
	}

	public R executeNoDelay(T obj, String in)
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

	public void shutdown(T obj, Result result, String response)
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
		return this.parser.processInput(this, obj, input);
	}

	public CommandManager<T, R> getCommandManager()
	{
		return manager;
	}

	public void setCommandManager(CommandManager<T, R> m)
	{
		manager = m;
	}

	public long getDelay()
	{
		return delay;
	}

	public void setDelay(long delay)
	{
		this.delay = delay;
	}

	public boolean hasDelay()
	{
		return this.delay > 0;
	}

	public boolean isDisabled()
	{
		return this.isDisabled;
	}

	public void setDisabled(boolean isDisabled)
	{
		this.isDisabled = isDisabled;
	}

	public void onResponse(CommandResponse<T> response)
	{
		this.manager.addWaitingResponse(response);
	}

	public Pattern getNumberPattern()
	{
		return this.numberPattern;
	}

	public Pattern getStringPattern()
	{
		return this.stringPattern;
	}

	public int getReqArgCount()
	{
		return this.reqArgCount;
	}

	public int getOptArgCount()
	{
		return this.optArgCount;
	}
}
