Release Process
===============
Introduction
------------
###Audience
 - veraPDF development staff looking to release a new version;
 - anyone interested in knowing how our release process works; and
 - anyone who wants to change the version number or re-package our software.

###Pre-requisites
If you're still reading you'll also need an understanding and some experience using [Git](https://git-scm.com) and [Maven](https://maven.apache.org/). A quick brush up on [Semantic Versioning](http://semver.org/) and a typical [Git branching workflow](http://nvie.com/posts/a-successful-git-branching-model/) and we're ready to go.

Releasing
---------
The veraPDF software projects use the `MINOR` version number to indicate the development status of a particular version. An even number signifies a release version while an odd numbers are assigned to development prototypes. We increment the `MINOR` version number twice at each release milestone. The first increment is from the current odd numbered development version number to the new even release version.

###Getting to integration
You should use the latest version of the `integration` branch of the project. In the following example we'll start from scratch and assume:
 - we're releasing `0.6.x` from development version `0.5.x`
 - you're using a bash shell; and
 - the `git remote` name for the repo used is `origin`.

Clone the repo:

    git clone git@github.com:veraPDF/veraPDF-library.git
    cd veraPDF-library

If you already have the git repository cloned then from the repo sub-tree:

    git fetch origin

We want to be sure we have the latest changes:

    git checkout -b integration origin/integration
    git pull origin integration

Finally check that we have no uncommitted local changes, i.e:

    git status

outputs:

    On branch integration
    Your branch is up-to-date with 'origin/integration'.

    nothing to commit, working directory clean

###Bumping minor
We bump the `MINOR` version on integration from `5` to `6` using Maven to set the version:

    mvn versions:set -DnewVersion=0.6.0

Now check this has worked so:

    git status

checks if we've changed anything, there should be some POM that are altered, e.g:

    On branch integration
    Your branch is up-to-date with 'origin/integration'.

    Changes not staged for commit:
    (use "git add <file>..." to update what will be committed)
    (use "git checkout -- <file>..." to discard changes in working directory)

        modified:   core/pom.xml
        modified:   feature-report/pom.xml
        modified:   gui/pom.xml
        modified:   installer/pom.xml
        modified:   legacy-types/pom.xml
        modified:   metadata-fixer/pom.xml
        modified:   model-implementation/pom.xml
        modified:   pom.xml
        modified:   verapdf-qa/pom.xml

    no changes added to commit (use "git add" and/or "git commit -a")

or issuing:

    cat pom.xml | grep version

will show the version numbers in the POM.

###Create a release branch
If the version has been set properly then add the changes to Git and create a release branch:

    git add .
    git commit -m "Bumped minor version 0.5->0.6 for release."
    git checkout -b release-0.6
    git push --set-upstream origin release-0.6

###Bump minor for integration to 0.7
We now need to move the minor version number on the `integration` branch to 0.7 for future development:

    git checkout integration
    mvn versions:set -DnewVersion=0.7.0-SNAPSHOT
    git add .
    git commit -m "Bumped minor version 0.6->0.7 for development."
    git push origin

Now we have a new v0.6 release branch `release-0.6` while integration is ready for development on the new 0.7 versioned `integration` branch.
