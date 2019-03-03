package com.ccl.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.ccl.CommandManager;

public class Scheduler<T, R>
{
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private ExecutorService executor = Executors.newSingleThreadExecutor();

	private CommandManager<T, R> manager;

	private List<Task<T, R>> tasks = new ArrayList<>();

	public Scheduler(CommandManager<T, R> manager)
	{
		this.manager = manager;
		this.run();
	}

	private void run()
	{
		scheduler.scheduleAtFixedRate(() ->
		{
			for (Task<T, R> task : tasks)
			{
				if (System.currentTimeMillis() >= task.getNextExecutionTime())
				{
					System.out.println("Executing...");
					executor.execute(() -> task.execute(this, this.manager));
				}
			}
			System.out.println(System.currentTimeMillis());
			System.out.println("Remaining tasks: " + tasks.size());
		}, 0, 1, TimeUnit.SECONDS);
	}

	public void addTask(Task<T, R> task)
	{
		this.tasks.add(task);
	}

	public void removeTask(Task<T, R> task)
	{
		this.tasks.remove(task);
	}
}
