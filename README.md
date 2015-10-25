# Vitsy
Vitsy Programming Language, WIP

### Basic Info
Vitsy is a 1-D (forcedly so) stack-based language (WIP) that defines itself with basic stack functions (which I will be adding onto at a later time).
It executes lengthwise with an instruction pointer.

### Naming
Vitsy comes from a common shortening for my alias, VTC, pronounced as a word.

### Command Line Usage
You can either write a program to a file and run it from said file with "java -jar Vitsy.jar <filename> [arg 1] [arg 2] ..." or directly from the command line with "java -jar Vitsy.jar --code '<program>' [arg 1] [arg 2] ..."'
You can also add values directly to the program stack with "java -jar Vitsy.jar <filename | --code '<program>'> -v [num 1] [num 2] ..."

### Basic Syntax
 - input: i
    - push the top item of the input stack as an integer to the program stack
 - input length: I
    - push the length of the input stack to the top of the program stack
 - wait: w
    - wait the top item of stack in seconds.
 - ifnot: )
    - if the top item of the stack, post integer truncation, is not zero, do the next operation. Otherwise, skip it.
 - if: (
    - if the top item of the stack, post integer truncation, is equal to zero, do the next operation. Otherwise, skip it.
 - skip: !
    - skip the next operation.
 - loop: [
    - begin a loop.
 - end loop: ]
    - End a loop only if the top item of the stack is zero. If this is called ouside of a loop it will throw an "Unknown Character: ]" error.
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
    - Rotate the stack to the right as the top item of the stack times.
 - rotateleft: {
    - Rotate the stack to the left as the top item of the stack times.
 - duplicate: D
    - Duplicate the top item of the stack.
 - part: p
    - Get the top value of the stack's integer value and get its value's index, replacing the top value of the stack with said index.'
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
 - changedir: |
    - Reverse the direction of the instruction pointer.
 - go left: <
    - Turn the instruction pointer to the left.
 - go right: >
    - Turn the instruction pointer to the right.
 - randdir: x
    - Turn the instruction pointer to a randomly selected left or right.
 - nothing:  
    - Space is a NOP.
 - quote: "
    - Double quotes begin to parse code as a string until encountering another quote.
 - err: anything else
    - Any heretofore undefined characters will cause an "Unknown Character: " error.

### Future Updates
I plan to include indexing of a stack (picking a specific item), removing approximations of complex numbers (and making them symbolic), and any more suggested changes.
