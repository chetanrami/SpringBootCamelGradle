package org.chetan.camel.rest.example.spring.boot;

import org.apache.camel.Endpoint;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ChetanTestSupport {
    @Autowired
    ProducerTemplate producerTemplate;
    @Autowired
    ModelCamelContext camelContext;

    @EndpointInject(uri = "direct:getRestEndPoint")
    Endpoint getRestEndPointSource;
    @EndpointInject(uri = "direct:source")
    Endpoint source;
    @EndpointInject(uri = "direct:source2")
    Endpoint source2;

    @EndpointInject(uri = "mock:target")
    MockEndpoint target;
    @EndpointInject(uri = "mock:target2")
    MockEndpoint target2;
    @EndpointInject(uri = "mock:getRestEndPointTarget")
    MockEndpoint getRestEndPointTarget;
    @EndpointInject(uri = "mock:intermediateDirectRouteMocking")
    MockEndpoint intermediateDirectRouteMocking;

    private static Logger logger = LoggerFactory.getLogger(AdviceWithTest.class);
}
