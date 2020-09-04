package com.iso.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InTransfer {

    String sourceAccountName,encryptedPinBlock,sourceAccountNumber,pan,amount,destinationAccountName, destinationAccountNumber;
    
}
