package com.eb.signingtest;

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

    }
}
