# Cool Com Library
The Cool Com Library is a library designed in Java 11 to make the creation and management of commands easier. The library simplifies handling arguments, has branching, and many more features.

## About
The library was designed in response to the unscalable design of command parsing with libraries such as the Discord API's Java wrappers JDA and Discord4j. Not only was it incredibly messy to manage command arguments, it was also nearly impossible to manage command branches without creating massive chains of logical checks and an incredibly ugly amount of parsing the arguments.

Not only does it solve the aforementioned problems above, but the library was also designed with certain design patterns in mind, such as if a command's execution needs to return data at the end, which the library allows for.

## Features
Below are some of the features that the library offers:

### Command Manager
* A ``CommandManager`` class to register and manage commands.

### Commands
* Set a name for commands.
* Commands can take on multiple names (aliases).
* Allows for code to be executed based on if the command failed or succeeded.

### Responses
* Commands support responses; commands can execute code based on the later desired input.

### Parsing
* Supports all primitive types (boolean, byte, char, double, float, int, long, short).
* Supports mult-word strings.
* Support for numbers as binary.
* Support for numbers as hexadecimal.
* Support for numbers as octal.
* Supports optional arguments.
* Supports grouping arguments.
* Supports continuous arguments.
* Supports command branching (Also known as "or" arguments. Commands can take on multiple argument structures).

### Categories

### Scheduling
* Built-in scheduler and scheduler command. Commands can be scheduled to execute at a specific interval a specific number of times (even infinite).

## Credits
* bright_spark#9873 on discord for numerous suggestions to improve the library's parsing and overall design.
* SizableShrimp#0755 for his suggestion on returning command executions.
