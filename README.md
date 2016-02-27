# Vitsy
Vitsy Programming Language, WIP

### Basic Info
Vitsy is a 1-D (forcedly so) stack-based language (WIP) that defines itself with basic stack functions (which I will be adding onto at a later time).
It executes lengthwise with an instruction pointer.

### Naming
Vitsy comes from a common shortening for my alias, VTC, pronounced as a word.

### VitsySafe
This jar file exists for servers with online interpreters. It is the 'safe mode' jar.

### Command Line Usage
You can either write a program to a file and run it from said file with `java -jar Vitsy.jar [--verbose] <filename> [arg 1] [arg 2] ...` or directly from the command line with `java -jar Vitsy.jar [--verbose] --code '<program>' [arg 1] [arg 2] ...`.
You can force values to be parsed as string input with `java -jar Vitsy.jar [--verbose] <filename | --code '<program>'> -v [num 1] [num 2] ...`.
Note that an empty program will print out the lyrics to 99 bottles of beer. c:

### Conversion
To convert a Vitsy program to/from verbose, add "--convert" to the command line arguments. Using "--verbose" will convert a verbose program to golfed, but the default (no extra flags) is the golfed to verbose.

### Wrap Nature
A program will only loop given that

- The last character on the line is a skip.
- The program is currently in any type of recursion (including if statements).
- The program is capturing a string.

### `;e` and `;u`
- When you declare `;e`, the text following `;ext` (with a space afterward) will be declared this class's superclass. This is important for use of the `K` command.
- When you declare `;u`, the text following `;use` (with a space afterward) will be declared as used by this class. This is important for use of the `k` command.
- Disabled in Safe mode.

Note that these can extend to the StackOverflow limit.

### Verbose Mode
- Verbose Mode is enabled through the command line with the `--verbose` flag.
- Basic Syntax:
   - Separate every command with a semicolon.
   - Separate every method index with a colon. (as you would separate every method in the golfed mode with a newline)
   - For details on individual commands, please see Converter.java

### Invalid Indexing
- Anything that is detected as causing a possible IndexOutOfBoundsException (or a subexception thereof) will be caught, with multiple filler 0s being pushed to the stack to compensate, then the operation continues.
   - An example of this is calling `-` on an empty stack: it will first detect that there is nothing to subtract from, and then push a zero, then detect that there is nothing subtracting, which pushes another 0, then carries out the subtraction, leaving 0 on the stack.

### Basic Syntax
 - input: `i`
    - push the top item of the input stack as an integer to the program stack
 - input length: `I`
    - push the length of the input stack to the top of the program stack
 - wait: `w`
    - wait the top item of stack in seconds.
 - ifnot: `)`
    - if the top item of the stack, post integer truncation, is not zero, do the next operation. Otherwise, skip it.
    - If an ifnot statement is found before a `[`, it will execute all information in `[...]` if and only if the top item is not zero. 
 - if: `(`
    - if the top item of the stack, post integer truncation, is equal to zero, do the next operation. Otherwise, skip it.
    - If an if statement is found before a `[`, it will execute all information in `[...]` if and only if the top item is zero. 
 - loop: `[`
    - begin a loop.
 - end loop: `]`
    - End a loop only if the top item of the stack is zero (and pop the zero). If this is called ouside of a loop it is treated as a NOP.
    - If the top item of the stack is not zero, return to the beginning of the loop (but do not pop the top value).
 - sine: `S`
    - Use the top value of the stack as input for a sine function, replacing said top value (in radians).
 - asine: `s`
    - Use the top value of the stack as input for an asine function, replacing said top value (in radians).
 - cosine: `C`
    - Use the top value of the stack as input for a cosine function, replacing said top value (in radians).
 - acosine: `A`
    - Use the top value of the stack as input for an acosine function, replacing said top value (in radians).
 - tangent: `T`
    - Use the top value of the stack as input for a tangent function, replacing said top value (in radians).
 - atangent: `t`
    - Use the top value of the stack as input for an atangent function, replacing said top value (in radians).
 - pi: `P`
    - Push a decimal approximation of pi to the top of the stack.
 - e: `E`
    - Push a decimal approximation of e to the top of the stack.
 - log: `L`
    - Take the log of the second-to-top item of the stack with a base of the top item of the stack.
 - outnum: `N`
    - Output the top number of the stack as a float.
 - outchar: `O`
    - Output the top number of the stack as a character (truncated first) with ISO-8859-1 encoding.
 - rand: `R`
    - Get a random number between 0 and the top item of the stack.
 - end: `;`
    - End execution of the current method or break out of a current while loop.
 - printall: `Z`
    - Print out the entire current stack as characters. Equivalent to `l\O`.
 - getall: `z`
    - Get all the input of the input stack and push it to the stack. Equivalent to `I\i`.
 - prompt: `W`
    - Get a line of STDIN. If there is no line input into the program, pause the program until another input line is input.
 - teleport: `#`
    - Teleport to the 1-D coordinates of the top item of the stack.
 - length: `l`
    - Get the length of the stack and push it to the top of the stack.
 - repeatnextchar: `\`
    - Repeat the next operation as many times as the truncated top item of the stack.
 - for loop: `\[` (relative to direction, if moving leftward, use `[\`)
    - Repeat the operations encapsulated in the `[...]` for the number of times specified by the top value of the stack.
 - reverse: `r`
    - Reverse the stack.
 - singswitch: `$`
    - Switch the top two items of the stack.
 - multiswitch: `%`
    - Rotate the top top items of the stack.
 - rotateright: `}`
    - Take the bottom item of the stack to the top.
 - rotateleft: `{`
    - Take the top item of the stack to the bottom.
 - duplicate: `D`
    - Duplicate the top item of the stack.
 - part: `@`
    - Get the top value of the stack's integer value and get its value's index, replacing the top value of the stack with said index.
 - rightstack: `?`
    - Shift one stack to the right - note that this will wrap around, so the 2-dimensionality of the stacks are... finicky.
 - leftstack: `|`
    - Shift one stack to the left. This will also wrap around.
 - clnstack: `:`
    - Clone the current stack, creating a new one and pushing all of the contents of the current stack to it. You will shift to this stack after this operation.
 - newstack: `&`
    - Create an empty stack.
 - rmstack: `Y`
    - Remove the current stack. This will shift you one stack to the right (if it exists).
 - numstack: `y`
    - Get the current number of stacks.
 - flatten: `u`
    - Push the contents of the top stack to the previous stack, removing the top stack in the process.
    - Note that if there is only one stack, it push the contents of itself to itself, then remove itself, acting like rmstack.
 - tempvar: `v`
    - If there is no value in tempvar, set the top item of the stack to it. Otherwise, push the tempvar value to the stack and clear the value of tempvar.
 - globalvar: `V`
    - If there is no value in globalvar, set the top item of the stack to it. Otherwise, push the globalvar value to the stack.
 - remove: `X`
    - Remove the top item of the current stack.
 - add: `+`
    - Add the top two items of the stack.
 - subtract: `-`
    - Subtract the top item of the stack by the second-to-top item of the stack.
 - multiply: `*`
    - Multiply the top two items of the stack.
 - divide: `/`
    - Divide the top item of the stack by the second-to-top item of the stack.
 - equal: `=`
    - If the top two items of the stack are equal, chop off the top item of the stack and replace it with a zero.
    - If they are not equal, chop off the top item of the stack and replace it with a 1.
 - modulo: `M`
    - Modulo the top item of the stack by the second-to-top item of the stack.
 - int: `_`
    - Set the top item of the stack to the top item with the fractional portion removed.
 - factorize: `h`
    - Push the prime factors of top item of the stack to the stack.
 - range: `H`
    - Push the range from the second-to-top item to the top item of the stack to the stack.
 - power: `^`
    - Put the second-to-top item of the stack to the power of the top item of the stack.
 - prime: `p`
    - If the top item of the stack is prime, push 1. Otherwise, push 0.
 - method: `m`
    - Go to the line in the source code file as defined by the truncated top item of the stack.
 - object: `o`
    - Push the current stack to an object referenced by the following character and remove it from the program stack. This will not override commands.
    - Once the aforementioned character is in the instruction pointer again, it will push the object's stack to the program stack.
    - Any character that is instantiated will draw the program's stack again if it has no object stack.
 - classmethod: `k`
    - Pop the top two items of the stack. Using the second-to-top item, get the index of the `;u` declaration and load that class. Then execute the top item line of code.
    - If the second to top item is -1, it will execute the specified line of code in the current program.
    - If the second to top item is -2, it will execute the specified line of code in the superclass.
    - Disabled in Safe mode.
 - super: `K`
    - Do the current line of code of the superclass (as defined in the `;e` declaration).
    - Disabled in Safe mode.
 - usecount: `g`
    - Count the amount of use declarations that are valid and push it to the stack.
    - Disabled in Safe mode.
 - classname: `G`
    - Get the name of the class as indexed by the top item of the stack.
    - If the reference index is -1, it will return the name of the current class.
    - If the reference index is -2, it will return the name of the superclass.
    - Disabled in Safe mode.
 - file: <code>\`</code>
    - Pop the entire current stack as a string and open the file with that string name, pushing its contents to the stack.
    - Disabled in Safe mode.
 - write: `.`
    - Pop the current stack and use that as a file name, then pop stack to the right and use that as contents of the file.
    - Disabled in Safe mode.
 - shell: `,`
    - Pop the current stack and use that as a command for use in the system terminal, then push the results of that terminal process to the current stack.
    - Disabled in Safe mode.
 - eval: `n`
    - Pop the current stack and evaluate the string equation, pushing the result to the current stack. If unevaluable, it will push NaN.
 - exitnow: `x`
 - go left: `<`
    - Turn the instruction pointer to the left.
 - go right: `>`
    - Turn the instruction pointer to the right.
 - nothing:  
    - Anything undefined is a NOP.
 - quote: `"` or `'`
    - Double or single quotes begin to parse code as a string until encountering another quote.

### Future Updates
I plan to include indexing of a stack (picking a specific item), removing approximations of complex numbers (and making them symbolic), and any more suggested changes.


### License
Copyright (c) 2015, Addison Crump

 All rights reserved.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:
Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
in the documentation and/or other materials provided with the distribution.
Neither the name of the <ORGANIZATION> nor the names of its contributors may be
used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
