veraPDF-library
===============
*Industry Supported PDF/A Validation*

[![Build Status](https://travis-ci.org/veraPDF/veraPDF-library.svg?branch=integration)](https://travis-ci.org/veraPDF/library "Travis-CI")
[![Build Status](http://jenkins.openpreservation.org/buildStatus/icon?job=veraPDF-library)](http://jenkins.openpreservation.org/job/veraPDF-library/ "OPF Jenkins Release")
[![Build Status](http://jenkins.openpreservation.org/buildStatus/icon?job=veraPDF-library-dev)](http://jenkins.openpreservation.org/job/veraPDF-library-dev/ "OPF Jenkins Development")
[![Maven Central](https://img.shields.io/maven-central/v/org.verapdf/verapdf-library.svg)](http://repo1.maven.org/maven2/org/verapdf/verapdf-library/ "Maven central")
[![CodeCov Coverage](https://img.shields.io/codecov/c/github/veraPDF/veraPDF-library.svg)](https://codecov.io/gh/veraPDF/veraPDF-library/ "CodeCov coverage")
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/cfafc08b44eb49b6aa790d6aaff09cd3)](https://www.codacy.com/app/carlwilson/veraPDF-library?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=veraPDF/veraPDF-library&amp;utm_campaign=Badge_Grade "Codacy grade")

[![GitHub issues](https://img.shields.io/github/issues/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/issues "Open issues on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-closed/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/issues-closed "Open issues on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-pr/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/issues-pr "Open issues on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-pr-closed/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/issues-pr-closed "Open issues on GitHub")

Licensing
---------
The veraPDF PDF/A Validation Library is dual-licensed, see:

 - [GPLv3+](LICENSE.GPL "GNU General Public License, version 3")
 - [MPLv2+](LICENSE.MPL "Mozilla Public License, version 2.0")

Documentation
-------------
See the [veraPDF documentation site](http://docs.verapdf.org/).

Quick Start
-----------
### Pre-requisites

In order to build the library you'll need:

 * Java 8, which can be downloaded [from Oracle](http://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](http://openjdk.java.net/install/index.html).
 * [Maven v3+](https://maven.apache.org/)

Life will be easier if you also use [Git](https://git-scm.com/) to obtain and manage the source.

### Building veraPDF
First you'll need to obtain a version of the veraPDF-library source code. You can compile either the latest relase version or the latest development source.

#### Downloading the latest release source
Use Git to clone the repository and ensure that the `master` branch is checked out:
```
git clone https://github.com/veraPDF/veraPDF-library
git checkout master
```
or download the latest [tar archive](https://github.com/veraPDF/veraPDF-library/archive/master.tar.gz "veraPDF-library latest GitHub tar archive") or [zip archive](https://github.com/veraPDF/veraPDF-library/archive/master.zip "veraPDF-library latest GitHub zip archive") from GitHub.

#### Downloading the latest development source
Use Git to clone the repository and ensure that the `integration` branch is checked out:

    git clone https://github.com/veraPDF/veraPDF-library
    git checkout integration

or download the latest [tar archive](https://github.com/veraPDF/veraPDF-library/archive/integration.tar.gz "veraPDF-library latest GitHub tar archive") or [zip archive](https://github.com/veraPDF/veraPDF-library/archive/integration.zip "veraPDF-library latest GitHub zip archive") from GitHub.

#### Use Maven to compile the source
Move to the downloaded project directory and call Maven install:

    cd veraPDF-library
    mvn clean install
