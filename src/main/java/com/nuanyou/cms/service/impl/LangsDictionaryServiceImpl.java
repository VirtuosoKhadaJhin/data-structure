package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.model.LangsDictionary;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.LangsDictionaryService;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.Locale;

/**
 * Created by Byron on 2017/5/26.
 */
@Service
public class LangsDictionaryServiceImpl implements LangsDictionaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger ( LangsDictionaryServiceImpl.class );

    @Autowired
    private EntityNyLangsDictionaryDao dictionaryDao;

    public LangsDictionaryServiceImpl() {
    }

    @Override
    public LangsDictionary findLangsDictionary(Long id) {
        EntityNyLangsDictionary langsDictionary = dictionaryDao.findOne ( id );
        return convertToLangsDictionary ( langsDictionary );
    }

    @Override
    public Page<LangsDictionary> findAllDictionary(final LangsDictionary request) {
        Pageable pageable = new PageRequest ( request.getIndex () - 1, PageUtil.pageSize );

        Page<EntityNyLangsDictionary> dictionaries = dictionaryDao.findAll ( new Specification () {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {

                List<Predicate> predicate = new ArrayList<Predicate> ();
                if (StringUtils.isNotEmpty ( request.getBaseName () )) {
                    Predicate p1 = cb.like ( root.get ( "baseName" ), "%" + request.getBaseName () + "%" );
                    predicate.add ( p1 );
                }
                if (StringUtils.isNotEmpty ( request.getKeyCode () )) {
                    Predicate p2 = cb.equal ( root.get ( "keyCode" ), request.getKeyCode () );
                    predicate.add ( p2 );
                }
                if (StringUtils.isNotEmpty ( request.getCountry () )) {
                    Predicate p3 = cb.equal ( root.get ( "country" ), request.getCountry () );
                    predicate.add ( p3 );
                }
                if (StringUtils.isNotEmpty ( request.getLanguage () )) {
                    Predicate p4 = cb.equal ( root.get ( "language" ), request.getLanguage () );
                    predicate.add ( p4 );
                }
                if (StringUtils.isNotEmpty ( request.getMessage () )) {
                    Predicate p5 = cb.equal ( root.get ( "message" ), request.getMessage () );
                    predicate.add ( p5 );
                }
                Predicate[] arrays = new Predicate[predicate.size ()];
                return query.where ( predicate.toArray ( arrays ) ).getRestriction ();
            }
        }, pageable );

        List<LangsDictionary> allDictionaries = this.convertToMultipleLangsCategories ( dictionaries.getContent () );
        Page<LangsDictionary> pageVOs = new PageImpl<LangsDictionary> ( allDictionaries, pageable, dictionaries.getTotalPages () );
        return pageVOs;
    }

    @Override
    public LangsDictionary findLangsDictionary(String keyCode, Locale locale) {
        EntityNyLangsDictionary entityDictionary = new EntityNyLangsDictionary ( keyCode, locale.getLanguage (), locale.getCountry (), locale.getVariant () );
        Example<EntityNyLangsDictionary> example = Example.of ( entityDictionary );
        EntityNyLangsDictionary entityResult = dictionaryDao.findOne ( example );
        return convertToLangsDictionary ( entityResult );
    }

    @Override
    public LangsDictionary addLangsDictionary(LangsDictionary dictionary) {
        EntityNyLangsDictionary entityNyLangsDictionary = this.convertToEntityLangsDictionary ( dictionary );
        return this.convertToLangsDictionary ( dictionaryDao.save ( entityNyLangsDictionary ) );
    }

    @Override
    public LangsDictionary updateLangsDictionary(LangsDictionary dictionary) {
        EntityNyLangsDictionary entityNyLangsDictionary = this.convertToEntityLangsDictionary ( dictionary );
        return this.convertToLangsDictionary ( dictionaryDao.saveAndFlush ( entityNyLangsDictionary ) );
    }

    @Override
    public void deleteLangsDictionary(Long id) {
        dictionaryDao.delete ( id );
    }

    @Override
    public List<LangsDictionary> findIdNameListByCat(Long id) {
        List<EntityNyLangsDictionary> list=this.dictionaryDao.findByCategoryId(id);
        List<LangsDictionary> langsDictionaries = convertToMultipleLangsCategories(list);
        return langsDictionaries;
    }

    private EntityNyLangsDictionary convertToEntityLangsDictionary(LangsDictionary dictionary) {
        return BeanUtils.copyBean ( dictionary, new EntityNyLangsDictionary () );
    }

    private LangsDictionary convertToLangsDictionary(EntityNyLangsDictionary entity) {
        LangsDictionary dictionary = BeanUtils.copyBean ( entity, new LangsDictionary () );
        return dictionary;
    }

    private List<LangsDictionary> convertToMultipleLangsCategories(List<EntityNyLangsDictionary> entities) {
        if (CollectionUtils.isEmpty ( entities )) {
            return Lists.newArrayList ();
        }
        List<LangsDictionary> dictionaries = Lists.newArrayList ();
        for (EntityNyLangsDictionary entity : entities) {
            dictionaries.add ( BeanUtils.copyBean ( entity, new LangsDictionary () ) );
        }
        return dictionaries;
    }
}
