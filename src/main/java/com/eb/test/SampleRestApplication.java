
package com.eb.test;

import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.rs.security.httpsignature.MessageSigner;
import org.apache.cxf.rs.security.httpsignature.MessageVerifier;
import org.apache.cxf.rs.security.httpsignature.filters.CreateSignatureInterceptor;
import org.apache.cxf.rs.security.httpsignature.filters.VerifySignatureFilter;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;


public class SampleRestApplication {

    public static void main(String[] args) throws Exception {
        TestClient testClient = new TestClient();
        testClient.getStuff();
    }
}
