package com.ccl.test;
import java.util.Scanner;

import com.ccl.CommandManager;

public class Main
{

	private static final CommandManager<Object> manager = new CommandManager<>();
	
	public static void main(String[] arguments)
	{

		manager.setPrefix("!");
		manager.register(new SpawnEntityCommand());
		manager.register(new ToggleDownfallCommand());

		Scanner scanner = new Scanner(System.in);

		String in;
		do
		{
			in = scanner.nextLine();

			manager.execute(new Object(), in);
		}
		while (!in.equalsIgnoreCase("END"));

		scanner.close();
	}
}
