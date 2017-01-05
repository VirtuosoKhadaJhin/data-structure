package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.dao.LandMarkDao;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.Landmark;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.DistrictService;
import com.nuanyou.cms.service.LandMarkService;
import com.nuanyou.cms.util.DistanceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("landMark")
public class LandMarkController {

    @Autowired
    private LandMarkService landMarkService;
    @Autowired
    private DistrictService districtService;
    @Autowired
    private LandMarkDao landMarkDao;

    @RequestMapping("add")
    public String add(Landmark entity) {
        landMarkDao.save(entity);
        return "landMark/list";
    }


    @RequestMapping(path = "edit", method = RequestMethod.GET)
    public String edit(Long id, Model model, Integer type) {
        List<District> districts = this.districtService.getIdNameList();
        Landmark entity = null;
        if (id != null) {
            entity = landMarkDao.findOne(id);
        }
        model.addAttribute("entity", entity);
        model.addAttribute("districts", districts);
        model.addAttribute("type", type);
        return "landMark/edit";
    }

    @RequestMapping("update")
    public String update(Landmark entity, HttpServletResponse response) throws IOException {
        landMarkService.saveNotNull(entity);
        String url = "edit?type=3&id=" + entity.getId();
        return "redirect:" + url;
    }

    @RequestMapping("list")
    public String list(@RequestParam(required = false, defaultValue = "1") int index,
                       @RequestParam(required = false, defaultValue = "id") String propertie,
                       @RequestParam(required = false) Sort.Direction direction,
                       Landmark entity, Model model) {

        Pageable pageable;
        if (direction == null)
            pageable = new PageRequest(index - 1, PageUtil.pageSize);
        else
            pageable = new PageRequest(index - 1, PageUtil.pageSize, direction, propertie);

        Page<Landmark> page = landMarkService.findByCondition(entity, pageable);

        List<District> districts = this.districtService.getIdNameList();
        model.addAttribute("districts", districts);

        model.addAttribute("page", page);
        model.addAttribute("entity", entity);
        return "landMark/list";
    }


    @RequestMapping(path = "api/list")
    @ResponseBody
    public APIResult list(Long id,
                          @RequestParam(required = false) final Double longitude,
                          @RequestParam(required = false) final Double latitude) {
        Landmark entity = new Landmark();
        District district = new District();
        district.setId(id);
        entity.setDistrict(district);
        List<Landmark> sourceList = landMarkDao.findAll(Example.of(entity));
        if (longitude != null && longitude != null) {
            Collections.sort(sourceList, new Comparator<Landmark>() {
                @Override
                public int compare(Landmark o1, Landmark o2) {
                    Integer distance1 = DistanceUtils.getDistance(longitude, latitude, o1.getLongitude(), o1.getLatitude());
                    Integer distance2 = DistanceUtils.getDistance(longitude, latitude, o2.getLongitude(), o2.getLatitude());
                    return distance1.compareTo(distance2);
                }
            });
        }
        return new APIResult(sourceList);
    }

}