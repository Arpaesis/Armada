package com.ccl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.ccl.impl.ScheduleCommand;
import com.ccl.schedule.Scheduler;

public final class CommandManager<T, R>
{
	protected final Map<String, Command<T, R>> REGISTRY = new HashMap<>();

	private String prefix = "";

	private final Scheduler<T, R> scheduler;

	public CommandManager()
	{
		scheduler = new Scheduler<T, R>(this);

		this.register(new ScheduleCommand<T, R>());
	}

	public Command<T, R> register(Command<T, R> command)
	{
		if (REGISTRY.containsKey(command.getName()))
		{
			System.err.println("The command (" + command.getName() + ") has already been registered!");
			return null;
		}

		for (Map.Entry<String, Command<T, R>> com : REGISTRY.entrySet())
		{
			if (com.getValue().getAliases() != null)
			{
				for (String alias : com.getValue().getAliases())
				{
					if (command.getAliases() != null)
					{
						for (String toCheck : command.getAliases())
						{
							if (alias.equals(toCheck))
							{
								System.err.println("Warning: command (" + command.getName() + ") has an alias (" + alias + ") that is already used by another command (" + com.getValue().getName() + ")!");
							}
						}
					}
				}
			}
		}

		command.setCommandManager(this);
		return REGISTRY.put(command.getName().toLowerCase(), command);
	}

	public void registerAll(Collection<? extends Command<T, R>> commands)
	{
		for (Command<T, R> command : commands)
		{
			this.register(command);
		}
	}
	
	public Command<T, R> unregister(Command<T, R> command)
	{
		if (!REGISTRY.containsKey(command.getName()))
		{
			System.err.println("The command (" + command.getName() + ") does not exist within the registry!");
			return null;
		}

		return REGISTRY.remove(command.getName().toLowerCase());
	}
	
	public void unregisterAll()
	{
		REGISTRY.clear();
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public R execute(T obj, String in)
	{

		if (!in.startsWith(this.getPrefix()))
			return null;

		String registryName = in.split(" ")[0].substring(prefix.length()).toLowerCase();

		if (REGISTRY.containsKey(registryName))
		{
			return REGISTRY.get(registryName).execute(obj, in);
		}
		else
		{
			for (Map.Entry<String, Command<T, R>> entry : REGISTRY.entrySet())
			{
				if (entry.getValue().getAliases() != null)
				{
					for (String alias : entry.getValue().getAliases())
					{
						if (registryName.equals(alias))
						{
							return entry.getValue().execute(obj, in);
						}
					}
				}
			}
		}
		return null;
	}
	
	public R executeNoDelay(T obj, String in)
	{

		if (!in.startsWith(this.getPrefix()))
			return null;

		String registryName = in.split(" ")[0].substring(prefix.length()).toLowerCase();

		if (REGISTRY.containsKey(registryName))
		{
			return REGISTRY.get(registryName).executeNoDelay(obj, in);
		}
		else
		{
			for (Map.Entry<String, Command<T, R>> entry : REGISTRY.entrySet())
			{
				if (entry.getValue().getAliases() != null)
				{
					for (String alias : entry.getValue().getAliases())
					{
						if (registryName.equals(alias))
						{
							return entry.getValue().executeNoDelay(obj, in);
						}
					}
				}
			}
		}
		return null;
	}

	public Scheduler<T, R> getScheduler()
	{
		return scheduler;
	}
}
