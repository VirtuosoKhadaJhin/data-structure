package com.nuanyou.cms.service.impl;

import com.google.common.base.Strings;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.ItemDirectmailDao;
import com.nuanyou.cms.dao.ItemStatsDao;
import com.nuanyou.cms.dao.ItemTuanDao;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.entity.order.Order;
import com.nuanyou.cms.model.ItemVO;
import com.nuanyou.cms.service.ItemService;
import com.nuanyou.cms.service.UserService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.TimeCondition;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private UserService userService;

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
     */
    @Override
    @Transactional
    public Item saveNotNull(ItemVO vo, List<ItemTuan> itemTuans) {
        boolean hasItems = itemTuans != null && !itemTuans.isEmpty();
        if (hasItems) {
            vo.setOkpPrice(null);
        }
        if(Strings.isNullOrEmpty(vo.getLocalNotice())){
            vo.setLocalNotice(null);
        }
        Item entity = BeanUtils.copyBeanNotNull(vo, new Item());
        Merchant merchant = vo.getMerchant();
        // 如果商户价未录入 默认取现价
//        BigDecimal mchPrice = entity.getMchPrice();
//        if (mchPrice == null || mchPrice.compareTo(BigDecimal.ZERO) == 0) {
//            entity.setMchPrice(entity.getKpPrice());
//        }
        if (entity.getId() == null) {
            entity = itemDao.save(entity);
            itemStatsDao.save(new ItemStats((double) 5, 0, entity.getId()));
        } else {
            Item oldItem = itemDao.findOne(entity.getId());
            BeanUtils.copyBean(entity, oldItem);
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
                    subItem.setMerchant(new Merchant(merchant.getId()));
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
                BigDecimal kpPrice = item.getOkpPrice();
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

    @Override
    public Page<Item> findByCondition(final Item entity, Pageable pageable) {
        final List<Long> countryIds = userService.findUserCountryId();
        return itemDao.findAll(new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (countryIds != null && countryIds.size() > 0) {
                    predicate.add(root.get("merchant").get("district").get("country").get("id").in(countryIds));
                }
                if (StringUtils.isNotBlank(entity.getName())) {
                    Predicate p = cb.equal(root.get("name"), entity.getName());
                    predicate.add(p);
                }
                if (entity.getItemType() != null) {
                    Predicate p = cb.equal(root.get("type"), entity.getItemType());
                    predicate.add(p);
                    predicate.add(cb.equal(root.get("itemType"), entity.getItemType()));
                }
                if (entity.getMerchant() != null && entity.getMerchant().getId() != null) {
                    Predicate p = cb.equal(root.get("merchant").get("id"), entity.getMerchant().getId());
                    predicate.add(p);
                }
                if (entity.getMerchant() != null && StringUtils.isNotBlank(entity.getMerchant().getName())) {
                    Predicate p = cb.equal(root.get("merchant").get("name"), entity.getMerchant().getName());
                    predicate.add(p);
                }
                if (entity.getCat() != null && StringUtils.isNotBlank(entity.getCat().getName())) {
                    Predicate p = cb.equal(root.get("cat").get("name"), entity.getCat().getName());
                    predicate.add(p);
                }
                Predicate[] pre = new Predicate[predicate.size()];
                return query.where(predicate.toArray(pre)).getRestriction();
            }
        }, pageable);
    }
}