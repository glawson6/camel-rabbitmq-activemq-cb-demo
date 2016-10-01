package com.taptech.ttis.camel;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.StringWriter;

/**
 * Created by tap on 9/30/16.
 */
@Component
public class ObjectToStringProcessor implements Processor {

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public void process(Exchange exchange) throws Exception {
        StringWriter sw = new StringWriter();
        objectMapper.writeValue(sw,exchange.getIn().getBody());
        exchange.getIn().setBody(sw.toString());
    }
}
