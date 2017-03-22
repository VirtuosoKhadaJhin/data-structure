package com.nuanyou.cms.sso.client.validation;

/**
 * Created by Felix on 2017/3/21.
 */
public class User {
    private String access_token;
    private Long userid;
    private String name;
    private String job;
    private Long expired;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Long getExpired() {
        return expired;
    }

    public void setExpired(Long expired) {
        this.expired = expired;
    }


    @Override
    public String toString() {
        return "User{" +
                "access_token=" + access_token +
                ", userid=" + userid +
                ", name='" + name + '\'' +
                ", job='" + job + '\'' +
                ", expired=" + expired +
                '}';
    }
}
