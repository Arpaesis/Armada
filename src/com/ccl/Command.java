package com.ccl;

public interface Command<T extends DataInterface>
{
	void execute(T obj, String in);
}
