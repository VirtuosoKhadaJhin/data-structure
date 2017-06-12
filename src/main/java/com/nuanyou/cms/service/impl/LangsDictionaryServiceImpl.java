package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.EntityNyLangsCategoryDao;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.entity.EntityNyLangsCategory;
import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.model.LangsCountryMessageVo;
import com.nuanyou.cms.model.LangsDictionary;
import com.nuanyou.cms.model.LangsDictionaryRequestVo;
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
import java.util.*;

/**
 * Created by Byron on 2017/5/26.
 */
@Service
public class LangsDictionaryServiceImpl implements LangsDictionaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LangsDictionaryServiceImpl.class);

    @Autowired
    private EntityNyLangsDictionaryDao dictionaryDao;

    @Autowired
    private EntityNyLangsCategoryDao categoryDao;

    public LangsDictionaryServiceImpl() {
    }

    @Override
    public LangsDictionary findLangDictionary(Long id) {
        EntityNyLangsDictionary langsDictionary = dictionaryDao.findOne(id);

        return null;
    }

    @Override
    public LangsDictionaryVo findLangsDictionary(Long id) {
        EntityNyLangsDictionary langsDictionary = dictionaryDao.findOne(id);
        //return convertToLangsDictionary ( langsDictionary );
        return null;
    }

    /**
     * 模板参数搜索message（suggest）
     *
     * @param message
     * @return
     */
    @Override
    public List<LangsDictionary> findSuggestSearch(final String message) {
        List<EntityNyLangsDictionary> dictionaries = dictionaryDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (StringUtils.isNotEmpty(message)) {
                    predicate.add(cb.like(root.get("message").as(String.class), "%" + message + "%"));
                }

                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        });

        List<LangsDictionary> dictionaryList = this.convertToMultipleLangsCategories(dictionaries);
        return dictionaryList;
    }

    @Override
    public Page<LangsDictionaryVo> findAllDictionary(final LangsDictionaryRequestVo requestVo) {

        final String baseNameStr = requestVo.getBaseNameStr();

        List<EntityNyLangsCategory> langsCategories = Lists.newArrayList();

        if (StringUtils.isNotEmpty(baseNameStr)) {
            langsCategories = categoryDao.findAll(new Specification() {

                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                    List<Predicate> predicate = new ArrayList<Predicate>();
                    predicate.add(cb.like(root.get("name"), "%" + baseNameStr + "%"));
                    Predicate[] arrays = new Predicate[predicate.size()];
                    return query.where(predicate.toArray(arrays)).getRestriction();
                }
            });
        }
        final List<Long> categoryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(langsCategories)) {
            for (EntityNyLangsCategory langsCategory : langsCategories) {
                categoryIds.add(langsCategory.getId());
            }
        }

        // 查询所有数据
        List<EntityNyLangsDictionary> dictionaries = dictionaryDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (CollectionUtils.isNotEmpty(categoryIds)) {
                    predicate.add(root.get("basename").in(categoryIds));
                }
                if (StringUtils.isNotEmpty(requestVo.getKeyCode())) {
                    predicate.add(cb.equal(root.get("keyCode"), requestVo.getKeyCode()));
                }
                if (StringUtils.isNotEmpty(requestVo.getMessage())) {
                    predicate.add(cb.like(root.get("name"), "%" + baseNameStr + "%"));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        });

        LinkedHashMap<String, LangsDictionaryVo> langsDictionaryMap = this.getStringLangsDictionaryVos(dictionaries, false);

        // 计算分页
        Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());

        Collection<LangsDictionaryVo> values = langsDictionaryMap.values();
        ArrayList<LangsDictionaryVo> langsDictionaryVos = Lists.<LangsDictionaryVo>newArrayList(values);
        Page<LangsDictionaryVo> pageVOs = new PageImpl<LangsDictionaryVo>(langsDictionaryVos, pageable, langsDictionaryMap.size());
        return pageVOs;
    }

    @Override
    public Page<LangsDictionaryVo> findAllLocalDictionary(final LangsDictionaryRequestVo requestVo) {
        final String baseNameStr = requestVo.getBaseNameStr();

        List<EntityNyLangsCategory> langsCategories = Lists.newArrayList();

        if (StringUtils.isNotEmpty(baseNameStr)) {
            langsCategories = categoryDao.findAll(new Specification() {

                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                    List<Predicate> predicate = new ArrayList<Predicate>();
                    predicate.add(cb.like(root.get("name"), "%" + baseNameStr + "%"));
                    Predicate[] arrays = new Predicate[predicate.size()];
                    return query.where(predicate.toArray(arrays)).getRestriction();
                }
            });
        }
        final List<Long> categoryIds = new ArrayList<Long>();
        if (CollectionUtils.isNotEmpty(langsCategories)) {
            for (EntityNyLangsCategory langsCategory : langsCategories) {
                categoryIds.add(langsCategory.getId());
            }
        }

        // 查询所有数据
        List<EntityNyLangsDictionary> dictionaries = dictionaryDao.findAll(new Specification() {

            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                List<Predicate> predicate = new ArrayList<Predicate>();
                if (CollectionUtils.isNotEmpty(categoryIds)) {
                    predicate.add(root.get("basename").in(categoryIds));
                }
                if (StringUtils.isNotEmpty(requestVo.getKeyCode())) {
                    predicate.add(cb.equal(root.get("keyCode"), requestVo.getKeyCode()));
                }
                if (StringUtils.isNotEmpty(requestVo.getMessage())) {
                    predicate.add(cb.like(root.get("name"), "%" + baseNameStr + "%"));
                }
                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        });

        LinkedHashMap<String, LangsDictionaryVo> langsDictionaryMap = this.getStringLangsDictionaryVos(dictionaries, true);

        // 计算分页
        Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());

        Collection<LangsDictionaryVo> values = langsDictionaryMap.values();
        ArrayList<LangsDictionaryVo> langsDictionaryVos = Lists.<LangsDictionaryVo>newArrayList(values);
        Page<LangsDictionaryVo> pageVOs = new PageImpl<LangsDictionaryVo>(langsDictionaryVos, pageable, langsDictionaryMap.size());
        return pageVOs;
    }

    @Override
    public LangsDictionaryVo findLangsDictionary(String keyCode, Locale locale) {
        Example<EntityNyLangsDictionary> example = Example.of(new EntityNyLangsDictionary(keyCode));
        List<EntityNyLangsDictionary> entityNyLangsDictionarys = this.dictionaryDao.findAll(example);

        List<LangsCountryMessageVo> langsMessageList = Lists.newArrayList();
        LangsDictionaryVo dictionaryVo = new LangsDictionaryVo();
        for (EntityNyLangsDictionary langsDictionary : entityNyLangsDictionarys) {
            dictionaryVo.setCategoryId(langsDictionary.getCategory().getId());
            dictionaryVo.setKeyCode(langsDictionary.getKeyCode());

            LangsCountryMessageVo messageVo = this.getLangsCountryMessageVo(langsDictionary);
            langsMessageList.add(messageVo);
        }
        dictionaryVo.setLangsMessageList(langsMessageList);
        return dictionaryVo;
    }

    @Override
    public LangsDictionary addLangsDictionary(LangsDictionary dictionary) {
        EntityNyLangsDictionary entityNyLangsDictionary = this.convertToEntityLangsDictionary(dictionary);
        return this.convertToLangsDictionary(dictionaryDao.save(entityNyLangsDictionary));
    }

    @Override
    public LangsDictionary updateLangsDictionary(LangsDictionary dictionary) {
        EntityNyLangsDictionary entityNyLangsDictionary = this.convertToEntityLangsDictionary(dictionary);
        return this.convertToLangsDictionary(dictionaryDao.saveAndFlush(entityNyLangsDictionary));
    }

    @Override
    public void deleteLangsDictionary(Long id) {
        dictionaryDao.delete(id);
    }

    @Override
    public List<LangsDictionary> findIdNameListByCat(Long id) {
        EntityNyLangsCategory entityNyLangsCategory = categoryDao.findOne(id);

        EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
        entityNyLangsDictionary.setCategory(entityNyLangsCategory);

        Example<EntityNyLangsDictionary> example = Example.of(entityNyLangsDictionary);
        List<EntityNyLangsDictionary> list = this.dictionaryDao.findAll(example);

        List<LangsDictionary> langsDictionaries = convertToMultipleLangsCategories(list);
        return langsDictionaries;
    }

    @Override
    public boolean verifykeyCode(LangsDictionaryVo dictionaryVo) {
        EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
        entityNyLangsDictionary.setKeyCode(dictionaryVo.getKeyCode());

        Example<EntityNyLangsDictionary> example = Example.of(entityNyLangsDictionary);
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findAll(example);

        if (entityResult.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean saveLangsDictionary(LangsDictionaryVo dictionaryVo) {
        // each message of langs
        EntityNyLangsDictionary entityNyLangsDictionary = null;
        if (CollectionUtils.isEmpty(dictionaryVo.getLangsMessageList())) {
            return false;
        }
        for (LangsCountryMessageVo langsCountryMessageVo : dictionaryVo.getLangsMessageList()) {
            String message = langsCountryMessageVo.getMessage();
            if (StringUtils.isEmpty(message)) {
                return false;
            }
            EntityNyLangsCategory entityNyLangsCategory = categoryDao.findOne(dictionaryVo.getCategoryId());
            if (entityNyLangsCategory == null) {
                return false;
            }
            // enum index
            Integer langsKey = langsCountryMessageVo.getLangsKey();
            LangsCountry langsCountry = LangsCountry.toEnum(langsKey);
            // if lang not null, save one record
            entityNyLangsDictionary = new EntityNyLangsDictionary();

            String[] langsCountrys = langsCountry.getValue().split("-");

            entityNyLangsDictionary.setLanguage(langsCountrys[0]);
            entityNyLangsDictionary.setCountry(langsCountrys.length > 1 ? langsCountrys[1] : null);
            entityNyLangsDictionary.setKeyCode(dictionaryVo.getKeyCode());

            entityNyLangsDictionary.setCategory(entityNyLangsCategory);
            entityNyLangsDictionary.setMessage(message);
            this.dictionaryDao.save(entityNyLangsDictionary);
        }
        return true;
    }

    @Override
    public LangsDictionary saveMessage(LangsDictionaryRequestVo vo) {
        EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
        entityNyLangsDictionary.setKeyCode(vo.getKeyCode());

        Example<EntityNyLangsDictionary> example = Example.of(entityNyLangsDictionary);
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findAll(example);

        EntityNyLangsDictionary entity = new EntityNyLangsDictionary();
        String[] splitValues = LangsCountry.toEnum(vo.getLangsKey()).getValue().split("-");

        entity.setLanguage(splitValues[0]);
        entity.setCountry(splitValues.length > 1 ? splitValues[1] : null);
        entity.setKeyCode(vo.getKeyCode());
        entity.setMessage(vo.getMessage());
        entity.setCategory(entityResult.get(0).getCategory());
        EntityNyLangsDictionary nyLangsDictionary = dictionaryDao.save(entity);
        LangsDictionary dictionary = this.convertToLangsDictionary(nyLangsDictionary);
        return dictionary;
    }

    private LinkedHashMap<String, LangsDictionaryVo> getStringLangsDictionaryVos(List<EntityNyLangsDictionary> allDictionaries, boolean isLocalLangs) {
        LinkedHashMap<String, LangsDictionaryVo> langsDictionaryMap = new LinkedHashMap<String, LangsDictionaryVo>();
        List<LangsCountryMessageVo> langsMessageList = Lists.newArrayList();
        LangsCountryMessageVo messageVo = null;
        Set<String> keyCodes = new HashSet<String>();
        LangsDictionaryVo dictionaryVo = null;
        if (isLocalLangs) {
            for (EntityNyLangsDictionary langsDictionary : allDictionaries) {
                if (langsDictionary.getLangsCountry().equals(LangsCountry.ZH_CN.getValue())
                        || langsDictionary.getLangsCountry().equals(LangsCountry.EN_UK.getValue())
                        || langsDictionary.getLangsCountry().equals(LangsCountry.En_GB.getValue())
                        || langsDictionary.getLangsCountry().equals(LangsCountry.DE_DE.getValue())) {
                    this.setLangsDictionaryVoValue(langsDictionaryMap, langsMessageList, keyCodes, langsDictionary);
                }
            }
        } else {
            for (EntityNyLangsDictionary langsDictionary : allDictionaries) {
                this.setLangsDictionaryVoValue(langsDictionaryMap, langsMessageList, keyCodes, langsDictionary);
            }
        }
        return langsDictionaryMap;
    }

    private void setLangsDictionaryVoValue(LinkedHashMap<String, LangsDictionaryVo> langsDictionaryMap, List<LangsCountryMessageVo> langsMessageList, Set<String> keyCodes, EntityNyLangsDictionary langsDictionary) {
        LangsDictionaryVo dictionaryVo;
        LangsCountryMessageVo messageVo;
        String keyCode = langsDictionary.getKeyCode();
        if (!keyCodes.contains(keyCode)) {
            langsMessageList = Lists.newArrayList();
            keyCodes.add(keyCode);
            dictionaryVo = new LangsDictionaryVo();
            dictionaryVo.setCategoryId(langsDictionary.getCategory().getId());
            dictionaryVo.setKeyCode(langsDictionary.getKeyCode());
            messageVo = this.getLangsCountryMessageVo(langsDictionary);
            langsMessageList.add(messageVo);
            dictionaryVo.setLangsMessageList(langsMessageList);
            langsDictionaryMap.put(keyCode, dictionaryVo);
        } else {
            messageVo = this.getLangsCountryMessageVo(langsDictionary);
            LangsDictionaryVo vo = langsDictionaryMap.get(keyCode);
            List<LangsCountryMessageVo> list = vo.getLangsMessageList();
            list.add(messageVo);
        }
    }

    private LangsCountryMessageVo getLangsCountryMessageVo(EntityNyLangsDictionary langsDictionary) {
        LangsCountryMessageVo messageVo;
        messageVo = new LangsCountryMessageVo();
        messageVo.setMessage(langsDictionary.getMessage());
        String langsCountry = langsDictionary.getLangsCountry();
        messageVo.setLangsKey(LangsCountry.toEnum(langsCountry).getKey());
        return messageVo;
    }

    private EntityNyLangsDictionary convertToEntityLangsDictionary(LangsDictionary dictionary) {
        return BeanUtils.copyBean(dictionary, new EntityNyLangsDictionary());
    }

    private LangsDictionary convertToLangsDictionary(EntityNyLangsDictionary entity) {
        LangsDictionary dictionary = BeanUtils.copyBean(entity, new LangsDictionary());
        return dictionary;
    }

    private List<LangsDictionary> convertToMultipleLangsCategories(List<EntityNyLangsDictionary> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Lists.newArrayList();
        }
        List<LangsDictionary> dictionaries = Lists.newArrayList();
        for (EntityNyLangsDictionary entity : entities) {
            dictionaries.add(BeanUtils.copyBean(entity, new LangsDictionary()));
        }
        return dictionaries;
    }
}
