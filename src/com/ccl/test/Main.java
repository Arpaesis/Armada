package com.ccl.test;
import java.util.Scanner;

import com.ccl.CommandManager;

public class Main
{

	public static void main(String[] arguments)
	{

		CommandManager.setPrefix("!");
		CommandManager.register(new SpawnEntityCommand());
		CommandManager.register(new ToggleDownfallCommand());

		Scanner scanner = new Scanner(System.in);

		String in;
		do
		{
			in = scanner.nextLine();

			CommandManager.execute(in);
		}
		while (!in.equalsIgnoreCase("END"));

		scanner.close();
	}
}
