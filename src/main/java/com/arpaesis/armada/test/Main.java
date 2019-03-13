package com.arpaesis.armada.test;

import java.util.Scanner;

import com.arpaesis.armada.CommandManager;

public class Main {

    private static final CommandManager<String, String> manager = new CommandManager<>(true);

    public static void main(String[] arguments) {

	manager.setPrefix("!");

	Scanner scanner = new Scanner(System.in);

	String in;
	do {
	    in = scanner.nextLine();

	    manager.execute("", in);
	} while (!in.equalsIgnoreCase("END"));

	scanner.close();
    }
}
