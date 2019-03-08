package com.armada.test;

import com.armada.Command;
import com.armada.args.Arguments;

public class DelayCommand extends Command<String, String>
{

	public DelayCommand()
	{
		this.setName("delay");
		this.setDelay(3);
	}

	@Override
	public String onExecute(String obj, Arguments in)
	{
		System.out.println("Hello, world!");
		return null;
	}

}
