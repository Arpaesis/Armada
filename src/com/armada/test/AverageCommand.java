package com.armada.test;

import com.armada.Command;
import com.armada.args.Arguments;
import com.armada.args.ContinuousArgument;
import com.armada.enumerations.ParamType;
import com.armada.enumerations.Status;

public class AverageCommand extends Command<String, String>
{

	public AverageCommand()
	{
		this.setName("average");
		this.setHelp("Gets the average from the given numbers.");

		this.addArgument(new ContinuousArgument("roleName", ParamType.INT));
	}

	@Override
	public String onExecute(String obj, Arguments args)
	{

		int i = 0;
		while (args.hasNext())
		{
			i += args.getInt();
		}
		
		System.out.println(i/args.size());

		return null;
	}
	
	@Override
	public void result(Status result, String response)
	{
		System.out.println(response);
		super.result(result, response);
	}
}
