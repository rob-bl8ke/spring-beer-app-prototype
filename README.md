# Java Spring and VS Code

This document contains everything you'll need to set up Java and Spring Boot with Visual Studio Code.

- [Spring Boot support in Visual Studio Code | Spring boot with Microsoft VS Code|Spring boot in VSCode](https://www.youtube.com/watch?v=RBmWIACTiKI)

## Pre-requisites
- You have Java and Maven installed. Check this by typing `mvn -v` and `javac -v` and `java -v` at the terminal.

## Install Extension Packs

- Install the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) (Microsoft)
- Install the [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack) (VMWare)


## Generating a Spring Maven project with dependencies

Let's create a Spring Maven project by clicking CTRL+SHIFT+P or simply opening the command palette. 
- When prompted for a Spring Boot version choose the version you want. 
- Type "spring" and look for "Spring Initializr: Create a maven project".
- Select Java as the  language.
- Type in a Group Id, Artifact Id.
- Select Jar as the packaging type and specify your version.

At this point you'll be given the option to add dependencies. For instance you could select:

- Lombok Developer Tools (org.projectlombok)
- Spring Boot Developer Tools

A dialog box will prompt you as to where to generate the folder. Select a folder and it will open in VS Code.

- Right click on the main application `.java` file and click "Run Java".
- The application will run.

Create a controller at the same level as your main application `.java` file. Name it `DummyController.json`. Add the following code:

```java
package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class DummyController {
    @RequestMapping("/")
    public String displayDefaultMessage() {
        return "Hello World, Spring Boot!";
    }
}
```
Navigate to `http://localhost:8080/` and you should see the response in the browser.

## Managing Versions

The extension pack is using a version of Java that comes bundled with VS Code. Look closely at the output and see this line at the start of execution.

```
& 'C:\Users\robert.blake\.vscode\extensions\redhat.java-1.36.0-win32-x64\jre\17.0.13-win32-x86_64\bin\java.exe' '@C:\Users\ROBERT~1.BLA\AppData\Local\Temp\cp_177ubyuock4g9douk4wgn9kbz.argfile' 'com.example.demo.DemoApplication'
```

You may wish to use a different version of Spring Boot or Java that is not available in the bundle.

One can manually change the the version for Spring Boot like such:
```xml
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.7.18</version>
		<relativePath/>
	</parent>
```
And one can do the same thing with the Java Version. Let's say you need to work with Java 8:
```xml
	<properties>
		<java.version>1.8</java.version>
	</properties>
```

### Changing things globally

- Go to View -> Command Pallette (CTRL+ALT+P).
- Search for Java: Configure Java Runtime
- Now you can select any Java JDK you have installed.

### Changing things for your Workspace (current folder/project)

- Open Settings (`CRTL + ,`).
- Ensure you select the "Workspace" tab.
- Search for "Java runtime" and look for a setting like "Java > Configuration: Runtimes".

You can manually change the settings.json and select a runtime by choosing one and supplying the path to it.

```json
{
    "java.configuration.runtimes": [
        {"name": "JavaSE-23", "path": "C:\\Program Files\\Java\\jdk-23\\"}
    ]
}
```

### You struggled to get this to work

- The way you found (which isn't workspace specific) is Command Pallette (CTRL+ALT+P) and then find "Java: Configure Java Runtime", and then simply change the JDK runtime there.

# Important Links

- [A Java DSL for reading JSON documents](https://github.com/json-path/JsonPath)
- [From Zero to Hero coding Spring Boot applications in VS Code](https://www.youtube.com/watch?v=XbpFSyeMYfg&list=WL&index=16)
- [Spring Pet Clinic on GitHub](https://github.com/spring-projects/spring-petclinic)