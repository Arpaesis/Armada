package com.ccl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandImpl implements Command<DataInterface>
{

	public final List<ParamType> requiredParams = new ArrayList<>();
	public final List<ParamType> optionalParams = new ArrayList<>();

	private String name = "";
	private String help = "";
	private String[] aliases;
	
	private int cooldown = 0;
	private long lastUsage = 0;
	
	private int timesUsed = 0;
	
	private boolean shouldExecute = true;

	public CommandImpl()
	{
	}
	
	public void onExecute(DataInterface obj, String[] in)
	{
	}

	@Override
	public void execute(DataInterface obj, String in)
	{
		this.processInput(in);
		
		if(this.shouldExecute && this.isCooldownReady())
		{
			String[] temp = in.split(" ");
			String[] rawArgs = Arrays.copyOfRange(temp, 1, temp.length);
			this.onExecute(obj, rawArgs);
			
			timesUsed++;
			System.out.println(this.timesUsed);
			this.lastUsage = System.currentTimeMillis();
		}else {
			//TODO: Handle the error in executing the command here.
		}
		
		// reset the command for usage.
		this.shouldExecute = true;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public String getHelp()
	{
		return help;
	}

	public void setHelp(String help)
	{
		this.help = help;
	}

	public boolean isShouldExecute()
	{
		return shouldExecute;
	}

	public String[] getAliases()
	{
		return aliases;
	}

	public void setAliases(String[] aliases)
	{
		this.aliases = aliases;
	}

	public int getCooldown()
	{
		return cooldown;
	}

	public void setCooldown(int cooldown)
	{
		this.cooldown = cooldown;
	}
	
	public boolean isCooldownReady()
	{
		long currentTime = System.currentTimeMillis();

		if (((currentTime - lastUsage) / 1000) >= this.cooldown)
		{
			return true;
		}

		return false;
	}

	private void processInput(String input)
	{
		String[] rawInput = input.split(" ");
		String[] rawArgs = Arrays.copyOfRange(rawInput, 1, rawInput.length);

		for (int i = 0; i < rawArgs.length; i++)
		{
			if(!this.shouldExecute || i > requiredParams.size() - 1)
			{
				break;
			}else if(rawArgs.length < requiredParams.size())
			{
				this.shouldExecute = false;
				break;
			}
			
			switch(requiredParams.get(i))
			{
			case BOOLEAN:
				if(rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0"))
				{
					break;
				}
				else {
					this.shouldExecute = false;
					break;
				}
			case BYTE:
				try { Byte.parseByte(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			case CHAR:
				if(rawArgs[i].length() >= 2)
				{
					this.shouldExecute = false;
				}
				break;
			case DOUBLE:
				try { Double.parseDouble(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			case FLOAT:
				try { Float.parseFloat(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			case INT:
				try { Integer.parseInt(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				
				break;
			case LONG:
				try { Long.parseLong(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			case SHORT:
				try { Short.parseShort(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			default:
				break;
			}
		}
		
		for (int i = this.requiredParams.size() - 1; i < rawArgs.length - 1 && rawArgs.length - requiredParams.size() != 0; i++)
		{
			if(!this.shouldExecute || i > (optionalParams.size() + requiredParams.size() - 1))
			{
				break;
			}
			
			switch(optionalParams.get(rawArgs.length - i - 2))
			{
			case BOOLEAN:
				if(rawArgs[i].equals("true") || rawArgs[i].equals("false") || rawArgs[i].equals("1") || rawArgs[i].equals("0"))
				{
					break;
				}
				else {
					this.shouldExecute = false;
					break;
				}
			case BYTE:
				try { Byte.parseByte(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			case CHAR:
				if(rawArgs[i].length() >= 2)
				{
					this.shouldExecute = false;
				}
				break;
			case DOUBLE:
				try { Double.parseDouble(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			case FLOAT:
				try { Float.parseFloat(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			case INT:
				try { Integer.parseInt(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				
				break;
			case LONG:
				try { Long.parseLong(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			case SHORT:
				try { Short.parseShort(rawArgs[i]); } catch(Exception e) { this.shouldExecute = false;}
				break;
			default:
				break;
			}
		}
	}
}
