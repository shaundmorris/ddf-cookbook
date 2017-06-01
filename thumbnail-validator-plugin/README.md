# [Distributed Data Framework \(DDF\)](http://ddf.codice.org/)

Distributed Data Framework (DDF) is an open source, modular integration framework. 

# Catalog PreIngestPlugin Recipe

## Introduction

Plugins extend the functionality of the Catalog Framework by performing actions at specified times during a transaction. 
Plugins can be Pre-Ingest, Post-Ingest, Pre-Query, Post-Query, Pre-Subscription, Pre-Delivery, Pre-Resource, or Post-Resource.
By implementing these interfaces, actions can be performed at the desired time. 
A Pre-Ingest Plugin is a plugin that receives Create, Update, or Delete requests and is executed before the CatalogProvider receives the request.
The most popular uses for a Pre-Ingest Plugin is to validate a metacard or modify the metacard before it reaches the CatalogProvider.

## Overview

In this lab, you will create a Pre-Ingest Plugin that validates that an incoming metacard's thumbnail is below a certain threshold.
If the thumbnail is too large, then the plugin will stop the metacard from being ingested.
If it is below the minimum, the plugin will allow the metacard to be passed onto the Catalog Provider for ingest.

## Objectives

* Familiarity with the CatalogFramework
* Pre-Ingest Plugin
* Catalog REST Endpoint

## Prerequisites

Installed JDK 8 and ensured that the JAVA_HOME environment variable is set to the JDK and not a JRE. Instructions on setting environment variable
Maven 3 installed
A settings.xml file with the following content in your <USER.HOME>/.m2 folder such as C:/Users/John_Doe/.m2 . If the .m2 folder did not exist, you should have created it.
```
    <settings>
        <profiles>
            <profile>
                <id>ddf</id>
                <activation>
                    <activeByDefault>true</activeByDefault>
                </activation>
                <repositories>
                    <repository>
                        <id>ddf-releases</id>
                        <name>DDF Releases</name>
                        <url>http://artifacts.codice.org/content/repositories/releases/</url>
                    </repository>
                </repositories>
            </profile>
        </profiles>
    </settings>
```
* Downloaded and installed a distribution of [DDF](http://codice.org/ddf/Downloads.html).

* To install, simply unzip the distribution archive. Take note of your DDF home directory.

* In src/main/resources/sample-requests good-thumbnail.json and bad-thumbnail.json

* Installed curl

  For Windows 7 64 bit, you can download it [here.](https://curl.haxx.se/download.html)
  It requires a particular Microsoft library to be installed. Curl is pre-installed on Mac

## Procedures

### Developing and Building Your Bundle

1. Open a command prompt or terminal window and change directories to the root of the plugin project you downloaded in the prerequisites.

	```cd <PROJECT_ROOT_DIRECTORY>```

2. Run the Maven build (this will create the Pre-Ingest bundle)

   ``` mvn install```
3. If DDF has already been started, then skip to the next step. Otherwise if DDF has not been started, navigate to <DDF_INSTALL_DIRECTORY>/bin and then execute ./ddf for Mac or Linux users or ddf.bat for Windows users. A new command prompt or terminal window should appear.
4. Go through the default install in your browser at https://localhost:8993

## Deploy Your Bundle
1. Hot deploy your newly created bundle into DDF.
   * Copy your bundle JAR file from the target directory in your project
   * You should be able to find it at <PROJECT_ROOT_DIRECTORY>/target. Paste your bundle jar file into <DDF_INSTALLATION_DIRECTORY>/deploy
2. Bring to focus the DDF Command Line console and type: list
   * ``` ddf@local>list ```
3. You should be able to confirm that Plugin is in ACTIVE state and Blueprint State should be CREATED.
Check the bundle's headers by typing at the Command Line Console
   * ``` ddf@local>headers <BUNDLE_ID_NUMBER> ```

## Test Plugin
1. Open a separate command prompt in your operating system. Change directory to where you stored the json files, specifically where the bad-thumbnail.json file is stored. Type:
	* ``` cd src/main/resources/sample-requests ```
2. Type the following curl command to POST to the REST Endpoint. This will ingest a metacard into the catalog
	* ``` curl -X POST -i -H "Content-Type: application/json" -d "@bad-thumbnail.json" https://localhost:8993/services/catalog ```
	* -X specifies type  of http operation
	* -H adds http HEADER
	* -i gets http headers back from the endpoint
	* Notice that in the command prompt, it returns a Server Error such as something like this
	 ```
      HTTP/1.1 500 Server Error Content-Type: text/html
      Date: Tue, 17 Mar 2015 22:08:29 GMT
      Content-Length: 137
      Server: Jetty(8.1.9.v20130131)
      <pre>Error while storing entry in catalog: Error during pre-ingest service invocation:
      Thumbnail is beyond maximum threshold size.</pre>
     ```
 Verify the REST Endpoint is up and running by looking at the Web services listed at this address https://localhost:8993/services (login as admin/admin)

 3. POST the good-thumbnail.json file
    ```curl -X POST -i -H "Content-Type: application/json" -d "@good-thumbnail.json" https://localhost:8993/services/catalog ```
    * You should now get a positive response, something similar to the following:
    * ```
      HTTP/1.1 201 Created
	  Content-Length: 0
	  Date: Tue, 17 Mar 2015 22:12:29 GMT
	  id: 45145440fb0c482687048894de3490b6
	  Location: https://localhost:8993/services/catalog/45145440fb0c482687048894de3490b
	  Server: Jetty(8.1.9.v20130131)
      ```
4. Verify it worked by querying the REST endpoint by following the address in Location which looks something similar to http://localhost:8181/services/catalog/ff47eb1ebcac41b68b1a64987ca3455b

## Summary
* In this lab you should have learned:
* how to make a Pre-Ingest Plugin
* how to build a bundle
* how to start DDF
* how to install a feature from the command line console
