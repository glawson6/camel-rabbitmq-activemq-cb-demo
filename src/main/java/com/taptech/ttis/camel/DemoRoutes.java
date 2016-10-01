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
package com.taptech.ttis.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class DemoRoutes extends RouteBuilder {

    @Autowired
    ObjectToStringProcessor objectToStringProcessor;

    @Autowired
    @Qualifier("rabbitEndpoint")
    Endpoint rabbitEndpoint;

    @Override
    public void configure() {
        // you can configure the route rule with Java DSL here
        from("timer:trigger?period=1s").routeId("toActiveMQ").streamCaching()
                .setBody(constant("ACTIVEMQ"))
            .bean("demoMessageProducer","createMessage(${body}")
                /*
            .hystrix()
                .to("http://localhost:9090/service1")
            //.onFallback()
            // we use a fallback without network that provides a repsonse message immediately
            //    .transform().simple("Fallback ${body}")
            .onFallbackViaNetwork()
                // we use fallback via network where we call a 2nd service
                .to("http://localhost:7070/service2")
            .end()
            */

                .process(objectToStringProcessor)
                .log(" Client request: ${body}")
                .to("activemq:topic:VirtualTopic.Count");

        from("activemq:Consumer.A.VirtualTopic.Count")
                .log("Client response ConsumerA: ${body}");


        from("activemq:Consumer.B.VirtualTopic.Count")
                .log("Client response ConsumerB: ${body}");

        from("timer:trigger?period=1s").routeId("toRabbitMQ").streamCaching()
                .setBody(constant("RABBITMQ"))
                .bean("demoMessageProducer","createMessage(${body}")
                /*
            .hystrix()
                .to("http://localhost:9090/service1")
            //.onFallback()
            // we use a fallback without network that provides a repsonse message immediately
            //    .transform().simple("Fallback ${body}")
            .onFallbackViaNetwork()
                // we use fallback via network where we call a 2nd service
                .to("http://localhost:7070/service2")
            .end()
            */

                .process(objectToStringProcessor)
                .log(" Client request: ${body}")
                .to(rabbitEndpoint);

        from(rabbitEndpoint)
                .log("Client response from Rabbit MQ ConsumerA: ${body}");



    }

}
