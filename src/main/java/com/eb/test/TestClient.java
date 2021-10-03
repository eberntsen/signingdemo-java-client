package com.eb.test;

import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.httpsignature.MessageSigner;
import org.apache.cxf.rs.security.httpsignature.MessageVerifier;
import org.apache.cxf.rs.security.httpsignature.filters.CreateSignatureInterceptor;
import org.apache.cxf.rs.security.httpsignature.filters.VerifySignatureFilter;
import org.springframework.core.io.ClassPathResource;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

public class TestClient {
    public void getStuff() throws Exception {

        //final String serviceURI = "http://localhost:8080/api/stuff";
        final String serviceURI = "https://signingtest.azurewebsites.net/api/stuff";

        List<Object> providers = new ArrayList<>();

        CreateSignatureInterceptor createSignatureInterceptor = new CreateSignatureInterceptor();
        VerifySignatureFilter verifySignatureFilter = new VerifySignatureFilter();

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(ClassLoaderUtils.getResourceAsStream("keystore.jks", this.getClass()),
                    "pw".toCharArray());

            Certificate certificate = keyStore.getCertificate("signing-demo");
            PrivateKey privateKey = (PrivateKey)keyStore.getKey("signing-demo", "pw".toCharArray());

            MessageVerifier messageVerifier = new MessageVerifier(keyId -> certificate.getPublicKey());
            verifySignatureFilter.setMessageVerifier(messageVerifier);
            verifySignatureFilter.setEnabled(true);

            MessageSigner messageSigner = new MessageSigner(keyId -> privateKey, "signing-demo-v1");
            createSignatureInterceptor.setMessageSigner(messageSigner);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        providers.add(createSignatureInterceptor);

        WebClient client = WebClient.create(serviceURI, providers);

        client.header("Content-type", "text/plain");

        //  client.type("multipart/mixed").accept("multipart/mixed");

        // MultipartBody body = createMultipartBody();

        ClientConfiguration config = WebClient.getConfig(client);
        config.getInInterceptors().add(new LoggingInInterceptor());
        config.getOutInterceptors().add(new LoggingOutInterceptor());

        Response response = client.invoke("GET", "");

        String responseAsString = response.readEntity(String.class);
        System.out.println(responseAsString);
    }
}
