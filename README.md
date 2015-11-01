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

- [![Build Status](http://jenkins.opf-labs.org/buildStatus/icon?job=veraPDF-library-0.6)](http://jenkins.opf-labs.org/view/A-veraPDF/job/veraPDF-library-0.6/ "OPF Jenkins v0.6 release build") OPF Jenkins: `release-0.6`

- [![Build Status](http://jenkins.opf-labs.org/buildStatus/icon?job=veraPDF-library-0.7-mvn)](http://jenkins.opf-labs.org/view/A-veraPDF/job/veraPDF-library-0.7-mvn/ "OPF Jenkins v0.7 development build") OPF Jenkins: `integration`

Getting veraPDF software
------------------------
###Pre-requisites
In order to use the GUI you'll need:

 * Java 7, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](http://openjdk.java.net/install/index.html).

###veraPDF GUI
####Download release version
You can download a Java based installer for the latest veraPDF GUI release [from our download site](http://downloads.verapdf.org/rel/verapdf-installer.jar). The current installation process requires Java 1.7 to be pre-installed, but we're working on a 1-click installer.

####veraPDF GUI manual
We've prepared a manual for the GUI which is included in the libary project and can be [downloaded from GitHub](https://github.com/veraPDF/veraPDF-library/raw/release-0.6/veraPDFPDFAConformanceCheckerGUI.pdf).

####Download latest development version
If you want to try the latest development version you can obtain it from our [development download site](http://downloads.verapdf.org/dev/http://downloads.verapdf.org/dev/verapdf-installer-dev-latest.jar). Be aware that we release development snapshots regularly, often more than once a day. While we try to ensure that development builds are well tested there are no guarantees.

Building the veraPDF-library from Source
----------------------------------------
###Pre-requisites
If you want to build the code from source you'll require:

 * Java 7, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](http://openjdk.java.net/install/index.html).
 * [Maven v3+](https://maven.apache.org/)

Life will be easier if you also use [Git](https://git-scm.com/) to obtain and manager the source.

###Building veraPDF
First you'll need to obtain a version of the veraPDF-library source code. You can compile either the latest relase version, or the latest development source.
####Downloading the latest release source
Use Git to clone the repository and ensure that the `master` branch is checked out:
```
git clone https://github.com/veraPDF/veraPDF-library
git checkout master
```
or download the latest [tar archive](https://github.com/veraPDF/veraPDF-library/archive/master.tar.gz "veraPDF-library latest GitHub tar archive") or [zip archive](https://github.com/veraPDF/veraPDF-library/archive/master.zip "veraPDF-library latest GitHub zip archive") from GitHub.

####Downloading the latest development source
Use Git to clone the repository and ensure that the `integration` branch is checked out:

    git clone https://github.com/veraPDF/veraPDF-library
    git checkout integration

or download the latest [tar archive](https://github.com/veraPDF/veraPDF-library/archive/integration.tar.gz "veraPDF-library latest GitHub tar archive") or [zip archive](https://github.com/veraPDF/veraPDF-library/archive/integration.zip "veraPDF-library latest GitHub zip archive") from GitHub.

####Use Maven to compile the source
Move to the downloaded project directory and call Maven install:

    cd veraPDF-library
    mvn clean install -P clone-test-resources

####Testing the build
You can test your build by runnung the GUI application from the VeraPDF Library GUI `gui` sub-module.

    java -jar gui/target/gui-${project.version}.jar

Where `${project.version}` is the current Maven project version. This should bring up the veraPDF GUI main window if the build was successful.
