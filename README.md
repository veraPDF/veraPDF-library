Adobe XMP Toolkit for Java Version 5.1.0
========================================

For information about the Extensible Metadata Platform (XMP), 
visit the XMP product page on the Adobe website: http://www.adobe.com/xmp.

The Java API contains only the XMPCore part of the XMP Toolkit;
it does NOT contain the XMPFiles component.

This file contains instructions for installing the XMPCore Java library
and example project for the Eclipse Java IDE 3.2 and higher,
and Java SDK 1.5.x or 1.6. 


To set up the projects in Eclipse 3.2 and higher:

1. Start Eclipse with an empty workspace
2. Choose  File > Import.
3. In the Wizard, choose Existing Projects into Workspace > Next
3. Click "Select root directory" and browse for the folder XMP-Toolkit-SDK-5.1.0/java
4. Two projects are displayed: "XMPCore" and "XMPCoreCoverage"
5. Select both and click Finish.


To set up the projects in Eclipse 3.0.x:

1. Start Eclipse with an empty workspace
2. Choose File > New > Project > Java Project
3. Enter the Project Name "XMPCore"
4. Click "Create project at external location" and select the folder "XMPCore" 
   (in the folder that contains this readme.txt file).
5. Click Finish.
6. To install the example, repeat steps 2 to 5, replacing "XMPCore" with "XMPCoreCoverage"


To build debug and release libraries of XMPCore, run the ANT script "build.xml" 
that is contained in the XMPCore project.
