package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.enums.*;
import com.nuanyou.cms.model.MerchantVO;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.util.*;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Controller
@RequestMapping("merchant")
public class MerchantController {

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantCatDao merchantCatDao;

    @Autowired
    private DistrictDao districtDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private MerchantStatsDao merchantStatsDao;

    @Value("${nuanyou-host}")
    private String nuanyouHost;

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            Merchant entity = merchantDao.findOne(id);
            model.addAttribute("entity", entity);
        }
        setEnums(model);
        return "merchant/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Model model) {
        Merchant entity = merchantDao.findOne(id);
        model.addAttribute("entity", entity);

        setEnums(model);
        model.addAttribute("disabled", true);
        return "merchant/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(MerchantVO vo, Model model) {
        MerchantVO entity = merchantService.saveNotNull(vo);
        model.addAttribute("entity", entity);
        setEnums(model);
        model.addAttribute("disabled", true);
        return "merchant/edit";
    }

    @RequestMapping("list")
    public String list(Merchant entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "id");

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", contains().ignoreCase()).withMatcher("kpname", contains().ignoreCase());

        BeanUtils.cleanEmpty(entity);
        Page<Merchant> page = merchantDao.findAll(Example.of(entity, matcher), pageable);
        model.addAttribute("page", page);

        List<Country> countries = countryDao.findAll();
        model.addAttribute("countries", countries);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        model.addAttribute("entity", entity);
        model.addAttribute("YouFu", SupportType.YouFu);
        return "merchant/list";
    }

    @RequestMapping(path = "copyItem", method = RequestMethod.POST)
    @ResponseBody
    public APIResult copyItem(Long sourceId, Long targetId, Model model) {
        merchantService.copyItem(sourceId, targetId);
        model.addAttribute("result", new APIResult<>());
        return new APIResult<>();
    }

    @RequestMapping(path = "updateScore", method = RequestMethod.POST)
    @ResponseBody
    public APIResult updateScore(Long id, Double score) {
        MerchantStats merchantStats = merchantStatsDao.getByMchId(id);
        if (merchantStats == null)
            throw new APIException(ResultCodes.UnkownError, "该商户没有对应的统计信息");
        merchantStats.setScore(score);
        merchantStatsDao.save(merchantStats);
        return new APIResult<>();
    }

    @RequestMapping(path = "lookCode", method = RequestMethod.GET)
    @ResponseBody
    public APIResult getImg(Long id) throws Exception {
        Channel channel = merchantService.genPayUrl(id);
        return new APIResult(channel);
    }

    @RequestMapping("export")
    public void export(Merchant entity, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=" + "UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("商户列表" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".xlsx", "UTF-8"));

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", contains().ignoreCase()).withMatcher("kpname", contains().ignoreCase());
        BeanUtils.cleanEmpty(entity);
        List<Merchant> list = merchantDao.findAll(Example.of(entity, matcher), new Sort(Sort.Direction.DESC, "id"));


//        for (Merchant merchant : list) {
//            Long id = merchant.getId();
//            Date date = new Date();
//            List<UserSubsidy> userSubsidyList = userSubsidyDao.findByMchIdAndStartTimeLessThanAndEndTimeGreaterThan(id, date, date);
//        }

        LinkedHashMap<String, String> propertyHeaderMap = new LinkedHashMap<>();
        propertyHeaderMap.put("id", "商户id");
        propertyHeaderMap.put("name", "名称（中文）");
        propertyHeaderMap.put("kpname", "名称（本地）");
        propertyHeaderMap.put("telphone", "商家电话");
        propertyHeaderMap.put("address", "地址（中文）");
        propertyHeaderMap.put("kpaddress", "地址（本地）");
        propertyHeaderMap.put("payTypes.name", "支付方式");
        propertyHeaderMap.put("supportType.name", "支持类型");
        propertyHeaderMap.put("display", "显示状态");
        propertyHeaderMap.put("issign", "合作情况");
        propertyHeaderMap.put("district.name", "商圈信息");
        propertyHeaderMap.put("mcat.name", "一级分类");
        propertyHeaderMap.put("subcat.name", "二级分类");
//        propertyHeaderMap.put("name", "优付名称");
//        propertyHeaderMap.put("name", "商户补贴率");
//        propertyHeaderMap.put("name", "商户补贴上限");
//        propertyHeaderMap.put("name", "商户补贴支付方式");
//        propertyHeaderMap.put("name", "商户补贴开始时间");
//        propertyHeaderMap.put("name", "商户补贴结束时间");
//        propertyHeaderMap.put("name", "优付用户补贴方式");
//        propertyHeaderMap.put("name", "优付用户固定折扣补贴");
//        propertyHeaderMap.put("name", "优付用户固定金额补贴");
//        propertyHeaderMap.put("name", "优付用户补贴上限");
//        propertyHeaderMap.put("name", "优付用户是否支持首单");
//        propertyHeaderMap.put("name", "优付用户开始时间");
//        propertyHeaderMap.put("name", "优付用户结束时间");
//        propertyHeaderMap.put("name", "商户用户补贴方式");
//        propertyHeaderMap.put("name", "商户用户固定折扣补贴");
//        propertyHeaderMap.put("name", "商户用户固定金额补贴");
//        propertyHeaderMap.put("name", "商户用户补贴上限");
//        propertyHeaderMap.put("name", "商户用户是否支持首单");
//        propertyHeaderMap.put("name", "商户用户开始时间");
//        propertyHeaderMap.put("name", "商户用户结束时间");
//        propertyHeaderMap.put("name", "国家");
        XSSFWorkbook ex = ExcelUtil.generateXlsxWorkbook(propertyHeaderMap, list);
        OutputStream os = response.getOutputStream();
        ex.write(os);

        os.flush();
        os.close();
    }

    private void setEnums(Model model) {
        List<MerchantCat> cats = merchantCatDao.findByPcat(null);
        model.addAttribute("cats", cats);

        List<District> districts = districtDao.getIdNameList();
        model.addAttribute("districts", districts);

        model.addAttribute("weeks", Week.values());
        model.addAttribute("payTypes", PayType.values());
        model.addAttribute("supportTypes", SupportType.values());
        model.addAttribute("verifyTypes", VerifyType.values());
    }

    @RequestMapping("api/list")
    @ResponseBody
    public APIResult list(Long id) {
        List<Merchant> list = merchantDao.findIdNameList(id);
        return new APIResult(list);
    }
}