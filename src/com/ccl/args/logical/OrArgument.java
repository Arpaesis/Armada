package com.ccl.args.logical;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ccl.args.Argument;
import com.ccl.args.GroupArgument;
import com.ccl.enumerations.ParamType;

public class OrArgument extends Argument
{
	private static Pattern numberPattern = Pattern.compile("[\\-+]{0,1}[0-9]+[0-9,_]*");
	private static Pattern stringPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	Map<Argument, Integer> arguments;

	public OrArgument(Argument... arguments)
	{
		this.arguments = new HashMap<>();
		for (Argument argument : arguments)
		{
			this.arguments.put(argument, 0);
		}
	}

	public Argument getLikelyBranch(String[] args)
	{

		int largest = 0;
		Argument mostMatchingArg = null;

		int j = 0;

		for (Map.Entry<Argument, Integer> entry : this.arguments.entrySet())
		{
			if (entry.getKey() instanceof GroupArgument)
			{

				for (int i = 0; i < ((GroupArgument) entry.getKey()).getArgs().size(); i++)
				{
					Argument tempArg = ((GroupArgument) entry.getKey()).getArgs().get(i);

					if (args[i + this.position].contains(":"))
					{
						break;
					}

					Matcher stringMatcher = stringPattern.matcher(args[i + this.position]);
					Matcher numberMatcher = numberPattern.matcher(args[i + this.position]);

					if (numberMatcher.matches() && this.isNumber(tempArg))
					{
						this.arguments.put(entry.getKey(), entry.getValue() + 1);
					}
					else if (stringMatcher.matches() && entry.getKey().getType() == ParamType.STRING)
					{
						this.arguments.put(entry.getKey(), entry.getValue() + 1);
					}
					else
					{
						this.arguments.put(entry.getKey(), entry.getValue());
					}

					if (entry.getValue() > largest)
					{
						largest = entry.getValue();
						mostMatchingArg = entry.getKey();
					}
				}
			}
			else
			{
				if (args[j].contains(":"))
				{
					break;
				}

				Matcher stringMatcher = stringPattern.matcher(args[j]);
				Matcher numberMatcher = numberPattern.matcher(args[j]);

				if (numberMatcher.matches())
				{
					this.arguments.put(entry.getKey(), entry.getValue() + 1);
				}
				else if (stringMatcher.matches() && entry.getKey().getType() == ParamType.STRING)
				{
					this.arguments.put(entry.getKey(), entry.getValue() + 1);
				}
				else
				{
					this.arguments.put(entry.getKey(), entry.getValue());
				}

				if (entry.getValue() > largest)
				{
					largest = entry.getValue();
					mostMatchingArg = entry.getKey();
				}
				j++;
			}
			this.arguments.put(entry.getKey(), 0);
		}

		largest = 0;

		for (Map.Entry<Argument, Integer> entry : this.arguments.entrySet())
		{
			this.arguments.put(entry.getKey(), 0);
		}

		return mostMatchingArg;
	}

	public boolean isNumber(Argument arg)
	{
		return arg.getType() == ParamType.BYTE || arg.getType() == ParamType.DOUBLE || arg.getType() == ParamType.FLOAT || arg.getType() == ParamType.INT || arg.getType() == ParamType.LONG || arg.getType() == ParamType.SHORT;
	}

	@Override
	public ParamType getType()
	{
		return ParamType.OR;
	}
}
