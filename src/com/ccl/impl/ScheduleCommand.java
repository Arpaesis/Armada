package com.ccl.impl;

import java.util.concurrent.TimeUnit;

import com.ccl.Command;
import com.ccl.CommandManager;
import com.ccl.args.Arguments;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.ParamType;
import com.ccl.enumerations.Result;
import com.ccl.schedule.Task;

public class ScheduleCommand<T, R> extends Command<T, R>
{

	public final CommandManager<T, R> manager;

	public ScheduleCommand(CommandManager<T, R> manager)
	{
		super(manager);
		this.manager = manager;

		this.setName("schedule");

		this.addArgument(new RequiredArgument("timeUnit", ParamType.STRING));
		this.addArgument(new RequiredArgument("count", ParamType.INT));
		this.addArgument(new RequiredArgument("isInfinite", ParamType.BOOLEAN));
		this.addArgument(new RequiredArgument("command", ParamType.STRING));
	}

	@Override
	public R onExecute(T obj, Arguments in)
	{
		String rawTime = in.getString();
		long timeAmount = 0;
		TimeUnit unit = null;

		if (rawTime.contains("sec") || rawTime.contains("s"))
		{
			rawTime = rawTime.replace("sec", "").replace("s", "");
			timeAmount = Long.parseLong(rawTime.replace("sec", "").replace("s", ""));
			unit = TimeUnit.SECONDS;
		}
		else if (rawTime.contains("min") || rawTime.contains("m"))
		{
			rawTime = rawTime.replace("min", "").replace("m", "");
			timeAmount = Long.parseLong(rawTime);
			unit = TimeUnit.MINUTES;
		}

		int count = in.getInt();
		boolean isInfinite = in.getBoolean();
		String command = in.getString();

		manager.getScheduler().addTask(new Task<T, R>(obj, command, timeAmount, unit).setInfinite(isInfinite).setExecutionCount(count));
		return null;
	}

	@Override
	public void result(Result result, String response)
	{
		if (result == Result.SUCCESS)
			System.out.println("Successfully added command to tasks!");
		super.result(result, response);
	}

}
