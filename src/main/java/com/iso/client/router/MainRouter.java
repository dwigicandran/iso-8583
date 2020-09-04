package com.iso.client.router;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

//created By Dwi S - Agustus 2020
@Component
public class MainRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration().component("netty-http").host("{{expose.local.http.host}}").port("{{expose.local.http.port}}")
                .bindingMode(RestBindingMode.json);

        rest("/service")
                .get("/echo").consumes("application/json").produces("/application/json")
                .type(String.class).to("seda:ECHO_TEST")

        ;


    }


}
