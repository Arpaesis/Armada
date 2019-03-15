package com.arpaesis.armada.test;

import java.util.Random;

import com.arpaesis.armada.Command;
import com.arpaesis.armada.args.Arguments;
import com.arpaesis.armada.args.GroupArgument;
import com.arpaesis.armada.args.RequiredArgument;
import com.arpaesis.armada.args.logical.OrArgument;
import com.arpaesis.armada.args.processed.ProcessedGroupArgument;
import com.arpaesis.armada.enumerations.ParamType;
import com.arpaesis.armada.enumerations.Status;

public class BalanceCommand extends Command<String, String>
{

	Random rand = new Random();

	public BalanceCommand()
	{
		this.setName("balance");
		this.setAliases(new String[] { "bal" });

		this.addArgument(new OrArgument(new RequiredArgument("single", ParamType.STRING),
				new GroupArgument("multi", new RequiredArgument("func", ParamType.STRING), new RequiredArgument("value", ParamType.INT))));
	}

	@Override
	public String onExecute(String event, Arguments in)
	{
		System.out.println(in.getBranchUsed());
		if (in.getBranchUsed().equals("single"))
		{
			String func = in.getString();
			if (func.equalsIgnoreCase("get"))
			{
				System.out.println("get");
			}
		}
		else if (in.getBranchUsed().equals("multi"))
		{
			ProcessedGroupArgument group = in.getGroup();

			String func = group.getString();

			if (func.equalsIgnoreCase("add"))
			{
				System.out.println("add");
			}
		}
		return null;
	}

	@Override
	public void result(String event, Status result, String response)
	{
		if (result == Status.FAILED)
			System.out.println(response);
		super.result(event, result, response);
	}
}
