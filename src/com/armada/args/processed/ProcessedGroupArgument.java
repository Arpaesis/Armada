package com.armada.args.processed;

import java.util.List;

import com.armada.args.Arguments;
import com.armada.args.GroupArgument;
import com.armada.enumerations.ParamType;

/**
 * A processed version of the {@link GroupArgument} class. Functions similarly
 * to the {@link Arguments} class.
 * 
 * @author Arpaesis
 *
 */
public class ProcessedGroupArgument extends ProcessedArgument<Object> {

    private List<ProcessedArgument<Object>> arguments;
    private String[] rawValues;

    private int counter = 0;

    public ProcessedGroupArgument(String argName, ParamType type, String[] rawValues,
	    List<ProcessedArgument<Object>> values) {
	super(argName, type, argName, null); // TODO: Solve how to not pass in null here.
	this.rawValues = rawValues;
	this.arguments = values;
    }

    /**
     * @return The collection of {@link ProcessedArgument}s within the processed
     *         group argument.
     */
    public List<ProcessedArgument<Object>> getValues() {
	return arguments;
    }

    /**
     * @return The raw values of the {@link ProcessedArgument}s.
     */
    public String[] getRawValues() {
	return rawValues;
    }

    /**
     * @return The next boolean in the processed group argument.
     */
    public boolean getBoolean() {
	return (boolean) arguments.get(counter++).getValue();
    }

    /**
     * @return The next byte in the processed group argument.
     */
    public byte getByte() {
	return ((Number) arguments.get(counter++).getValue()).byteValue();
    }

    /**
     * @return The next char in the processed group argument.
     */
    public char getCharacter() {
	return (char) arguments.get(counter++).getValue();
    }

    /**
     * @return The next double in the processed group argument.
     */
    public double getDouble() {
	return ((Number) arguments.get(counter++).getValue()).doubleValue();
    }

    /**
     * @return The next float in the processed group argument.
     */
    public float getFloat() {
	return ((Number) arguments.get(counter++).getValue()).floatValue();
    }

    /**
     * @return The next int in the processed group argument.
     */
    public int getInt() {
	return ((Number) arguments.get(counter++).getValue()).intValue();
    }

    /**
     * @return The next long in the processed group argument.
     */
    public long getLong() {
	return ((Number) arguments.get(counter++).getValue()).longValue();
    }

    public short getShort() {
	return ((Number) arguments.get(counter++).getValue()).shortValue();
    }

    /**
     * @return The next String in the processed group argument.
     */
    public String getString() {
	return ((String) arguments.get(counter++).getValue());
    }

    /**
     * Fetches a boolean argument in the processed group argument with the given
     * tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return A boolean value from the argument with the game given tag name.
     */
    public boolean getBooleanFor(String tag, boolean def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().matches(tag)) {
		return (boolean) argument.getValue();
	    }
	}
	return def;
    }

    /**
     * Fetches a byte argument in the processed group argument with the given tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return A byte value from the argument with the game given tag name.
     */
    public byte getByteFor(String tag, byte def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().matches(tag)) {
		return ((Number) argument.getValue()).byteValue();
	    }
	}
	return def;
    }

    /**
     * Fetches a char argument in the processed group argument with the given tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return A char value from the argument with the game given tag name.
     */
    public char getCharacterFor(String tag, char def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().matches(tag)) {
		return (char) argument.getValue();
	    }
	}
	return def;
    }

    /**
     * Fetches a double argument in the processed group argument with the given tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return A double value from the argument with the game given tag name.
     */
    public double getDoubleFor(String tag, double def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().matches(tag)) {
		return ((Number) argument.getValue()).doubleValue();
	    }
	}
	return def;
    }

    /**
     * Fetches a float argument in the processed group argument with the given tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return A float value from the argument with the game given tag name.
     */
    public float getFloatFor(String tag, float def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().matches(tag)) {
		return ((Number) argument.getValue()).floatValue();
	    }
	}
	return def;
    }

    /**
     * Fetches a int argument in the processed group argument with the given tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return An int value from the argument with the game given tag name.
     */
    public int getIntFor(String tag, int def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().matches(tag)) {
		return ((Number) argument.getValue()).intValue();
	    }
	}
	return def;
    }

    /**
     * Fetches a long argument in the processed group argument with the given tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return A long value from the argument with the game given tag name.
     */
    public long getLongFor(String tag, long def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().matches(tag)) {
		return ((Number) argument.getValue()).longValue();
	    }
	}
	return def;
    }

    /**
     * Fetches a short argument in the processed group argument with the given tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return A short value from the argument with the game given tag name.
     */
    public short getShortFor(String tag, short def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().matches(tag)) {
		return ((Number) argument.getValue()).shortValue();
	    }
	}
	return def;
    }

    /**
     * Fetches a short argument in the processed group argument with the given tag.
     * 
     * @param tag The tag to search with.
     * @param def The default value to fall back on if the argument could not be
     *            found.
     * @return A short value from the argument with the game given tag name.
     */
    public String getStringFor(String tag, String def) {
	for (ProcessedArgument<?> argument : arguments) {
	    if (argument.getRawValue().contains(tag)) {
		return (String) argument.getValue();
	    }
	}
	return def;
    }

    /**
     * Sets the @{link ProcessedArgument} of the processed group argument to the
     * specified values.
     * 
     * @param values The processed arguments to be put into the processed group
     *               argument.
     */
    public void setValues(List<ProcessedArgument<Object>> values) {
	this.arguments = values;
    }
}