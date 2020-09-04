package com.iso.client.router.transferAntarBank;

import com.iso.client.process.transferAntarBank.OutTransferReq;
import com.iso.client.process.transferAntarBank.OutTransferRes;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OutTransferNetwork extends RouteBuilder{

    OutTransferReq reqProcess = new OutTransferReq();
    OutTransferRes resProcess = new OutTransferRes();

    @Override
    public void configure() {

        from("seda:OUTTRANSFER_TEST")
                .doTry()
                .process(reqProcess)
                .to("netty:tcp://{{remote.tcp.server.host}}:{{remote.tcp.server.port}}?"
                        + "encoders=#stringToByteEncoder&decoders=#byteToStringDecoder")
                .process(resProcess)
                .removeHeaders("*")
                .setHeader(Exchange.CONTENT_TYPE, simple("application/json"));



    }
    
}
