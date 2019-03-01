package com.ccl.test;

import com.ccl.CommandImpl;
import com.ccl.DataInterface;
import com.ccl.ParamType;

public class ToggleDownfallCommand extends CommandImpl
{

	boolean isRaining;

	public ToggleDownfallCommand()
	{
		this.setName("toggleDownfall");
		this.setAliases(new String[]
		{ "isRaining" });
		this.optionalParams.add(ParamType.STRING); // is raining
	}

	@Override
	public void onExecute(DataInterface obj, String[] in)
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
}
