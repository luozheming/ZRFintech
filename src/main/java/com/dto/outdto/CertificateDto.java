package com.dto.outdto;

import lombok.Data;

import java.security.PrivateKey;
import java.security.PublicKey;

@Data
public class CertificateDto {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String serialNumber;
}
