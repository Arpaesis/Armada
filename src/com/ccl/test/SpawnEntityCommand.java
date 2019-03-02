package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Arguments;
import com.ccl.args.OptionalArgument;
import com.ccl.args.RequiredArgument;
import com.ccl.enumerations.ParamType;

public class SpawnEntityCommand extends Command<Object>
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
		this.addArgument(new OptionalArgument("spawnCount", ParamType.INT));
		this.addArgument(new OptionalArgument("health", ParamType.INT).setRange(0, 100));
	}

	@Override
	public void onExecute(Object obj, Arguments args)
	{

		this.registryName = args.getString();
		this.posX = args.getInt();
		this.posY = args.getInt();
		this.posZ = args.getInt();

		// optionals
		this.spawnCount = 4 == args.size() - 2 ? args.getInt() : 1;
		this.health = 5 == args.size() - 1 ? args.getInt() : 100;

		System.out.println("Spawning " + this.registryName + " at " + this.posX + ", " + this.posY + ", " + this.posZ + " with a spawn count of " + this.spawnCount + " with health " + health);

	}
}
