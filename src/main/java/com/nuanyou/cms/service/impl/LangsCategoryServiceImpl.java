package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.EntityNyLangsCategoryDao;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.entity.EntityNyLangsCategory;
import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.model.LangsCategory;
import com.nuanyou.cms.model.LangsCategoryVo;
import com.nuanyou.cms.service.LangsCategoryService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Byron on 2017/5/27.
 */
@Service
public class LangsCategoryServiceImpl implements LangsCategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LangsCategoryServiceImpl.class);

    @Autowired
    private EntityNyLangsCategoryDao categoryDao;

    @Autowired
    private EntityNyLangsDictionaryDao dictionaryDao;

    @Override
    public Page<LangsCategory> findAllCategories(final LangsCategory request) {

        Pageable pageable = new PageRequest(request.getIndex() - 1, request.getSize());

        List<EntityNyLangsCategory> categories = categoryDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {

                List<Predicate> predicate = new ArrayList<Predicate>();
                if (StringUtils.isNotEmpty(request.getName())) {
                    Predicate p1 = cb.like(root.get("name"), "%" + request.getName() + "%");
                    predicate.add(p1);
                }
                if (request.getIsGlobal() != null) {
                    Predicate p2 = cb.equal(root.get("isGlobal"), request.getIsGlobal());
                    predicate.add(p2);
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        });

        List<LangsCategory> allCate = this.convertToMultipleLangsCategories(categories);
        Page<LangsCategory> pageVOs = new PageImpl<LangsCategory>(allCate, pageable, categories.size());
        return pageVOs;
    }

    @Override
    public List<LangsCategory> findAllCategories() {
        List<EntityNyLangsCategory> categories = categoryDao.findAll();
        List<LangsCategory> allCate = this.convertToMultipleLangsCategories(categories);
        return allCate;
    }

    @Override
    public LangsCategory findLangsCategory(Long id) {
        return this.convertToLangsCategory(categoryDao.findOne(id));
    }

    @Override
    public LangsCategory save(LangsCategory category) {
        EntityNyLangsCategory langsCat = this.convertToEntityLangsCategory(category);
        return this.convertToLangsCategory(categoryDao.save(langsCat));
    }

    @Override
    public LangsCategory update(LangsCategory category) {
        EntityNyLangsCategory langsCat = this.convertToEntityLangsCategory(category);
        return this.convertToLangsCategory(categoryDao.saveAndFlush(langsCat));
    }

    @Override
    public void delete(Long id) {
        categoryDao.delete(id);
    }

    @Override
    public Boolean verifyRelatedLangsDictionary(LangsCategoryVo categoryVo) {
        // 查询要删除的分类
        EntityNyLangsCategory entityNyLangsCategory = new EntityNyLangsCategory();
        entityNyLangsCategory.setId(categoryVo.getId());

        Example<EntityNyLangsCategory> example = Example.of(entityNyLangsCategory);
        EntityNyLangsCategory category = categoryDao.findOne(example);

        EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
        entityNyLangsDictionary.setCategory(category);

        Example<EntityNyLangsDictionary> langsExample = Example.of(entityNyLangsDictionary);
        langsExample = Example.of(entityNyLangsDictionary);
        List<EntityNyLangsDictionary> entityNyLangsDictionarys = dictionaryDao.findAll(langsExample);

        if(entityNyLangsDictionarys.size() == 0){
            categoryDao.delete(category);
        }

        return BooleanUtils.isTrue(entityNyLangsDictionarys.size() > 0);

    }

    private EntityNyLangsCategory convertToEntityLangsCategory(LangsCategory category) {
        return BeanUtils.copyBean(category, new EntityNyLangsCategory());
    }

    private LangsCategory convertToLangsCategory(EntityNyLangsCategory category) {
        LangsCategory langsCategory = BeanUtils.copyBean(category, new LangsCategory());
        return langsCategory;
    }

    private List<LangsCategory> convertToMultipleLangsCategories(List<EntityNyLangsCategory> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        List<LangsCategory> categories = Lists.newArrayList();
        for (EntityNyLangsCategory entity : entities) {
            categories.add(BeanUtils.copyBean(entity, new LangsCategory()));
        }
        return categories;
    }
}
