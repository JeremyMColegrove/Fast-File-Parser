# FFP Programming Language

FFP is a programming language designed to read like natural language, similar to SQL, and is primarily used for processing and parsing files. 
It is implemented in Java and distributed as an installable .exe file.

## Usage

### If you are looking to install FFP, you can [download it here.](https://github.com/JeremyMColegrove/FFP/releases)

### Run File
To run a FFP script, use the following command:
```textmate
ffp <file.ffp>
```


For example:
```textmate
ffp myscript.ffp
```

### Run Code
To run code directly in the terminal, you can use the following command:
```textmate
ffp --code "<your code>"
```
### Help Command

To display the help message, use any of the following commands:
```textmate
ffp --help
ffp -help
ffp
```

## Keywords

FFP supports the following keywords:
```textmate
SET, READ, WRITE, APPEND, FOR, ENDFOR, SPLIT, BY, IF, ENDIF,
FROM, TO, AS, DO, CONTAINS, MATCHES, REPLACE, PRINT, JOIN,
TRIM, UPPERCASE, LOWERCASE, SUBSTRING, SORT, LENGTH, THEN, SLEEP,
IN, EQUALS, NOT, ELSE, COMMA, REVERSE, NUMBER, STRING, ARRAY.
```
It is highly recommended to use the official VS Code extension, which provides snippets for all of the commands, so you can learn the syntax.

## Comments

Comments in FFP are denoted by the `#` symbol. Any text following `#` on the same line is considered a comment and is ignored by the interpreter.

```textmate
# This is a comment
SET x TO "Hello"
```


## Structure

### Data Types
FFP only consists of 3 data types: `STRING`, `NUMBER`, `ARRAY`.
It only supports integer arithmetic, no floating point numbers or silly decimals.
Strings are defined with either double quotes or single quotes. 
There is no quote escaping yet (\"), but that is on the roadmap.

### Arrays
You can slice arrays by using the SUBSTRING keyword, like this:
```textmate
PRINT SUBSTRING [1, 2, 3, 4] FROM 0 TO 2 #prints [1, 2]
```
Optionally, you can append items to the end of your arrays by using the APPEND keyword, like so:
```textmate
SET x TO []
APPEND 1 TO x
PRINT x #prints [1]
```

### CAST

Casting data types in FFP are written as follows:
```textmate
SET x TO "10"
PRINT x AS NUMBER   # prints 10
PRINT x AS ARRAY    # prints ["1", "0"]
PRINT 10 AS STRING  # prints "10"
```

You can cast strings to arrays and arrays to strings, like this:
```textmate
PRINT ["a", "b", "c"] AS STRING #prints "abc"
PRINT "abc" AS ARRAY #prints ["a", "b", "c"]
```

### IF/ELSE

Conditional statements in FFP are written as follows:
```textmate
IF condition THEN
    # Then code
ELSE
    # Else code
ENDIF
```

Example:
```textmate
SET x TO 10
IF x EQUALS 10 THEN
    PRINT "x is 10"
ELSE
    PRINT "x is not 10"
ENDIF
```

### FOR Loops

#### Iteration

To iterate over elements in an array:
```textmate
FOR item IN collection DO
    # code
ENDFOR
```

Example:
```textmate
READ "input.txt" TO data
SPLIT data BY "\n" TO lines

FOR line IN lines DO
PRINT line
ENDFOR
```


#### Range

To iterate over a range of numbers:
```textmate
FOR i FROM start TO end DO
    # code
ENDFOR
```

Example:

```textmate
FOR i FROM 0 TO 10 DO
    PRINT i
ENDFOR
```
### SLEEP

To make the program sleep by a certain number of milliseconds:

```textmate
SLEEP 3000 #ms
```
### SPLIT

To split a string by a delimiter:

```textmate
SPLIT variable BY delimiter TO newVariable
```

Example:
```textmate
SET data TO "one,two,three"
SPLIT data BY "," TO parts
```

### SET

To assign a value to a variable:

```textmate
SET variable TO value
```

Example:

```textmate
SET message TO "Hello, world!"
```
