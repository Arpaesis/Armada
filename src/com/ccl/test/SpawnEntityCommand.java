package com.ccl.test;

import com.ccl.CommandImpl;
import com.ccl.enumerations.ParamType;

public class SpawnEntityCommand extends CommandImpl<Object>
{
	
	String registryName;
	int posX;
	int posY;
	int posZ;
	int spawnCount;

	public SpawnEntityCommand()
	{
		this.setName("spawnEntity");
		this.setHelp("params: (entityName) (posX) (posY) (posZ) opt(spawnCount)");
		this.requiredParams.add(ParamType.STRING); // registryName
		this.requiredParams.add(ParamType.INT); // xPos
		this.requiredParams.add(ParamType.INT); // yPos
		this.requiredParams.add(ParamType.INT); // zPos
		
		//Optional parameters
		this.optionalParams.add(ParamType.INT); // Spawn count
		this.optionalParams.add(ParamType.INT); // health
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
		
		
		System.out.println("Spawning " + this.registryName + " at " + this.posX + ", " + this.posY + ", " + this.posZ + " with a spawn count of " + this.spawnCount);
	}
}
