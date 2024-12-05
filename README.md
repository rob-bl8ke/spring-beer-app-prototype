# Java Spring and VS Code

This document contains everything you'll need to set up Java and Spring Boot with Visual Studio Code.

- [Spring Boot support in Visual Studio Code | Spring boot with Microsoft VS Code|Spring boot in VSCode](https://www.youtube.com/watch?v=RBmWIACTiKI)

## Pre-requisites
- You have Java and Maven installed. Check this by typing `mvn -v` and `javac -v` and `java -v` at the terminal.

## Install Extension Packs

- Install the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) (Microsoft)
- Install the [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=vmware.vscode-boot-dev-pack) (VMWare)


## Day-to-day Dev

#### Run the Tests

- Navigate to "Tests".
- Click the play or debug button to run the test(s) selection.

#### Run as a particular Profile
- Navigate to the Spring Boot dashboard.
- Find the Apps pane. Under it you'll see the app listed.
- Right-click it and choose "Run with profile..." or "Debug with profile".

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

## Check your `classpath`

```java
String classpathStr = System.getProperty("java.class.path");
System.out.print(classpathStr);
```

# MySQL and H2

When adding the MySQL connector depencency to the `pom.xml` file and running the application you'll still see that the connection is to the `h2` database.

This is the desired behavior until MySQL is configured. Spring boot will always fall back to h2. MySQL will be configured through a `profile`. Allows for control over when one is used above the other. In this case:
- The h2 database is used for tests
- The MySQL database is used for a more realistic purpose.

```
2024-11-15T11:39:10.629+02:00  INFO 19084 --- [spring-6-rest-api] [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:3d27779d-e8b3-495c-8ce1-7714f4fe3e73 user=SA
```

Create a new file under the `resources` folder. Any file that follows this pattern will be recognised as a profile file. To specifically get information about what the properties for this profile are for MySQL, [see the applicable lecture](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33859396).

```
application-[profilename].properties
```

In VS Code, if one has installed the Spring Boot Dashboard, navigate to Apps and then right click on the app and select "Run with Profile". Here is a [video walkthrough example](https://github.com/microsoft/vscode-spring-boot-dashboard/pull/309#issuecomment-1499866377). Otherwise, if you don't mind checking in your `launch.json` file you [can set up configurations as below](https://github.com/microsoft/vscode-spring-boot-dashboard/issues/50#issuecomment-447411590):

```json
{
    "type": "java",
    "name": "Debug (Launch)-PetClinicApplication<spring-petclinic>",
    "request": "launch",
    "cwd": "${workspaceFolder}",
    "console": "internalConsole",
    "stopOnEntry": false,
    "mainClass": "org.springframework.samples.petclinic.PetClinicApplication",
    "projectName": "spring-petclinic",
    "args": "--spring.profiles.active=profile1,profile2",     // <-- Choice 1. via commandline  args
    "vmArgs": "-Dspring.profiles.active=profile1,profile2"  // <-- Choice 2.  via a java system property
}
```
At this point when the profile is run it will become clear that MySQL connection has beend added and the connection is present.
```
2024-11-15T12:04:33.365+02:00  INFO 15184 --- [spring-6-rest-api] [  restartedMain] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection com.mysql.cj.jdbc.ConnectionImpl@337c46d0
```

> 💥💥 You may find that you'll get tons of errors when the profile runner executes. Take a good look at the mappings on the entity classes to see how you get around this. You can also set `logging.level.org.hibernate.orm.jdbc.bind=trace` to help you find and fix these issues. Just make sure you don't leave it on in a production environment.

### Connection Pooling with Hikari

Following properties in your profile help manage connection pooling. Open MySQL Workbench and navigate to Server Status. Have a look at the number of connections as you play wit these settings.

```
spring.datasource.hikari.pool-name=RestDB-Pooling
spring.datasource.hikari.maximum-pool-size=5
```

### Database Migrations with Flyway

- See [this lesson](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/34442916)
- [Execute Flyway database migrations on start up](https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/reference/html/howto-database-initialization.html#howto-execute-flyway-database-migrations-on-startup)

# Random Stuff

### Use the debug console in VS Code

All `print` and `println` stuff is written to the debug console. If you can't find it run a filter search in the console. Here is an example of printing information to this console while running tests:

```java
    @Test
    void testBeerOrders() {
        // Look in the "debug console" of VS Code
        System.out.println("The number of beer orders are: " + beerOrderRepository.count());
        System.out.println("The number of customers are: " + customerRepository.count());
        System.out.println("The number of beers are: " + beerRepository.count());

        System.out.println(testCustomer.getName());
        System.out.println(testBeer.getBeerName());
    }
```

# Important Links

- [A Java DSL for reading JSON documents](https://github.com/json-path/JsonPath)
- [From Zero to Hero coding Spring Boot applications in VS Code](https://www.youtube.com/watch?v=XbpFSyeMYfg&list=WL&index=16)
- [Spring Pet Clinic on GitHub (JPA and Hibernate)](https://github.com/spring-projects/spring-petclinic)
- [Database Initialization with Spring Boot](https://docs.spring.io/spring-boot/docs/2.1.x/reference/html/howto-database-initialization.html) and also see [the lesson](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33878898).
- [Processing data with Java SE 8 Streams, Part 1](https://www.oracle.com/technical-resources/articles/java/ma14-java-se-8-streams.html)