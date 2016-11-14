package com.nuanyou.cms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class.getSimpleName());

    private static final SSLSocketFactory SSLFactory = initSSLSocketFactory();
    private static final SSLHandler SimpleVerifier = new SSLHandler();

    private static final String CharsetName = "UTF-8";
    private static final int Timeout = 60000;

    private static final String Separator = System.getProperty("line.separator");

    public static String get(String url, String... params) {
        return send("GET", buildUrl(url, params), params);
    }

    public static String get(String url, Map<String, String> params) {
        return send("GET", buildUrl(url, params));
    }

    public static String post(String url, String... params) {
        return send("POST", buildUrl(url, params), params);
    }

    public static String post(String url, Map<String, String> params) {
        return send("POST", buildUrl(url, params));
    }

    private static String send(String method, String url, String... request) {
        log.info("Method={}, Url={}, Request={}", method, url, request.length == 1 ? request : "null");

        HttpURLConnection connection = null;
        OutputStream os = null;
        BufferedReader reader = null;
        try {
            connection = createHttpConnection(url);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Connection", "Close");
            connection.setRequestProperty("Accept", "application/json");
            if (request.length == 1) {
                byte[] bytes = request[0].getBytes(CharsetName);
                connection.setRequestProperty("Content-length", String.valueOf(bytes.length));
                connection.setRequestProperty("Content-type", "application/json");
                os = connection.getOutputStream();
                os.write(bytes);
                os.flush();
            } else {
                connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            }

            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(new BufferedInputStream(is, 4096), CharsetName);
            reader = new BufferedReader(isr);

            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
                response.append(line).append(Separator);
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException("调用远程系统出错url=" + url, e);
        } finally {
            IOUtils.close(os);
            IOUtils.close(reader);
            if (connection != null)
                connection.disconnect();
        }
    }

    private static String buildUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url).append("?");
        Set<Entry<String, String>> entrySet = params.entrySet();
        for (Entry<String, String> entry : entrySet) {
            String name = entry.getKey();
            String value = entry.getValue();
            builder.append(name).append("=").append(value).append("&");
        }
        builder.deleteCharAt(builder.length() - 1);
        url = builder.toString();
        return url;
    }

    private static String buildUrl(String url, String... request) {
        if (request.length > 1) {
            StringBuilder builder = new StringBuilder(url).append("?");
            for (int i = 0; i < request.length; i += 2) {
                String name = request[i];
                String value = request[i + 1];
                builder.append(name).append("=").append(value).append("&");
            }
            builder.deleteCharAt(builder.length() - 1);
            url = builder.toString();
        }
        return url;
    }

    private static HttpURLConnection createHttpConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setReadTimeout(Timeout);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
            httpsConn.setSSLSocketFactory(SSLFactory);
            httpsConn.setHostnameVerifier(SimpleVerifier);
        }

        return (HttpURLConnection) connection;
    }

    private static SSLSocketFactory initSSLSocketFactory() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSLv3");
            sslContext.init(null, new TrustManager[]{SimpleVerifier}, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }

    private static class SSLHandler implements X509TrustManager, HostnameVerifier {
        private SSLHandler() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        /**
         * 可接受的 CA 发行者证书的非 null（可能为空）的数组
         */
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        /**
         * @param hostname 主机名
         * @param session  到主机的连接上使用的 SSLSession
         * @return 如果主机名是可接受的，则返回 true
         */
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}