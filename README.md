# Project #2: NFA

* Author: James Hyle, Rebecca Berg
* Class: CS 361 
* Semester: Fall 2025

## Overview

This program models a non-deterministic finite automaton with transitions on 'e' (epsilon) and a string of symbols 
passed to the automata. We use packages to organize classes with object oriented code to represent each element and
operation of the NFA 5-Tuple. The user can test parameterized NFAs created in the test directory.

## Reflection

This project was difficult, but rewarding to implement. While it built on the foundations of DFA, we had to shift
our thinking about handling transitions of multiple sets of states, and model parallel exploration of the states
using Bfs and Dfs. Managing the paths of the NFA involved significantly more mental overhead than a DFA, as it required
tracking multiple states and transitions simultaneously. Implementing the maxcopies method turned out to be one of
the more challenging problems of the project as managing the input string, set of states to explore, copies of the
transitions, and visited states in an efficient way was not readily apparent. Once the connection was made to the logic
of the accepts method, writing the code for it was much easier.

We used a divide and conquer strategy which was very beneficial as no code we wrote required correcting a
merge-conflict and allowed each other to build on the project. The project did not require serialization of the DFA,
which was nice to not have to worry about this for the project as well. There may be some optimizations to make for our
program, but overall the implementation is solid and uses OOP.

## Compiling and Using

To compile execute the following command in the project directory: 
[you@onyx]$ javac -cp .:/usr/share/java/junit.jar ./test/nfa/NFATest.java

To run the program once compiled use the following: 
[you@onyx]$  java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore test.nfa.NFATest

If you are using an IDE such as IntelliJ, simply navigate to the test file and run it with the IDE's 
built-in run function.

## Sources used

https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Set.html
https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Map.html
