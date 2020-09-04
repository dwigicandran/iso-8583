package com.iso.client.router.transferAntarBank;

import com.iso.client.model.OutTransfer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class OutTransferRouter extends RouteBuilder{

    @Override
    public void configure() throws Exception {
        // TODO Auto-generated method stub


        restConfiguration()
        .component("netty-http")
        .host("{{expose.local.http.host}}")
        .port("{{expose.local.http.port}}")
        .bindingMode(RestBindingMode.json);

        rest("/service")
                .post("/outTransfer")
                .type(OutTransfer.class)
                .consumes("application/json")
                .produces("/application/json")
                .to("seda:OUTTRANSFER_TEST")

        ;

    }
    
}
