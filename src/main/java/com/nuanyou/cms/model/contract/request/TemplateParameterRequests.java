package com.nuanyou.cms.model.contract.request;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;

/**
 * Created by young on 2017/5/24.
 */
public class TemplateParameterRequests {

    private List<Long> paramId;

    private List<String> name;

    private List<String> key;

    private List<String> defaultValue;

    private List<Integer> dataType;

    private List<Boolean> editable;

    private List<Boolean> nullable;

    private List<String> hint;
    private String[] hint1;

    private List<String> remark;

    private List<Boolean> multiValuable;

    private List<String> regex;

    public List<Long> getParamId() {
        return paramId;
    }

    public void setParamId(List<Long> paramId) {
        this.paramId = paramId;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getKey() {
        return key;
    }

    public void setKey(List<String> key) {
        this.key = key;
    }

    public List<String> getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(List<String> defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<Integer> getDataType() {
        return dataType;
    }

    public void setDataType(List<Integer> dataType) {
        this.dataType = dataType;
    }

    public List<Boolean> getEditable() {
        return editable;
    }

    public void setEditable(List<Boolean> editable) {
        this.editable = editable;
    }

    public List<Boolean> getNullable() {
        return nullable;
    }

    public void setNullable(List<Boolean> nullable) {
        this.nullable = nullable;
    }

    public List<String> getHint() {
        return hint;
    }

    public void setHint(List<String> hint) {
        this.hint = hint;
    }

    public List<String> getRemark() {
        return remark;
    }

    public void setRemark(List<String> remark) {
        this.remark = remark;
    }

    public List<Boolean> getMultiValuable() {
        return multiValuable;
    }

    public void setMultiValuable(List<Boolean> multiValuable) {
        this.multiValuable = multiValuable;
    }

    public List<String> getRegex() {
        return regex;
    }

    public void setRegex(List<String> regex) {
        this.regex = regex;
    }

    public String[] getHint1() {
        return hint1;
    }

    public void setHint1(String[] hint1) {
        this.hint1 = hint1;
    }

    public boolean validateKeys() {
        if (key == null) {
            return true;
        }
        List<String> key = this.key;
        if (key.size() != new HashSet<>(key).size()) {
            return false;
        }
        return true;


    }


    public boolean validateTemplate() {
        if (dataType == null) {
            return true;
        }
        Integer size = dataType.size();
        if (
                key.size() != size ||
                        name.size() != size) {
            return false;

        }

        for (int i = 0; i < name.size(); i++) {
            String one = name.get(i);
            if (StringUtils.isBlank(one)) {
                return false;
            }
        }
        for (int i = 0; i < key.size(); i++) {
            String one = key.get(i);
            if (StringUtils.isBlank(one)) {
                return false;
            }
        }
//        for (int i = 0; i < defaultValue.size(); i++) {
//            String one = defaultValue.get(i);
//            if(StringUtils.isBlank(one)){
//                return false;
//            }
//        }

        for (int i = 0; i < dataType.size(); i++) {
            Integer one = dataType.get(i);
            if (one == null) {
                return false;
            }
        }


        return true;
    }
}
