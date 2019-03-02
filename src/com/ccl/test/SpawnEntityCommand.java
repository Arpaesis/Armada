package com.ccl.test;

import com.ccl.Command;
import com.ccl.args.Argument;
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
		this.setHelp("params: (entityName) (posX) (posY) (posZ) opt(spawnCount)");
		Categories.ENTITIES.addToCategory(this);

		this.requiredParams.add(new Argument("registryName", ParamType.STRING));
		this.requiredParams.add(new Argument("xPos", ParamType.INT));
		this.requiredParams.add(new Argument("yPos", ParamType.INT));
		this.requiredParams.add(new Argument("zPos", ParamType.INT));

		// Optional parameters
		this.optionalParams.add(new Argument("spawnCount", ParamType.INT));
		this.optionalParams.add(new Argument("health", ParamType.INT).setRange(0, 100));
	}

	@Override
	public void onExecute(Object obj, String[] in)
	{
		this.registryName = in[0];
		this.posX = Integer.parseInt(in[1]);
		this.posY = Integer.parseInt(in[2]);
		this.posZ = Integer.parseInt(in[3]);

		// optionals
		this.spawnCount = 4 == in.length - 1 ? Integer.parseInt(in[4]) : 1;
		this.health = 5 == in.length - 1 ? Integer.parseInt(in[5]) : 100;

		System.out.println("Spawning " + this.registryName + " at " + this.posX + ", " + this.posY + ", " + this.posZ + " with a spawn count of " + this.spawnCount + " with health " + health);
	}
}
