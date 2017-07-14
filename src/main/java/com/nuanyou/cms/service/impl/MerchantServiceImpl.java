package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.enums.ChannelType;
import com.nuanyou.cms.entity.enums.CodeType;
import com.nuanyou.cms.entity.enums.MerchantCooperationStatus;
import com.nuanyou.cms.model.MerchantVO;
import com.nuanyou.cms.service.ItemDetailimgService;
import com.nuanyou.cms.service.MerchantCollectionCodeService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.MerchantStaffService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.MyCacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.nuanyou.cms.commons.ResultCodes.NotFoundMerchant;

/**
 * Created by nuanyou on 2016/9/7.
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ItemCatDao itemCatDao;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private MerchantStatsDao merchantStatsDao;

    @Autowired
    private ChannelDao channelDao;

    @Autowired
    private MerchantStaffService merchantStaffService;

    @Autowired
    private ItemDetailimgService itemDetailimgService;

    @Value("${nuanyou-host}")
    private String nuanyouHost;
    @Autowired
    private MerchantCollectionCodeService collectionCodeService;

    private static String key = "getMerchantList";
    @Autowired
    private MyCacheManager<List<Merchant>> cacheManager;
    @Autowired
    private CmsUserDao cmsUserDao;
    private Specification specification;

    @Override
    public List<Merchant> getIdNameList() {
        return getIdNameList(null);
    }

    private static final String sg_url = "https://sg.h5.m.91nuanyou.com/view/order/youfu.html";
    private static final String kr_url = "https://kr.h5.m.91nuanyou.com/view/order/youfu.html";

    @Override
    public List<Merchant> getIdNameList(Boolean display) {
        if (display == null)
            return merchantDao.getIdNameList();
        else
            return merchantDao.getIdNameList();

 /*       if (cacheManager.getValue(key)!=null){
            return cacheManager.getValue(key);
        }else {
            List<Merchant> merchants = merchantDao.getIdNameList();
            cacheManager.addOrUpdateCache(key,merchants);
            return merchants;
        }*/

    }

    @Override
    public void updateScore(Long id, double score) {
        Merchant merchant = merchantDao.getOne(id);
        if (merchant == null)
            throw new APIException(NotFoundMerchant, id);
        merchant.setScore(score);
        merchantDao.save(merchant);
    }

    @Override
    @Transactional
    public void copyItem(Long sourceId, Long targetId) {
        Merchant sourceMerchant = merchantDao.getOne(sourceId);
        if (sourceMerchant == null)
            throw new APIException(NotFoundMerchant, sourceId);

        Merchant targetMerchant = merchantDao.getOne(targetId);
        if (targetMerchant == null)
            throw new APIException(NotFoundMerchant, targetId);

        itemDao.deleteByMerchantId(targetId);
        itemCatDao.deleteByMerchantId(targetId);

        List<ItemCat> sourceCats = itemCatDao.findByMerchantId(sourceId);
        for (ItemCat sourceCat : sourceCats) {
            ItemCat targetCat = BeanUtils.copyBean(sourceCat, new ItemCat());
            targetCat.setMerchant(targetMerchant);
            targetCat.setId(null);
            targetCat = itemCatDao.save(targetCat);

            List<Item> sourceItems = itemDao.findByMerchantIdAndCatId(sourceId, sourceCat.getId());
            if (!sourceItems.isEmpty()) {

                for (Item sourceItem : sourceItems) {
                    Item targetItem = BeanUtils.copyBean(sourceItem, new Item());
                    targetItem.setMerchant(targetMerchant);
                    targetItem.setCat(targetCat);
                    targetItem.setId(null);
                    itemDao.save(targetItem);

                    List<ItemDetailimg> sourceImgs = itemDetailimgService.find(new ItemDetailimg(sourceItem.getId()));
                    for (ItemDetailimg sourceImg : sourceImgs) {
                        ItemDetailimg targetImg = BeanUtils.copyBean(sourceImg, new ItemDetailimg());

                        targetImg.setReferId(targetItem.getId());
                        targetImg.setId(null);
                        itemDetailimgService.saveNotNull(targetImg);
                    }
                }
            }
        }
    }

    @Override
    public MerchantVO saveNotNull(MerchantVO vo) {
        Merchant entity;

        for (String code : vo.getCollectionCodeList()) {
            if (StringUtils.isNotEmpty(code)) {
                EntityBdMerchantCollectionCode collectionCode = collectionCodeService.findCollectionCode(code);
                if (collectionCode == null) {
                    throw new APIException(ResultCodes.CollectionCodeError);
                }
                if (collectionCode.getMchId() != null && collectionCode.getMchId() != 0 && (vo.getId() == null || collectionCode.getMchId().longValue() != vo.getId().longValue())) {
                    throw new APIException(ResultCodes.CollectionCodeExist, MessageFormat.format(ResultCodes.CollectionCodeExist.getMessage(), code, collectionCode.getMchId()));
                }
            }
        }

        if (vo.getId() == null) {
            entity = BeanUtils.copyBean(vo, new Merchant());
            entity.setLocateExactly(true);
            entity = merchantDao.save(entity);
            merchantStatsDao.save(new MerchantStats(0, entity.getId()));
            merchantStaffService.saveNotNull(new MerchantStaff(entity.getId(), StringUtils.leftPad(entity.getId().toString(), 4, '0')));

            genPayUrl(entity.getId());
        } else {
            entity = merchantDao.findOne(vo.getId());
            Double latitude_before = entity.getLatitude();
            Double longitude_before = entity.getLongitude();
            BeanUtils.copyBean(vo, entity);
            Double latitude_after = entity.getLatitude();
            Double longitude_after = entity.getLongitude();
            if (!latitude_before.equals(latitude_after) || !longitude_before.equals(longitude_after)) {
                entity.setLocateExactly(true);
            }

            if (entity.getFirstshowTime() == null && Boolean.TRUE.equals(entity.getDisplay())) {
                entity.setFirstshowTime(new Date());
            }
            entity = merchantDao.save(entity);
        }
        dealCollectionCodes(vo.getCollectionCodeList(), entity);

        MerchantVO result = new MerchantVO();
        BeanUtils.copyBean(entity, result);
        return result;
    }

    @Transactional
    private void dealCollectionCodes(List<String> codelist, Merchant entity) {
        List<EntityBdMerchantCollectionCode> collectionCodes = collectionCodeService.findEntityBdMerchantCollectionCodesByMchId(entity.getId());
        List<String> existCodeList = new ArrayList<>();
        List<String> tmp = new ArrayList<>();
        for (EntityBdMerchantCollectionCode collectionCode : collectionCodes) {
            existCodeList.add(collectionCode.getCollectionCode());
            tmp.add(collectionCode.getCollectionCode());
        }
        tmp.removeAll(codelist);
        //unbind code
        for (String tmpCode : tmp) {
            for (EntityBdMerchantCollectionCode collectionCode : collectionCodes) {
                if (tmpCode.equals(collectionCode.getCollectionCode())) {
                    do_unbindNumber (collectionCode);
                    break;
                }
            }
        }
        codelist.removeAll(existCodeList);
        //bind code
        for (String code : codelist) {
            EntityBdMerchantCollectionCode collectionCode = collectionCodeService.findCollectionCode(code);
            do_bindNumber (  collectionCode, entity);
        }
    }

    @Override
    public EntityBdMerchantCollectionCode bindNumber ( EntityBdMerchantCollectionCode collectionCode,Long mchId){
        Merchant entity = merchantDao.findOne(mchId);
        return do_bindNumber(collectionCode,entity);
    }

    @Transactional
    private EntityBdMerchantCollectionCode do_bindNumber (EntityBdMerchantCollectionCode collectionCode , Merchant entity){
        if (entity == null) {
            throw new APIException(ResultCodes.NotFoundMerchant);
        }
        collectionCode.setUpdateTime(new Date());
        collectionCode.setMchId(entity.getId());
        collectionCode.setMchName(entity.getName());
//        collectionCode.setCountryId(entity.getDistrict().getCountry().getId());
//        collectionCode.setCountryName(entity.getDistrict().getCountry().getName());

        String target_url ;
        if (collectionCode.getCountryId()!=null && collectionCode.getCountryId().longValue() == 3) {//TH
            target_url = sg_url + "?mchid=" + entity.getId() + "&source=qplcid_" + entity.getId();
        } else {
            target_url = kr_url + "?mchid=" + entity.getId() + "&source=qplcid_" + entity.getId();
        }
        collectionCode.setUrl(target_url);

        CmsUser cmsUser = cmsUserDao.findByEmail(UserHolder.getUser().getEmail());
        if (cmsUser != null) {
            collectionCode.setModifierId(cmsUser.getId());
            collectionCode.setModifier(cmsUser.getUsername());
        }
        collectionCodeService.saveEntityBdMerchantCollectionCode(collectionCode);

        try {
            boolean bind_result = collectionCodeService.bindNumberLink(Long.valueOf(collectionCode.getCollectionCode()), target_url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collectionCode;
    }

    @Override
    public EntityBdMerchantCollectionCode unbindNumber (EntityBdMerchantCollectionCode collectionCode) {
        return do_unbindNumber (collectionCode);
    }

    @Transactional
    public EntityBdMerchantCollectionCode do_unbindNumber (EntityBdMerchantCollectionCode collectionCode) {
        collectionCode.setMchId(null);
        collectionCode.setMchName(null);
//        collectionCode.setCountryId(null);
//        collectionCode.setCountryName(null);
        collectionCode.setUrl(null);
        CmsUser cmsUser = cmsUserDao.findByEmail(UserHolder.getUser().getEmail());
        if (cmsUser != null) {
            collectionCode.setModifierId(cmsUser.getId());
            collectionCode.setModifier(cmsUser.getUsername());
        }
        collectionCode.setUpdateTime(new Date());
        collectionCodeService.saveEntityBdMerchantCollectionCode(collectionCode);
        try {
            boolean unbind_result = collectionCodeService.unbindNumberLink(Long.valueOf(collectionCode.getCollectionCode()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collectionCode;
    }


    /**
     * @param id merchantId
     * @return
     */
    @Override
    @Transactional
    public Channel genPayUrl(Long id) {
        Merchant merchant = merchantDao.findOne(id);
        Long channelId = merchant.getChannelId();

        Channel channel;
        if (channelId != null) {
            channel = channelDao.findOne(channelId);
        } else {
            channel = new Channel();
            channel.setKeyword("");
            channel.setTitle("新优付渠道码_" + id);
            channel.setSceneId("qplcid_" + id);
            channel.setGroupId("kfqdm");
            channel.setChannelType(ChannelType.Link);
            channel.setQrCodeType(CodeType.Persistent);
            channel.setUrl(nuanyouHost + "/view/order/youfu.html?mchid=" + id + "&source=qplcid_" + id);
            channelDao.save(channel);

            merchant.setChannelId(channel.getId());
            merchantDao.save(merchant);
        }
        return channel;
    }

    @Override
    public List<Merchant> findMerchantByCountry(final Long country) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (country != null) {
                    predicate.add(cb.equal(root.get("district").get("country").get("id"), country));
                }
                predicate.add(cb.equal(root.get("display"), true));
                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        return merchantDao.findAll(specification);
    }

    @Override
    public List<Merchant> findMerchantByDistrict(final Long district) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (district != null) {
                    predicate.add(cb.equal(root.get("district").get("id"), district));
                }
                predicate.add(cb.equal(root.get("display"), true));
                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        return merchantDao.findAll(specification);
    }

    @Override
    public Page<Merchant> findMerchantByCountryFilter(final Long country, final String mchName, final Long mchId, Pageable pageable) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                // TODO: 2017/7/14 jpa 无效
                query.multiselect(root.get("id").alias("id"),root.get("name").alias("name"));
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (country != null) {
                    predicate.add(cb.equal(root.get("district").get("country").get("id"), country));
                }
                if (mchName != null) {
                    predicate.add(cb.like(root.get("name"), "%"+mchName+"%"));
                }
                if (mchId != null) {
                    predicate.add(cb.equal(root.get("id"), mchId));
                }
                predicate.add(cb.equal(root.get("display"), true));
                Predicate[] arrays = new Predicate[predicate.size()];

                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        };
        return merchantDao.findAll(specification,pageable);
    }
}
