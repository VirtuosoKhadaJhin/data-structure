

package com.nuanyou.cms.sso.client.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * 工具类(不依赖于第三方jar)
 */
public final class CommonUtils {

    private static final Log LOG = LogFactory.getLog(CommonUtils.class);


    private CommonUtils() {
        // nothing to do
    }

    public static void assertNotNull(final Object object, final String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void assertNotEmpty(final Collection<?> c, final String message) {
        assertNotNull(c, message);
        if (c.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }


    public static void assertTrue(final boolean cond, final String message) {
        if (!cond) {
            throw new IllegalArgumentException(message);
        }
    }


    public static boolean isEmpty(final String string) {
        return string == null || string.length() == 0;
    }


    public static boolean isNotEmpty(final String string) {
        return !isEmpty(string);
    }


    public static boolean isBlank(final String string) {
        return isEmpty(string) || string.trim().length() == 0;
    }


    public static boolean isNotBlank(final String string) {
        return !isBlank(string);
    }


    public static String constructRedirectUrl(final String loginUrl, final String serviceParameterName, final String serviceUrl, final String state, Boolean relogin) {
        String reloginParam = relogin.toString();
        try {
            String url = loginUrl +
                    (loginUrl.indexOf("?") != -1 ? "&" : "?")
                    + serviceParameterName + "="
                    + URLEncoder.encode(serviceUrl, "UTF-8")
                    + (state != null ? "&state=#state".replace("#state", state) : "")
                    + (relogin != null ? "&relogin=#relogin".replace("#relogin", reloginParam) : "");
            return url.toString();
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 两个作用
     * 1 组装第一次重定向的URL有这些参数 serverName   资源路径  参数路径
     * 2 最后一次重定向时去掉code=...
     *
     * @param request
     * @param response
     * @param serverName
     * @param artifactParameterName
     * @return
     */
    public static String constructServiceUrl(final HttpServletRequest request,
                                             final HttpServletResponse response, final String serverName, final String artifactParameterName) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(serverName);
        buffer.append(request.getRequestURI());
        String queryString = request.getQueryString();
        omitArtifitParam(artifactParameterName, buffer, queryString);
        buffer = omitReloginParam(buffer);
        String returnValue = response.encodeURL(buffer.toString());
        LOG.debug("serviceUrl generated: " + returnValue);
        return returnValue;
    }

    private static StringBuilder omitReloginParam(StringBuilder buffer) {
        buffer = new StringBuilder(buffer.toString().replaceAll("\\?relogin=(true|false)", ""));
        buffer = new StringBuilder(buffer.toString().replaceAll("&relogin=(true|false)", ""));
        return buffer;
    }


    private static void omitArtifitParam(String artifactParameterName, StringBuilder buffer, String queryString) {
        if (CommonUtils.isNotBlank(queryString)) {
            final int location = queryString.indexOf(artifactParameterName + "=");
            if (location != 0) {//http://serverName:8080?ret=
                if (buffer.toString().contains("?")) {
                    buffer.append("&");
                } else {
                    buffer.append("?");
                }
                if (location == -1) {//ttp://serverName:8080?sdf=df
                    buffer.append(queryString);
                } else if (location > 0) {//http://serverName:8080?sdf=df&ret= 确保存在&ret 而非ret
                    final int actualLocation = queryString.indexOf("&" + artifactParameterName + "=");
                    if (actualLocation == -1) {
                        buffer.append(queryString);
                    } else if (actualLocation > 0) {
                        buffer.append(queryString.substring(0, actualLocation));
                    }
                }
            }
        }
    }


    public static String safeGetParameter(final HttpServletRequest request, final String parameter) {
        if ("POST".equals(request.getMethod()) && "logoutRequest".equals(parameter)) {
            return request.getParameter(parameter);
        }else{
            return request.getQueryString() == null || request.getQueryString().indexOf(parameter) == -1 ? null : request.getParameter(parameter);
        }

    }


    public static String getResponseFromServer(final URL constructedUrl, final String encoding) {
        return getResponseFromServer(constructedUrl, HttpsURLConnection.getDefaultHostnameVerifier(), "UTF-8");
    }


    public static String getResponseFromServer(final URL constructedUrl, final HostnameVerifier hostnameVerifier, final String encoding) {
        URLConnection conn = null;
        try {
            conn = constructedUrl.openConnection();
            if (conn instanceof HttpsURLConnection) {
                ((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier);
            }
            final BufferedReader in;

            if (CommonUtils.isEmpty(encoding)) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            }

            String line;
            final StringBuilder stringBuffer = new StringBuilder(255);

            while ((line = in.readLine()) != null) {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
            return stringBuffer.toString();
        } catch (final Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            if (conn != null && conn instanceof HttpURLConnection) {
                ((HttpURLConnection) conn).disconnect();
            }
        }

    }


    public static boolean isRequestExcluded(HttpServletRequest httpRequest, Pattern urlExcludePattern) {
        if (urlExcludePattern == null) {
            LOG.debug("urlExcludePattern is null");
            return true;
        }
        Boolean excluded = urlExcludePattern != null
                && urlExcludePattern.matcher(
                httpRequest.getRequestURI()
                        .substring(httpRequest.getContextPath().length())).matches();
        return excluded;
    }


//    public static void main(String[] args) {
//        String urlExcludePattern="^/test|^/dist/.*|^/favicon.*";
//        String url="/dist/list";
//        Pattern compile = Pattern.compile(urlExcludePattern);
//        Boolean excluded=compile != null
//            && compile.matcher(url).matches();
//        System.out.println(excluded);
//    }

//    public static void main11(String[] args) {
//        String bb="abd";
//        String a="ggg?sdf=a1&relogin=true";
//        String b=".*relogin=(true|false)+&.*";
//        System.out.println(a.matches(b));
//        System.out.println(a.replaceAll("\\?relogin=(true|false)","*"));
//        String v="http://127.0.0.1:8085/index?relogin=true";
//        System.out.println(v.replaceAll("\\?relogin=true",""));
//    }

}
