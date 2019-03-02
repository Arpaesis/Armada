package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.OptionalArgument;
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
		this.setGlobalCooldown(3);
		this.addArgument(new OptionalArgument("flag", ParamType.STRING)); // is raining
		Categories.WORLD.addToCategory(this);
	}

	@Override
	public void onExecute(Object obj, Arguments args)
	{
		
		String flag = "";
		if(!args.isEmpty())
		{
			flag = args.getString();
			
			if (flag.equals("on") || flag.equals("1"))
			{
				this.isRaining = true;
			}
			else if (flag.equals("off") || flag.equals("0"))
			{
				this.isRaining = false;
			}
			else
			{
				this.isRaining = Boolean.parseBoolean(flag);
			}
		}
		
		
		else {
			this.isRaining = !this.isRaining;
		}
		
		System.out.println(isRaining);
	}
	
	@Override
	public void result(Result result, String response)
	{
		if(result == Result.FAILURE) System.err.println(response);
	}
}
