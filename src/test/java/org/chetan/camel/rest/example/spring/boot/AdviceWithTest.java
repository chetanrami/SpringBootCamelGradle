package org.chetan.camel.rest.example.spring.boot;

import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringBootRunner.class)
@UseAdviceWith
@SpringBootApplication
@SpringBootTest(classes = AdviceWithTest.class)
@EnableAutoConfiguration
public class AdviceWithTest extends ChetanTestSupport{

    @Test
    public void shouldMockEndpoints() throws Exception {
        camelContext.getRouteDefinition("simpleTimer_RouteID").adviceWith(camelContext, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(source);
                weaveAddLast().to(target);
            }
        });
        camelContext.getRouteDefinition("status_RouteID").adviceWith(camelContext, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith(source2);
                weaveAddLast().to(target2);
            }
        });

        camelContext.start();

        MockEndpoint mock = camelContext.getEndpoint(target.getEndpointUri(), MockEndpoint.class);
        MockEndpoint mock2 = camelContext.getEndpoint(target2.getEndpointUri(), MockEndpoint.class);

        // Given
        mock.expectedMessageCount(1);
        mock2.expectedMessageCount(1);

        // When
        producerTemplate.sendBody(source, "abc");
        producerTemplate.sendBody(source2, "cde");

        // Then
        mock.assertIsSatisfied();
        mock2.assertIsSatisfied();
        Assert.assertEquals("I'm Spring bean!", mock.getExchanges().get(0).getIn().getBody());
    }

}