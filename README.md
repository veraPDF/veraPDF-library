veraPDF-library
===============

The open source, definitive PDF/A validation library

CI Status
---------

Travis: [![Build Status](https://travis-ci.org/veraPDF/veraPDF-library.svg?branch=master)](https://travis-ci.org/veraPDF/veraPDF-library)

Jenkins: [![Build Status](http://jenkins.opf-labs.org/view/veraPDF-jobs/job/veraPDF-library/badge/icon)](http://jenkins.opf-labs.org/view/veraPDF-jobs/job/veraPDF-library/)

Pre-requisites
--------------

In order to run the applications or use the GUI you'll need:

 * Java 7, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](http://openjdk.java.net/install/index.html).

If you want to build the code from source you'll also require:

 * [Maven v3+](https://maven.apache.org/)

Building and Running the veraPDF-library
----------------------------------------

### Building from source

 1. Download the veraPDF-model repository, either cloning via Git
 `git clone https://github.com/veraPDF/veraPDF-library` or downloading the [latest zip archive from GitHub](https://github.com/veraPDF/veraPDF-library/archive/master.zip).

 2. Move to the downloaded project directory, e.g. `cd veraPDF-library`.

 3. Build and install using Maven, `mvn clean install -DskipTests`. We know skipping tests is bad practise, but we're currently formalising our testing strategy.

### Running the command line validation application

 1. Move to the build directory for the command line interface application, e.g. `cd {project directory}/cli/target`.

 2. Run the compiled command line application:
 ```
 java -jar vera-cli-1.0-SNAPSHOT-jar-with-dependencies.jar verapdf --validate --input "{path to input pdf file}" --profile "{path to validation profile}" --output "{path to saved report}"
 ```
