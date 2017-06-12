package com.nuanyou.cms.service;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.poi.ss.formula.functions.T;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 * Created by Byron on 2017/6/12.
 */
public interface HttpService {
    String doGet(URI uri) throws IOException;

    String doPost(URI uri, String request) throws IOException;

    String doPost(URI uri, HttpEntity request) throws IOException;

    <CodePayResponse> T doPostJson(URI uri, String request, Class<CodePayResponse> clazz) throws IOException;

    <CodePayResponse> T doPostJson(URI uri, HttpEntity request, Class<CodePayResponse> clazz) throws IOException;

    <CodePayResponse> T doGetJson(URI uri, Class<CodePayResponse> clazz) throws IOException;

    String doSSLPost(URI uri, String request, InputStream inputStream, char[] password) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;

    String doPost(URI uri, StringEntity request, InputStream inputStream, char[] password) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException;
}
