package com.nuanyou.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.enums.MerchantCooperationStatus;
import com.nuanyou.cms.model.VisitQueryRequest;
import com.nuanyou.cms.model.visit.VisitChangeVo;
import com.nuanyou.cms.model.visit.VisitDetailExtension;
import com.nuanyou.cms.model.visit.VisitSaveParameter;
import com.nuanyou.cms.service.MerchantVisitService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.sso.client.util.CommonUtils;
import com.nuanyou.cms.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by young on 2017/9/1.
 */
@Service
public class MerchantVisitServiceImpl implements MerchantVisitService {

    @Autowired
    private UserService userService;
    @Autowired
    private MerchantVisitDao visitDao;
    @Autowired
    private VisitTypeDao visitTypeDao;
    @Autowired
    private BdUserDao bdUserDao;
    @Autowired
    private DistrictDao districtDao;
    @Autowired
    private MerchantCatDao merchantCatDao;

    @Override
    public Page<MerchantVisit> queryMerchantVisit(final VisitQueryRequest param,Pageable pageable) {
        final List<Long> countryIds = userService.findUserCountryId();
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                List<Long> cids =  CommonUtils.StringToList(param.getCountryids());
                if (cids != null && cids.size() > 0) {
                    predicate.add(root.get("merchant").get("district").get("country").get("id").in(cids));
                }else {
                    if (countryIds != null && countryIds.size() > 0) {
                        predicate.add(root.get("merchant").get("district").get("country").get("id").in(countryIds));
                    }
                }
                if (param.getUserId() != null) {
                    predicate.add(cb.equal(root.get("user").get("id"), param.getUserId()));
                }
                if (param.getMchId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("id"), param.getMchId()));
                }
                if (StringUtils.isNotEmpty(param.getMchName())) {
                    predicate.add(cb.like(root.get("merchant").get("name"), "%"+param.getMchName()+"%"));
                }
                if (param.getCountryId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("country").get("id"), param.getCountryId()));
                }
                if (param.getCityId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("city").get("id"), param.getCityId()));
                }
                if (param.getDistrictId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("id"), param.getDistrictId()));
                }
                if (param.getVisitTypeId() != null) {
                    predicate.add(cb.equal(root.get("type"), param.getVisitTypeId()));
                }
                if (param.getStartTime()  != null) {
                    Pair<Date, Date> dayStartEndTime = DateUtils.getDayStartEndTime(param.getStartTime());
                    predicate.add(cb.greaterThanOrEqualTo(root.get("createTime"), dayStartEndTime.getLeft()));
                }
                if (param.getEndTime()  != null) {
                    Pair<Date, Date> dayStartEndTime = DateUtils.getDayStartEndTime(param.getEndTime());
                    predicate.add(cb.lessThanOrEqualTo(root.get("createTime"), dayStartEndTime.getRight()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];

                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        return visitDao.findAll(specification,pageable);
    }

    @Override
    public List<VisitType> findVisitTypes(){
        return visitTypeDao.findAll();
    }

    @Override
    public  Page<MerchantVisit> queryLatestVisit (VisitQueryRequest request,Pageable pageable) {
        List<Long> merchantIds = getMerchantIds(request);
        Object[][] objects = visitDao.findLatestVisit(merchantIds);
        Page<MerchantVisit> merchantVisits = null;
        if (objects != null && objects.length > 0) {
            List<Long> visitIds = new ArrayList<>();
            Map<Long,Integer> v_count = new HashMap<>();
            for (Object[] objs : objects){
                visitIds.add(Long.parseLong(objs[0].toString()));
                v_count.put(Long.parseLong(objs[0].toString()),Integer.parseInt(objs[1].toString()));
            }
            merchantVisits = visitDao.findLatestVisitDetail(visitIds,pageable);
            for (MerchantVisit v :merchantVisits.getContent()) {
                v.setVisitCount(v_count.get(v.getId()));
            }
        }
        return merchantVisits;
    }

    public List<Long> getMerchantIds(final VisitQueryRequest param) {
        final List<Long> countryIds = userService.findUserCountryId();
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                List<Long> cids =  CommonUtils.StringToList(param.getCountryids());
                if (cids != null && cids.size() > 0) {
                    predicate.add(root.get("merchant").get("district").get("country").get("id").in(cids));
                }else {
                    if (countryIds != null && countryIds.size() > 0) {
                        predicate.add(root.get("merchant").get("district").get("country").get("id").in(countryIds));
                    }
                }
                if (param.getMchId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("id"), param.getMchId()));
                }
                if (StringUtils.isNotEmpty(param.getMchName())) {
                    predicate.add(cb.like(root.get("merchant").get("name"), "%"+param.getMchName()+"%"));
                }
                if (param.getCountryId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("country").get("id"), param.getCountryId()));
                }
                if (param.getCityId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("city").get("id"), param.getCityId()));
                }
                if (param.getDistrictId() != null) {
                    predicate.add(cb.equal(root.get("merchant").get("district").get("id"), param.getDistrictId()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                query.groupBy(root.get("merchant").get("id"));
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        List<MerchantVisit> merchantVisits = visitDao.findAll(specification);
        List<Long> merchantIds = new ArrayList<>();
        for (MerchantVisit visit : merchantVisits) {
            merchantIds.add(visit.getMerchant().getId());
        }
        return merchantIds;
    }

    @Override
    public VisitChangeVo getChange(Long visitId){
        MerchantVisit visit = visitDao.findOne(visitId);
        if (visit == null)
            throw new APIException(ResultCodes.MissingParameter);
        VisitSaveParameter detail =  JSON.parseObject(visit.getContent(),VisitSaveParameter.class);
        VisitChangeVo changeVo = new VisitChangeVo();
        changeVo.setBdName(visit.getUser().getChineseName());
        changeVo.setChangeTime(visit.getCreateTime());

        VisitDetailExtension<String> localName = new VisitDetailExtension<String>();
        localName.setCurrentValue(detail.getName());
        VisitDetailExtension<String> districtName = new VisitDetailExtension<String>();
        District district = districtDao.findOne(detail.getDistrictId());
        districtName.setCurrentValue(district.getName());
        VisitDetailExtension<String> localAddress = new VisitDetailExtension<String>();
        localAddress.setCurrentValue(detail.getAddress());
        VisitDetailExtension<String> cooperationName = new VisitDetailExtension<String>();
        cooperationName.setCurrentValue(MerchantCooperationStatus.toEnum(detail.getCooperationStatus()).getName());
        VisitDetailExtension<String> catName = new VisitDetailExtension<String>();
        if (detail.getCatId() != null) {
            MerchantCat cat = merchantCatDao.findOne(detail.getCatId());
            catName.setCurrentValue(cat.getName());
        }
        VisitDetailExtension<String> subCatName = new VisitDetailExtension<String>();
        if (detail.getSubCatId() != null) {
            MerchantCat subCat = merchantCatDao.findOne(detail.getSubCatId());
            subCatName.setCurrentValue(subCat.getName());
        }
        VisitDetailExtension<Date> startTime = new VisitDetailExtension<Date>();
        startTime.setCurrentValue(detail.getOpeningStartTime());
        VisitDetailExtension<Date> endTime = new VisitDetailExtension<Date>();
        endTime.setCurrentValue(detail.getOpeningEndTime());
        VisitDetailExtension<List<String>> businessDays = new VisitDetailExtension<List<String>>();
        List<String> businessDaylist = JSON.parseArray(detail.getBusinessDays(), String.class);

        businessDays.setCurrentValue(businessDaylist);
        VisitDetailExtension<String> tel = new VisitDetailExtension<String>();
        List<String> telList = JSON.parseArray(detail.getTels(), String.class);
        if (telList != null && telList.size() > 0) {
            tel.setCurrentValue(telList.get(0));
        }
        VisitDetailExtension<String> cosume = new VisitDetailExtension<String>();
        cosume.setCurrentValue(detail.getConsumptionPerPerson()!=null?detail.getConsumptionPerPerson().toString():null);

        VisitDetailExtension<List<String>> payTypes = new VisitDetailExtension<List<String>>();
        List<String> payTypeList = JSON.parseArray(detail.getPayTypes(), String.class);
        payTypes.setCurrentValue(payTypeList);

        VisitDetailExtension<List<String>> supportTypes = new VisitDetailExtension<List<String>>();
        List<String> supportTypeList = JSON.parseArray(detail.getSupportTypes(), String.class);
        supportTypes.setCurrentValue(supportTypeList);

        VisitDetailExtension<String> headImg = new VisitDetailExtension<String>();
        headImg.setCurrentValue(detail.getIndexImgUrl());

        List<String> imgList = JSON.parseArray(detail.getHeadImgUrls(), String.class);

        VisitDetailExtension<String> indexImg = new VisitDetailExtension<String>();
        VisitDetailExtension<List<String>> detailImgs = new VisitDetailExtension<List<String>>();
        if (imgList != null && imgList.size() > 0) {
            indexImg.setCurrentValue(imgList.get(0));
            List<String> imgs = new ArrayList<>();
            if (imgList.size() > 1){
                for (int i = 1;i< imgList.size();i++){
                    String img = imgList.get(i);
                    imgs.add(img);
                }
                detailImgs.setCurrentValue(imgs);
            }
        }
        List<MerchantVisit> visits = visitDao.findPreviousVisit(visit.getMerchant().getId(),visit.getCreateTime());
        if (visits != null && visits.size() > 0) {
            MerchantVisit previousVisit = visits.get(0);
            VisitSaveParameter previous_detail =  JSON.parseObject(previousVisit.getContent(),VisitSaveParameter.class);
            localName.setOriginalValue(previous_detail.getName());
            localName.setChange(localName.compareChange());
            District previous_district = districtDao.findOne(previous_detail.getDistrictId());
            districtName.setOriginalValue(previous_district.getName());
            localAddress.setOriginalValue(previous_detail.getAddress());
            localAddress.setChange(localAddress.compareChange());
            cooperationName.setOriginalValue(MerchantCooperationStatus.toEnum(previous_detail.getCooperationStatus()).getName());
            if (previous_detail.getCatId() != null){
                MerchantCat previous_cat = merchantCatDao.findOne(previous_detail.getCatId());
                catName.setOriginalValue(previous_cat.getName());
            }
            catName.setChange(catName.compareChange());
            if (previous_detail.getSubCatId() != null){
                MerchantCat previous_subCat = merchantCatDao.findOne(previous_detail.getSubCatId());
                subCatName.setOriginalValue(previous_subCat.getName());
            }
            subCatName.setChange(subCatName.compareChange());
            startTime.setOriginalValue(previous_detail.getOpeningStartTime());
            startTime.setChange(startTime.compareChange());
            endTime.setOriginalValue(previous_detail.getOpeningEndTime());
            endTime.setChange(endTime.compareChange());
            List<String> previous_businessDaylist = JSON.parseArray(previous_detail.getBusinessDays(), String.class);

            businessDays.setOriginalValue(previous_businessDaylist);
            businessDays.setChange(businessDays.compareChange());
            List<String> previous_telList = JSON.parseArray(previous_detail.getTels(), String.class);
            if (previous_telList != null && previous_telList.size() > 0) {
                tel.setOriginalValue(previous_telList.get(0));
                tel.setChange(tel.compareChange());
            }
            cosume.setOriginalValue(previous_detail.getConsumptionPerPerson()!=null?previous_detail.getConsumptionPerPerson().toString():null);
            cosume.setChange(cosume.compareChange());
            List<String> previous_payTypeList = JSON.parseArray(previous_detail.getPayTypes(), String.class);
            payTypes.setOriginalValue(previous_payTypeList);
            payTypes.setChange(payTypes.compareChange());
            List<String> previous_supportTypeList = JSON.parseArray(previous_detail.getSupportTypes(), String.class);
            supportTypes.setOriginalValue(previous_supportTypeList);
            supportTypes.setChange(supportTypes.compareChange());
            headImg.setOriginalValue(previous_detail.getIndexImgUrl());
            headImg.setChange(headImg.compareChange());
            List<String> previous_imgList = JSON.parseArray(previous_detail.getHeadImgUrls(), String.class);

            if (previous_imgList != null && previous_imgList.size() > 0) {
                indexImg.setOriginalValue(previous_imgList.get(0));
                indexImg.setChange(indexImg.compareChange());
                List<String> imgs = new ArrayList<>();
                if (previous_imgList.size() > 1){
                    for (int i = 1;i< previous_imgList.size();i++){
                        String img = previous_imgList.get(i);
                        imgs.add(img);
                    }
                    detailImgs.setOriginalValue(imgs);
                    detailImgs.setChange(detailImgs.compareChange());
                }
            }
        }
        changeVo.setLocalName(localName);
        changeVo.setDistrictName(districtName);
        changeVo.setLocalAddress(localAddress);
        changeVo.setCooperationName(cooperationName);
        changeVo.setCatName(catName);
        changeVo.setSubCatName(subCatName);
        changeVo.setStartTime(startTime);
        changeVo.setEndTime(endTime);
        changeVo.setBusinessDays(businessDays);
        changeVo.setTel(tel);
        changeVo.setCosume(cosume);
        changeVo.setPayTypes(payTypes);
        changeVo.setSupportTypes(supportTypes);
        changeVo.setHeadImg(headImg);
        changeVo.setListImg(indexImg);
        changeVo.setDetailImgs(detailImgs);

        return changeVo;
    }
}
