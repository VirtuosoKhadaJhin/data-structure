package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.dao.MerchantStatsDao;
import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MerchantStaff;
import com.nuanyou.cms.entity.MerchantStats;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.MerchantStaffService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.MyCacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private MerchantDao merchantDao;

    @Autowired
    private MerchantStatsDao merchantStatsDao;

    @Autowired
    private MerchantStaffService merchantStaffService;


    private static String key="getMerchantList";
    @Autowired
    private MyCacheManager<List<Merchant>> cacheManager;

    @Override
    public List<Merchant> getIdNameList() {
        List<Merchant> merchants = merchantDao.getIdNameList();
        return merchants;

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

        List<Item> targetItemList = itemDao.findByMerchantId(targetId);
        itemDao.delete(targetItemList);

        List<Item> sourceItemList = itemDao.findByMerchantId(sourceId);
        targetItemList = new ArrayList<>(sourceItemList.size());
        for (Item sourceItem : sourceItemList) {
            Item targetItem = new Item();
            BeanUtils.copyBean(sourceItem, targetItem);
            targetItem.setMerchant(targetMerchant);
            targetItem.setId(null);
            targetItemList.add(targetItem);
        }
        itemDao.save(targetItemList);
    }

    @Override
    public Merchant saveNotNull(Merchant entity) {
        if (entity.getId() == null) {
            entity = merchantDao.save(entity);
            merchantStatsDao.save(new MerchantStats(0, entity.getId()));
            merchantStaffService.saveNotNull(new MerchantStaff(entity.getId(), StringUtils.leftPad(entity.getId().toString(), 4, '0')));
            return entity;
        }
        Merchant oldEntity = merchantDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);

        if (entity.getFirstshowTime() == null && Boolean.TRUE.equals(entity.getDisplay())) {
            entity.setFirstshowTime(new Date());
        }
        oldEntity.setDisplay(entity.getDisplay());
        oldEntity.setIssign(entity.getIssign());
        oldEntity.setRecommend(entity.getRecommend());
        oldEntity.setBusinessDay(entity.getBusinessDay());
        oldEntity.setSupportType(entity.getSupportType());
        oldEntity.setPayTypes(entity.getPayTypes());
        return merchantDao.save(oldEntity);
    }
}
