package com.eb.test;

import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.httpsignature.MessageSigner;
import org.apache.cxf.rs.security.httpsignature.filters.CreateSignatureInterceptor;

import javax.ws.rs.core.Response;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Collections;

public class TestClient {
    public void getStuff() {
        CreateSignatureInterceptor createSignatureInterceptor = new CreateSignatureInterceptor();

        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(ClassLoaderUtils.getResourceAsStream("keystore.jks", this.getClass()),
                    "pw".toCharArray());

            PrivateKey privateKey = (PrivateKey)keyStore.getKey("signing-demo", "pw".toCharArray());

            MessageSigner messageSigner = new MessageSigner(keyId -> privateKey, "signing-demo-v1");
            createSignatureInterceptor.setMessageSigner(messageSigner);
        }
        catch(Exception e) {
            System.out.println(e);
        }

        final String serviceURI = "https://signingtest.azurewebsites.net/api/stuff";
        WebClient client = WebClient.create(serviceURI, Collections.singletonList(createSignatureInterceptor));

        ClientConfiguration config = WebClient.getConfig(client);
        config.getInInterceptors().add(new LoggingInInterceptor());
        config.getOutInterceptors().add(new LoggingOutInterceptor());

        client.header("Content-type", "text/plain");
        Response response = client.get();
        String responseString = response.readEntity(String.class);
    }
}
