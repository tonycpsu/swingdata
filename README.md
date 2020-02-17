Swing Data Coding Challenge
===========================

This repository contains my proposed solution for the Swing Data Coding
Challenge.


Repository Structure
====================

In addition to this README, the test data supplied with the exercise, and files
used by the Gradle build system, this repository contains the following files:

* The implementation for the four methods described in the challenge is located in
`src/main/java/swingdata/Analysis.java`.

* `src/main/java/swingdata/SwingData.java` contains the data structure
representing the data for a swing.

* `src/test/src/test/java/TestSwingDataAnalysis.java` contains some unit tests of
the four requested functions.


Build Instructions
------------------

To build:

    $ ./gradlew build

on UNIX platforms, or

    C:> gradlew.bat

on Windows.


Testing
-------

To run the unit tests:

    $ ./gradlew test
