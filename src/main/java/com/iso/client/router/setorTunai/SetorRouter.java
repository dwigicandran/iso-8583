package com.iso.client.router.setorTunai;

import com.iso.client.model.SetorReq;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class SetorRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration()
        .component("netty-http")
        .host("{{expose.local.http.host}}")
        .port("{{expose.local.http.port}}")
        .bindingMode(RestBindingMode.json);

        rest("/service")
                .post("/setor")
                .type(SetorReq.class)
                .consumes("application/json")
                .produces("/application/json")
                .to("seda:SETOR_TEST")

        ;


    }


}