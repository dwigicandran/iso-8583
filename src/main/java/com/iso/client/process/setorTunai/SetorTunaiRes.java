package com.iso.client.process.setorTunai;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.iso.client.configuration.PackagerConfig;
import com.iso.client.configuration.Util;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jpos.iso.ISOException;

import lombok.extern.slf4j.Slf4j;

//created By Dwigi Candra N - Agustus 2020

@Slf4j
public class SetorTunaiRes implements Processor {

    PackagerConfig packagerConfig = new PackagerConfig();
    Util util = new Util();

    @Override
    public void process(Exchange exchange) throws Exception {
        // TODO Auto-generated method stub
        String isoMessage = exchange.getIn().getBody(String.class);
        exchange.getIn().setBody(unpackFromIso(isoMessage));

    }

    private Map unpackFromIso(String stringMsg) {
        Map resultMap = new HashMap();
        
        try {
            org.jpos.iso.ISOMsg isoMsg = new org.jpos.iso.ISOMsg();
            isoMsg.setPackager(packagerConfig.getPackagerFinancial());
            isoMsg.unpack(stringMsg.getBytes());

            if (isoMsg.getString(39).equals("00")) {
                resultMap.put("name", isoMsg.getString(43));
                resultMap.put("totalDebit",util.formatCurrency(isoMsg.getString(88), 0));
                resultMap.put("message", isoMsg.getString(120));
                resultMap.put("accountNumber", isoMsg.getString(102));
                resultMap.put("responseCode", isoMsg.getString(39));
            } else {
                resultMap.put("responseCode", isoMsg.getString(39));
                resultMap.put("message", "failed ");
            }

            resultMap.put("ISO", stringMsg);
        } catch (ISOException e) {
            e.printStackTrace();
            resultMap.put("responseCode", "99");
            resultMap.put("message", "failed ");
        } catch (IOException e) {
            e.printStackTrace();
            resultMap.put("message", "failed ");
            resultMap.put("responseCode", "99");
        }
        return resultMap;
    }
    
}