package com.iso.client.router;

import com.iso.client.process.echo.InTransferReq;
import com.iso.client.process.echo.InTransferRes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class InTransferNetwork extends RouteBuilder{

    InTransferReq reqProcess = new InTransferReq();
    InTransferRes resProcess = new InTransferRes();

    @Override
    public void configure() {
        from("seda:INTERNALTRANSFER_TEST")
                .doTry()
                .process(reqProcess)
                .to("netty:tcp://{{remote.tcp.server.host}}:{{remote.tcp.server.port}}?"
                        + "encoders=#stringToByteEncoder&decoders=#byteToStringDecoder")
                .process(resProcess)
                .removeHeaders("*")
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"));


    }
    
}
