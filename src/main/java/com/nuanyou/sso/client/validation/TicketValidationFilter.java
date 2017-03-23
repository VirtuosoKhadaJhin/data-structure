package com.nuanyou.sso.client.validation;

import org.springframework.stereotype.Service;

import javax.servlet.FilterConfig;
import java.util.*;

/**
 * Created by Felix on 2017/3/23.
 */

@Service
public class TicketValidationFilter extends AbstractTicketValidationFilter  {

    private static final String[] RESERVED_INIT_PARAMS = new String[] {"proxyGrantingTicketStorageClass", "proxyReceptorUrl", "acceptAnyProxy", "allowedProxyChains", "casServerUrlPrefix", "proxyCallbackUrl", "renew", "exceptionOnValidationFailure", "redirectAfterValidation", "useSession", "serverName", "service", "artifactParameterName", "serviceParameterName", "encodeServiceUrl", "millisBetweenCleanUps", "hostnameVerifier", "encoding", "config"};
    protected final TicketValidator getTicketValidator(final FilterConfig filterConfig) {
        final String allowAnyProxy = getPropertyFromInitParams(filterConfig, "acceptAnyProxy", null);
        final String allowedProxyChains = getPropertyFromInitParams(filterConfig, "allowedProxyChains", null);
        final String casServerUrlPrefix = getPropertyFromInitParams(filterConfig, "validateCodeUrl", null);
        final ServiceTicketValidator validator;

        validator = new ServiceTicketValidator(casServerUrlPrefix);
        validator.setRenew(parseBoolean(getPropertyFromInitParams(filterConfig, "renew", "false")));
        validator.setEncoding(getPropertyFromInitParams(filterConfig, "encoding", null));

        final Map<String,String> additionalParameters = new HashMap<String,String>();
        final List<String> params = Arrays.asList(RESERVED_INIT_PARAMS);

        for (final Enumeration<?> e = filterConfig.getInitParameterNames(); e.hasMoreElements();) {
            final String s = (String) e.nextElement();

            if (!params.contains(s)) {
                additionalParameters.put(s, filterConfig.getInitParameter(s));
            }
        }

        validator.setCustomParameters(additionalParameters);
        //validator.setHostnameVerifier(getHostnameVerifier(filterConfig));

        return validator;
    }

}
