package com.iso.client.router;

import com.iso.client.process.echo.EchoReqProcess;
import com.iso.client.process.echo.EchoResProcess;
import org.apache.camel.Exchange;


import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


//created By Dwi S - Agustus 2020
@Component
public class NetworkRoute extends RouteBuilder {

    EchoReqProcess reqProcess = new EchoReqProcess();
    EchoResProcess resProcess = new EchoResProcess();

    @Override
    public void configure() {
        from("seda:ECHO_TEST")
                .doTry()
                .process(reqProcess)
                .to("netty:tcp://{{remote.tcp.server.host}}:{{remote.tcp.server.port}}?"
                        + "encoders=#stringToByteEncoder&decoders=#byteToStringDecoder")
                .process(resProcess)
                .removeHeaders("*")
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"));


    }
}
