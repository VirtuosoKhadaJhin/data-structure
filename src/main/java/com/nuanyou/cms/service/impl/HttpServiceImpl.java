package com.nuanyou.cms.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuanyou.cms.service.HttpService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * Created by xiejing on 2016/1/28.
 */
@Service
public class HttpServiceImpl implements HttpService {

    private HttpClientBuilder builder = HttpClientBuilder.create ();

    @Override
    public String doGet(URI uri) throws IOException {
        try {
            HttpGet get = new HttpGet ( uri );
            try (CloseableHttpResponse response = createHttpClient ().execute ( get )) {
                HttpEntity entity = response.getEntity ();
                String result = loadEntity ( entity );
                return result;
            }
        } catch (IOException e) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream ();
            e.printStackTrace ( new PrintStream ( stream ) );
            throw e;
        } finally {
            long endtime = System.currentTimeMillis ();
        }
    }

    private CloseableHttpClient createHttpClient() {
        builder.setConnectionManagerShared ( true );
//        builder.setConnectionManager()
        return builder.build ();
    }

    private CloseableHttpClient createHttpClient(InputStream inputStream, char[] password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance ( "PKCS12" );
        try {
            keyStore.load ( inputStream, password );
        } finally {
            inputStream.close ();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom ()
                .loadKeyMaterial ( keyStore, password )
                .build ();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory (
                sslcontext,
                new String[]{"TLSv1"},
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER );
        CloseableHttpClient httpclient = HttpClientBuilder.create ()
                .setSSLSocketFactory ( sslsf )
                .build ();
        return httpclient;
    }

    @Override
    public String doPost(URI uri, String request) throws IOException {
        return doPost ( uri, new StringEntity ( request, StandardCharsets.UTF_8 ) );
    }

    @Override
    public String doPost(URI uri, HttpEntity request) throws IOException {
        try {
            HttpPost post = new HttpPost ( uri );
            post.setEntity ( request );
            try (CloseableHttpResponse response = createHttpClient ().execute ( post )) {
                HttpEntity entity = response.getEntity ();
                String result = loadEntity ( entity );
                return result;
            }
        } catch (IOException e) {
            throw e;
        } finally {
        }
    }

    @Override
    public <T> T doPostJson(URI uri, String request, Class<T> clazz) throws IOException {
        return doPostJson ( uri, new StringEntity ( request, StandardCharsets.UTF_8 ), clazz );
    }

    @Override
    public <T> T doPostJson(URI uri, HttpEntity request, Class<T> clazz) throws IOException {
        try {
            HttpPost post = new HttpPost ( uri );
            post.setEntity ( request );
            post.setHeader ( "Accept", "application/json" );
//            post.setHeader("Content-type", "application/json");
            try (CloseableHttpResponse response = createHttpClient ().execute ( post )) {
                HttpEntity entity = response.getEntity ();
                String result = loadEntity ( entity );
                ObjectMapper mapper = new ObjectMapper ();
                mapper.disable ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
                return mapper.readValue ( result, clazz );
            }
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public <T> T doGetJson(URI uri, Class<T> clazz) throws IOException {
        try {
            HttpGet get = new HttpGet ( uri );
            try (CloseableHttpResponse response = createHttpClient ().execute ( get )) {
                HttpEntity entity = response.getEntity ();
                String result = loadEntity ( entity );
                ObjectMapper mapper = new ObjectMapper ();
                mapper.disable ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
                return mapper.readValue ( result, clazz );
            }
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public String doSSLPost(URI uri, String request, InputStream inputStream, char[] password) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return doPost ( uri, new StringEntity ( request, StandardCharsets.UTF_8 ), inputStream, password );
    }

    @Override
    public String doPost(URI uri, StringEntity request, InputStream inputStream, char[] password) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        try {
            HttpPost post = new HttpPost ( uri );
            post.setEntity ( request );
            try (CloseableHttpResponse response = createHttpClient ( inputStream, password ).execute ( post )) {
                HttpEntity entity = response.getEntity ();
                String result = loadEntity ( entity );
                return result;
            }
        } catch (CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException | KeyManagementException e) {
            throw e;
        }
    }

    private String loadEntity(HttpEntity entity) throws IOException {
        byte[] bytes = EntityUtils.toByteArray ( entity );
        return new String ( bytes, "UTF-8" );
    }
}
