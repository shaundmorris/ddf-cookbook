# Text Metacard Recipe

## Introduction

Transformers are utility objects used to transform a set of standard DDF components into a desired format or vice versa. Specifically a MetacardTransformer is used to transform a Metacard into some desired format useful to the end user or as input to another process. Programmatically a MetacardTransformer transforms a Metacard into a BinaryContent instance, which contains the translated Metacard in the desired final format. Metacard Transformers are utilized by requesting the Service from the OSGi Service Registry. See Included Metacard Transformers for more examples.

## Overview
In this lab, you will create a Metacard Transformer that transforms a Metacard into plain text. You will write your own implementation of the text transformation, deploy your bundle, register that transformer, and then use that transformer with the REST endpoint. You will be able to view your text by using a web browser.
### Objectives
Upon completing this lab you should have a better understanding of 
* Metacard Transformers
* Registering services in the OSGi Service Registry
* DDF REST Endpoint

## Prerequisites
* Installed Java JDK 8 and ensured that the JAVA_HOME environment variable is set to the JDK and not a JRE. Instructions on setting environment variable
* Maven 3 installed
* A settings.xml file with the following content in your <USER.HOME>/.m2 folder such as C:/Users/John_Doe/.m2 . If the .m2 folder did not exist, you should have created it.

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

* Downloaded and installed a distribution of DDF. To install, simply unzip the distribution archive. Take note of your DDF home directory
Downloaded the text-metacard-transformer project

## Procedures
### Developing and Building Your Bundle
1. Open a command prompt or terminal window and change directories to the root of the plugin project you downloaded in the prerequisites. 
   
   ```cd <PROJECT_ROOT_DIRECTORY>```
2. Run the tests that are provided in the project by executing the following commands at the project root level
	
    ```mvn test```
3. You should see a BUILD FAILURE similar to 
	```Results :
	Failed tests:   			
    test(ddf.training.catalog.transformer.metacard.TestTextMetacardTransformer)
	Tests run: 1, Failures: 1, Errors: 0, Skipped: 0
	[INFO] ------------------------------------------------------------------------
	[INFO] BUILD FAILURE
	[INFO] ------------------------------------------------------------------------
	[INFO] Total time: 1.998s
	[INFO] Finished at: Sat Sep 08 13:16:02 MST 2012
	[INFO] Final Memory: 6M/245M
	[INFO] ------------------------------------------------------------------------```
4. You will need to implement the getText method in the src/main/java/TextMetacardTransformer.java in order to allow the tests to pass Import the project into your favorite IDE or just modify the file directly with your favorite text editor. Continue to work on the method until the tests pass. After running mvn test that BUILD SUCCESS will appear.
5. After the tests pass, you can now build your bundle. Run the maven build by doing a 
	
    ```mvn install```
6. Once again a BUILD SUCCESS will appear. At the root level of the plugin project is a target directory. All the artifacts that Maven builds are by default stored in the target directory. You should see in the target folder a .jar file.

## Project Setup
1. If DDF has already been started, then skip to the next step. Otherwise if DDF has not been started, navigate to <DDF_INSTALL_DIRECTORY>/bin and then execute ./ddf for Mac or Linux users or ddf.bat for Windows users. A new command prompt or terminal window should appear.
	* Once DDF has started, it brings up its own command line console with commands unique to its container. This tutorial will distinguish this command line prompt with the following prompt prefix
2. Go through the default install in your browser at https://localhost:8993
3. 	Wait until DDF has started before continuing with development. You can see if DDF has started properly by doing the following command
	* ```ddf@local> waitForReady ```

## Deploy Your Bundle
1. Hot deploy your newly created bundle into DDF.  
	* Copy your bundle jar file from the target directory in your project. You should be able to find it at <PROJECT_ROOT_DIRECTORY>/target. 
	* Paste your bundle jar file into <DDF_INSTALLATION_DIRECTORY>/deploy directory.
2. Bring to focus the DDF Command Line Console and type
	* ```ddf@local>list ```
3. 	Confirm that the intended transformer is in the ACTIVE  state and Blueprint State should be CREATED.

## Ready for Your Transformation?
1. In order to test if your transformation works, you will need data in your CatalogProvider. If you already have data in your CatalogProvider, then skip to the next step. Otherwise curl this file into DDF. 
	
    ``` curl -X POST -i -H "Content-Type: application/json" -d "@good-thumbnail.json" http://localhost:8181/services/catalog ``` 

2. With some data in your CatalogProvider, you simply only need to make a GET REST call by executing the right address in your web browser. Open up a web browser and hit the following address http://localhost:8993/services/catalog/<ID>?transform=text where the <ID> should be replaced with the ID of one of the metacards in the CatalogProvider. Therefore, an example would look like http://localhost:8993/services/catalog/ff47eb1ebcac41b68b1a64987ca3455b?transform=text.
	* A failure will appear. 
	```
   HTTP ERROR 500 Problem accessing /services/catalog/33b4aa1ba2034d6bbca2352571705185.
   Reason: org.apache.cxf.interceptor.Fault: Transformer text not found
   Caused by:
   java.lang.RuntimeException: org.apache.cxf.interceptor.Fault: Transformer text not found 
   ```
	* So what happened? The REST Endpoint searched for a transformer that could match the ID text, but did not find one in the Service Registry. In order for your transformer to be called, you will need to register your transformer as a MetacardTransformer in the Service Registry.

## Introducing Blueprint
1. Open in your IDE or in your favorite text editor the xml file <PROJECT_ROOT_DIRECTORY>/src/main/resources/OSGI-INF/blueprint/blueprint.xml 
	* Notice that the xml file is mostly blank and needs some input.
2. Make the file look like the following xml file 
   ```
   <blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0">

	<bean id="transformer"
		class="ddf.training.catalog.transformer.metacard.TextMetacardTransformer" />

	<!-- Register in the OSGi Service Registry -->
	<service ref="transformer" interface="ddf.catalog.transform.MetacardTransformer">
		<service-properties>
			<entry key="id" value="text" />
			<!-- shortname only exists for backwards compatibility -->
			<entry key="shortname" value="text" />
			<entry key="mime-type" value="text/plain" />
		</service-properties>
	</service>
	</blueprint> 
    ```
    * In Line 3, we are instructing the Blueprint implementation to construct the object.
Line 7 is where the constructed object will be registered under the ddf.catalog.transform.MetacardTransformer interface, along with its service properties. Notice that the service properties id and shortname are  text  which is what we appended previously in our web address (?transform=text).
3.  Recompile the bundle with the following command 
	```
    mvn clean install
	```
    *  BUILD SUCCESS will appear if there are no xml syntax errors

## Redeploy Your Bundle
1. Hot deploy your bundle once again. 
	* Copy your bundle jar file from the target directory in your project.You should be able to find it at <PROJECT_ROOT_DIRECTORY>/target.
	* Paste your bundle jar file into <DDF_INSTALLATION_DIRECTORY>/deploy  (overwrite the existing bundle)
2. Bring to focus the DDF Command Line Console. Check the status of your bundle by typing 
   * ``` ddf@local>list ```
   * The new bundle should have reached the ACTIVE state. 
3. Check the bundle's headers by typing at the Command Line Console
   * ``` ddf@local>headers <BUNDLE_ID_NUMBER> ```
   * Your headers output should look similar to the following 
    ```
  	Text Metacard Transformer (241)
	-------------------------------
	Manifest-Version = 1.0
	Bnd-LastModified = 1347140743815
	Tool = Bnd-1.15.0
    Built-By = Connexta
    Build-Jdk = 1.7.0_75
    Created-By = Apache Maven Bundle Plugin
    Bundle-Name = Text Metacard Transformer
    Bundle-Description = Metacard Transformer that transforms metacards into text
    Bundle-SymbolicName = text-metacard-transformer
    Bundle-Version = 0.0.1.SNAPSHOT
    Bundle-ManifestVersion = 2
    Export-Service =
        ddf.catalog.transform.MetacardTransformer;id=text;mimetype=text/plain;shortname=text
	Import-Package =
        ddf.catalog.data;version="[2.0,3)",
        ddf.catalog.transform;version="[2.0,3)",
        javax.activation,
        org.apache.commons.io;version="[2.1,3)",
        org.apache.commons.lang.builder;version="[2.6,3)",
        org.osgi.service.blueprint;version="[1.0.0,2.0.0)",
        org.slf4j;version="[1.7,2)",
        org.slf4j.ext;version="[1.7,2)"
   ```
   * Notice the new bundle is Exporting a Service, i.e. you are exporting an implementation of ddf.catalog.transform.MetacardTransformer.
It also is importing packages from the OSGi container in order to resolve its dependencies.

## Let's try again
1. Open up a web browser if it is not open and try the following address https://localhost:8993/services/catalog/<ID>?transform=text again where the <ID> should be replaced with the ID of one of the metacards in the CatalogProvider. Therefore, an example would look like https://localhost:8993/services/catalog/ff47eb1ebcac41b68b1a64987ca3455b?transform=text.
 	* If you see your text from the transformation, then you have succesfully created a MetacardTransformer. 

### Summary
In this lab the following should have been learned:
* how to make a Metacard Transformer
* how register a service in the OSGi Service Registry
* how to build a bundle


## Appendix
### Possible Implementations
```
private String getText(Metacard metacard) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (AttributeDescriptor ad : metacard.getMetacardType().getAttributeDescriptors()) {
			
			Attribute attribute = metacard.getAttribute(ad.getName());
			if (attribute != null) {
				builder.append("[").append(ad.getName()).append("=").append(attribute.getValue()).append("]");
			}
		}
		builder.append("}");
		return builder.toString();
	}
```
```
<blueprint xmlns="http://www.osgi.org/xmlns/blueprint/v1.0.0">

	<bean id="transformer"
		class="ddf.training.catalog.transformer.metacard.TextMetacardTransformer" />

	<!-- Register in the OSGi Service Registry -->
	<service ref="transformer" interface="ddf.catalog.transform.MetacardTransformer">
		<service-properties>
			<entry key="id" value="text" />
			<!-- shortname only exists for backwards compatibility -->
			<entry key="shortname" value="text" />
			<entry key="mime-type" value="text/plain" />
		</service-properties>
	</service>

</blueprint>
```