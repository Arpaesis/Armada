package com.ccl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccl.impl.ScheduleCommand;
import com.ccl.schedule.Scheduler;

public final class CommandManager<T, R>
{
	protected final Map<String, Command<T, R>> REGISTRY = new HashMap<>();

	protected final List<CommandResponse<T>> responses = new ArrayList<>();

	private String prefix = "";

	private boolean isEnabled = true;

	private final Scheduler<T, R> scheduler;

	public CommandManager()
	{
		scheduler = new Scheduler<T, R>(this);

		this.register(new ScheduleCommand<T, R>());
	}

	public Command<T, R> register(Command<T, R> command)
	{
		if (!this.isEnabled())
			return null;

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
		if (!this.isEnabled())
			return;

		for (Command<T, R> command : commands)
		{
			this.register(command);
		}
	}

	public Command<T, R> unregister(Command<T, R> command)
	{
		if (!this.isEnabled())
			return null;

		if (!REGISTRY.containsKey(command.getName()))
		{
			System.err.println("The command (" + command.getName() + ") does not exist within the registry!");
			return null;
		}

		return REGISTRY.remove(command.getName().toLowerCase());
	}

	public void unregisterAll()
	{
		if (!this.isEnabled())
			return;

		REGISTRY.clear();
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		if (!this.isEnabled())
			return;

		this.prefix = prefix;
	}

	public List<R> execute(T obj, String in)
	{
		if (!this.isEnabled())
			return null;

		List<R> results = new ArrayList<>();

		this.handleCallbacks(obj, in);

		if (!in.startsWith(this.getPrefix()))
			return null;

		String[] commands = in.split("&&");

		for (String cmdIn : commands)
		{
			String cmdInFormatted = cmdIn.trim();

			String registryName = cmdInFormatted.split(" ")[0].substring(prefix.length()).toLowerCase();

			if (REGISTRY.containsKey(registryName))
			{
				results.add(REGISTRY.get(registryName).execute(obj, cmdInFormatted));
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
								results.add(entry.getValue().execute(obj, cmdInFormatted));
							}
						}
					}
				}
			}
		}
		return results;
	}

	public List<R> executeNoDelay(T obj, String in)
	{
		if (!this.isEnabled())
			return null;

		List<R> results = new ArrayList<>();

		this.handleCallbacks(obj, in);

		if (!in.startsWith(this.getPrefix()))
			return null;

		String[] commands = in.split("&&");

		for (String cmdIn : commands)
		{
			String cmdInFormatted = cmdIn.trim();

			String registryName = cmdInFormatted.split(" ")[0].substring(prefix.length()).toLowerCase();

			if (REGISTRY.containsKey(registryName))
			{
				results.add(REGISTRY.get(registryName).executeNoDelay(obj, cmdInFormatted));
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
								results.add(entry.getValue().executeNoDelay(obj, cmdInFormatted));
							}
						}
					}
				}

			}
		}
		return results;
	}

	public Scheduler<T, R> getScheduler()
	{
		return scheduler;
	}

	public void addWaitingResponse(CommandResponse<T> response)
	{
		if (!this.isEnabled())
			return;

		this.responses.add(response);
	}

	private void handleCallbacks(T obj, String in)
	{
		if (!this.isEnabled())
			return;

		List<CommandResponse<T>> toRemove = new ArrayList<>();

		for (int i = 0; i < responses.size(); i++)
		{
			CommandResponse<T> current = responses.get(i);

			boolean flag = current.onResponse(obj, in);

			if (flag)
			{
				toRemove.add(current);
				break;
			}
		}
		this.responses.removeAll(toRemove);
	}

	public boolean isEnabled()
	{
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
}
