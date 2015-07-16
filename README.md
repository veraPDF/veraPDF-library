veraPDF-library
===============
*Definitive PDF/A Validation*

Licensing
---------
The veraPDF PDF/A Validation Library is dual-licensed, see:

 - [GPLv3+](LICENSE.GPL "GNU General Public License, version 3")
 - [MPLv2+](LICENSE.MPL "Mozilla Public License, version 2.0")

CI Status
---------
- [![Build Status](https://travis-ci.org/veraPDF/veraPDF-library.svg?branch=master)](https://travis-ci.org/veraPDF/veraPDF-library "veraPDF-library Travis-CI master branch build") Travis-CI: `master`

- [![Build Status](https://travis-ci.org/veraPDF/veraPDF-library.svg?branch=integration)](https://travis-ci.org/veraPDF/veraPDF-library "veraPDF-library Travis-CI integration build") Travis-CI: `integration`

- [![Build Status](http://jenkins.opf-labs.org/view/veraPDF-jobs/job/veraPDF-library-0.2/badge/icon)](http://jenkins.opf-labs.org/view/veraPDF-jobs/job/veraPDF-library-0.2/ "OPF Jenkins v0.2 release build") OPF Jenkins: `release-0.2`

- [![Build Status](http://jenkins.opf-labs.org/view/veraPDF-jobs/job/veraPDF-library-0.3/badge/icon)](http://jenkins.opf-labs.org/view/veraPDF-jobs/job/veraPDF-library-0.3/ "OPF Jenkins v0.3 development build") OPF Jenkins: `integration`

Pre-requisites
--------------

In order to run the applications or use the GUI you'll need:

 * Java 7, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](http://openjdk.java.net/install/index.html).

If you want to build the code from source you'll also require:

 * [Maven v3+](https://maven.apache.org/)

Building and Running the veraPDF-library
----------------------------------------

### Building from source

1. Download the veraPDF-library repository, either:
```
git clone https://github.com/veraPDF/veraPDF-library
```
or download the [latest tar archive](https://github.com/veraPDF/veraPDF-library/archive/master.tar.gz "veraPDF-library latest GitHub tar archive") or [zip equivalent](https://github.com/veraPDF/veraPDF-library/archive/master.zip "veraPDF-library latest GitHub zip archive") from GitHub.

2. Move to the downloaded project directory, e.g.
```
cd veraPDF-library
```
3. Build and install using Maven:
```
mvn clean install
```

### Running the command line validation application

 1. Move to the build directory for the command line interface application, e.g.:
 ```
 cd {project directory}/cli/target
 ```

 2. Run the compiled command line application:
 ```
 java -jar vera-cli-1.0-SNAPSHOT-jar-with-dependencies.jar verapdf --validate --input "{path to input pdf file}" --profile "{path to validation profile}" --output "{path to saved report}"
 ```

### Getting the veraPDF-library-GUI zip package

The GUI application is built in the VeraPDF Library GUI sub-module.

 1. Move to the build directory for the command line interface application, e.g.:
 ```
 cd {project directory}/gui/target
 ```
 2. Locate the built zip package:
 ```
 ls veraPDF-library-GUI-*.zip
 ```

The zip package contains the following resources:

 - a copy of the latest [veraPDF-corpus master](https://github.com/veraPDF/veraPDF-corpus/archive/master.zip) branch;
 - a copy of the latest [veraPDF-validation-profiles integration](https://github.com/veraPDF/veraPDF-validation-profiles/archive/integration.tar.gz) branch
 - a copy of the latest [veraPDF-model integration](https://github.com/veraPDF/veraPDF-model/archive/integration.tar.gz) branch
