package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.MerchantCatDao;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.dao.MerchantStatsDao;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.enums.*;
import com.nuanyou.cms.model.*;
import com.nuanyou.cms.remote.sevenmoor.SevenmoorService;
import com.nuanyou.cms.service.*;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.ExcelUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private DistrictService districtService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private MerchantStatsDao merchantStatsDao;

    @Autowired
    MerchantCollectionCodeService collectionCodeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RemoteCrmService remoteCrmService;

    @Autowired
    private SevenmoorService sevenmoorService;

    @Autowired
    private OrderService orderService;

    private static final Map<String, Long> countryMap = new HashMap<>();

    static {
        countryMap.put("kr", 1L);
        countryMap.put("jp", 2L);
        countryMap.put("th", 3L);
        countryMap.put("de", 4L);
    }

    @Value("${nuanyou-host}")
    private String nuanyouHost;
    @Value("${cms.redirect}")
    private String cmsRedirect;

    @ApiOperation("获取商户信息(客服)")
    @ApiResponses(@ApiResponse(code = 200, message = "获取商户信息(客服)", response = CustomerServicePage.class))
    @RequestMapping(path = "customer.html", method = RequestMethod.GET)
    public String customerservice(@RequestParam(required = false) String originCallNo,
                                  @RequestParam(required = false) String CallNo,
                                  @RequestParam(required = false,defaultValue = "false") Boolean q,
                                  Model model, HttpServletResponse response) throws Exception {
        String queryPhone = originCallNo;
        if (StringUtils.isEmpty(queryPhone)) {
            queryPhone = CallNo;
        }
        CustomerServiceInfo info = remoteCrmService.getCustomerServiceInfo(queryPhone);
        if (info == null || info.getMerchant() == null) {
            if (!q) {
                if (StringUtils.isEmpty(originCallNo)) {
                    originCallNo = "";
                }
                if (StringUtils.isEmpty(CallNo)) {
                    CallNo = "";
                }
                response.sendRedirect(cmsRedirect + "/merchant/customer.html?q=true&originCallNo=" + originCallNo + "&CallNo=" + CallNo);
            }
        }
        model.addAttribute("info", info);

        if (info != null && info.getMerchant() != null) {
            String name = "";
            if (info.getMerchant().getNyid() != null) {
                name = "["+info.getMerchant().getNyid() +"] "+info.getMerchant().getName();
            }else {
                name = info.getMerchant().getName();
            }
            List<String> tels = new ArrayList<>();
            if (StringUtils.isNotEmpty(info.getMerchant().getPhone())) {
                tels.add(info.getMerchant().getPhone());
            }
            if (info.getContacts() != null && info.getContacts().size() > 0) {
                for (ContactModel contactModel : info.getContacts()) {
                    if (StringUtils.isNotEmpty(contactModel.getTelephone())) {
                        tels.add(contactModel.getTelephone());
                    }
                }
            }

            sevenmoorService.addCustomer(info.getMerchant().getId().toString(),name,tels);
        }
        return "customer/customer";
    }

    @ApiOperation(value = "订单列表", notes = "订单列表")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "订单列表")})
    @RequestMapping(path = {"/orderList"}, method = RequestMethod.GET)
    @ResponseBody
    public APIResult<PageModel<OrderModel>> orderList(@RequestParam Long mchId,
                                                 @ApiParam("页码") @RequestParam(required = false, defaultValue = "1") Integer page,
                                                 @ApiParam("分页") @RequestParam(required = false, defaultValue = "40") Integer size) {
        PageModel<OrderModel> orders = orderService.getMerchantOrders(mchId,page,size);
        return new APIResult(orders);
    }

    @RequestMapping(path = {"edit", "add"}, method = RequestMethod.GET)
    public String edit(@RequestParam(required = false) Long id, Long countryId, Model model) {
        if (id != null) {
            Merchant entity = merchantDao.findOne(id);
            List<EntityBdMerchantCollectionCode> codeList = collectionCodeService.findEntityBdMerchantCollectionCodesByMchId(id);
            List<String> list = new ArrayList<>();
            for (EntityBdMerchantCollectionCode code : codeList) {
                list.add(code.getCollectionCode());
            }
            entity.setCollectionCodeList(list);
            model.addAttribute("entity", entity);
        }
        model.addAttribute("cooperationStatuses", MerchantCooperationStatus.values());
        setEnums(model, countryId);
        return "merchant/edit";
    }

    @RequestMapping(path = {"{countryCode}/edit", "{countryCode}/add"}, method = RequestMethod.GET)
    public String edit_country(@RequestParam(required = false) Long id, @PathVariable("countryCode") String countryCode, Model model) {
        Long countryId = countryMap.get(countryCode);
        model.addAttribute("countryHide", true);
        edit(id, countryId, model);
        return "merchant/edit";
    }

    @RequestMapping(path = "detail", method = RequestMethod.GET)
    public String detail(Long id, Long countryId, Model model) {
        Merchant entity = merchantDao.findOne(id);
        model.addAttribute("entity", entity);

        setEnums(model, countryId);
        model.addAttribute("disabled", true);
        model.addAttribute("cooperationStatuses", MerchantCooperationStatus.values());
        return "merchant/edit";
    }


    @RequestMapping(path = "{countryCode}/detail", method = RequestMethod.GET)
    public String detail_country(Long id, @PathVariable("countryCode") String countryCode, Model model) {
        Long countryId = countryMap.get(countryCode);
        detail(id, countryId, model);
        model.addAttribute("cooperationStatuses", MerchantCooperationStatus.values());
        return "merchant/edit";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String update(MerchantVO vo, Long countryId, Model model) {
        List<String> tmp = new ArrayList<>();
        List<String> returnCodes = new ArrayList<>();
        if (vo.getCollectionCodeList()!=null && vo.getCollectionCodeList().size()>0) {
            for (String code : vo.getCollectionCodeList())
                if (StringUtils.isNotEmpty(code)) {
                    tmp.add(code);
                    returnCodes.add(code);
                }
        }
        vo.setCollectionCodeList(tmp);
        validateCollectionCodes (vo.getCollectionCodeList());

        MerchantVO entity = merchantService.saveNotNull(vo);
        entity.setCollectionCodeList(returnCodes);
        model.addAttribute("entity", entity);
        setEnums(model, countryId);
        model.addAttribute("cooperationStatuses", MerchantCooperationStatus.values());
        model.addAttribute("disabled", true);
        model.addAttribute("update", true);
        return "merchant/edit";
    }

    public void validateCollectionCodes ( List<String> codelist) {
//        if (codelist == null || codelist.size() == 0) {
//            throw new APIException(ResultCodes.CollectionCodeError);
//        }
//        if (codelist != null && codelist.size()>3) {
//            throw new APIException(ResultCodes.CollectionCodeGreaterThan3);
//        }
        String regex = "^\\d{8,9}$";
        Pattern pattern = Pattern.compile(regex);

        for (String code : codelist) {
            if (StringUtils.isNotEmpty(code)) {
                Matcher matcher = pattern.matcher(code);
                if (!matcher.matches()) {
                    throw new APIException(ResultCodes.CollectionCodeError);
                }
            }
        }
        Set<String> set = new HashSet<>(codelist);
        if (set.size() != codelist.size() ){
            throw new APIException(ResultCodes.CollectionCodeRepeat);
        }
    }

    @RequestMapping(path = "{countryCode}/update", method = RequestMethod.POST)
    public String update_country(MerchantVO vo, @PathVariable("countryCode") String countryCode, Model model) {
        Long countryId = countryMap.get(countryCode);
        model.addAttribute("countryHide", true);
        update(vo, countryId, model);
        model.addAttribute("cooperationStatuses", MerchantCooperationStatus.values());
        return "merchant/edit";
    }

    /**
     * 商铺列表
     * @param entity
     * @param index
     * @param model
     * @return
     */
    @RequestMapping("list")
    public String list(Merchant entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "id");
        MerchantQueryParam param = new MerchantQueryParam();
        param.id = entity.getId();
        param.name = entity.getName();
        param.kpname = entity.getKpname();
        param.countryId = entity.getDistrict()!=null?entity.getDistrict().getCountry().getId():null;
        param.display = entity.getDisplay();
        param.cooperationStatus = entity.getCooperationStatus()!=null?entity.getCooperationStatus().getKey():null;
        Page<Merchant> page = merchantService.findMerchant(param,pageable);
        model.addAttribute("page", page);

        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("countries", countries);

        List<Merchant> merchants = merchantService.getIdNameList();
        model.addAttribute("merchants", merchants);

        model.addAttribute("entity", entity);
        model.addAttribute("YouFu", SupportType.YouFu);
        model.addAttribute("cooperationStatuses", MerchantCooperationStatus.values());

        List<Long> countryids = userService.findUserCountryId();
        model.addAttribute("countryids", countryids);
        return "merchant/list";
    }


    @RequestMapping("{countryCode}/list")
    public String list(Merchant entity,
                       @PathVariable("countryCode") String countryCode,
                       @RequestParam(required = false, defaultValue = "1") int index,
                       Model model) {
        Long id = countryMap.get(countryCode);
        if (id != null) {
            entity.setDistrict(new District(new Country(id)));
            list(entity, index, model);
            model.addAttribute("countryHide", true);
        }
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


    @RequestMapping(path = "/name", method = RequestMethod.GET)
    @ResponseBody
    public APIResult<Merchant> getMerchantName(Long id) throws Exception {
        Merchant merchant = merchantService.getMerchant(id);
        return new APIResult(merchant);
    }

    @RequestMapping(path = "export")
    public void export( String countryids,Merchant entity, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=" + "UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("商户列表" + DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss") + ".xlsx", "UTF-8"));

        Pageable pageable = new PageRequest(0, 10000, Sort.Direction.DESC, "id");

        MerchantQueryParam param = new MerchantQueryParam();
        param.id = entity.getId();
        param.name = entity.getName();
        param.kpname = entity.getKpname();
        param.countryId = entity.getDistrict()!=null?entity.getDistrict().getCountry().getId():null;
        param.display = entity.getDisplay();
        param.cooperationStatus = entity.getCooperationStatus()!=null?entity.getCooperationStatus().getKey():null;
        param.countryids = CommonUtils.StringToList(countryids);
        Page<Merchant> page = merchantService.findMerchant(param,pageable);

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
        propertyHeaderMap.put("cooperationStatus.name", "合作情况");
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
        XSSFWorkbook ex = ExcelUtil.generateXlsxWorkbook(propertyHeaderMap, page.getContent());
        OutputStream os = response.getOutputStream();
        ex.write(os);

        os.flush();
        os.close();
    }

    @RequestMapping(path = "{countryCode}/export")
    public void export(Merchant entity, @PathVariable("countryCode") String countryCode, HttpServletResponse response) throws IOException {
        Long id = countryMap.get(countryCode);
        if (id != null) {
            entity.setDistrict(new District(new Country(id)));
        }
        export(null,entity, response);
    }

    private void setEnums(Model model, Long countryId) {
        List<MerchantCat> cats = merchantCatDao.findByPcat(null);
        model.addAttribute("cats", cats);

        List<District> districts;
        if (countryId == null)
            districts = districtService.getIdNameList();
        else
            districts = districtService.getIdNameList(countryId);
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

    @RequestMapping("/collectioncodes")
    public String getCollectionCodes (EntityBdMerchantCollectionCode entity,
                                      @RequestParam(required = false, defaultValue = "1") int index,
                                      @RequestParam(required = false, defaultValue = "20") int limit,
                                      Model model) {
        BeanUtils.cleanEmpty(entity);
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        Pageable pageable = new PageRequest(index - 1, limit, sort);
        Page<EntityBdMerchantCollectionCode>  page = collectionCodeService.query(entity,pageable);
        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        List<Country> countries = countryService.getIdNameList();
        model.addAttribute("countries", countries);
        return "merchant/code_list";
    }

    @RequestMapping(path = "/bind/number", method = RequestMethod.POST)
    @ResponseBody
    public APIResult<EntityBdMerchantCollectionCode> bindNumber(EntityBdMerchantCollectionCode entity, Model model) {
        String number = entity.getCollectionCode();
        Long mchId = entity.getMchId();
        if (mchId == null) {
            throw new APIException(ResultCodes.MissingParameter);
        }
        List<EntityBdMerchantCollectionCode>  codeList = collectionCodeService.findEntityBdMerchantCollectionCodesByMchId(mchId);
        if ( codeList != null && codeList.size() >= 3) {
            throw new APIException(ResultCodes.CollectionCodeGreaterThan3);
        }
        EntityBdMerchantCollectionCode collectionCode = collectionCodeService.findCollectionCode(number);
        if (collectionCode == null) {
            throw new APIException(ResultCodes.CollectionCodeError);
        }
        if (collectionCode.getMchId() != null && collectionCode.getMchId() != 0 && collectionCode.getMchId().longValue() != mchId.longValue()) {
            throw new APIException(ResultCodes.CollectionCodeExist, MessageFormat.format(ResultCodes.CollectionCodeExist.getMessage(), number, collectionCode.getMchId()));
        }
        collectionCode = merchantService.bindNumber(collectionCode,mchId);
        return new APIResult(collectionCode);
    }

    @RequestMapping(path = "/unbind/number", method = RequestMethod.POST)
    @ResponseBody
    public APIResult<EntityBdMerchantCollectionCode> unbindNumber(EntityBdMerchantCollectionCode entity, Model model) {
        String number = entity.getCollectionCode();
        EntityBdMerchantCollectionCode collectionCode = collectionCodeService.findCollectionCode(number);
        if (collectionCode == null) {
            throw new APIException(ResultCodes.CollectionCodeError);
        }
        collectionCode = merchantService.unbindNumber(collectionCode);
        return new APIResult(collectionCode);
    }

    @RequestMapping(path = "/{countryId}/merchantlist", method = RequestMethod.GET)
    @ResponseBody
    public APIResult<Merchant> getMerchantsByCountry(@PathVariable Long countryId,
                                                     @RequestParam(required = false) Long mchId,
                                                     @RequestParam(required = false) String mchName) {
        Pageable pageable = new PageRequest(0,10000, Sort.Direction.DESC, "id");
        Page<Merchant> merchants = merchantService.findMerchantByCountryFilter(countryId,mchName,mchId,pageable);
        return new APIResult(merchants);
    }
}