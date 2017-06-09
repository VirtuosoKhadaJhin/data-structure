package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.EntityNyLangsCategoryDao;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.entity.EntityNyLangsCategory;
import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.model.LangsCountryMessageVo;
import com.nuanyou.cms.model.LangsDictionary;
import com.nuanyou.cms.model.LangsDictionaryVo;
import com.nuanyou.cms.model.enums.LangsCountry;
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

    @Autowired
    private EntityNyLangsCategoryDao categoryDao;

    public LangsDictionaryServiceImpl() {
    }

    @Override
    public LangsDictionaryVo findLangsDictionary(Long id) {
        EntityNyLangsDictionary langsDictionary = dictionaryDao.findOne ( id );
        //return convertToLangsDictionary ( langsDictionary );
        return null;
    }

    @Override
    public Page<LangsDictionary> findAllDictionary(final LangsDictionary request) {

        Pageable pageable = new PageRequest ( request.getIndex () - 1, request.getSize () );

        Page<EntityNyLangsDictionary> uniqueDictionaries = dictionaryDao.findAll ( new Specification () {

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

        List<LangsDictionary> allDictionaries = this.convertToMultipleLangsCategories ( uniqueDictionaries.getContent () );
        Page<LangsDictionary> pageVOs = new PageImpl<LangsDictionary> ( allDictionaries, pageable, uniqueDictionaries.getTotalPages () );
        return pageVOs;
    }

    @Override
    public List<LangsDictionaryVo> findAllLangsDictionary(final LangsDictionary request) {
        // 查询所有baseNames
/*
        List<EntityNyLangsDictionary> dictionaries = dictionaryDao.findAll();

        List<String> baseNames = new ArrayList<String>();
        for(EntityNyLangsDictionary entityNyLangsDictionary : dictionaries){
            if(!baseNames.contains(entityNyLangsDictionary.getKeyCode())){
                baseNames.add(entityNyLangsDictionary.getKeyCode());
            }
        }

        LangsCountry[] langsCountrys = LangsCountry.values();
        List<LangsDictionaryVo> langsDictionaryVos =new ArrayList<LangsDictionaryVo>();
        for(String code : baseNames){
            LangsDictionaryVo langsDictionaryVo = new LangsDictionaryVo();
            langsDictionaryVo.setKeyCode(code);

            List<LangsCountryVo> langsCountryVos = new ArrayList<LangsCountryVo>();
            for(LangsCountry langsCountry : langsCountrys){
                EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
                entityNyLangsDictionary.setKeyCode(code);
                entityNyLangsDictionary.setCountry(langsCountry.getDesc());

                ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("keyCode", contains().ignoreCase());

                EntityNyLangsDictionary etityNyLangsDictionary = dictionaryDao.findOne(Example.of(entityNyLangsDictionary));

                if(null == etityNyLangsDictionary){
                    langsCountryVos.add(new LangsCountryVo("空","空"));
                }else{
                    langsCountryVos.add(new LangsCountryVo(etityNyLangsDictionary.getLanguage(), etityNyLangsDictionary.getCountry()));
                }
            }
            langsDictionaryVo.setLangsCountryList(langsCountryVos);
            langsDictionaryVos.add(langsDictionaryVo);
        }
*/

        return null;
    }

    @Override
    public LangsDictionaryVo findLangsDictionary(String keyCode, Locale locale) {
        EntityNyLangsDictionary entityDictionary = new EntityNyLangsDictionary ( keyCode, locale.getLanguage (), locale.getCountry (), locale.getVariant () );
        Example<EntityNyLangsDictionary> example = Example.of ( entityDictionary );
        EntityNyLangsDictionary entityResult = dictionaryDao.findOne ( example );
        //return convertToLangsDictionary ( entityResult );
        return null;
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
        EntityNyLangsCategory entityNyLangsCategory = categoryDao.findOne(id);

        EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
        entityNyLangsDictionary.setCategory(entityNyLangsCategory);

        Example<EntityNyLangsDictionary> example = Example.of ( entityNyLangsDictionary );
        List<EntityNyLangsDictionary> list = this.dictionaryDao.findAll( example );

        List<LangsDictionary> langsDictionaries = convertToMultipleLangsCategories(list);
        return langsDictionaries;
    }

    @Override
    public boolean verifykeyCode(LangsDictionaryVo dictionaryVo) {
        EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
        entityNyLangsDictionary.setKeyCode(dictionaryVo.getKeyCode());

        Example<EntityNyLangsDictionary> example = Example.of ( entityNyLangsDictionary );
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findAll ( example );

        if(entityResult.size() == 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean saveLangsDictionary(LangsDictionaryVo dictionaryVo) {
        // each message of langs
        EntityNyLangsDictionary entityNyLangsDictionary = null;
        if (CollectionUtils.isEmpty ( dictionaryVo.getLangsMessageList () )) {
            return false;
        }
        for (LangsCountryMessageVo langsCountryMessageVo : dictionaryVo.getLangsMessageList ()) {
            String message = langsCountryMessageVo.getMessage ();
            if (StringUtils.isEmpty ( message )) {
                return false;
            }
            EntityNyLangsCategory entityNyLangsCategory = categoryDao.findOne ( dictionaryVo.getCategoryId () );
            if (entityNyLangsCategory == null) {
                return false;
            }
            // enum index
            Integer langsKey = langsCountryMessageVo.getLangsKey ();
            LangsCountry langsCountry = LangsCountry.toEnum ( langsKey );
            // if lang not null, save one record
            entityNyLangsDictionary = new EntityNyLangsDictionary ();
            entityNyLangsDictionary.setCountry ( langsCountry.getValue () );
            entityNyLangsDictionary.setKeyCode ( dictionaryVo.getKeyCode () );
            entityNyLangsDictionary.setLanguage ( langsCountry.getDesc () );
            entityNyLangsDictionary.setCategory ( entityNyLangsCategory );
            entityNyLangsDictionary.setMessage ( message );
            this.dictionaryDao.save ( entityNyLangsDictionary );
        }
        return true;
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
