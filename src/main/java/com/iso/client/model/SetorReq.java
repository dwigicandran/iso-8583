package com.iso.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SetorReq {
    
    String sourceAccountName,encryptedPinBlock,sourceAccountNumber,pan,amount;



    
}