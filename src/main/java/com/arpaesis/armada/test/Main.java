package com.arpaesis.armada.test;

import java.util.Scanner;

import com.arpaesis.armada.CommandManager;

public class Main {

    private static final CommandManager<String, String> manager = new CommandManager<>();

    public static void main(String[] arguments) {

	manager.setPrefix("!");
	manager.register(new ResponsiveCommand());
	manager.register(new SpawnEntityCommand());
	manager.register(new ToggleDownfallCommand());
	manager.register(new AddRoleCommand());
	manager.register(new AverageCommand());
	manager.register(new TimeCommand());
	manager.register(new BalanceCommand());

	Scanner scanner = new Scanner(System.in);

	String in;
	do {
	    in = scanner.nextLine();

	    manager.execute("", in);
	} while (!in.equalsIgnoreCase("END"));

	scanner.close();
    }
}
