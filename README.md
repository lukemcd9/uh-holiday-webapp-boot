A web application to display holidays used by UH.

[![Build Status](https://travis-ci.org/fduckart/uh-holiday-webapp-boot.png?branch=master)](https://travis-ci.org/fduckart/uh-holiday-webapp-boot)
[![Coverage Status](https://coveralls.io/repos/github/fduckart/uh-holiday-webapp-boot/badge.svg)](https://coveralls.io/github/fduckart/uh-holiday-webapp-boot)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/d68ffad24e34410a9186edd61494a749)](https://www.codacy.com/app/fduckart/uh-holiday-webapp-boot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=fduckart/uh-holiday-webapp-boot&amp;utm_campaign=Badge_Grade)

##### Java
You'll need a Java JDK to build and run the project (version 1.8).

##### Building
Install the necessary project dependencies:

    $ ./mvnw install

To run the Application from the Command Line:

    $ ./mvnw clean spring-boot:run

To build a deployable war file for local development, if preferred:

    $ ./mvnw clean package

You should have a deployable war file in the target directory.
Deploy as usual in a servlet container, e.g. tomcat.

To run the Application, point your browser at:

http://localhost:8080/holidays/


##### Running Unit Tests
The project includes Unit Tests for various parts of the system.
For this project, Unit Tests are defined as those tests that will
rely on only the local development computer.
A development build of the application will run the Unit Tests.

To run the Unit Tests:

    $ ./mvnw clean test

To run a specific test class:

    $ ./mvnw clean test -Dtest=StringsTest

To run a single method in a test class:

    $ ./mvnw clean test -Dtest=StringsTest#trunctate

