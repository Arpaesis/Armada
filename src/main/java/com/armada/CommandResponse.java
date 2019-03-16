package com.armada;

/**
 * A special interface used in handling command responses.
 * 
 * @author Arpaesis
 *
 * @param <T>
 *            The type of object to be used in the response.
 */
public interface CommandResponse<T>
{

	boolean onResponse(T inputType, String input);
}
