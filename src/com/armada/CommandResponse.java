package com.armada;

public interface CommandResponse<T>
{

	public abstract boolean onResponse(T inputType, String input);
}
