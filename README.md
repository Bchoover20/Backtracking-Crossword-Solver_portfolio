# CS 1501 – Algorithm Implementation – Assignment #1 

*This assignment is adapted from Dr. John Ramirez’s CS 1501 class.*

Due: Friday September 29th @ 11:59pm on Gradescope

Late submission deadline: Sunday October 1st @11:59pm with 10% penalty per late day

You should submit the Java file `Crossword.java` to GradeScope (the link is on Canvas). You must also submit a writeup named `a1.md` and an Assignment Information Sheet `InfoSheet.md` as described below.

## Table of Contents

- [Overview](#overview)
- [Background](#background)
- [TASK 1 - Finding a Single Solution](#TASK-1---Finding-a-Single-Solution)
- [TASK 2 - Checking if a Solution is Correct](#TASK-2---Checking-if-a-Solution-is-Correct)
- [Important Notes](#important-notes)
- [Writeup](#writeup)
- [Submission Requirements](#submission-requirements)
- [Rubrics](#rubrics)

## Overview
 
* __Purpose__:  To implement a backtracking algorithm that finds one legal filling of the squares of a given crossword puzzle as specified in detail below.

You will define a class named `Crossword` that implements `WordPuzzleInterface` defined in `WordPuzzleInterface.java`. The interface has two methods that you will have to define in `Crossword.java`:

```java
/**
 * An interface for operations on a word puzzle
 */
public interface WordPuzzleInterface {

    /*
     * Fill out a word puzzle defined by an empty board. 
     * 
     *  @param board is a 2-d array representing the empty board to be filled
     *  The characters in the empty board can be:
     *    '+': any letter can go here
     *    '-': no letter is allowed to go here
     *     a letter: this letter has to remain as-is at the same position in the filled puzzle
     *     a value between '1' and '9': any letter can go here and the provided value is an upper bound on the number 
     *                                  of times the letter can appear in the filled board. If a letter has multiple 
     *                                  upper bounds, the largest bound is the effective one.
     *  @param dictionary is the dictinary to be used for filling out the puzzle. Check DictInterface for
     *                    more details on the operations provided by the dictionary
     *  @return a 2-d array representing the filled out puzzle or null if the puzzle has no solution
     */
    public char[][] fillPuzzle(char[][] board, DictInterface dictionary);

    /*
     * Check if filledBoard is a correct filling for a given empty board
     * 
     * @param emptyBoard is a 2-d array representing an empty board
     * @param filledBoard is a 2-d array representing a filled out board
     * @param dictionary is the dictinary to be used for checking the filled out board
     * @return true if rules defined in fillPuzzle has been followed and 
     *  every row and column is a valid word in the dictionary. If a row
     *  or a column includes one or more '-', then each segment should be 
     *  a valid word in the dictionary; the method returns false otherwise
     */
    public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary);
    
} 
```

## Background

Crossword puzzles are challenging games that test both vocabularies and reasoning skills.  However, creating a legal crossword puzzle is not a trivial task.  This is because the words both across and down must be legal, and the choice of a word in one direction restricts the possibilities of words in the other direction.  This restriction progresses recursively, so that some word choices "early" in the board could make it impossible to complete the board successfully.  For example, look at the simple crossword puzzle below (note: in this example X<sub>1</sub>, X<sub>2</sub>, X<sub>3</sub> are variables not the letter X):

![puzzle](img/puzzle.png)
 

Assume that the word LENS has been selected for row 0 of the puzzle, as shown in Figure 1 above.  Now, the word in column 1 (the second column) must begin with an E, the word in column 2 must being with an N and the word in column 3 must begin with an S. All single characters are valid words in our dictionary so the L in column 0 is a valid word but is also irrelevant to the rest of the puzzle, since its progress is blocked by a filled-in square.  There are many ways to proceed from this point, and finding a good way is part of the assignment.  However, if we are proceeding character by character in a row-wise fashion, we now need a letter X<sub>1</sub> such that EX<sub>1</sub> is a valid prefix to a word.  Several letters will meet this criterion (EA, EB and EC are all valid prefixes, just to pick the first three letters of the alphabet).  Once a possibility is selected, there are now two restrictions on the next character X<sub>2</sub>: NX<sub>2</sub> must be a valid word and X<sub>1</sub>X<sub>2</sub> must be a valid prefix to a word (see Figure 1). Assume that we choose Q for X<sub>1</sub> (since EQ is a valid prefix). We can then choose U for X<sub>2</sub>, (see Figure 2 (NU is a valid word in our dictionary)). Continuing in the same fashion, we can choose the other letters shown in Figure 2 (in our dictionary QUA, DU and DC are all legal words).

Unfortunately, in row 3, column 1 we run into a problem (Figure 3).  There is no word in our dictionary EQUX<sub>3</sub> for any letter X<sub>3</sub> (note that since we are at a terminating block, we are no longer just looking for a prefix) so we are stuck.  At this point we need to undo some of our previous choices (i.e., backtrack) in order to move forward again toward a solution.  If our algorithm were very intelligent, it would know that the problem that we need to fix is the prefix EQU in the second column .  However, based on the way we progressed in this example, we would simply go back to the previous square (row 3, column 0), try the next legal letter there, and move forward again.  This would again fail at row 3, column 1, as shown in Figure 3.  Note that the backtracking could occur many times for a given board, possibly going all the way back to the first word on more than one occasion.  In fact, the general run-time complexity for this problem is exponential.  However, if the board sizes are not too large, we can likely solve the problem (or determine that no solution exists) in a reasonable amount of time.  One solution (but not the only one) to the puzzle above is shown in Figure 4.

## TASK 1 - Finding a Single Solution

Your first task in this assignment is to create a legal crossword puzzle (if one exists). The found solution is the return value of the method 

```java
    public char[][] fillPuzzle(char[][] board, DictInterface dictionary);
```
in `WordPuzzleInterface`. The method is called from `CrosswordTest` after the following happens (check the constructor of `CrosswordTest`).

1. A dictionary of words is read in from a file and used to build a `MyDictionary` object of these words. The name of the dictionary file is specified as a command-line argument. The interface `DictInterface` (in `DictInterface.java`) and the class `MyDictionary` (in `MyDictionary.java`) are provided for you in this repository, and you must use them in this assignment.  Read over the code and comments carefully so that you understand what they do and how.  The file used to initialize the `MyDictionary` object will contain ASCII strings, one word per line.  Use the file `dict8.txt`.  If you are unsure of how to use `DictInterface` and `MyDictionary` correctly, see the `DictTest.java` example program (and read the comments). Lab 1 can be used for reference as well.

2. A crossword board is read in from a file.  The name of the board file is specified as a command-line argument.  The crossword board is formatted in the following way:

    - The first line contains a single integer, N.  This represents the number of rows and columns that will be in the board.
    - The next N lines will each have N characters, representing the NxN total locations on the board.  Each character will be either
      - `+` (plus) which means that any letter can go in this square
      - `–` (minus) which means that the square is solid (filled-in) and no letter can go in here
      - a..z (a letter from a to z) which means that the specified letter must be in this square (i.e., the square can be used in the puzzle, but only for the letter indicated)

For the board shown in Figure 1 above, the board file `sample.txt` is as follows:
```
4
++++
-+++
++-+
++++ 
```

Some test boards have been put onto this repository. Please consult `testFiles.md` for an overview of these board files. Many of the test files may have many solutions, but for this assignment, you only need to find one solution. More test boards will be given in the autograder on GradeScope.

3. `fillPuzzle` is called to get a legal crossword puzzle for the given board and print it out to standard output. For example, one output to the crossword shown above in Figure 4 would be:

```
> java CrosswordTest dict8.txt sample.txt
LENS
-TON
AC-O
THAW
>
```
For a second example, the board file `test3a.txt` contains the following board:

```
3
+++
+++
+++
```
One possible output to the crossword shown above would be:
```
> java CrosswordTest dict8.txt test3a.txt
ABA
BAD
ADA
```

For a third example, the board file `test3c.txt` contains the following board:

```
3
+++
+++
++1
```
One possible output to the crossword shown above would be:
```
> java CrosswordTest dict8.txt test3a.txt
ABA
BAD
ADO
```

Note that
```
ABA
BAD
ADA
```
is not a valid solution for text3c.txt because the number 1 at the bottom right square requires the letter put there to appear at most once in the board. Placing an A in that square would have violated the constraint.

Depending upon your algorithm, the single solution that you find may differ from that of my program or your classmates' programs.  This is fine as long as all of the solutions are legal.  Note that because of the severe performance limitations of the `MyDictionary` class, some of the run-times for the test files will be very long.  See more details on this in `testFiles.html`. We will see later in the course a much more efficient way of implementing the dictionary.

To help you to get started, first think of boards with all squares open (you can consider filled in squares later). In this case a solution for a `KxK` board will consist of `K` words of length `K` in the columns of the board and `K` words of length K in the rows of the board. 
- Your program has to make one decision for each square of the board. How many options do you have for each decision? What are these options? You will have to choose from the 26 letters.
- Construct an array of K `StringBuilder`s for the columns (call it `colStr`) and an array of K `StringBuilder`s for the rows (call it `rowStr`), each initially empty. Now consider a single recursive call at square `(i,j)` on the board. For an option (i.e., a letter) at position `(i,j)` to be valid, the following must be true: 
    - If `j` is not an end index, then `rowStr[i]` + the letter a must be a valid prefix in the dictionary
    - If `j` is an end index, then `rowStr[i]` + the letter must be a valid word in the dictionary 
    - If `i` is not an end index, then `colStr[j]` + the letter must be a valid prefix in the dictionary 
    - If `i` is an end index, then `colStr[j]` + the letter must be a valid word in the dictionary
- If the letter is valid, you append it to both corresponding `StringBuilder`s and recurse to the next square (unless you are on the last square of the board, in which case you have a solution!). If it is not valid you try the next character at that square or, if all have been tried, you backtrack.

To handle filled-in squares, you may think of using the method 
```java
public int searchPrefix(StringBuilder s, int start, int end)
```

instead of 
```java
public int searchPrefix(StringBuilder s)
```

To find the proper `start` values to pass into `searchPrefix`, you may keep track of the last seen position of a '-' in each row and in each column. You can use two arrays, each of size N, for this purpose. Think of when you should change the array values and when and how to undo the changes.

**Search algorithm details**: Carefully consider the algorithm to fill the words into the board.  Make sure it potentially considers all possibilities yet does not waste time rechecking prefixes that have already been checked.   Although you are not required to use the exact algorithm described above, your algorithm must be a recursive backtracking algorithm that uses pruning.  The algorithm you use can vary greatly in its efficiency.  If your algorithm is very inefficient or otherwise poorly implemented, you will lose some points.  This algorithm is a significant part of the overall assignment, so put a good amount of effort into doing it correctly.  For guidance on your board-filling algorithm, it is strongly recommended that you revise the Boggle game lab code (Lab 1).

## TASK 2 - Checking if a Solution is Correct

In the second task, represented by the method 
```java
    public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary);
```
in `WordPuzzleInterface`, your job is to check if a found solution is correct with respect to an empty board. Check the documentation of the method for more details on how to perform the check.

## Important Notes:

- **Refer to the backtracking template that we discussed in class.**
- For consistency, you **must** name your main program `Crossword.java`.
- **The only code file that you are allowed to change is `Crossword.java`.**
- You don't have to try letters to place at a square in the alphabetical order (i.e., 'a' then 'b' then 'c' up to 'z'). Actually, the order in which you try the letters may have a siginficant impact on the running time of your search method.
- When grading your assignments, the autograder and your TAs will redirect your output to a file so that they can refer to it at a later point.  To make sure this will work correctly, make sure that once your search algorithm begins there is _NO INPUT_ or anything that would make your program require any user interaction (since the TA will not see any prompts given that they will be sent to a file rather than the display).
- The `MyDictionary` implementation of the `DictInterface` that is provided to you should work correctly, but it is not very efficient.  Note that it is doing a linear search of an `ArrayList` to determine if the argument is a prefix or word in the dictionary.  
-	Be sure to thoroughly document your code, especially the code that fills the board.

## Coding Style and Documentation

Please check [this guide](https://introcs.cs.princeton.edu/java/11style/) for directions regarding the expected coding style and documentation.

## Writeup

Once you have completed your algorithm, write a short paper (500-750 words) using [Github Markdown syntax](https://guides.github.com/features/mastering-markdown/) and named `a1.md` that summarizes your project in the following ways:
1.	Discuss how you solved the crossword-filling problem in some detail. Include
    * how you set up the data structures necessary for the problem and 
    * how your algorithm proceeded.  
    * Also, indicate any coding or debugging issues you faced and how you resolved them.  If you were not able to get the program to work correctly, still include your approach and speculate as to what still needs to be corrected.
2.	Include the (approximate) run-times for the programs for the various files in a table.  
3.	Include an asymptotic analysis of the worst-case run-time of the program.  Some values to consider in this analysis may include:
    * Number of words in the dictionary
    * Number of characters in a word
    * Number of possible letters in a crossword location
    * Number of crossword locations in the puzzle

If you were unable to complete the crossword solving program, speculate (using some intelligent guessing) for the actual run-times.


## Submission Requirements

You must submit your Github repository to GradeScope. We will only grade the following file:
1)	`Crossword.java`
3)	`a1.md`: A well written/formatted paper explaining your search algorithm and results (see the Writeup section above for details on the paper) 
4)	`InfoSheet.md`: Assignment Information Sheet (including compilation and execution information).

_The idea from your submission is that your TA (and/or the autograder if available) can compile and run your programs from the command line WITHOUT ANY additional files or changes, so be sure to test it thoroughly before submitting it. If the TA (and/or the autograder if available) cannot compile or run your submitted code it will be graded as if the program does not work.
If you cannot get the programs working as given, clearly indicate any changes you made and clearly indicate why on your Assignment Information Sheet.  You will lose some credit for not getting it to work properly, but getting the main programs to work with modifications is better than not getting them to work at all.  A template for the Assignment Information Sheet can be found in this repository. You do not have to use this template, but your sheet should contain the same information._

_Note: If you use an IDE, such as NetBeans, Eclipse, or IntelliJ, to develop your programs, make sure the programs will compile and run on the command-line before submitting – this may require some modifications to your program (e.g., removing package information)._

## Rubrics 

Please note that if an autograder is available, its score will be used as a guidance for the TA, not as an official final score. Please also note that the autograder rubrics are the definitive rubrics for the assignment. The rubrics below will be used by the TA to assign partial credit in case your code scored less than 40% of the autograder score. If your code is manually graded for partial credit, the maximum you can get for the autograded part is 60%.

Item|Points
----|------
Basic backtracking approach is correct|	10
Search works for board with all open squares|	20
Search works for board with filled in squares|	10
Algorithm works in an efficient manner|	10
Solution is checked correctly|	25
Write-up paper|	10
Code style and documentation |	10
Assignment Information Sheet|	5
