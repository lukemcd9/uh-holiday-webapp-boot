A web application to display holidays used by UH.

[![Build Status](https://travis-ci.org/fduckart/uh-holiday-webapp-boot.png?branch=master)](https://travis-ci.org/fduckart/uh-holiday-webapp-boot)
[![Coverage Status](https://coveralls.io/repos/github/fduckart/uh-holiday-webapp-boot/badge.svg)](https://coveralls.io/github/fduckart/uh-holiday-webapp-boot)
##### Build Tool
First, you need to download and install maven (version 3.2.1+).

##### Java
You'll need a Java JDK to build and run the project (version 1.8).

The files for the project are kept in a code repository,
available from here:

https://github.com/fduckart/uh-holiday-webapp-boot

##### Building
Install the necessary project dependencies:

    $ ./mvnw install

To run the Application from the Command Line:

    $ ./mvnw clean spring-boot:run

To build a deployable war file for local development, if preferred:

    $ ./mvnw clean package

You should have a deployable war file in the target directory.
Deploy as usual in a servlet container, e.g. tomcat.

##### Running Unit Tests
The project includes Unit Tests for various parts of the system.
For this project, Unit Tests are defined as those tests that will
rely on only the local development computer.
A development build of the application will run the Unit Tests.
A test and production build of the application will run both the
Unit Tests and the System Tests (which may require network access).
You can also run specific Unit Tests using the appropriate command
line arguments.

To run the Unit Tests with a standard build:

    $ ./mvnw clean test

To run a test class:

    $ ./mvnw clean test -Dtest=StringsTest

To run a single method in a test class:

    $ ./mvnw clean test -Dtest=StringsTest#trunctate

##### Running the Application locally

http://localhost:8080/holidays/
