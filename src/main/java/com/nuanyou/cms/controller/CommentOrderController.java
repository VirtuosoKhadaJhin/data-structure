package com.nuanyou.cms.controller;

import com.alibaba.fastjson.JSONArray;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.CommentOrderDao;
import com.nuanyou.cms.dao.FakeUserDao;
import com.nuanyou.cms.entity.CommentOrder;
import com.nuanyou.cms.entity.CommentReply;
import com.nuanyou.cms.entity.FakeUser;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.CommentOrderService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.util.ExcelUtil;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping("commentOrder")
public class CommentOrderController {

    @Autowired
    private CommentOrderService commentOrderService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CommentOrderDao commentOrderDao;

    @Autowired
    private  FakeUserDao fakeUserDao;

    private  Integer currentCount = 0;//当前选中条数

    private  String checkboxAllStatus = "false";//全选状态

    private  String currentPageInfoStr = "";//导出需要拼接参数

    private  Integer targetPage = 0;//跳转目标页面

    private  List<Integer> exportList = null;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        List<FakeUser> fakeUsers = fakeUserDao.getIdNameList();
        model.addAttribute("fakeUsers", fakeUsers);

        if (id != null) {
            CommentOrder entity = commentOrderDao.findOne(id);
            model.addAttribute("entity", entity);
        }
        return "commentOrder/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public APIResult detail(Long id) {
        CommentOrder entity = commentOrderDao.findOne(id);
        return new APIResult(entity);
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(CommentOrder entity, Model model) {
        entity.setAnonymous(false);
        if (entity.getDisplay() == null)
            entity.setDisplay(true);

        if (entity.getType() == null) {
            entity.setType(2);
        }
        entity = commentOrderService.saveNotNull(entity);
        model.addAttribute("entity", entity);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        List<FakeUser> fakeUsers = fakeUserDao.getIdNameList();
        model.addAttribute("fakeUsers", fakeUsers);

        model.addAttribute("disabled", true);
        return "commentOrder/edit";
    }

    @RequestMapping(path = "reply", method = RequestMethod.POST)
    @ResponseBody
    public APIResult reply(CommentReply entity) {
        commentOrderService.reply(entity);
        return new APIResult();
    }

    @RequestMapping(path = "replies", method = RequestMethod.GET)
    @ResponseBody
    public APIResult replyList(Long id) {
        List<CommentReply> replies = commentOrderService.findReply(id);
        return new APIResult(replies);
    }

    @RequestMapping(path = "remove", method = RequestMethod.POST)
    @ResponseBody
    public APIResult remove(Long id) {
        commentOrderService.delete(id);
        return new APIResult();
    }

    /**
     * 显示或隐藏评论
     *
     * @param id
     * @param isShow
     * @return
     */
    @RequestMapping("showOrHideComment")
    @ResponseBody
    public APIResult showOrHideComment(Long id, Boolean isShow) {
        commentOrderService.showOrHideComment(id, isShow);
        return new APIResult();
    }


    /**
     * @return 清除勾选
     */
    @ResponseBody
    @RequestMapping("clearCheck")
    public APIResult clearCheck(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("recordedInfo");//清空需要导出的Session
        checkboxAllStatus =  "false";//全选状态
        currentCount = 0;
        return new APIResult();
    }

    /**
     * 勾选记录数据
     */
    @ResponseBody
    @RequestMapping("recordedInfo")
    public APIResult recordedInfo(@RequestBody Map<String, Object> map,HttpServletRequest request){
        HttpSession session = request.getSession();
        String currentPageStrTemp = map.get("currentPage").toString();
        currentPageInfoStr = "currentPageInfo"+currentPageStrTemp;
        Map<String,Object> SessionMap ;
        Map<String,Object> SessionChildMap ;
        Map<String,Object> SessionGrandsonMap ;
        if(session.getAttribute("recordedInfo") != null){
             SessionMap = (Map<String,Object>)session.getAttribute("recordedInfo");
        }else{
             SessionMap = new HashMap<>();
        }
        SessionMap.put(currentPageInfoStr,map);//当前也对象
        checkboxAllStatus =  map.get("checkboxAllStatus").toString();//全选状态
        if("true".equals(checkboxAllStatus)){
            for (String key : SessionMap.keySet()) {
                SessionChildMap = (Map<String,Object>)SessionMap.get(key);
                SessionGrandsonMap = (Map<String,Object>)SessionChildMap.get("currentPageMap");
                for(String keys : SessionGrandsonMap.keySet()){
                    SessionGrandsonMap.put(keys,"checked");
                }
            }
        }else{
            for (String key : SessionMap.keySet()) {
                SessionChildMap = (Map<String,Object>)SessionMap.get(key);
                SessionGrandsonMap = (Map<String,Object>)SessionChildMap.get("currentPageMap");
                for(String keys : SessionGrandsonMap.keySet()){
                    SessionGrandsonMap.put(keys,"");
                }
            }
        }
        currentCount =  Integer.parseInt(map.get("currentCount").toString());//当前勾选数量
        targetPage = Integer.valueOf(map.get("targetPage").toString())-1;//目标页
        session.setAttribute("recordedInfo",SessionMap);
        return new APIResult();
    }


    /**
     * @return 导出需要的数据
     */
    @ResponseBody
    @RequestMapping("exportData")
    public APIResult exportData(HttpServletRequest request,@RequestBody Map<String, Object> map) {
        checkboxAllStatus = map.get("checkboxAllStatus").toString();
        String tempStr = "";
        if("true".equals(checkboxAllStatus)){
            tempStr = "IdStrNoChecked";
        }else{
            tempStr = "IdStrChecked";
        }
        if(map.get(tempStr)!=null){
            exportList = JSONArray.parseArray(map.get(tempStr).toString(), Integer.class);
        }
        HttpSession session = request.getSession();
        Map<String,Map<String,Object>> SessionMap ;
        if(session.getAttribute("recordedInfo") != null) {
            SessionMap = (Map<String, Map<String,Object>>) session.getAttribute("recordedInfo");
            for (String key : SessionMap.keySet()) {
                Map<String, Object> SessionChildMap = SessionMap.get(key);
                if(SessionChildMap.get(tempStr)!=null){
                    exportList.addAll(JSONArray.parseArray(SessionChildMap.get(tempStr).toString(), Integer.class));
                }
            }
        }
        return new APIResult();
    }
    /**
     * @return 导出评论
     */
    @RequestMapping("exportComment")
    public void exportComment(CommentOrder entity, TimeCondition time,@RequestParam(required = false) String scoreStr, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("订单列表" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".xlsx", "UTF-8"));
        List<CommentOrder> page ;
        page = commentOrderService.findByCondition(entity,time,scoreStr);
        System.out.println(page.size());
        if("true".equals(checkboxAllStatus)){
            if(exportList.size() != 0){
                for (Iterator<CommentOrder> it = page.iterator(); it.hasNext();) {//循环查询的数据
                    CommentOrder value = it.next();
                    Integer itemInt = Integer.parseInt(value.getId().toString());
                    if (exportList.contains(itemInt)) {//把根据条件查询的数据过滤到只有勾选的数据
                        it.remove();
                    }
                }
            }
        }else{//未全选 只把勾选的数据导出
            if(exportList.size() != 0){
                //需要导出的数据List
                for (Iterator<CommentOrder> it = page.iterator(); it.hasNext();) {//循环查询的数据
                    CommentOrder value = it.next();
                    Integer itemInt = Integer.parseInt(value.getId().toString());
                    if (!exportList.contains(itemInt)) {//把根据条件查询的数据过滤到只有勾选的数据
                        it.remove();
                    }
                }
            }
        }
        System.out.println(page.size());
        LinkedHashMap<String, String> CommentMap = new LinkedHashMap<>();
        CommentMap.put("id", "ID");
        CommentMap.put("score", "评分");
        CommentMap.put("order.ordersn", "订单编号");
        CommentMap.put("merchant.name", "店铺名称");
        CommentMap.put("content", "店铺评价");
        CommentMap.put("createTime", "评论时间");
        CommentMap.put("replyTime", "回复时间");
        XSSFWorkbook ex = ExcelUtil.generateXlsxWorkbook(CommentMap, page);
        OutputStream os = response.getOutputStream();
        ex.write(os);
        os.flush();
        os.close();
    }

    /**
     * 评价管理
     */
    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false) String scoreStr,
                       TimeCondition time, CommentOrder entity,HttpServletRequest request,
                        Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "replyTime", "createTime");
        Page<CommentOrder> page = commentOrderService.findByCondition(entity, time, scoreStr, pageable);
        model.addAttribute("page", page);
        model.addAttribute("checkboxAllStatus", checkboxAllStatus);//全选状态
        model.addAttribute("targetPage", targetPage);//目标页
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);
        HttpSession session = request.getSession();
        Map<String,Object> SessionMap ;
        if(session.getAttribute("recordedInfo") != null){//session中是否存在勾选数据
            SessionMap = (Map<String,Object>)session.getAttribute("recordedInfo");
            String targetPagePageInfo = "currentPageInfo"+targetPage;
            for (String key : SessionMap.keySet()){//跳转页面是否是勾选过的页面
                if(targetPagePageInfo.equals(key)){
                    Map<String,Object> targetPagePageMap = (Map<String,Object>)SessionMap.get(targetPagePageInfo);
                    model.addAttribute("SessionMap", targetPagePageMap.get("currentPageMap"));
                }
            }
        }
        model.addAttribute("currentCount", currentCount);//目前所选条数
        model.addAttribute("entity", entity);
        model.addAttribute("scoreStr", scoreStr);
        model.addAttribute("time", time);
        return "commentOrder/list";
    }

    /**
     * 评价美化
     */
    @RequestMapping("listFake")
    public String listFake(CommentOrder entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        entity.setType(2);

        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "replyTime", "createTime");
        Page<CommentOrder> page = commentOrderService.findByCondition(entity, null, null, pageable);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);
        return "commentOrder/listFake";
    }
}