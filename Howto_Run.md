# How to Run the Project #

These instructions indicate how to get and run the project under Eclipse 3.2 -- the steps necessary to do the same on older versions of Eclipse may vary from those presented here.

## Check Out Source ##

First, check out the source code from the repository using Eclipse and Subclipse.  This you can do by following the instructions on the Subversion\_Access page.

## Refresh Required Plugins ##

Open both the org.cc.dam and org.cc.xmp projects in your Eclipse workspace.  In the org.cc.dam project, open the cc-dam.product file, and click on the "Configuration" tab.  In the Plugins section, click the "Remove All" button.  Now click the "Add..." button, and select org.cc.dam and org.cc.xmp from the list that appears.  Click OK.  Back on the Configuration tab, click the "Add Required Plug-ins" button. Save the cc-dam.product file.

## Set the Compiler Level ##

This project requires a version of Java compatible with Sun Java 5 or higher. To make sure your compiler is generating code at this level, choose "Window" from the Eclipse main menubar and click on "Preferences". Expand the "Java" entry in the left-hand pane and click on the "Compiler" sub-entry. Under "JDK Compliance", set "Compiler compliance level" to 5.0 or higher (you may choose 6.0 at your option, assuming you can build and run Java 6 code). Click OK to set and exit the preferences.

## Run The Program ##

On the cc-dam.product file's "Overview" tab, at the bottom on the left-hand side, click the "Launch the product" link.