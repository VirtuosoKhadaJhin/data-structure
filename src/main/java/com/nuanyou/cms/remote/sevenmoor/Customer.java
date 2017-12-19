package com.nuanyou.cms.remote.sevenmoor;

import java.util.List;

/**
 * Created by young on 2017/12/15.
 */
public class Customer {

    private String _id;

    private String name;

    private String title;

    private String version;

    private String status;

    private List<Tel> phone;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Tel> getPhone() {
        return phone;
    }

    public void setPhone(List<Tel> phone) {
        this.phone = phone;
    }
}
