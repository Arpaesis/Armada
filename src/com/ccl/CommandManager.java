package com.ccl;

import java.util.HashMap;
import java.util.Map;

public final class CommandManager<T>
{
	protected final Map<String, Command<T>> REGISTRY = new HashMap<>();

	private String prefix = "";

	public CommandManager()
	{

	}

	public Command<T> register(Command<T> command)
	{
		if (REGISTRY.containsKey(command.getName()))
		{
			throw new RuntimeException("The command " + command.getName() + " has already been registered!");
		}

		for (Map.Entry<String, Command<T>> com : REGISTRY.entrySet())
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

		return REGISTRY.put(command.getName(), command);
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public void execute(T obj, String in)
	{
		if (in.startsWith(this.getPrefix()))
		{
			String registryName = in.split(" ")[0].substring(prefix.length());

			if (REGISTRY.containsKey(registryName))
			{
				REGISTRY.get(registryName).execute(obj, in);
			}
			else
			{
				for (Map.Entry<String, Command<T>> entry : REGISTRY.entrySet())
				{
					if (entry.getValue().getAliases() != null)
					{
						for (String alias : entry.getValue().getAliases())
						{
							if (registryName.equals(alias))
							{
								entry.getValue().execute(obj, in);
								break;
							}
						}
					}
				}
			}
		}
	}
}
