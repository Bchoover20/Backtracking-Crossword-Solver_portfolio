# CS 1501 Assignment 1 Test Files

__*Note: You are only required to find a single solution for each test file.*__

- `test3a.txt`: Example solutions Simple 3x3 board. This has `392586` solutions. Included is some of them in the order they were found by one example program.
    e e l 
    e a t
    l t v

- `test3b.txt`: Restricted 3x3 board. This should have ONE solution only. 

    c p a
    p a x
    a w e

- `test4a.txt`: Simple 4x4 board. This one has `1621736` solutions. 

    e e o c
    e a v e
    o v e r
    c e r n

- `test4b.txt`: Another 4x4 board. This one has 10872485 solutions. 

    e e o c
    e l - a
    o l a f
    c a f e

- `test4c.txt`: Restricted version of example from Assignment Sheet. This one has `47845` solutions. 

    l e n s
    - e o c
    n o - a
    s c a r

- `test4d.txt`: No solution (should be determined quickly if a good pruning algorithm is used). 

    NO SOLUTION 
    
    x y l +
    + + + +
    + + + +
    + + + +

- `test4e.txt`: No solution but may take MUCH longer to determine due to the backtracking required. Think about why. Experiment if you'd like with placements of a single x on the board. Some will yield solutions, and others will not, with greatly varying runtimes based on the location of the x. 


- `test4f.txt`: More restricted version of example from README. This should have ONE solution only. 

    l e n s
    - + y +
    p + - +
    + + + k

- `test5a.txt`: Simple 5x5 board. This one has `53399` solutions. 

    e e r i e 
    e l e n a
    r e m u s
    i n u r e
    e a s e l

- `test6a.txt`: Simple 6x6 board.

    e r r a t a
    r e e v e s 
    r e m i s s
    a v i a t e
    t e s t e s
    a s s e s s

- `test6b.txt` This also has a LOT of solutions. For MyDictionary it took about 20 minutes to find the first solution. 

    s e a s o n
    e - d e a r
    s o - g f +
    + a + - + +
    + + + + - +
    + + + + + +

- `test6c.txt`: Board with no solution but that still requires a lot of backtracking. With MyDictionary it will take more than 2.5 hours. 

NO SOLUTION

i b e x
c o n e 
e g o +
+ + + +

- `test7a.txt`:

NO SOLUTION

e a r n e s t
f l u t t e r
t i d i + + +
+ + + + + + +
+ + + + + + +
+ + + + + + +
+ + + + + + +

- `test8a.txt`: 
The above are basic 7x7 and 8x8 boards. The run-times for these should be extremely high given the MyDictionary implementation of DictInterface. One thing to keep in mind is that because the words must all be longer, there may in fact be fewer possible solutions than there would be for the smaller board sizes. In fact, neither of these boards has a solution. 

NO SOLUTION

e a s t w a r d
s c h u y l e r
o r a t o r i o
t o k e + + + +
+ + + + + + + +
+ + + + + + + +
+ + + + + + + +
+ + + + + + + +

- `test8b.txt` Partial list of solutions This restricted 8x8 board has many solutions but it may take some time to find even one. I have included a partial list of solutions for you to examine. 

s c - w a r t z
a e r o g e n e
c a - - u t t + 
- - + + + + - -
+ + + + - + + +
+ + + + + + + +
+ + + - - + + +
- z + + + - - z

- `test8c.txt`: This restricted 8x8 board is a combination of boards test3b.txt and test4f.txt. It has only one solution. With MyDictionary it took an example program about 10 minutes. 

c z + - l e n s 
+ + x - - + y +
+ x + - p + - +
- - - - + + + k
l e n s - - - -
- + y + - c + +
p + - + - + + x
+ + + k - + x +
