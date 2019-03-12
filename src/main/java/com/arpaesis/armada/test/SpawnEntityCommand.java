package com.arpaesis.armada.test;

import com.arpaesis.armada.Command;
import com.arpaesis.armada.args.Arguments;
import com.arpaesis.armada.args.GroupArgument;
import com.arpaesis.armada.args.OptionalArgument;
import com.arpaesis.armada.args.RequiredArgument;
import com.arpaesis.armada.args.logical.OrArgument;
import com.arpaesis.armada.args.processed.ProcessedGroupArgument;
import com.arpaesis.armada.enumerations.ParamType;

public class SpawnEntityCommand extends Command<String, String> {

    public SpawnEntityCommand() {
	this.setName("spawnEntity");
	this.setHelp(
		"Spawns an entity in the world at the given coordinates. Can also take a spawn count and health value.");
	Categories.ENTITIES.addToCategory(this);

	this.addArgument(new RequiredArgument("registryName", ParamType.STRING));
	this.addArgument(new OrArgument(
		new GroupArgument("coords", new RequiredArgument("xPos", ParamType.INT),
			new RequiredArgument("yPos", ParamType.INT), new RequiredArgument("zPos", ParamType.INT)),

		new RequiredArgument("playerName", ParamType.STRING)));

	// Optional parameters
	this.addArgument(new OptionalArgument("count", ParamType.INT).setRange(1, Integer.MAX_VALUE));
	this.addArgument(new OptionalArgument("health", ParamType.DOUBLE).setRange(0, 100));
    }

    @Override
    public String onExecute(String obj, Arguments args) {

	String registryName = args.getString();

	// optionals
	int spawnCount = args.getIntFor("count", 1);
	double health = args.getDoubleFor("health", 100d);

	if (args.getBranchUsed().equals("coords")) {
	    ProcessedGroupArgument coords = args.getGroup();

	    int posX = coords.getInt();
	    int posY = coords.getInt();
	    int posZ = coords.getInt();

	    System.out.println("Spawning " + registryName + " at " + posX + ", " + posY + ", " + posZ
		    + " with a spawn count of " + spawnCount + " with health " + health);
	} else if (args.getBranchUsed().equals("playerName")) {
	    String playerName = args.getString();
	    System.out.println("Spawning " + registryName + " at " + playerName + " with a spawn count of " + spawnCount
		    + " with health " + health);
	}
	return null;
    }
}
