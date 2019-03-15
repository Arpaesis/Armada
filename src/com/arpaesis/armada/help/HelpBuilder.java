package com.arpaesis.armada.help;

import com.arpaesis.armada.Command;
import com.arpaesis.armada.args.Argument;
import com.arpaesis.armada.args.OptionalArgument;
import com.arpaesis.armada.args.RequiredArgument;

/**
 * A builder class to construct a help message for a given command.
 * 
 * @author Arpaesis
 *
 */
public class HelpBuilder
{

	private boolean displayArgTypes = true;
	private boolean showOptionals = true;

	private HelpBuilder()
	{
	}

	/**
	 * @return A new help builder instance.
	 */
	public static HelpBuilder builder()
	{
		return new HelpBuilder();
	}

	/**
	 * @return Whether or not the help message should display the argument types.
	 */
	public HelpBuilder disableArgTypes()
	{
		this.displayArgTypes = false;
		return this;
	}

	/**
	 * @return Whether or not the help message should display the optional arguments.
	 */
	public HelpBuilder disableOptionals()
	{
		this.showOptionals = false;
		return this;
	}

	/**
	 * Builds the help builder and returns a neat help message for the command.
	 * 
	 * @param command
	 *            The command to build the help message for.
	 * @return The help message.
	 */
	public String build(Command<?, ?> command)
	{
		StringBuilder builder = new StringBuilder();

		builder.append(command.getCommandManager().getPrefix()).append(command.getName()).append(" ");

		String argType;

		for (Argument arg : command.arguments)
		{
			argType = this.displayArgTypes ? "(" + arg.getType().toString().toLowerCase() + ")" : "";

			if (arg instanceof RequiredArgument)
				builder.append(arg.getName()).append(argType).append(" ");
			else if (arg instanceof OptionalArgument && this.showOptionals)
				builder.append("[").append(arg.getName()).append("]").append(argType).append(" ");
		}

		builder.append("\n");

		return builder.toString().trim();
	}
}
