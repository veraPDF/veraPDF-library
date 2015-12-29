License Headers for Source Code
===============================
Introduction
------------
###Audience
 - veraPDF developers wanting to add a license header to the source code; and
 - anyone interested in knowing how our release process works.

###Pre-requisites
If you're still reading you'll also need an understanding and some experience using [Git](https://git-scm.com) and [Maven](https://maven.apache.org/).

License Headers
---------------
All veraPDF software is dual-licensed, see:

 - [GPLv3+](LICENSE.GPL "GNU General Public License, version 3")
 - [MPLv2+](LICENSE.MPL "Mozilla Public License, version 2.0")

The Maven license plug-in uses template files that it adds as headers to the source files. For the veraPDF-library these templates are available in the `license/template` sub-folder. There are two header files one for the [GPL version](https://raw.githubusercontent.com/veraPDF/veraPDF-library/master/license/template/GPL-3.txt), and another for the [MPL version](https://raw.githubusercontent.com/veraPDF/veraPDF-library/master/license/template/MPL-2.txt).

The following worked example shows how to add the license headers to the 0.8 release version of the veraPDF-library. Specifically we will use git and Maven to:

 - check out the 0.8 release branch;
 - create a temporary Git branch with GPL headers;
 - create a temporary Git branch with MPL headers; and
 - use Git to create a zip and tar archive of the headed code.

###Getting to the release branch
You should use the latest version of the `release-0.8` branch of the project. In the following example we'll start from scratch and assume:
 - you're using a bash shell; and
 - the `git remote` name for the repo used is `origin`.

Clone the repo:

    git clone git@github.com:veraPDF/veraPDF-library.git
    cd veraPDF-library

If you already have the git repository cloned then from the repo sub-tree:

    git fetch origin

We want to be sure we have the latest changes:

    git checkout -b release-0.8 origin/release-0.8
    git pull origin release-0.8

Finally check that we have no uncommitted local changes, i.e:

    git status

outputs:

    On branch release-0.8
    Your branch is up-to-date with 'origin/release-0.8'.

    nothing to commit, working directory clean

###Creating a GPL source headed branch
From the release-0.8 branch create a new branch for the licensed source files:

    git checkout -b release-0.8-gpl origin/release-0.8

The Maven POM holds profiles for both of the license headers, we simply invoke with the correct profile for the GPL header, test the changes and update the branch:

    mvn license:format -P apply-gpl-header
    mvn clean install
    git add .
    git commit -m "Added GPL source headers."

Now to create the MPL headed branch.

###Creating an MPL source headed branch
From the release-0.8 branch create a new branch for the licensed source files:

    git checkout -b release-0.8-mpl origin/release-0.8

Again we invoke Maven, this time with the profile for the MPL header, test the changes and update the branch:

    mvn license:format -P apply-mpl-header
    mvn clean install
    git add .
    git commit -m "Added MPL source headers."

###Creating the source archives
We can now use Git to create zip or tar source archives from our headed branches. To create a compressed tarball called `veraPDF-0.8-gpl.tar.gz` from our GPL headed branch `release-0.8-gpl` in the local directory:

    git archive --format=tar release-0.8-gpl | gzip > veraPDF-0.8-gpl.tar.gz

or use Git's built in tar.gz archiver:

    git archive --format=tar.gz release-0.8-gpl > veraPDF-0.8-gpl.tar.gz

or allow Git to infer the format:

    git archive --format=tar -o veraPDF-0.8-gpl.tar.gz release-0.8-gpl

To create a zip archive from the MPL headed branch `release-0.8-mpl` in the `/tmp` directory:

    git archive --format=zip release-0.8-mpl > veraPDF-0.8-mpl.zip
