package com.nuanyou.cms.controller;

import com.nuanyou.cms.model.LogVO;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.model.es.ESResult;
import com.nuanyou.cms.remote.LogService;
import com.nuanyou.cms.util.NumberUtils;
import com.nuanyou.cms.util.OperationLog;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.*;

@Controller
@RequestMapping("log")
public class LogController {

    @Autowired
    private LogService logService;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    private Map<String, String> uriMapper = new LinkedHashMap<>();

    @PostConstruct
    private void initParams() {
        uriMapper.put("/banner", "Banner管理");
        uriMapper.put("/city", "城市管理");
        uriMapper.put("/commentOrder", "评价管理");
        uriMapper.put("/coupon", "优惠券派发");
        uriMapper.put("/couponTemplate", "优惠券分类");
        uriMapper.put("/culturalRecommendations", "特色管理");
        uriMapper.put("/culturalRecommendationsGroup", "特色专辑管理");
        uriMapper.put("/discount", "优惠管理");
        uriMapper.put("/district", "商圈管理");
        uriMapper.put("/fakeOrder", "评论美化");
        uriMapper.put("/feature", "特色频道管理");
        uriMapper.put("/guideline", "攻略管理");
        uriMapper.put("/item", "单品管理");
        uriMapper.put("/itemCat", "单品分类管理");
        uriMapper.put("/itemTuan", "团购商品管理");
        uriMapper.put("/landMark", "地标管理");
        uriMapper.put("/merchant", "商户管理");
        uriMapper.put("/merchantCard", "商户卡券管理");
        uriMapper.put("/merchantCat", "商户分类管理");
        uriMapper.put("/merchantHeadimg", "商户详情图3.0");
        uriMapper.put("/merchantHeadimgOld", "商户详情图2.0");
        uriMapper.put("/merchantLabel", "商户标签管理");
        uriMapper.put("/merchantStaff", "商户员工编辑");
        uriMapper.put("/merchantSubsidy", "商户补贴管理");
        uriMapper.put("/news", "新闻管理");
        uriMapper.put("/orderRefundLog", "");
        uriMapper.put("/pasUserProfile", "用户管理");
        uriMapper.put("/pushDetail", "推送消息管理");
        uriMapper.put("/pushGroup", "推送管理");
        uriMapper.put("/rank", "榜单管理");
        uriMapper.put("/rate", "汇率管理");
        uriMapper.put("/recommend", "推荐管理");
        uriMapper.put("/recommendCat", "推荐组管理");
        uriMapper.put("/searchKeyword", "关键词管理");
        uriMapper.put("/userNotify", "用户推送管理");
        uriMapper.put("/userSubsidy", "用户补贴管理");

        Map<RequestMappingInfo, HandlerMethod> map = handlerMapping.getHandlerMethods();
        Iterator<Map.Entry<RequestMappingInfo, HandlerMethod>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<RequestMappingInfo, HandlerMethod> entry = iterator.next();
            RequestMappingInfo key = entry.getKey();
            Set<String> patterns = key.getPatternsCondition().getPatterns();
            for (String pattern : patterns) {
                int i = pattern.indexOf("/", 1);
                String uri = i == -1 ? pattern : pattern.substring(0, i);
                if (!uriMapper.containsKey(uri)) {
                    uriMapper.put(uri, uri);
                }
            }
        }
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        model.addAttribute("disabled", true);
        return "log/edit";
    }

    @RequestMapping("list")
    public String list(@ModelAttribute("vo") LogVO entity,
                       @RequestParam(required = false) String nameOrId,
                       @RequestParam(required = false, defaultValue = "1") int index,
                       TimeCondition time,
                       Model model) {
        if (StringUtils.isNotBlank(nameOrId)) {
            if (StringUtils.isNumeric(nameOrId)) {
                entity.setUserId(NumberUtils.toLong(nameOrId));
            } else {
                entity.setUserName(nameOrId);
            }
        }

        ESResult page = logService.find(entity, time, index - 1, PageUtil.pageSize);
        List<ESResult.HitsSum.Hit> hits = page.getHits().getHits();
        if (hits != null && !hits.isEmpty())
            for (ESResult.HitsSum.Hit hit : hits) {
                LogVO source = hit.getSource();
                if (source != null) {
                    String uri = source.getUri();
                    if (uri != null && !uri.isEmpty()) {
                        uri = uri.split("/")[1];
                        String s = uriMapper.get("/" + uri);
                        source.setUri(s);
                    }
                }
            }

        model.addAttribute("actions", OperationLog.Action.values());
        model.addAttribute("page", page);
        model.addAttribute("nameOrId", nameOrId);
        model.addAttribute("time", time);
        model.addAttribute("actions", OperationLog.Action.values());
        model.addAttribute("uriMap", uriMapper);
        return "log/list";
    }

}