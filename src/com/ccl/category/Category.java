package com.ccl.category;

import java.util.ArrayList;
import java.util.List;

import com.ccl.Command;

public class Category
{

	private final List<Command<?, ?>> commands = new ArrayList<>();

	public List<Command<?, ?>> getCommands()
	{
		return commands;
	}

	public void addToCategory(Command<?, ?>... command)
	{
		for (Command<?, ?> com : command)
		{
			commands.add(com);
		}
	}

}
