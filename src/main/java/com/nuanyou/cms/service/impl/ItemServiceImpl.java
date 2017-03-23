package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.ItemDirectmailDao;
import com.nuanyou.cms.dao.ItemStatsDao;
import com.nuanyou.cms.dao.ItemTuanDao;
import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.ItemDirectmail;
import com.nuanyou.cms.entity.ItemStats;
import com.nuanyou.cms.entity.ItemTuan;
import com.nuanyou.cms.model.ItemVO;
import com.nuanyou.cms.service.ItemService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/20.
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemTuanDao itemTuanDao;

    @Autowired
    private ItemDirectmailDao itemDirectmailDao;

    @Autowired
    private ItemStatsDao itemStatsDao;

    @Autowired
    private ItemDao itemDao;

    @Override
    @Transactional
    public Item saveNotNull(ItemVO vo) {
        Item entity = BeanUtils.copyBeanNotNull(vo, new Item());
        // 如果商户价未录入 默认取现价
        BigDecimal mchPrice = entity.getMchPrice();
        if (mchPrice == null || mchPrice.compareTo(BigDecimal.ZERO) == 0) {
            entity.setMchPrice(entity.getKpPrice());
        }
        if (entity.getId() == null) {
            ItemDirectmail directmail = entity.getDirectmail();
            if (directmail != null)
                directmail.setItem(entity);
            entity = itemDao.save(entity);
            itemStatsDao.save(new ItemStats((double) 5, 0, entity.getId()));
        } else {
            Item oldItem = itemDao.findOne(entity.getId());
            ItemDirectmail directmail = oldItem.getDirectmail();
            if (directmail == null)
                directmail = new ItemDirectmail(oldItem);

            directmail.setPostage(entity.getDirectmail() != null ? entity.getDirectmail().getPostage() : null);

            BeanUtils.copyBean(entity, oldItem);
            oldItem.setSupportType(entity.getSupportType());
            oldItem.setDirectmail(directmail);
            entity = itemDao.save(oldItem);
        }
        return entity;
    }

    /**
     * 若存在商品列表 设置价格为null
     * 取得时候 若单品为0 那么就取出字段值
     *
     * @param entity
     * @param itemTuans
     * @return
     */
    @Override
    @Transactional
    public Item saveNotNull(Item entity, List<ItemTuan> itemTuans) {
        // 如果商户价未录入 默认取现价
        BigDecimal mchPrice = entity.getMchPrice();
        if (mchPrice == null || mchPrice.compareTo(BigDecimal.ZERO) == 0) {
            entity.setMchPrice(entity.getKpPrice());
        }
        if (entity.getId() == null) {
            entity = itemDao.save(entity);
            itemStatsDao.save(new ItemStats((double) 5, 0, entity.getId()));
        } else {
            Item oldItem = itemDao.findOne(entity.getId());

            BeanUtils.copyBeanNotNull(entity, oldItem);
            oldItem.setSupportType(entity.getSupportType());
            oldItem.setTemplateId(entity.getTemplateId());
            entity = itemDao.save(oldItem);

        }

        if (itemTuans != null) {
            Long itemId = entity.getId();
            itemTuanDao.deleteByItemId(itemId);
            for (ItemTuan itemTuan : itemTuans) {
                Item subItem = itemTuan.getSubItem();
                if (subItem.getId() < 1) {
                    subItem.setId(null);
                    subItem.setItemType(4);
                    itemTuan.setSubItem(itemDao.save(subItem));
                }
                itemTuan.setId(null);
                itemTuan.setItemId(itemId);
            }
            if (!itemTuans.isEmpty())
                itemTuanDao.save(itemTuans);
        }
        return entity;
    }

    @Override
    public BigDecimal calcItemTuanPrice(Long id) {
        List<ItemTuan> itemTuanList = itemTuanDao.findByItemId(id);
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (ItemTuan itemTuan : itemTuanList) {
            Item item = itemTuan.getSubItem();
            if (item != null) {
                Integer num = itemTuan.getNum();
                BigDecimal kpPrice = item.getKpPrice();
                if (kpPrice != null) {
                    if (num == null)
                        num = 1;
                    totalPrice = totalPrice.add(kpPrice.multiply(new BigDecimal(num)));
                }
            }
        }
        return totalPrice;
    }

    @Override
    public List<Item> getIdNameList() {
        return itemDao.getIdNameList();
    }

    @Override
    public List<Item> findItemTuans() {
        return this.itemDao.findItemTuans();
    }
}