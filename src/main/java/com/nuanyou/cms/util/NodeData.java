package com.nuanyou.cms.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.nuanyou.cms.entity.MerchantCat;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Felix on 2016/12/1 0001.
 */
@JSONType(orders = { "id", "pId", "name", "checked", "nocheck", "open" })
public class NodeData implements Serializable {

    private static final long serialVersionUID = -1820842836903814445L;

    private long id;
    private long pId;
    private String name;
    private boolean checked;
    private boolean nocheck;
    private boolean open;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }

    private NodeData(long id, long pId, String name) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.checked = false;
    }

    public NodeData(long id, long pid, String name, boolean checked) {
        super();
        this.id = id;
        this.pId = pid;
        this.name = name;
        this.checked = checked;
        this.open = true;
    }

    //
    public NodeData(boolean showOnly, Integer id, int pid, String name, boolean checked) {
        this.id = id;
        this.pId = pid;
        this.name = name;
        this.checked = checked;
        this.nocheck = showOnly;
        this.open = true;
    }

    //
    public NodeData(boolean showOnly, Integer id, int pid, String name) {
        this.id = id;
        this.pId = pid;
        this.name = name;
        this.nocheck = showOnly;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getPId() {
        return pId;
    }

    public void setPId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public static String getNodeData(List<MerchantCat> merchantCats) {
        //List<CommonReportMenuBean> all, List<CommonReportMenuBean> selecteReports

//        Set<NodeData> rs = new HashSet<NodeData>();
//        //NodeData n1 = new NodeData(0,-1,"root",false);
//        //rs.add(n1);
//        for (CommonReportMenuBean c : all) {
//
//            NodeData n = new NodeData(c.getRpcId(),c.getRpcParentId(),c.getRpcName(),false);
//            rs.add(n);
//        }
//        for (CommonReportMenuBean commonReportInfo : selecteReports) {
//            for (NodeData nodeData : rs) {
//                if (commonReportInfo.getRpcId().equals(nodeData.getId())) {
//                    nodeData.setChecked(true);
//                }
//            }
//        }
//        List<NodeData> ds = new ArrayList<NodeData>(rs);
//        Collections.sort(ds, new NodeDataCompare());
//        return toJsonString(ds);
        return null;
    }


    public static String toJsonString(List<NodeData> rs) {
        SerializeConfig mapping = new SerializeConfig();
        mapping.setAsmEnable(false);
        return JSON.toJSONString(rs);
        //return JSON.toJSONStringZ(rs, mapping, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullListAsEmpty);
    }



}