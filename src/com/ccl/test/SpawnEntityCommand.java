package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.OptionalArgument;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.ParamType;
import com.ccl.help.HelpBuilder;

public class SpawnEntityCommand extends Command<String, String>
{

	String registryName;
	int posX;
	int posY;
	int posZ;
	int spawnCount;
	int health;

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
		this.addArgument(new OptionalArgument("spawnCount", ParamType.INT).setRange(1, Integer.MAX_VALUE));
		this.addArgument(new OptionalArgument("health", ParamType.INT).setRange(1, 100));
	}

	@Override
	public String onExecute(String obj, Arguments args)
	{

		this.registryName = args.getString();
		this.posX = args.getInt();
		this.posY = args.getInt();
		this.posZ = args.getInt();

		// optionals
		this.spawnCount = args.getIntFor("count", 1);
		this.health = args.getIntFor("health", 100);

		System.out.println("Spawning " + this.registryName + " at " + this.posX + ", " + this.posY + ", " + this.posZ + " with a spawn count of " + this.spawnCount + " with health " + health);

		System.out.println(HelpBuilder.builder().build(this));
		return null;
	}
}
