package com.iso.client.process.echo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.iso.client.configuration.PackagerConfig;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutTransferRes implements Processor{


    PackagerConfig packagerConfig = new PackagerConfig();

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
                resultMap.put("bankCode", isoMsg.getString(63));
                resultMap.put("accountName", isoMsg.getString(43));
                resultMap.put("accountNumber", isoMsg.getString(102));
                resultMap.put("message", isoMsg.getString(120));
                resultMap.put("responseCode", isoMsg.getString(39));
                resultMap.put("destinationAccountNumber",ISOUtil.takeLastN(isoMsg.getString(48),10));
                resultMap.put("destinationAccountName",ISOUtil.takeFirstN(isoMsg.getString(48), 30));
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
