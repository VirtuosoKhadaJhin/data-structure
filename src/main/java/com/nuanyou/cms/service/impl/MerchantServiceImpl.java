package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.dao.ItemCatDao;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.dao.MerchantStatsDao;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.model.MerchantVO;
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
    private ItemCatDao itemCatDao;

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private MerchantStatsDao merchantStatsDao;

    @Autowired
    private MerchantStaffService merchantStaffService;


    private static String key = "getMerchantList";
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

                List<Item> targetItems = new ArrayList<>(sourceItems.size());
                for (Item sourceItem : sourceItems) {
                    Item targetItem = BeanUtils.copyBean(sourceItem, new Item());

                    targetItem.setMerchant(targetMerchant);
                    targetItem.setCat(targetCat);
                    targetItem.setId(null);
                    targetItems.add(targetItem);
                }
                itemDao.save(targetItems);
            }
        }
    }

    @Override
    public MerchantVO saveNotNull(MerchantVO vo) {
        Merchant entity;

        if (vo.getId() == null) {
            entity = BeanUtils.copyBean(vo, new Merchant());
            entity = merchantDao.save(entity);
            merchantStatsDao.save(new MerchantStats(0, entity.getId()));
            merchantStaffService.saveNotNull(new MerchantStaff(entity.getId(), StringUtils.leftPad(entity.getId().toString(), 4, '0')));
        } else {
            entity = merchantDao.findOne(vo.getId());
            BeanUtils.copyBean(vo, entity);

            if (entity.getFirstshowTime() == null && Boolean.TRUE.equals(entity.getDisplay())) {
                entity.setFirstshowTime(new Date());
            }
            entity = merchantDao.save(entity);
        }
        return BeanUtils.copyBean(entity, new MerchantVO());
    }
}
