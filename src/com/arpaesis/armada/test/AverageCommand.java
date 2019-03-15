package com.arpaesis.armada.test;

import com.arpaesis.armada.Command;
import com.arpaesis.armada.args.Arguments;
import com.arpaesis.armada.args.ContinuousArgument;
import com.arpaesis.armada.enumerations.ParamType;
import com.arpaesis.armada.enumerations.Status;

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

		System.out.println(i / args.size());

		return null;
	}

	@Override
	public void result(Status result, String response)
	{
		System.out.println(response);
		super.result(result, response);
	}
}
