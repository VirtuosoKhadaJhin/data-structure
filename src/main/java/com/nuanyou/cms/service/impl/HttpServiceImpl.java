package com.nuanyou.cms.service.impl;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuanyou.cms.model.CodePayResponse;
import com.nuanyou.cms.service.HttpService;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

/**
 * Created by Byron on 2017/6/13.
 */
@Service
public class HttpServiceImpl implements HttpService {

    private static final Logger _LOGGER = LoggerFactory.getLogger(HttpServiceImpl.class);

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
            e.printStackTrace(new PrintStream(stream));
            throw e;
        } finally {
        }
    }

    private CloseableHttpClient createHttpClient() {
        builder.setConnectionManagerShared ( true );
        return builder.build ();
    }

    private CloseableHttpClient createHttpClient(InputStream inputStream, char[] password) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyManagementException {
        KeyStore keyStore = KeyStore.getInstance ( "PKCS12" );
        try {
            keyStore.load ( inputStream, password );
        } finally {
            inputStream.close ();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, password).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
        return httpclient;
    }

    @Override
    public String doPost(URI uri, String request) throws IOException {
        return doPost ( uri, new StringEntity ( request, StandardCharsets.UTF_8 ) );
    }

    @Override
    public String doRequest(HttpRequestBase request) throws IOException {
        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request)) {
            Header[] headers = response.getAllHeaders();
            HttpEntity entity = response.getEntity();
            String result = loadEntity(entity);
            return result;
        }
    }

    @Override
    public <T> T doPost(URI uri, List<NameValuePair> parameters, Class<T> tClass) throws IOException {
        HttpPost post = new HttpPost(uri);
        post.setEntity(new UrlEncodedFormEntity(parameters, StandardCharsets.UTF_8));
        String result = doRequest(post);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result, tClass);
    }

    @Override
    public String doPost(URI uri, HttpEntity request) throws IOException {
        try {
            HttpPost post = new HttpPost ( uri );
            post.setEntity ( request );
            CloseableHttpResponse response = createHttpClient().execute(post);
            HttpEntity entity = response.getEntity();
            String result = loadEntity(entity);
            return result;
        } catch (IOException e) {
            throw e;
        } finally {
        }
    }

    @Override
    public CodePayResponse doPostJson(URI uri, String request) throws IOException {
        return doPostJson(uri, new StringEntity(request, StandardCharsets.UTF_8));
    }

    @Override
    public CodePayResponse doPostJson(URI uri, HttpEntity request) throws IOException {
        try {
            HttpPost post = new HttpPost ( uri );
            post.setEntity ( request );
            post.setHeader ( "Accept", "application/json" );
            try (CloseableHttpResponse response = createHttpClient ().execute ( post )) {
                HttpEntity entity = response.getEntity ();
                String result = loadEntity ( entity );
                ObjectMapper mapper = new ObjectMapper ();
                mapper.disable ( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
                mapper.readValue(result, CodePayResponse.class);
            }
        } catch (IOException e) {
            throw e;
        }
        return null;
    }

    @Override
    public CodePayResponse doGetJson(URI uri) throws IOException {
        try {
            HttpGet get = new HttpGet ( uri );
            CloseableHttpResponse response = createHttpClient().execute(get);
            HttpEntity entity = response.getEntity();
            String result = loadEntity(entity);
            ObjectMapper mapper = new ObjectMapper();
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            CodePayResponse value = mapper.readValue(result, CodePayResponse.class);
            return value;
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public String doSSLPost(URI uri, String request, InputStream inputStream, char[] password) throws IOException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return doPost(uri, new StringEntity(request, StandardCharsets.UTF_8), inputStream, password);
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
