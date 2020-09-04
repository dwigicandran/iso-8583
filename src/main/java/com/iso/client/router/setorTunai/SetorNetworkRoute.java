package com.iso.client.router.setorTunai;

import com.iso.client.process.setorTunai.SetorTunaiReq;
import com.iso.client.process.setorTunai.SetorTunaiRes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SetorNetworkRoute extends RouteBuilder{

    SetorTunaiReq reqProcess = new SetorTunaiReq();
    SetorTunaiRes resProcess = new SetorTunaiRes();

    @Override
    public void configure() {
        from("seda:SETOR_TEST")
                .doTry()
                .process(reqProcess)
                .to("netty:tcp://{{remote.tcp.server.host}}:{{remote.tcp.server.port}}?"
                        + "encoders=#stringToByteEncoder&decoders=#byteToStringDecoder")
                .process(resProcess)
                .removeHeaders("*")
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"));


    }
    
}