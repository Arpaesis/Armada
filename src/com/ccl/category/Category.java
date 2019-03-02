package com.ccl.category;

import java.util.ArrayList;
import java.util.List;

import com.ccl.Command;

public class Category
{

	private final List<Command<?>> COMMANDS = new ArrayList<>();

	public List<Command<?>> getCommands()
	{
		return COMMANDS;
	}

	public void addToCategory(Command<?>... command)
	{
		for (Command<?> com : command)
		{
			COMMANDS.add(com);
		}
	}

}
