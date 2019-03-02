package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Argument;
import com.ccl.enumerations.ParamType;
import com.ccl.enumerations.Result;

public class ToggleDownfallCommand extends Command<Object>
{

	boolean isRaining;

	public ToggleDownfallCommand()
	{
		this.setName("toggleDownfall");
		this.setAliases(new String[]
		{ "isRaining" });
		this.setCooldown(3);
		this.optionalParams.add(new Argument("flag", ParamType.STRING)); // is raining
		Categories.WORLD.addToCategory(this);
	}

	@Override
	public void onExecute(Object obj, String[] in)
	{
		if (in.length != 0)
		{
			if (in[0].equals("on") || in[0].equals("1"))
			{
				this.isRaining = true;
			}
			else if (in[0].equals("off") || in[0].equals("0"))
			{
				this.isRaining = false;
			}
			else
			{
				this.isRaining = Boolean.parseBoolean(in[0]);
			}
		}
		else
		{
			isRaining = !isRaining;
		}

		System.out.println(isRaining);
	}
	
	@Override
	public void result(Result result, String response)
	{
		if(result == Result.FAILURE)
		{
			System.out.println("THE COMMAND HAS FAILED!");
		}else {
			System.out.println("THE COMMAND HAS SUCCEEDED!");
		}
	}
}
