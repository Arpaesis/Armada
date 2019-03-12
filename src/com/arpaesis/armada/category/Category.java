package com.arpaesis.armada.category;

import java.util.ArrayList;
import java.util.List;

import com.arpaesis.armada.Command;

/**
 * A category class to contain {@link Command}s.
 * 
 * @author Arpaesis
 *
 */
public class Category {

    private final List<Command<?, ?>> commands = new ArrayList<>();

    public List<Command<?, ?>> getCommands() {
	return commands;
    }

    /**
     * Adds the specified {@link Command} to the category.
     * 
     * @param command The command to add to the category.
     */
    public void addToCategory(Command<?, ?>... command) {
	for (Command<?, ?> com : command) {
	    commands.add(com);
	}
    }

}
