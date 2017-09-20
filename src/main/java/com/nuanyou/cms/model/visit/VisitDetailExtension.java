package com.nuanyou.cms.model.visit;

import java.util.Date;
import java.util.List;

/**
 * Created by young on 2017/9/18.
 */
public class VisitDetailExtension<T> {

    private T originalValue;

    private T currentValue;

    private Boolean change = false;

    public T getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(T originalValue) {
        this.originalValue = originalValue;
    }

    public T getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(T currentValue) {
        this.currentValue = currentValue;
    }

    public Boolean getChange() {
        return change;
    }

    public void setChange(Boolean change) {
        this.change = change;
    }

    public Boolean compareChange(){
        if (this.currentValue != null && this.originalValue != null) {
            if (this.currentValue instanceof String) {
                String o = (String) this.originalValue;
                String c = (String) this.currentValue;
                if (o.equals(c))
                    return false;
                else
                    return true;
            }else if (this.currentValue instanceof Date) {
                Date o = (Date)this.originalValue;
                Date c = (Date)this.currentValue;
                if (o.compareTo(c) == 0) {
                    return false;
                }else {
                    return true;
                }
            }else if (this.currentValue instanceof List) {
                List o = (List)this.originalValue;
                List c = (List)this.currentValue;

                if (o.size() == c.size()) {
                    Integer exists_count = 0;
                    for (Object o_obj : o){
                        Boolean exists = false;
                        for (Object c_obj : c){
                            if (o_obj instanceof String && c_obj instanceof String ){
                                String oo = (String) o_obj;
                                String cc = (String) c_obj;
                                if (oo.equals(cc)){
                                    exists = true;
                                    break;
                                }
                            }else {
                                if (o_obj == c_obj){
                                    exists = true;
                                    break;
                                }
                            }
                        }
                        if (exists)
                            exists_count ++;
                    }
                    if (exists_count == o.size())
                        return false;
                    else
                        return true;
                }else {
                    return true;
                }
            }else {
                return this.currentValue == this.originalValue;
            }
        }else if (this.originalValue == null && this.originalValue == null)
            return false;
        else
            return true;
    }
}
