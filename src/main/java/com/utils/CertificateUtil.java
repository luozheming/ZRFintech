package com.utils;

import com.dto.outdto.CertificateDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

/**
 * KeyPairFactory
 *
 * @author dax
 * @since 13:41
 **/
public class CertificateUtil {

    private static KeyStore store;

    private static final Object lock = new Object();

    /**
     * 获取公私钥.
     *
     * @param keyPath  the key path
     * @param keyAlias the key alias
     * @param keyPass  password
     * @return the key pair
     */
    public static CertificateDto createPKCS12(String keyPath, String keyAlias, String keyPass) {
        CertificateDto certificateDto = new CertificateDto();
        try {
            InputStream is = new FileInputStream(new File(keyPath));
            char[] pem = keyPass.toCharArray();
            synchronized (lock) {
                if (store == null) {
                    synchronized (lock) {
                        store = KeyStore.getInstance("PKCS12");
                        store.load(is, pem);
                    }
                }
            }
            X509Certificate certificate = (X509Certificate) store.getCertificate(keyAlias);
            certificate.checkValidity();
            // 证书的序列号 也有用
            String serialNumber = certificate.getSerialNumber().toString(16).toUpperCase();
            // 证书的 公钥
            PublicKey publicKey = certificate.getPublicKey();
            // 证书的私钥
            PrivateKey privateKey = (PrivateKey) store.getKey(keyAlias, pem);
            certificateDto.setSerialNumber(serialNumber);
            certificateDto.setPublicKey(publicKey);
            certificateDto.setPrivateKey(privateKey);
            return certificateDto;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot load keys from store: " , e);
        }
    }
}
