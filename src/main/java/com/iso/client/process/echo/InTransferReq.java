package com.iso.client.process.echo;

import java.util.Date;

import com.iso.client.configuration.Constants;
import com.iso.client.configuration.PackagerConfig;
import com.iso.client.model.InTransfer;
import com.iso.client.utility.CurrentId;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InTransferReq implements Processor{

    private PackagerConfig config = new PackagerConfig();
    private ISOUtil isoUtil = new ISOUtil();
    CurrentId currentId = new CurrentId();

    @Override
    public void process(Exchange exchange) throws Exception {
        // TODO Auto-generated method stub
        InTransfer inTransfer = exchange.getMessage().getBody(InTransfer.class);
        exchange.getIn().setBody(new String(buildISOMessage(currentId.stanGenerator(),currentId.nextJournalId(),inTransfer)));

    }

    private byte[] buildISOMessage(String stan,String journalId,InTransfer inTransfer)throws Exception {
        Date dateNow = new Date();


        try {
            ISOMsg isoMsg = new ISOMsg("0200"); //network mti
            isoMsg.setPackager(config.getPackagerFinancial());
            isoMsg.set(2,inTransfer.getPan());//PAN
            isoMsg.set(3,Constants.INQUIRY_DE3_INTERNAL_TRANSFER_PROCESS_CODE);//PROCESSING CODE SETOR
            isoMsg.set(4,inTransfer.getAmount());//NOMINAL SETOR
            isoMsg.set(7, Constants.DE_007_TIME_FORMATTER.format(dateNow)); //TRANSMISSION DATE AND TIME
            isoMsg.set(11, stan); //SYSTEM TRACE AUDIT NUMBER
            isoMsg.set(12, Constants.DE_012_TIME_FORMATTER.format(dateNow)); //TIME, LOCAL TRANSACTION
            isoMsg.set(13, Constants.DE_013_TIME_FORMATTER.format(dateNow)); //DATE, LOCAL TRANSACTION
            isoMsg.set(37, journalId); //RETRIEVAL REVERENCE NUMBER
            isoMsg.set(43,inTransfer.getSourceAccountName());//CARD ACCEPTOR NAME
            isoMsg.set(48,ISOUtil.strpad(inTransfer.getDestinationAccountName(),30)  + inTransfer.getDestinationAccountNumber());
            isoMsg.set(49,Constants.INQUIRY_DE49_CURRENCY);//CURRENCY CODE TRANSACTION RP.
            isoMsg.set(52,inTransfer.getEncryptedPinBlock());//PIN
            isoMsg.set(102,inTransfer.getSourceAccountNumber());//NOMOR REKENING

            return isoMsg.pack();
        } catch (ISOException e) {
            throw new Exception(e);
        }
    }

    
}
