package com.github.hdghg.xmpplib.api;

import org.apache.commons.codec.binary.Base64;
import sun.security.provider.X509Factory;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * This class sysouts certificate
 */
public class SysoutCertificate implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        System.out.println(s);
        for (X509Certificate x509Certificate : x509Certificates) {
            System.out.println(X509Factory.BEGIN_CERT);
            System.out.println(Base64.encodeBase64URLSafeString(x509Certificate.getEncoded()));
            System.out.println(X509Factory.END_CERT);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
