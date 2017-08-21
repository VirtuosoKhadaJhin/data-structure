package com.nuanyou.cms.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by young on 2017/8/8.
 */
public class MerchantQueryParam implements Serializable{

    public Long id;

    public String name;

    public String kpname;

    public Boolean display;

    public Long countryId;

    public Integer cooperationStatus;

    public List<Long> countryids;
}
