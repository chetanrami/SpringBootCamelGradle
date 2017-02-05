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
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.chetan.camel.rest.example.spring.boot.utils.StringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MySpringBootRouter extends RouteBuilder {

    @Autowired
    private HealthEndpoint health;

    @Autowired
    private StringConverter sc;

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

        from("timer://simpleTimer?period=1000000").routeId("simpleTimer_RouteID")
                .transform().simple("ref:myBean")
                .to("log:out").id("simpleTimer_ID");

        from("timer://status?period=300000").routeId("status_RouteID")
            .bean(health, "invoke")
            .log("Health is ${body}").id("status_ID")
            .bean(StringConverter.class, "convert2Base64(${body})")
            .log("After convert2Base64 ${body}")
            .bean(StringConverter.class, "convertBase642String(${body})")
            .log("After Base64ToString ${body}");

        Namespaces ns = new Namespaces("env", "http://schemas.xmlsoap.org/soap/envelope/")
                .add("wsa", "http://www.w3.org/2005/08/addressing")
                .add("ns0", "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/");

        from("file:src/test/resources?include=SoapResponse_Orig.xml&move=.done")
                .log("-------------->>>>> ${body.class}")
                .setBody(simple("${bodyAs(String)}"))
                .log("-------------->>>>> ${body.class}")
                .setHeader("result").xpath("/*[name()='env:Envelope']/*[name()='env:Body']/*[name()='ns0:loadAndImportDataResponse']/*[name()='result']/text()", String.class)
                .log("-------------->>>>> ${header.result.class}, ${header.result}")
                .setHeader("msgID").xpath("/*[name()='env:Envelope']/*[name()='env:Header']/*[name()='wsa:MessageID']/text()", String.class)
                .log("-------------->>>>> ${header.result.class}, ${header.msgID}")
                .setHeader("action").xpath("/*[name()='env:Envelope']/*[name()='env:Header']/*[name()='wsa:Action']/text()", String.class)
                .log("-------------->>>>> ${header.action.class}, ${header.action}");
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
