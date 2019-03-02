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
