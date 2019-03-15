package com.arpaesis.armada;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.arpaesis.armada.args.Argument;
import com.arpaesis.armada.args.Arguments;
import com.arpaesis.armada.args.GroupArgument;
import com.arpaesis.armada.args.OptionalArgument;
import com.arpaesis.armada.args.RequiredArgument;
import com.arpaesis.armada.args.logical.OrArgument;
import com.arpaesis.armada.enumerations.Status;
import com.arpaesis.armada.utils.Parser;

/**
 * A class designed to represent a command.
 * 
 * @author Arpaesis
 *
 * @param <T>
 *            The type of object that will be expected as a parameter in the execute methods of the command.
 * @param <R>
 *            The return type that the execution methods will expect.
 */
public abstract class Command<T, R>
{

	public final List<Argument> arguments = new ArrayList<>();
	private int reqArgCount = 0;
	private int optArgCount = 0;

	protected String name = "";
	protected String help = "";
	protected String[] aliases;

	private int globalGlobalCooldown = 0;
	private long lastUsage = 0;
	private int timesUsed = 0;
	private int maxUsage = -1;

	private int level = 0;

	private final Pattern numberPattern = Pattern.compile("[\\-+]?[0-9]+[0-9,_]*");
	private final Pattern stringPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	private boolean shouldExecute = true;

	private boolean isDisabled = false;

	private CommandManager<T, R> manager;

	public Command()
	{
	}

	/**
	 * The method that fires when a command executes.
	 * 
	 * @param obj
	 *            The generic object passed into the execute method.
	 * @param in
	 *            The arguments that were fed to the command prior to execution.
	 * @return A generic object specified by the command class.
	 */
	public abstract R onExecute(T obj, Arguments in);

	/**
	 * The method first called by the command manager in processing a command.
	 * 
	 * @param obj
	 *            The generic object passed in.
	 * @param in
	 *            The raw input given to the command for processing.
	 * @return An object of the specified return type.
	 */
	public R process(T obj, String in)
	{
		R result = null;

		if (!this.isDisabled)
		{
			Arguments processedInput = this.processInput(obj, in);

			if (this.shouldExecute && this.isNotUnderCooldown() && this.timesUsed != maxUsage)
			{
				result = this.onExecute(obj, processedInput);

				timesUsed++;
				this.lastUsage = System.currentTimeMillis();
				this.shutdown(obj, Status.SUCCESSFUL, "The command has successfully been executed.");
			}
			else if (!this.isNotUnderCooldown())
			{
				this.shutdown(obj, Status.FAILED, "The command is currently under cooldown!");
			}
			else if (this.timesUsed == maxUsage)
			{
				this.shutdown(obj, Status.FAILED, "The has reached the maximum amount of uses!");
			}

			// reset the command for usage.
			this.shouldExecute = true;
		}

		return result;
	}

	/**
	 * @return The primary name for the command.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the primary name for the command. This will be the default name the command has during execution.
	 * 
	 * @param name
	 *            The name to set for the command.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return The help message for the command.
	 */
	public String getHelp()
	{
		return help;
	}

	/**
	 * Sets the help message for the command.
	 * 
	 * @param help
	 *            The message to be displayed when referencing this command's help.
	 */
	public void setHelp(String help)
	{
		this.help = help;
	}

	/**
	 * @return Whether or not this command should execute.
	 */
	public boolean isShouldExecute()
	{
		return shouldExecute;
	}

	/**
	 * @return A String array of the different names this command is associated with.
	 */
	public String[] getAliases()
	{
		return aliases;
	}

	/**
	 * Sets the aliases for this command to have. Aliases are optional names that can be used in place of this command's primary name.
	 * 
	 * @param aliases
	 *            The String array of aliases the command will use.
	 */
	public void setAliases(String[] aliases)
	{
		this.aliases = aliases;
	}

	/**
	 * @return The global cooldown (in seconds) the command has.
	 */
	public int getGlobalCooldown()
	{
		return globalGlobalCooldown;
	}

	/**
	 * Sets the cooldown in seconds for the command. The command will not execute until the cooldown is over.
	 * 
	 * @param cooldown
	 *            The number of seconds this command should wait until executing again.
	 */
	public void setGlobalCooldown(int cooldown)
	{
		this.globalGlobalCooldown = cooldown;
	}

	/**
	 * @return Whether or not this command's cooldown is over.
	 */
	public boolean isNotUnderCooldown()
	{
		long currentTime = System.currentTimeMillis();

		return ((currentTime - lastUsage) / 1000) >= this.globalGlobalCooldown;

	}

	/**
	 * Called at the end of a command's execution, signaling whether or not the command failed.
	 * 
	 * @param obj
	 *            The generic object that was used in the execute method.
	 * @param result
	 *            The result of the command's execution.
	 * @param response
	 *            A detailed response message about what went wrong with the execution.
	 */
	public void result(T obj, Status result, String response)
	{

	}

	/**
	 * Shuts down the command and prevents it from executing.
	 * 
	 * @param obj
	 *            The object used in the command's execution.
	 * @param result
	 *            The result of the command's execution.
	 * @param response
	 *            A detailed response message about what went wrong with the execution.
	 */
	public void shutdown(T obj, Status result, String response)
	{
		this.result(obj, result, response);
		this.result(result, response);
		this.shouldExecute = false;
	}

	/**
	 * Called at the end of a command's execution, signaling whether or not the command failed.
	 * 
	 * @param result
	 *            The result of the command's execution.
	 * @param response
	 *            A detailed response message about what went wrong with the execution.
	 */
	public void result(Status result, String response)
	{

	}

	/**
	 * @return The maximum amount of times this command can be used.
	 */
	public int getMaxUsage()
	{
		return maxUsage;
	}

	/**
	 * Sets the maximum number of times this command can be executed. Every usage of the command contributes towards reaching this value. If this value is reached, the command will cease to execute. By default, command's max usages are set to -1.
	 * 
	 * @param maxUsage
	 *            The maximum amount of times this command will be usable for.
	 */
	public void setMaxUsage(int maxUsage)
	{
		this.maxUsage = maxUsage;
	}

	/**
	 * @return The level of access this command requires.
	 */
	public int getLevel()
	{
		return level;
	}

	/**
	 * Sets the level of access this command requires. Typically, a higher value means the command is more dangerous to use.
	 * 
	 * @param level
	 *            The access level to set for the command.
	 */
	public void setLevel(int level)
	{
		this.level = level;
	}

	/**
	 * Adds an {@link Argument} to this command's argument chain.
	 * 
	 * @param argument
	 *            The argument to add to the command's argument chain.
	 */
	public void addArgument(Argument argument)
	{
		if (argument instanceof RequiredArgument || argument instanceof OrArgument || argument instanceof GroupArgument)
			this.reqArgCount++;
		else if (argument instanceof OptionalArgument)
			this.optArgCount++;

		this.arguments.add(argument);
	}

	/**
	 * Processes the input fed to this command.
	 * 
	 * @param obj
	 *            The custom object with the specified type in the command.
	 * @param input
	 *            The input fed from the {@link CommandManager}.
	 * @return A collection of {@link Arguments} that can be navigated through.
	 */
	private Arguments processInput(T obj, String input)
	{
		return Parser.processInput(this, obj, input);
	}

	/**
	 * @return The {@link CommandManager} for the command.
	 */
	public CommandManager<T, R> getCommandManager()
	{
		return manager;
	}

	/**
	 * Sets the {@link CommandManager} for the command.
	 * 
	 * @param manager
	 *            The command manager to set for the command.
	 */
	public void setCommandManager(CommandManager<T, R> manager)
	{
		this.manager = manager;
	}

	/**
	 * @return Whether or not the command is currently disabled.
	 */
	public boolean isDisabled()
	{
		return this.isDisabled;
	}

	/**
	 * Sets whether or not the command is disabled. Disabled commands can not be executed.
	 * 
	 * @param isDisabled
	 *            Whether or not the command is disabled.
	 */
	public void setDisabled(boolean isDisabled)
	{
		this.isDisabled = isDisabled;
	}

	/**
	 * Allows for callbacks in command execution (also known as a {@link CommandResponse}).
	 * 
	 * @param response
	 *            The response the command should execute.
	 */
	public void onResponse(CommandResponse<T> response)
	{
		this.manager.addWaitingResponse(response);
	}

	/**
	 * @return The pattern used in analyzing numeric arguments.
	 */
	public Pattern getNumberPattern()
	{
		return this.numberPattern;
	}

	/**
	 * @return The pattern used in analyzing String arguments.
	 */
	public Pattern getStringPattern()
	{
		return this.stringPattern;
	}

	/**
	 * @return The number of required arguments the command has.
	 */
	public int getReqArgCount()
	{
		return this.reqArgCount;
	}

	/**
	 * @return The number of optional arguments the command has.
	 */
	public int getOptArgCount()
	{
		return this.optArgCount;
	}

	/**
	 * Gets an {@link Argument} from the command's argument chain by name.
	 * 
	 * @param name
	 *            The name of the argument to retrieve.
	 * @return The argument with the specified name.
	 */
	public Argument getArgumentFor(String name)
	{
		for (Argument arg : this.arguments)
		{
			if (arg.getName() != null && arg.getName().matches(name))
			{
				return arg;
			}
		}
		return null;
	}
}
