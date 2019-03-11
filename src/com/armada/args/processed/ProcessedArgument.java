package com.armada.args.processed;

import com.armada.args.Argument;
import com.armada.enumerations.ParamType;

/**
 * A processed version of the {@link Argument} class.
 * 
 * @author Arpaesis
 *
 * @param <T> The generic value of the processed argument.
 */
public class ProcessedArgument<T> extends Argument {

    private final T value;
    private final String rawValue;

    public ProcessedArgument(String argName, ParamType type, String rawValue, T value) {
	super(argName, type);
	this.rawValue = rawValue;
	this.value = value;
    }

    public ProcessedArgument(String rawValue, T value) {
	this.rawValue = rawValue;
	this.value = value;
    }

    /**
     * @return The value contained within the processed argument.
     */
    public T getValue() {
	return value;
    }

    /**
     * @return The raw value of the argument prior to processing.
     */
    public String getRawValue() {
	return rawValue;
    }
}
