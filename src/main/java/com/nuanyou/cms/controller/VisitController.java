package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.mission.MissionGroup;
import com.nuanyou.cms.model.DistrictVo;
import com.nuanyou.cms.model.MerchantQueryParam;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.model.VisitQueryRequest;
import com.nuanyou.cms.model.mission.MissionBdMerchantTrack;
import com.nuanyou.cms.model.visit.VisitChangeVo;
import com.nuanyou.cms.service.*;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.util.ExcelUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by young on 2017/9/1.
 */
@Controller
@RequestMapping("visit")
public class VisitController {

    @Autowired
    private MerchantVisitService visitService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private BdUserService bdUserService;

    @Autowired
    private CityService cityService;

    @Autowired
    private DistrictService districtService;

    /**
     * 查看拜访商户记录
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/visitmerchantlist")
    public String getVisitMerchantlist(VisitQueryRequest request, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "createTime");

        List<Country> countries = countryService.getIdNameList();
        List<BdUser> bdUsers = bdUserService.findAllBdUsers();
        Page<MerchantVisit> visits = visitService.queryLatestVisit(request,pageable);

        List<City> cities;
        List<District> districts ;
        if (countries != null && countries.size() == 1) {
            cities = cityService.findCityByCountryId(countries.get(0).getId());
            districts = districtService.getIdNameList(countries.get(0).getId());
        }else {
            cities = cityService.findAllCities();
            districts = districtService.getIdNameList();
        }
        List<VisitType> visitTypes = visitService.findVisitTypes();
        model.addAttribute("visitTypes", visitTypes);
        List<Long> countryids = new ArrayList<>();
        for (Country country : countries) {
            countryids.add(country.getId());
        }
        model.addAttribute("countryids", countryids);
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("districts", districts);
        model.addAttribute("page", visits);
        model.addAttribute("requestVo", request);
        model.addAttribute("bdUsers", bdUsers);
        return "visit/visit_merchant_list";
    }

    /**
     * 查看变更
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/change")
    public String change(Long id, Model model){
        VisitChangeVo vo = visitService.getChange(id);
        model.addAttribute("change", vo);
        return "visit/change";
    }

    /**
     * 查看拜访记录
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/list")
    public String list(VisitQueryRequest request, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize, Sort.Direction.DESC, "createTime");

        List<Country> countries = countryService.getIdNameList();
        List<BdUser> bdUsers = bdUserService.findAllBdUsers();
        Page<MerchantVisit> visits = visitService.queryMerchantVisit(request,pageable);

        List<City> cities;
        List<District> districts ;
        if (countries != null && countries.size() == 1) {
            cities = cityService.findCityByCountryId(countries.get(0).getId());
            districts = districtService.getIdNameList(countries.get(0).getId());
        }else {
            cities = cityService.findAllCities();
            districts = districtService.getIdNameList();
        }
        List<VisitType> visitTypes = visitService.findVisitTypes();
        model.addAttribute("visitTypes", visitTypes);
        List<Long> countryids = new ArrayList<>();
        for (Country country : countries) {
            countryids.add(country.getId());
        }
        model.addAttribute("countryids", countryids);
        model.addAttribute("countries", countries);
        model.addAttribute("cities", cities);
        model.addAttribute("districts", districts);
        model.addAttribute("page", visits);
        model.addAttribute("requestVo", request);
        model.addAttribute("bdUsers", bdUsers);
        return "visit/list";
    }

    @RequestMapping("/{cityId}/districts")
    @ResponseBody
    public APIResult<List<DistrictVo>> getDistrictsByCityId(@PathVariable Long cityId){
        List<DistrictVo> districtVos = districtService.findByCity(cityId);
        return new APIResult(districtVos);
    }

    @RequestMapping(path = "export")
    public void export( VisitQueryRequest request,HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=" + "UTF-8");
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=30");
        String fileName = "拜访记录" + DateFormatUtils.format(new Date(), "yyMMdd");
        fileName = processFileName(req,fileName);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

        Pageable pageable = new PageRequest(0, 10000, Sort.Direction.DESC, "createTime");

        Page<MerchantVisit> visits = visitService.queryMerchantVisit(request,pageable);

        LinkedHashMap<String, String> propertyHeaderMap = new LinkedHashMap<>();
        propertyHeaderMap.put("user.chineseName", "BD名称");
        propertyHeaderMap.put("createTime", "拜访时间");
        propertyHeaderMap.put("type.name", "拜访类型");
        propertyHeaderMap.put("merchant.id", "门店ID");
        propertyHeaderMap.put("merchant.name", "拜访门店");
        propertyHeaderMap.put("merchant.kpname", "当地名称");
        propertyHeaderMap.put("merchant.district.country.name", "国家");
        propertyHeaderMap.put("merchant.district.city.name", "城市");
        propertyHeaderMap.put("merchant.district.name", "商圈");
        propertyHeaderMap.put("note", "拜访记录");

        XSSFWorkbook ex = ExcelUtil.generateXlsxWorkbook(propertyHeaderMap, visits.getContent());
        OutputStream os = response.getOutputStream();
        ex.write(os);

        os.flush();
        os.close();
    }

    /**
     *
     * @Title: processFileName
     *
     * @Description: ie,chrom,firfox下处理文件名显示乱码
     */
    public static String processFileName(HttpServletRequest request, String fileNames) {
        String codedfilename = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent
                    && -1 != agent.indexOf("Trident")) {// ie

                String name = java.net.URLEncoder.encode(fileNames, "UTF8");

                codedfilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等


                codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedfilename;
    }

}
