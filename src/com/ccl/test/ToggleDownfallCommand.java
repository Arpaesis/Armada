package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.OptionalArgument;
import com.ccl.enumerations.ParamType;
import com.ccl.enumerations.Status;

public class ToggleDownfallCommand extends Command<String, String>
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
	public String onExecute(String obj, Arguments args)
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
		
		return null;
	}
	
	@Override
	public void result(String obj, Status result, String response)
	{
		if(result == Status.FAILED) System.err.println(response);
	}
}
