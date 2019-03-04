package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.OptionalArgument;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.ParamType;
import com.ccl.help.HelpBuilder;

public class SpawnEntityCommand extends Command<String, String>
{

	public SpawnEntityCommand()
	{
		this.setName("spawnEntity");
		this.setHelp("Spawns an entity in the world at the given coordinates. Can also take a spawn count and health value.");
		Categories.ENTITIES.addToCategory(this);

		this.addArgument(new RequiredArgument("registryName", ParamType.STRING));
		this.addArgument(new RequiredArgument("xPos", ParamType.INT));
		this.addArgument(new RequiredArgument("yPos", ParamType.INT));
		this.addArgument(new RequiredArgument("zPos", ParamType.INT));

		// Optional parameters
		this.addArgument(new OptionalArgument("count", ParamType.INT).setRange(1, Integer.MAX_VALUE));
		this.addArgument(new OptionalArgument("health", ParamType.SHORT).setRange(0, 200));
	}

	@Override
	public String onExecute(String obj, Arguments args)
	{

		String registryName = args.getString();
		int posX = args.getInt();
		int posY = args.getInt();
		int posZ = args.getInt();

		// optionals
		int spawnCount = args.getIntFor("count", 1);
		short health = args.getShortFor("health", (short) 100);

		System.out.println("Spawning " + registryName + " at " + posX + ", " + posY + ", " + posZ + " with a spawn count of " + spawnCount + " with health " + health);
		return null;
	}
}
