package com.ccl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CommandManager<T, R>
{
	protected final Map<String, Command<T, R>> REGISTRY = new HashMap<>();

	private String prefix = "";

	public CommandManager()
	{

	}

	public Command<T, R> register(Command<T, R> command)
	{
		if (REGISTRY.containsKey(command.getName()))
		{
			throw new RuntimeException("The command " + command.getName() + " has already been registered!");
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

		return REGISTRY.put(command.getName().toLowerCase(), command);
	}

	public void registerAll(Collection<? extends Command<T, R>> commands)
	{
		for (Command<T, R> command : commands)
		{
			this.register(command);
		}
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
}
