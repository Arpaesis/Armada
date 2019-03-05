package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.GroupArgument;
import com.ccl.args.OptionalArgument;
import com.ccl.args.RequiredArgument;
import com.ccl.args.processed.ProcessedGroupArgument;
import com.ccl.enumerations.ParamType;

public class SpawnEntityCommand extends Command<String, String>
{

	public SpawnEntityCommand()
	{
		this.setName("spawnEntity");
		this.setHelp("Spawns an entity in the world at the given coordinates. Can also take a spawn count and health value.");
		Categories.ENTITIES.addToCategory(this);

		this.addArgument(new RequiredArgument("registryName", ParamType.STRING));
		this.addArgument(new GroupArgument("coords", new RequiredArgument("xPos", ParamType.INT), new RequiredArgument("yos", ParamType.INT), new RequiredArgument("zPos", ParamType.INT)));

		// Optional parameters
		this.addArgument(new OptionalArgument("count", ParamType.INT).setRange(1, Integer.MAX_VALUE));
		this.addArgument(new OptionalArgument("health", ParamType.DOUBLE).setRange(0, 100));
	}

	@Override
	public String onExecute(String obj, Arguments args)
	{

		String registryName = args.getString();

		ProcessedGroupArgument<?> coords = args.getGroup();

		int posX = coords.getInt();
		int posY = coords.getInt();
		int posZ = coords.getInt();

		// optionals
		int spawnCount = args.getIntFor("count", 1);
		double health = args.getDoubleFor("health", 100d);

		System.out.println("Spawning " + registryName + " at " + posX + ", " + posY + ", " + posZ + " with a spawn count of " + spawnCount + " with health " + health);
		return null;
	}
}
