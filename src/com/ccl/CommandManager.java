package com.ccl;

import java.util.HashMap;
import java.util.Map;

public final class CommandManager
{
	protected static final Map<String, CommandImpl> REGISTRY = new HashMap<>();

	private static String prefix = "";

	private CommandManager()
	{

	}

	public static CommandImpl register(CommandImpl command)
	{
		return REGISTRY.put(command.getName(), command);
	}

	public static String getPrefix()
	{
		return prefix;
	}

	public static void setPrefix(String prefix)
	{
		CommandManager.prefix = prefix;
	}

	public static void execute(String in)
	{
		String registryName = in.split(" ")[0].substring(prefix.length());

		if (REGISTRY.containsKey(registryName))
		{
			REGISTRY.get(registryName).execute(null, in);
		}
		else
		{
			for (Map.Entry<String, CommandImpl> entry : REGISTRY.entrySet())
			{
				if (entry.getValue().getAliases() != null)
				{
					for (String alias : entry.getValue().getAliases())
					{
						if (registryName.equals(alias))
						{
							entry.getValue().execute(null, in);
							break;
						}
					}
				}
			}
		}
	}
}
