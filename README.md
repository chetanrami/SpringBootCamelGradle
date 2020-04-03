# Spring Boot Example


### Introduction
This example shows how to work with the simple Camel application based on the Spring Boot.

The example generates messages using timer trigger, writes them to the standard output and the mock
endpoint (for testing purposes).

This example exposes Jolokia API and Spring Boot actuators endpoints (like metrics) via the webmvc endpoint. We consider
this as the best practice - Spring Boot applications with these API exposed can be easily monitored and managed by the
3rd parties tools.

This example packages your application as a JAR, but you can also package as a WAR and deploy to 
servlet containers like Tomcat. 

### Build
You will need to compile this example first:

	mvn install

### Run
Just go to MySpringBootApplication.java and run the main method...

You can also execute the JAR directly:

	java -jar target/camel-example-spring-boot.jar

You will see the message printed to the console every second.

To stop the example hit <kbd>ctrl</kbd>+<kbd>c</kbd>

### Remote Shell

The example ships with remote shell enabled which includes the Camel commands as well, so you can SSH into the running Camel application and use the camel commands to list / stop routes etc.

You can SSH into the JVM using

    ssh -p 2000 user@localhost

And then use `password` when the server will prompt to encode the password.


### Documentation

This example is documented at <http://camel.apache.org/spring-boot-example.html>

### Forum, Help, etc

If you hit an problems please let us know on the Camel Forums
	<http://camel.apache.org/discussion-forums.html>

Please help us make Apache Camel better - we appreciate any feedback you may
have.  Enjoy!



The Camel riders!

package com.fmr.rrk.futurestate.custom_rest_header_validations.actuatorservice;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Controller
@Validated
public class HelloWorldController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/hello-person")
    @ResponseBody
    public Greeting sayHello(@RequestParam(name = "name", required = false, defaultValue = "Stranger") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/hello-person/{id}")
    @ResponseBody
    public Greeting getByPersonId(
            @PathVariable @Min(value = 1, message = "=====> ID HAS TO BE GREATER THAN 1")
            @Max(value = 4, message = "=====> ID HAS TO BE LESS THAN OR EQUAL TO 4") long id) { //jsr 303 annotations
        if (id == 1) {
            return new Greeting(id, String.format(template, "Remo"));
        } else if (id == 2) {
            return new Greeting(id, String.format(template, "Bill"));
        } else if (id == 3) {
            return new Greeting(id, String.format(template, "Mike"));
        } else {
            return new Greeting(id, String.format(template, "Stranger"));
        }
    }

}
