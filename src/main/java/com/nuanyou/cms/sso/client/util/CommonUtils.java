/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
 * 工具类(不依赖于任何第三方jar)
 */
public final class CommonUtils {

    private static final Log LOG = LogFactory.getLog(CommonUtils.class);
    //protected static final Logger log = LoggerFactory.getLogger(CommonUtils.class.getSimpleName());


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
        try {
            String url = loginUrl + (loginUrl.indexOf("?") != -1 ? "&" : "?") + serviceParameterName + "="
                    + URLEncoder.encode(serviceUrl, "UTF-8")
                    + (state != null ? "&state=#state" : "")
                    + (relogin != null ? "&relogin=#relogin" : "");
            return url.replace("#state", state).replace("#relogin", relogin.toString());
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    public static String constructServiceUrl(final HttpServletRequest request,
                                             final HttpServletResponse response, final String service, final String serverName, final String artifactParameterName, final boolean encode) {
        if (CommonUtils.isNotBlank(service)) {
            return encode ? response.encodeURL(service) : service;
        }

        final StringBuilder buffer = new StringBuilder();


        if (!serverName.startsWith("https://") && !serverName.startsWith("http://")) {
            buffer.append(request.isSecure() ? "https://" : "http://");
        }

        buffer.append(serverName);
        buffer.append(request.getRequestURI());

        if (CommonUtils.isNotBlank(request.getQueryString())) {
            final int location = request.getQueryString().indexOf(artifactParameterName + "=");

            if (location == 0) {
                final String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("serviceUrl generated: " + returnValue);
                }
                return returnValue;
            }

            buffer.append("?");

            if (location == -1) {
                buffer.append(request.getQueryString());
            } else if (location > 0) {
                final int actualLocation = request.getQueryString()
                        .indexOf("&" + artifactParameterName + "=");

                if (actualLocation == -1) {
                    buffer.append(request.getQueryString());
                } else if (actualLocation > 0) {
                    buffer.append(request.getQueryString().substring(0,
                            actualLocation));
                }
            }
        }

        final String returnValue = encode ? response.encodeURL(buffer.toString()) : buffer.toString();
        if (LOG.isDebugEnabled()) {
            LOG.debug("serviceUrl generated: " + returnValue);
        }
        return returnValue;
    }


    public static String safeGetParameter(final HttpServletRequest request, final String parameter) {
        if ("POST".equals(request.getMethod()) && "logoutRequest".equals(parameter)) {
            LOG.debug("safeGetParameter called on a POST HttpServletRequest for LogoutRequest.  Cannot complete check safely.  Reverting to standard behavior for this Parameter");
            return request.getParameter(parameter);
        }
        return request.getQueryString() == null || request.getQueryString().indexOf(parameter) == -1 ? null : request.getParameter(parameter);
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
}
