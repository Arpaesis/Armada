package com.ccl.args.logical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ccl.args.Argument;
import com.ccl.args.GroupArgument;
import com.ccl.enumerations.ParamType;
import com.ccl.utils.Parser;

public class OrArgument extends Argument
{
	private static Pattern numberPattern = Pattern.compile("[\\-+]{0,1}[0-9]+[0-9,_]*");
	private static Pattern stringPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	TreeMap<Argument, Number> sorted;

	public OrArgument(Argument... arguments)
	{
		sorted = new TreeMap<>();

		for (Argument argument : arguments)
		{
			this.sorted.put(argument, 0);
		}
	}

	public Argument getLikelyBranch(String[] rawArgs)
	{

		int largest = 0;
		Argument mostMatchingArg = null;

		List<String> args = new ArrayList<String>(Arrays.asList(rawArgs));
		Iterator<String> iterator = args.iterator();
		while (iterator.hasNext())
		{
			String current = iterator.next();
			if (current.contains(":"))
			{
				iterator.remove();
			}
			// other operations
		}

		for (Entry<Argument, Number> entry : this.sorted.entrySet())
		{
			if (entry.getKey() instanceof GroupArgument)
			{

				if (((GroupArgument) entry.getKey()).getArgs().size() > args.size())
				{
					continue; // Don't bother dealing with the group, it doesn't fit!!!
				}

				for (int i = 0; i < ((GroupArgument) entry.getKey()).getArgs().size(); i++)
				{
					Argument tempArg = ((GroupArgument) entry.getKey()).getArgs().get(i);

					Matcher stringMatcher = stringPattern.matcher(args.get(i + this.position));
					Matcher numberMatcher = numberPattern.matcher(args.get(i + this.position));

					if (numberMatcher.matches() && this.isNumber(tempArg))
					{
						this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
					}
					else if (this.isBoolean(args.get(i + this.position)) && entry.getKey().getType() == ParamType.BOOLEAN)
					{
						this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
					}
					else if (this.isChar(args.get(i + this.position)) && entry.getKey().getType() == ParamType.CHAR)
					{
						this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
					}
					else if (stringMatcher.matches() && entry.getKey().getType() == ParamType.STRING)
					{
						this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
					}
					else
					{
						this.sorted.put(entry.getKey(), entry.getValue().intValue());
					}

					if (entry.getValue().intValue() > largest)
					{
						largest = entry.getValue().intValue();
						mostMatchingArg = entry.getKey();
					}
				}
			}
			else
			{
				int i = this.getPosition();

				Argument tempArg = entry.getKey();

				Matcher stringMatcher = stringPattern.matcher(Parser.formatString(args.get(i)));
				Matcher numberMatcher = numberPattern.matcher(Parser.formatNumberAsString(args.get(i)));

				if (numberMatcher.matches() && this.isNumber(tempArg))
				{
					this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
				}
				else if (this.isBoolean(args.get(i)) && entry.getKey().getType() == ParamType.BOOLEAN)
				{
					this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
				}
				else if (this.isChar(args.get(i)) && entry.getKey().getType() == ParamType.CHAR)
				{
					this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
				}
				else if (stringMatcher.matches() && entry.getKey().getType() == ParamType.STRING)
				{
					this.sorted.put(entry.getKey(), entry.getValue().intValue() + 1);
				}
				else
				{
					this.sorted.put(entry.getKey(), entry.getValue());
				}

				if (entry.getValue().intValue() > largest)
				{
					largest = entry.getValue().intValue();
					mostMatchingArg = entry.getKey();
				}
			}
			this.sorted.put(entry.getKey(), 0);
		}

		largest = 0;

		for (Entry<Argument, Number> entry : this.sorted.entrySet())
		{
			this.sorted.put(entry.getKey(), 0);
		}

		return mostMatchingArg;
	}

	private boolean isChar(String arg)
	{
		return arg.length() == 1;
	}

	private boolean isBoolean(String arg)
	{
		if (arg.equals("true") || arg.equals("false") || arg.equals("1") || arg.equals("0"))
		{
			if (arg.equals("1"))
			{
				arg = "true";
			}
			else if (arg.equals("0"))
			{
				arg = "false";
			}
			return true;
		}
		return false;
	}

	private boolean isNumber(Argument arg)
	{
		return arg.getType() == ParamType.BYTE || arg.getType() == ParamType.DOUBLE || arg.getType() == ParamType.FLOAT || arg.getType() == ParamType.INT || arg.getType() == ParamType.LONG || arg.getType() == ParamType.SHORT;
	}

	@Override
	public ParamType getType()
	{
		return ParamType.OR;
	}
}
