# Project #: DFA

* Author: James Hyle, Rebecca Berg
* Class: CS 361 
* Semester: Fall 2025

## Overview

This program allows the user to create a DFA.  It allows the user to test if strings are accepted by 
the DFA, and swap transition labels between two symbols of the DFA.  Additionally, it can print the
5-tuple of the machine to the console.

## Reflection

Overall this project went fairly smoothly.  Going over the java documentation for Sets and Maps helped
tremendously with implementation for the various collections in the DFA.  Having the toString() function
written out early on in the process made debugging much easier as it was immediately obvious where the
inconsistencies between our output and the expected results were.  We also took a divide and conquer
approach which worked out nicely as the shapes of our objects were already decided on, even if they were
not fully implemented yet.

Looking back on the project, there might be some simplification and reevaluation that could be done on
a few things regarding the start and final states.  It might not be necessary for both the state itself
and the DFA to know about whether a certain state is final, starting or neither.  Though, having both
be aware gives us flexibility in how the DFA methods work and could allow us to adapt this program to 
accommodate NFAs more easily in the future should that come up.

## Compiling and Using

To compile execute the following command in the project directory:
[you@onyx]$ javac -cp .:/usr/share/java/junit.jar ./test/dfa/DFATest.java

To run the program once compiled use the following:
[you@onyx]$ java -cp .:/usr/share/java/junit.jar:/usr/share/java/hamcrest/core.jar
org.junit.runner.JUnitCore test.dfa.DFATest

## Sources used
https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Set.html
https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Map.html
