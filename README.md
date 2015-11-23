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
You can either write a program to a file and run it from said file with "java -jar Vitsy.jar <filename> [arg 1] [arg 2] ..." or directly from the command line with "java -jar Vitsy.jar --code '<program>' [arg 1] [arg 2] ..."'
You can force values to be parsed as string input with "java -jar Vitsy.jar <filename | --code '<program>'> -v [num 1] [num 2] ..."
Note that an empty program will print out the lyrics to 99 bottles of beer. c:

### Wrap Nature
A program will only loop given that

- The last character on the line is a skip.
- The program is currently in either type of loop.
- It is capturing a string.

### Basic Syntax
 - input: i
    - push the top item of the input stack as an integer to the program stack
 - input length: I
    - push the length of the input stack to the top of the program stack
 - wait: w
    - wait the top item of stack in seconds.
 - ifnot: )
    - if the top item of the stack, post integer truncation, is not zero, do the next operation. Otherwise, skip it.
    - If an ifnot statement is found before a [, it will execute all information in [] if and only if the top item is not zero. 
 - if: (
    - if the top item of the stack, post integer truncation, is equal to zero, do the next operation. Otherwise, skip it.
    - If an if statement is found before a [, it will execute all information in [] if and only if the top item is zero. 
 - skip: !
    - skip the next operation.
 - loop: [
    - begin a loop.
 - end loop: ]
    - End a loop only if the top item of the stack is zero. If this is called ouside of a loop it is treated as a NOP.
    - If the top item of the stack is not zero, return to the beginning of the loop.
 - sine: S
    - Use the top value of the stack as input for a sine function, replacing said top value (in radians).
 - asine: s
    - Use the top value of the stack as input for an asine function, replacing said top value (in radians).
 - cosine: C
    - Use the top value of the stack as input for a cosine function, replacing said top value (in radians).
 - acosine: A
    - Use the top value of the stack as input for an acosine function, replacing said top value (in radians).
 - tangent: T
    - Use the top value of the stack as input for a tangent function, replacing said top value (in radians).
 - atangent: t
    - Use the top value of the stack as input for an atangent function, replacing said top value (in radians).
 - pi: P
    - Push a decimal approximation of pi to the top of the stack.
 - e: E
    - Push a decimal approximation of e to the top of the stack.
 - log: L
    - Take the log of the second-to-top item of the stack with a base of the top item of the stack.
 - outnum: N
    - Output the top number of the stack as a float.
 - outchar: O
    - Output the top number of the stack as a character (truncated first) with ISO-8859-1 encoding.
 - rand: R
    - Get a random number between 0 and the top item of the stack.
 - end: ;
    - End execution.
 - printall: Z
    - Print out the entire current stack as characters. Equivalent to 'l\O'
 - getall: z
    - Get all the input of the input stack and push it to the stack. Equivalent to 'I\i'
 - teleport: #
    - Teleport to the 1-D coordinates of the top item of the stack.
 - length: l
    - Get the length of the stack and push it to the top of the stack.
 - repeatnextchar: \\
    - Repeat the next operation as many times as the truncated top item of the stack.
 - for loop: \\[ (relative to direction, if moving leftward, use [\)
    - Repeat the operations encapsulated in the [] for the number of times specified by the top value of the stack.
 - reverse: r
    - Reverse the stack.
 - rotateright: }
    - Rotate the stack to the right once.
 - rotateleft: {
    - Rotate the stack to the left once.
 - duplicate: D
    - Duplicate the top item of the stack.
 - part: @
    - Get the top value of the stack's integer value and get its value's index, replacing the top value of the stack with said index.
 - changestack: ?
    - Shift one stack to the right - note that this will wrap around, so the 2-dimensionality of the stacks are... finicky.
 - clnstack: :
    - Clone the current stack, creating a new one and pushing all of the contents of the current stack to it. You will shift to this stack after this operation.
 - newstack: &
    - Create an empty stack.
 - rmstack: Y
    - Remove the current stack. This will shift you one stack to the right (if it exists).
 - numstack: y
    - Get the current number of stacks.
 - tempvar: v
    - If there is no value in tempvar, set the top item of the stack to it. Otherwise, push the tempvar value to the stack and clear the value of tempvar.
 - globalvar: V
    - If there is no value in globalvar, set the top item of the stack to it. Otherwise, push the globalvar value to the stack.
 - remove: X
    - Remove the top item of the current stack.
 - add: +
    - Add the top two items of the stack.
 - subtract: -
    - Subtract the top item of the stack by the second-to-top item of the stack.
 - multiply: *
    - Multiply the top two items of the stack.
 - divide: /
    - Divide the top item of the stack by the second-to-top item of the stack.
 - equal: =
    - If the top two items of the stack are equal, chop off the top item of the stack and replace it with a zero.
    - If they are not equal, chop off the top item of the stack and replace it with a 1.
 - modulo: M
    - Modulo the top item of the stack by the second-to-top item of the stack.
 - power: ^
    - Put the second-to-top item of the stack to the power of the top item of the stack.
 - prime: p
    - If the top item of the stack is prime, push 1. Otherwise, push 0.
 - method: m
    - Go to the line in the source code file as defined by the truncated top item of the stack.
 - object: o
    - Push the current stack to an object referenced by the following character and remove it from the program stack. This will not override commands.
    - Once the aforementioned character is in the instruction pointer again, it will push the object's stack to the program stack.
    - Any character that is instantiated will draw the program's stack again if it has no object stack.'
 - file: \`
    - Pop the entire current stack as a string and open the file with that string name, pushing its contents to the stack.
 - changedir: |
    - Reverse the direction of the instruction pointer.
 - go left: <
    - Turn the instruction pointer to the left.
 - go right: >
    - Turn the instruction pointer to the right.
 - randdir: x
    - Turn the instruction pointer to a randomly selected left or right.
 - nothing:  
    - Anything undefined is a NOP.
 - quote: " or '
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
