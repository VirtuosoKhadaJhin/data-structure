package com.nuanyou.cms.model.contract.request;

import java.util.List;

/**
 * Created by young on 2017/5/24.
 */
public class BatchTemplateParameterRequest {

    private List<TemplateParameterRequest> parameterRequests;

    public List<TemplateParameterRequest> getParameterRequests() {
        return parameterRequests;
    }

    public void setParameterRequests(List<TemplateParameterRequest> parameterRequests) {
        this.parameterRequests = parameterRequests;
    }
}
