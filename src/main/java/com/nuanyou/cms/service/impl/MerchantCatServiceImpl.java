package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.dao.MerchantCatDao;
import com.nuanyou.cms.dao.MerchantDao;
import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.entity.MerchantCat;
import com.nuanyou.cms.model.MerchantCatResultVo;
import com.nuanyou.cms.model.MerchantCatVo;
import com.nuanyou.cms.service.MerchantCatService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by nuanyou on 2016/9/6.
 */
@Service
public class MerchantCatServiceImpl implements MerchantCatService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantCatServiceImpl.class);

    @Autowired
    private MerchantCatDao merchantCatDao;
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private EntityNyLangsDictionaryDao dictionaryDao;

    @Override
    public void add(MerchantCat merchantCat) {
        merchantCatDao.save(merchantCat);
    }

    @Override
    public List<MerchantCatVo> findAllParentCats() {
        List<MerchantCat> merchantCats = merchantCatDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(cb.isNull(root.get("pcat")));
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.asc(root.get("sort")), cb.asc(root.get("updatetime")));
                return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
            }
        });
        List<MerchantCatVo> vos = Lists.newArrayList();
        for (MerchantCat entity : merchantCats) {
            vos.add(convertToMerchantCatVo(entity));
        }
        return vos;
    }

    @Override
    public List<MerchantCatVo> findParentCat(final MerchantCat entity, Integer index, final Locale locale) {
        // Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        // 孙昊修改了查询方式, 在原有的字段sort上实现了排序功能
        List<MerchantCat> merchantCats = merchantCatDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(cb.isNull(root.get("pcat")));

                if (entity.getId() != null) {
                    predicate.add(cb.equal(root.get("id"), entity.getId()));
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getName())) {
                    predicate.add(cb.like(root.get("name"), "%" + entity.getName() + "%"));
                }
                if (null != entity.getDisplay()) {
                    predicate.add(cb.equal(root.get("display"), entity.getDisplay().booleanValue()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.asc(root.get("sort")), cb.asc(root.get("updatetime")));
                return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
            }
        }/*, pageable*/);

        // keyCode结果集
        final List<String> searchKeyCodes = Lists.newArrayList();

        List<MerchantCatResultVo> merchantCatResultVos = Lists.newArrayList();

        // 根据截取中文和英文
        List<MerchantCat> pageLists = merchantCats;
        List<MerchantCatVo> merchantCatVos = Lists.newArrayList();

        MerchantCatResultVo merchantCatResultVo;
        for (MerchantCat cat : pageLists) {
            MerchantCatVo vo = convertToMerchantCatVo(cat);
            merchantCatVos.add(vo);
            if (cat.getKeyCode() != null) {
                merchantCatResultVo = new MerchantCatResultVo(cat.getKeyCode());
                merchantCatResultVos.add(merchantCatResultVo);
                searchKeyCodes.add(cat.getKeyCode());
            } else {
                merchantCatResultVo = new MerchantCatResultVo("");
                merchantCatResultVos.add(merchantCatResultVo);
                searchKeyCodes.add("");
            }
        }

        if (searchKeyCodes != null && searchKeyCodes.size() > 0) {
            List<EntityNyLangsDictionary> dicts = dictionaryDao.findAll(new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                    List<Predicate> predicate = new ArrayList<Predicate>();

                    predicate.add(root.get("keyCode").in(searchKeyCodes));

                    Predicate[] arrays = new Predicate[predicate.size()];
                    return query.where(predicate.toArray(arrays)).getRestriction();
                }
            });

            for (MerchantCatVo vo : merchantCatVos) {
                if (StringUtils.isNotEmpty(vo.getKeyCode())) {
                    for (EntityNyLangsDictionary dict : dicts) {
                        if (dict.getKeyCode().equals(vo.getKeyCode())) {
                            if (dict.getLanguage().equals("en")) {
                                vo.setEnNameLabel(dict.getMessage());
                            } else if (dict.getLanguage().equals(locale.getLanguage())) {
                                vo.setLocalNameLabel(dict.getMessage());
                            }
                        }
                    }
                }
            }

            return merchantCatVos;
        }

        return merchantCatVos;
    }

    @Override
    public Page<MerchantCatVo> findChildCat(final MerchantCatVo entity, Integer index, final Locale locale, final Long pcatId) {
        Pageable pageable = new PageRequest(index - 1, 10);

        // 孙昊修改了查询方式, 在原有的字段sort上实现了排序功能
        Page<MerchantCat> merchantCats = merchantCatDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                predicate.add(cb.equal(root.get("pcat").get("id"), pcatId));
                if (entity.getId() != null) {
                    predicate.add(cb.equal(root.get("id"), entity.getId()));
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getName())) {
                    predicate.add(cb.like(root.get("name"), "%" + entity.getName() + "%"));
                }
                if (null != entity.getDisplay()) {
                    predicate.add(cb.equal(root.get("display"), entity.getDisplay().booleanValue()));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                ArrayList<Order> orderBys = Lists.newArrayList(cb.asc(root.get("sort")), cb.asc(root.get("updatetime")));
                return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
            }
        }, pageable);

        // keyCode结果集
        final List<String> keyCodes = Lists.newArrayList();

        // 根据截取中文和英文
        List<MerchantCat> pageLists = merchantCats.getContent();
        List<MerchantCatVo> merchantCatVos = Lists.newArrayList();
        for (MerchantCat cat : pageLists) {
            MerchantCatVo vo = convertToMerchantCatVo(cat);
            merchantCatVos.add(vo);
            if (cat.getKeyCode() != null) {
                keyCodes.add(cat.getKeyCode());
            } else {
                keyCodes.add("");
            }
        }

        if (keyCodes != null && keyCodes.size() > 0) {
            List<EntityNyLangsDictionary> dicts = dictionaryDao.findAll(new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                    List<Predicate> predicate = new ArrayList<Predicate>();

                    predicate.add(root.get("keyCode").in(keyCodes));

                    Predicate[] arrays = new Predicate[predicate.size()];
                    return query.where(predicate.toArray(arrays)).getRestriction();
                }
            });

            for (EntityNyLangsDictionary dict : dicts) {
                int keyCodeIndex = keyCodes.indexOf(dict.getKeyCode());

                MerchantCatVo merchantCatVo = merchantCatVos.get(keyCodeIndex);

                if (dict.getLanguage().equals("en")) {
                    merchantCatVo.setEnNameLabel(dict.getMessage());
                } else if (dict.getLanguage().equals(locale.getLanguage())) {
                    merchantCatVo.setLocalNameLabel(dict.getMessage());
                }

                // 更新链表的甘油中英文的数据
                merchantCatVos.set(keyCodeIndex, merchantCatVo);
            }

            Page<MerchantCatVo> lists = new PageImpl<MerchantCatVo>(merchantCatVos, pageable, merchantCats.getTotalElements());

            return lists;
        }

        Page<MerchantCatVo> lists = new PageImpl<MerchantCatVo>(merchantCatVos, pageable, merchantCats.getTotalElements());
        return lists;
    }

    @Override
    public void updateMerchantCat(MerchantCatVo merchantCatVo) {
        // 父节点
        if (merchantCatVo.getPcat() == null) {
            merchantCatVo.setPcat(null);
        }

        String nameLabel = merchantCatVo.getName();
        String shnName = merchantCatVo.getShortname();

        try {
            if (StringUtils.isNotEmpty(nameLabel)) {
                nameLabel = nameLabel.substring(0, nameLabel.indexOf("("));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (StringUtils.isNotEmpty(shnName)) {
                shnName = shnName.substring(0, shnName.indexOf("("));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        MerchantCat merchantCat = new MerchantCat(nameLabel, shnName, merchantCatVo.getDisplay(), merchantCatVo.getSort(),
                merchantCatVo.getImageUrl(), merchantCatVo.getMapimgurl());

        merchantCat.setKeyCode(merchantCatVo.getKeycode());
        merchantCat.setShnKeyCode(merchantCatVo.getShnKeyCode());

        if(StringUtils.isEmpty(merchantCatVo.getKeycode())){
            merchantCat.setKeyCode(null);
        }
        if(StringUtils.isEmpty(merchantCatVo.getShnKeyCode())){
            merchantCat.setShnKeyCode(null);
        }

        if (merchantCatVo.getPcat() != null) {
            MerchantCat cat = merchantCatDao.findOne(merchantCatVo.getPcat());
            merchantCat.setPcat(cat);
        } else {
            merchantCat.setPcat(null);
        }
        if (merchantCatVo.getId() == null) {
            merchantCatDao.save(merchantCat);
        } else {
            MerchantCat oldEntity = merchantCatDao.findOne(merchantCatVo.getId());
            BeanUtils.copyBeanNotNull(merchantCat, oldEntity);
            oldEntity.setUpdatetime(new Date());
            if (merchantCatVo.getPcat() == null) {
                oldEntity.setPcat(null);
            }
            if(StringUtils.isEmpty(merchantCatVo.getKeycode())){
                oldEntity.setKeyCode(null);
            }
            if(StringUtils.isEmpty(merchantCatVo.getShnKeyCode())){
                oldEntity.setShnKeyCode(null);
            }
            merchantCatDao.save(oldEntity);
        }

    }

    @Override
    public Boolean delCat(MerchantCatVo merchantCatVo) {
        MerchantCat merchantCat = new MerchantCat(merchantCatVo.getId());
        List<MerchantCat> pCats = merchantCatDao.findByPcat(merchantCat);

        if (pCats.size() > 0) {
            return false;
        } else {
            // merchantCatDao.delete(merchantCatVo.getId());
        }

        return true;
    }

    @Override
    public List<MerchantCat> getIdNameList() {
        return this.merchantCatDao.getIdNameList();
    }

    @Override
    public MerchantCat saveNotNull(MerchantCat entity) {
        if (entity.getId() == null) {
            return merchantCatDao.save(entity);
        }
        MerchantCat oldEntity = merchantCatDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        return merchantCatDao.save(oldEntity);
    }

    private MerchantCatVo convertToMerchantCatVo(MerchantCat entity) {
        MerchantCatVo merchantCatVo = BeanUtils.copyBean(entity, new MerchantCatVo());
        return merchantCatVo;
    }

}
