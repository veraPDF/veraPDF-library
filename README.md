veraPDF-library
===============
*Industry Supported PDF/A and PDF/UA Validation*

[![Build Status](https://jenkins.openpreservation.org/job/veraPDF/job/1.26rc/job/library/badge/icon)](https://jenkins.openpreservation.org/job/veraPDF/job/1.26rc/job/library/ "OPF Jenkins")
[![Maven Central](https://img.shields.io/maven-central/v/org.verapdf/verapdf-library.svg)](https://repo1.maven.org/maven2/org/verapdf/verapdf-library/ "Maven central")
[![CodeCov Coverage](https://img.shields.io/codecov/c/github/veraPDF/veraPDF-library.svg)](https://codecov.io/gh/veraPDF/veraPDF-library/ "CodeCov coverage")
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/79a5ff15e77444d79d6e97cc40bb458c)](https://app.codacy.com/gh/veraPDF/veraPDF-library/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade "Codacy grade")

[![GitHub issues](https://img.shields.io/github/issues/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/issues "Open issues on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-closed/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/issues?q=is%3Aissue+is%3Aclosed "Closed issues on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-pr/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/pulls "Open pull requests on GitHub")
[![GitHub issues](https://img.shields.io/github/issues-pr-closed/veraPDF/veraPDF-library.svg)](https://github.com/veraPDF/veraPDF-library/pulls?q=is%3Apr+is%3Aclosed "Closed pull requests on GitHub")

Licensing
---------
The veraPDF PDF/A Validation Library is dual-licensed, see:

 - [GPLv3+](LICENSE.GPL "GNU General Public License, version 3")
 - [MPLv2+](LICENSE.MPL "Mozilla Public License, version 2.0")

Documentation
-------------
See the [veraPDF documentation site](https://docs.verapdf.org/).

Quick Start
-----------
### Pre-requisites

In order to build the library you'll need:

 * Java 8 - 21, which can be downloaded [from Oracle](https://www.oracle.com/technetwork/java/javase/downloads/index.html), or for Linux users [OpenJDK](https://openjdk.java.net/install/index.html).
 * [Maven v3+](https://maven.apache.org/)

Life will be easier if you also use [Git](https://git-scm.com/) to obtain and manage the source.

### Building veraPDF
First you'll need to obtain a version of the veraPDF-library source code. You can compile either the latest release version or the latest development source.

#### Downloading the latest release source
Use Git to clone the repository and ensure that the `master` branch is checked out:

    git clone https://github.com/veraPDF/veraPDF-library
    cd veraPDF-library
    git checkout master

or download the latest [tar archive](https://github.com/veraPDF/veraPDF-library/archive/master.tar.gz "veraPDF-library latest GitHub tar archive") or [zip archive](https://github.com/veraPDF/veraPDF-library/archive/master.zip "veraPDF-library latest GitHub zip archive") from GitHub.

#### Downloading the latest development source
Use Git to clone the repository and ensure that the `integration` branch is checked out:

    git clone https://github.com/veraPDF/veraPDF-library
    cd veraPDF-library
    git checkout integration

or download the latest [tar archive](https://github.com/veraPDF/veraPDF-library/archive/integration.tar.gz "veraPDF-library latest GitHub tar archive") or [zip archive](https://github.com/veraPDF/veraPDF-library/archive/integration.zip "veraPDF-library latest GitHub zip archive") from GitHub.

#### Use Maven to compile the source
Call Maven install:

    mvn clean install
