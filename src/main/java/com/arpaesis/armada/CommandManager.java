package com.arpaesis.armada;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arpaesis.armada.utils.Parser;

/**
 * 
 * @author Arpaesis
 *
 * @param <T> The type that command manager will expect to be used in command
 *        execution.
 * @param <R> The type that the command manager will expected to be returned
 *        from the executed command.
 */
public final class CommandManager<T, R> {
    protected final Map<String, Command<T, R>> REGISTRY = new HashMap<>();

    protected final List<CommandResponse<T>> responses = new ArrayList<>();

    private String prefix = "";

    private boolean isEnabled = true;

    public CommandManager() {
    }

    /**
     * Registers the given command instance.
     * 
     * @param command The command object to register.
     * @return The command that was registered.
     */
    public Command<T, R> register(Command<T, R> command) {
	if (!this.isEnabled())
	    return null;

	if (REGISTRY.containsKey(command.getName())) {
	    System.err.println("The command (" + command.getName() + ") has already been registered!");
	    return null;
	}

	for (Map.Entry<String, Command<T, R>> com : REGISTRY.entrySet()) {
	    if (com.getValue().getAliases() != null) {
		for (String alias : com.getValue().getAliases()) {
		    if (command.getAliases() != null) {
			for (String toCheck : command.getAliases()) {
			    if (alias.equals(toCheck)) {
				System.err.println("Warning: command (" + command.getName() + ") has an alias (" + alias
					+ ") that is already used by another command (" + com.getValue().getName()
					+ ")!");
			    }
			}
		    }
		}
	    }
	}

	command.setCommandManager(this);
	return REGISTRY.put(command.getName().toLowerCase(), command);
    }

    /**
     * Registers a collection of command objects.
     * 
     * @param commands The commands to register.
     */
    public void registerAll(Collection<? extends Command<T, R>> commands) {
	if (!this.isEnabled())
	    return;

	for (Command<T, R> command : commands) {
	    this.register(command);
	}
    }

    /**
     * Removes the specified command from the registry. Commands are unregistered by
     * name.
     * 
     * @param command The command to unregister.
     * @return The command that was removed from the registry.
     */
    public Command<T, R> unregister(Command<T, R> command) {
	if (!this.isEnabled())
	    return null;

	if (!REGISTRY.containsKey(command.getName())) {
	    System.err.println("The command (" + command.getName() + ") does not exist within the registry!");
	    return null;
	}

	return REGISTRY.remove(command.getName().toLowerCase());
    }

    /**
     * Unregisters all commands currently in the registry.
     */
    public void unregisterAll() {
	if (!this.isEnabled())
	    return;

	REGISTRY.clear();
    }

    /**
     * Gets a command object with the specified primary name. Does not accept
     * aliases.
     * 
     * @param name The name of the command to retrieve.
     * @return The command retrieved by name.
     */
    public Command<T, R> getCommand(String name) {
	for (Map.Entry<String, Command<T, R>> entry : REGISTRY.entrySet()) {
	    if (entry.getKey().equalsIgnoreCase(name)) {
		return entry.getValue();
	    }
	}

	return null;
    }

    /**
     * @return The registry of commands.
     */
    public Map<String, Command<T, R>> getRegistry() {
	return REGISTRY;
    }

    /**
     * @return The prefix used by the command manager.
     */
    public String getPrefix() {
	return prefix;
    }

    /**
     * Sets the prefix that will be expected in processing a command.
     * 
     * @param prefix The prefix to be used.
     */
    public void setPrefix(String prefix) {
	if (!this.isEnabled())
	    return;

	this.prefix = prefix;
    }

    /**
     * The method used by the command manager to execute a [@link Command}. Also
     * fires even if the input is not a command.
     * 
     * @param obj The object to be used in the command's execution.
     * @param in  The raw input to be processed by the {@link Parser}.
     * @return The command's result from execution.
     */
    public R execute(T obj, String in) {
	if (!this.isEnabled())
	    return null;

	this.handleResponses(obj, in);

	if (!in.startsWith(this.getPrefix()))
	    return null;

	String registryName = in.split(" ")[0].substring(prefix.length()).toLowerCase();

	if (REGISTRY.containsKey(registryName)) {
	    return REGISTRY.get(registryName).process(obj, in);
	} else {
	    for (Map.Entry<String, Command<T, R>> entry : REGISTRY.entrySet()) {
		if (entry.getValue().getAliases() != null) {
		    for (String alias : entry.getValue().getAliases()) {
			if (registryName.equals(alias)) {
			    return entry.getValue().process(obj, in);
			}
		    }
		}
	    }
	}
	return null;
    }

    /**
     * Adds a response to await execution.
     * 
     * @param response The response to wait.
     */
    public void addWaitingResponse(CommandResponse<T> response) {
	if (!this.isEnabled())
	    return;

	this.responses.add(response);
    }

    /**
     * Handles responses during execution on input.
     * 
     * @param obj The object to be used in the response.
     * @param in  The input to be used in the response execution.
     */
    private void handleResponses(T obj, String in) {
	if (!this.isEnabled())
	    return;

	List<CommandResponse<T>> toRemove = new ArrayList<>();

	for (int i = 0; i < responses.size(); i++) {
	    CommandResponse<T> current = responses.get(i);

	    boolean flag = current.onResponse(obj, in);

	    if (flag) {
		toRemove.add(current);
		break;
	    }
	}
	this.responses.removeAll(toRemove);
    }

    /**
     * @return Whether or not the command manager is enabled.
     */
    public boolean isEnabled() {
	return isEnabled;
    }

    /**
     * Sets whether or not the command manager is enabled.
     * 
     * @param isEnabled Whether or not the command manager should be enabled.
     */
    public void setEnabled(boolean isEnabled) {
	this.isEnabled = isEnabled;
    }
}
