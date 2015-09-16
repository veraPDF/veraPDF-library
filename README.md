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

- [![Build Status](http://jenkins.opf-labs.org/buildStatus/icon?job=veraPDF-library-0.4)](http://jenkins.opf-labs.org/view/A-veraPDF/job/veraPDF-library-0.4/ "OPF Jenkins v0.4 release build") OPF Jenkins: `release-0.4`

- [![Build Status](http://jenkins.opf-labs.org/buildStatus/icon?job=veraPDF-library-0.5-mvn)](http://jenkins.opf-labs.org/view/A-veraPDF/job/veraPDF-library-0.5-mvn/ "OPF Jenkins v0.5 development build") OPF Jenkins: `integration`

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
mvn clean install -P clone-test-resources
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

### Building & obtaining the veraPDF-library-GUI zip package

The latest release of the GUI zip package is available from [our release download area](http://downloads.verapdf.org/rel/), using [this link](http://downloads.verapdf.org/rel/veraPDF-library-GUI-latest.zip). The latest development build can be downloaded from [our development download directory](http://downloads.verapdf.org/dev/) via [this link](http://downloads.verapdf.org/dev/veraPDF-library-GUI-dev-latest.zip).

If you've built the project from source then the GUI application is available in the VeraPDF Library GUI ```gui``` sub-module. The example commands are linux based:

 1. Move to the build directory for the command line interface application, e.g.:
 ```
 cd {project directory}/gui/target
 ```
 2. Locate the built zip package, e.g.:
 ```
 ls veraPDF-library-GUI-*.zip
 ```

### Unpacking & using the GUI package

Again the example commands assume the user is linux based and use a specific 0.4.9 version number, you will quite possibly download another version.

 1. Unzip the archive to a location that can be used for installation, e.g.
 ```
 unzip veraPDF-library-GUI-0.4.9.zip -d /home/username
 ```
 2. The scripts to run the GUI application are located under ```install-directory/bin```:
   - Mac or Linux users can run: ```VeraPDF validation GUI```
   - Windows users should run: ```VeraPDF validation GUI.bat```
 3. In order to perform validation you'll need to load a validation profile. Currently the GUI package comes with a copy of our PDF/A-1b validation profile. This is located under the install/unzip directory:
 ```
 install-directory/resources/veraPDF-validation-profiles-integration/PDF_A/PDFA-1B.xml
 ```

The zip package contains the latest versions, at the time of packaging, of the following resources:

 - the staging branch of the [veraPDF-corpus](https://github.com/veraPDF/veraPDF-corpus/archive/staging.zip) project;
 - the integration branch of the [veraPDF-validation-profiles](https://github.com/veraPDF/veraPDF-validation-profiles/archive/integration.tar.gz) project; and
 - the integration branch of the [veraPDF-model](https://github.com/veraPDF/veraPDF-model/archive/integration.tar.gz) project.
