/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.chetan.camel.rest.example.spring.boot;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;

@Component
public class MySpringBootRouter extends RouteBuilder {

    @Autowired
    private HealthEndpoint health;

    public static final String LOG_NAME = "org.chetan.camel.rest.example.spring.boot";

    @Override
    public void configure() {

        //region restRoutes
        restConfiguration();

        rest().get("/abc")//http://localhost:8080/rest/abc
                .produces("text/plain").route()
                .transform().constant("This is rest abc get")
                .log(LoggingLevel.INFO, LOG_NAME, "get body:----- ${body}").end();

        rest().post("/cde")//http://localhost:8080/rest/cde
                .produces("text/plain").route()
                .log(LoggingLevel.INFO, LOG_NAME, "post body:----- ${body}")
                .transform().constant("This is rest cde post")
                .log(LoggingLevel.INFO, LOG_NAME, "After transform body:----- ${body}").end();
        //endregion restRoutes

        from("timer://simpleTimer?period=10000").routeId("simpleTimer_RouteID")
                .transform().simple("ref:myBean")
                .to("log:out").id("simpleTimer_ID");

        from("timer://status?period=3000").routeId("status_RouteID")
            .bean(health, "invoke")
            .log("Health is ${body}").id("status_ID");
    }

    @Bean
    String myBean() {
        return "I'm Spring bean!";
    }

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(
                new CamelHttpTransportServlet(), "/rest/*");
        servlet.setName("CamelServlet");
        return servlet;
    }

}
