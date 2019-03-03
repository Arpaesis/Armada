package com.ccl.schedule;

import java.util.concurrent.TimeUnit;

import com.ccl.CommandManager;

public class Task<T, R>
{

	private final long creationTime;
	private final String input;
	private final T object;

	private long nextExecutionTime;

	private final TimeUnit timeUnit;

	private int executionCounts;

	private long lastTimeGap = 0;

	private boolean isInfinite = false;

	public Task(T object, String input, long value, TimeUnit timeUnit)
	{
		this.object = object;
		this.creationTime = System.currentTimeMillis();
		this.input = input;
		this.timeUnit = timeUnit;

		this.setNextExecutionTime(value);
	}

	public long getCreationTime()
	{
		return creationTime;
	}

	public long getNextExecutionTime()
	{
		return nextExecutionTime;
	}

	public Task<T, R> setNextExecutionTime(long value)
	{
		this.lastTimeGap = value;
		this.nextExecutionTime = System.currentTimeMillis() + timeUnit.toMillis(value);
		System.out.println("current time: " + System.currentTimeMillis());
		System.out.println("next execution time: " + this.nextExecutionTime);
		return this;
	}

	public TimeUnit getTimeUnit()
	{
		return timeUnit;
	}

	public void execute(Scheduler<T, R> scheduler, CommandManager<T, R> manager)
	{
		System.out.println(this.input);
		manager.execute(this.object, this.input);

		this.setNextExecutionTime(this.lastTimeGap);
		this.executionCounts = this.executionCounts - 1;

		if (!this.isInfinite && this.executionCounts == 0)
		{
			scheduler.removeTask(this); // Remove the task when it has finished executing.
		}
	}

	public String getInput()
	{
		return input;
	}

	public T getObject()
	{
		return object;
	}

	public int getExecutionCounts()
	{
		return executionCounts;
	}

	public Task<T, R> setExecutionCount(int count)
	{
		this.executionCounts = count;
		return this;
	}

	public boolean isInfinite()
	{
		return isInfinite;
	}

	public Task<T, R> setInfinite(boolean isInfinite)
	{
		this.isInfinite = isInfinite;
		return this;
	}
}
