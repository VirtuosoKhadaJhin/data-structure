package com.nuanyou.cms.model.contract.request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by young on 2017/5/23.
 */
public class TemplateUpdateRequest {

    private String title;

    private List<Long> paramterids = new ArrayList<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Long> getParamterids() {
        return paramterids;
    }

    public void setParamterids(List<Long> paramterids) {
        this.paramterids = paramterids;
    }

}
