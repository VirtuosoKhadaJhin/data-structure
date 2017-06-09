package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.dao.*;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.enums.ChannelType;
import com.nuanyou.cms.entity.enums.CodeType;
import com.nuanyou.cms.model.MerchantVO;
import com.nuanyou.cms.service.ItemDetailimgService;
import com.nuanyou.cms.service.MerchantService;
import com.nuanyou.cms.service.MerchantStaffService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.MyCacheManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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


    private static String key = "getMerchantList";
    @Autowired
    private MyCacheManager<List<Merchant>> cacheManager;

    @Override
    public List<Merchant> getIdNameList() {
        return getIdNameList(null);
    }

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
            if(!latitude_before.equals(latitude_after) || !longitude_before.equals(longitude_after)){
                entity.setLocateExactly(false);
            }

            if (entity.getFirstshowTime() == null && Boolean.TRUE.equals(entity.getDisplay())) {
                entity.setFirstshowTime(new Date());
            }
            entity = merchantDao.save(entity);
        }
        return BeanUtils.copyBean(entity, new MerchantVO());
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
}
