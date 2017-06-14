package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.EntityNyLangsCategoryDao;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.dao.EntityNyLangsMessageTipDao;
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
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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

    @Autowired
    private EntityNyLangsMessageTipDao messageTipDao;

    private static final Integer LOCAL_KEY = 5;

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
    public void remove(LangsDictionaryRequestVo requestVo) {
        EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
        entityNyLangsDictionary.setKeyCode(requestVo.getKeyCode());

        Example<EntityNyLangsDictionary> example = Example.of(entityNyLangsDictionary);
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findAll(example);

        dictionaryDao.delete(entityResult);
    }

    @Override
    public void modifyLangsDictionary(LangsDictionaryVo requestVo) {
        EntityNyLangsDictionary entityNyLangsDictionary;

        entityNyLangsDictionary = new EntityNyLangsDictionary();

        String keyCode = requestVo.getKeyCode();
        try {
            keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        entityNyLangsDictionary.setKeyCode(keyCode);

        Example<EntityNyLangsDictionary> example = Example.of(entityNyLangsDictionary);
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findAll(example);

        dictionaryDao.delete(entityResult);

        EntityNyLangsCategory entityNyLangsCategory = categoryDao.findOne(requestVo.getCategoryId());

        // 迭代每一个语言的数据
        for (LangsCountryMessageVo langsCountryMessageVo : requestVo.getLangsMessageList()) {
            if (StringUtils.isNotEmpty(langsCountryMessageVo.getMessage())) {
                entityNyLangsDictionary = new EntityNyLangsDictionary();

                // ENUM
                LangsCountry langsCountry = LangsCountry.toEnum(langsCountryMessageVo.getLangsKey());
                String[] langsCountrys = langsCountry.getValue().split("-");

                entityNyLangsDictionary = new EntityNyLangsDictionary();

                entityNyLangsDictionary.setKeyCode(keyCode);
                entityNyLangsDictionary.setCategory(entityNyLangsCategory);
                entityNyLangsDictionary.setDelFlag(false);
                entityNyLangsDictionary.setCreateDt(new Date());
                entityNyLangsDictionary.setUpdateDt(new Date());
                entityNyLangsDictionary.setLanguage(langsCountrys[0]);
                entityNyLangsDictionary.setCountry(langsCountrys.length > 1 ? langsCountrys[1] : langsCountrys[0]);
                entityNyLangsDictionary.setMessage(langsCountryMessageVo.getMessage());
                dictionaryDao.save(entityNyLangsDictionary);
            }
        }
    }

    @Override
    public List<LangsDictionary> viewLocalLangsDictionary(LangsDictionaryVo dictionaryVo) {
        EntityNyLangsDictionary entityNyLangsDictionary = new EntityNyLangsDictionary();
        entityNyLangsDictionary.setKeyCode(dictionaryVo.getKeyCode());

        Example<EntityNyLangsDictionary> example = Example.of(entityNyLangsDictionary);
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findAll(example);

        List<EntityNyLangsDictionary> dictionaries = new ArrayList<EntityNyLangsDictionary>();
        for (EntityNyLangsDictionary entity : entityResult) {
            if (LangsCountry.verifyIsLocalLanguage(entity.getLanguage() + "-" + entity.getCountry(), LOCAL_KEY)) {
                dictionaries.add(entity);
            }
        }

        List<LangsDictionary> dictionaryList = this.convertToMultipleLangsCategories(dictionaries);

        return dictionaryList;
    }

    @Override
    public LangsCountry viewLocalLanguage(HttpServletRequest request) {
        Locale locale = request.getLocale();
        LangsCountry langCountry = LangsCountry.toEnum(locale.getLanguage() + "-" + locale.getCountry());
        return langCountry;
    }

    @Override
    public Page<LangsDictionaryVo> findAllDictionary(final LangsDictionaryRequestVo requestVo) {
        return findAllDictioryByRequestVo(requestVo, false);
    }

    @Override
    public Page<LangsDictionaryVo> findAllLocalDictionary(final LangsDictionaryRequestVo requestVo) {
        return findAllDictioryByRequestVo(requestVo, true);
    }


    @Override
    public LangsDictionaryVo findLangsDictionary(String keyCode, Locale locale) throws UnsupportedEncodingException {
        keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();

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
    public String findLocalMessageByKeyCode(String keyCode, Locale locale) {
        try {
            keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("throw", e);
        }
        Example<EntityNyLangsDictionary> example = Example.of(new EntityNyLangsDictionary(keyCode));
        List<EntityNyLangsDictionary> entityNyLangsDictionarys = this.dictionaryDao.findAll(example);
        List<LangsCountryMessageVo> langsMessageList = Lists.newArrayList();
        LangsDictionaryVo dictionaryVo = new LangsDictionaryVo();
        for (EntityNyLangsDictionary langsDictionary : entityNyLangsDictionarys) {
            if (langsDictionary.getCountry().equals(locale.getCountry()) && langsDictionary.getLanguage().equals(locale.getLanguage())) {
                return langsDictionary.getMessage();
            }
        }
        return null;
    }

    @Override
    public List<LangsDictionary> findAllLanguagesByCatId(Long id) {
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
    public LangsDictionary saveLangsDictionary(LangsDictionaryVo dictionaryVo) {
        String localMessage = "";
        // each message of langs
        EntityNyLangsDictionary entityNyLangsDictionary = null;
        if (CollectionUtils.isEmpty(dictionaryVo.getLangsMessageList())) {
            return null;
        }
        for (LangsCountryMessageVo langsCountryMessageVo : dictionaryVo.getLangsMessageList()) {
            String message = langsCountryMessageVo.getMessage();
            EntityNyLangsCategory entityNyLangsCategory = categoryDao.findOne(dictionaryVo.getCategoryId());
            if (entityNyLangsCategory == null) {
                return null;
            }
            if (StringUtils.isNotEmpty(message)) {
                // enum index
                Integer langsKey = langsCountryMessageVo.getLangsKey();
                LangsCountry langsCountry = LangsCountry.toEnum(langsKey);
                // if lang not null, save one record
                entityNyLangsDictionary = new EntityNyLangsDictionary();

                String[] langsCountrys = langsCountry.getValue().split("-");

                entityNyLangsDictionary.setLanguage(langsCountrys[0]);
                entityNyLangsDictionary.setDelFlag(false);
                entityNyLangsDictionary.setCountry(langsCountrys.length > 1 ? langsCountrys[1] : langsCountrys[0]);
                entityNyLangsDictionary.setKeyCode(dictionaryVo.getKeyCode());

                entityNyLangsDictionary.setCreateDt(new Date());
                entityNyLangsDictionary.setCategory(entityNyLangsCategory);
                entityNyLangsDictionary.setMessage(message);
                this.dictionaryDao.save(entityNyLangsDictionary);

/*                // 查询本地化语言
                String keyCode = entityNyLangsDictionary.getKeyCode();
                if (BooleanUtils.isTrue(LangsCountry.toEnum(langsKey).getKey() == LOCAL_KEY)) {
                    Locale locale = new Locale(entityNyLangsDictionary.getLanguage(), entityNyLangsDictionary.getCountry());
                    localMessage = findLocalMessageByKeyCode(keyCode, locale);
                }*/
            }
        }

        LangsDictionary langsDictionary = convertToLangsDictionary(entityNyLangsDictionary);

        return langsDictionary;
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
        entity.setCountry(splitValues.length > 1 ? splitValues[1] : splitValues[0]);
        entity.setKeyCode(vo.getKeyCode());
        entity.setMessage(vo.getMessage());
        entity.setCreateDt(new Date());
        entity.setDelFlag(false);
        entity.setCategory(entityResult.get(0).getCategory());
        EntityNyLangsDictionary nyLangsDictionary = dictionaryDao.save(entity);
        LangsDictionary dictionary = this.convertToLangsDictionary(nyLangsDictionary);
        return dictionary;
    }

    private Page<LangsDictionaryVo> findAllDictioryByRequestVo(LangsDictionaryRequestVo requestVo, Boolean isLocal) {

        //根据分类名称查询分类Ids
        final List<Long> categoryIds = this.getCategoryIdsByBaseName(requestVo.getBaseNameStr());

        //条件查询所有结果
        List<EntityNyLangsDictionary> dictionaries = this.getEntityNyLangsDictionaries(requestVo, categoryIds);

        //合并查询结果Entity->VO
        LinkedHashMap<String, LangsDictionaryVo> langsDictionaryMap = this.getStringLangsDictionaryVos(dictionaries, isLocal);

        //填充message查询结果未补全
        this.searchResultFillMessage(requestVo, langsDictionaryMap.values());

        ArrayList<LangsDictionaryVo> langsDictionaryVos = Lists.<LangsDictionaryVo>newArrayList(langsDictionaryMap.values());
        Pageable pageable = new PageRequest(requestVo.getIndex() - 1, requestVo.getPageNum());  // 计算分页
        Page<LangsDictionaryVo> pageVOs = new PageImpl<LangsDictionaryVo>(langsDictionaryVos, pageable, langsDictionaryMap.size());
        return pageVOs;
    }

    private LinkedHashMap<String, LangsDictionaryVo> getStringLangsDictionaryVos(List<EntityNyLangsDictionary> allDictionaries, Boolean isLocalLangs) {
        LinkedHashMap<String, LangsDictionaryVo> langsDictionaryMap = new LinkedHashMap<String, LangsDictionaryVo>();
        List<LangsCountryMessageVo> langsMessageList = Lists.newArrayList();
        LangsCountryMessageVo messageVo = null;
        Set<String> keyCodes = new HashSet<String>();
        LangsDictionaryVo dictionaryVo = null;
        if (BooleanUtils.isTrue(isLocalLangs)) {
            for (EntityNyLangsDictionary langsDictionary : allDictionaries) {
                if (LangsCountry.verifyIsLocalLanguage(langsDictionary.getLanguage() + "-" + langsDictionary.getCountry(), LOCAL_KEY)) {
                    this.setLangsDictionaryVoValue(langsDictionaryMap, langsMessageList, keyCodes, langsDictionary);
                }
            }
            return langsDictionaryMap;
        }
        for (EntityNyLangsDictionary langsDictionary : allDictionaries) {
            this.setLangsDictionaryVoValue(langsDictionaryMap, langsMessageList, keyCodes, langsDictionary);
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
        if (langsDictionary.getCountry().equals(langsDictionary.getLanguage())) {//TH
            messageVo.setLangsKey(LangsCountry.toEnum(langsDictionary.getCountry()).getKey());
        } else {
            messageVo.setLangsKey(LangsCountry.toEnum(langsCountry).getKey());
        }
        return messageVo;
    }

    private void searchResultFillMessage(LangsDictionaryRequestVo requestVo, Collection<LangsDictionaryVo> result) {
        if (StringUtils.isEmpty(requestVo.getMessage())) {
            return;
        }
        LangsCountryMessageVo vo = null;
        Iterator<LangsDictionaryVo> iterator = result.iterator();
        List<LangsCountryMessageVo> newMessageVos = null;
        while (iterator.hasNext()) {
            LangsDictionaryVo next = iterator.next();
            String keyCode = next.getKeyCode();
            newMessageVos = Lists.newArrayList();
            List<EntityNyLangsDictionary> lists = dictionaryDao.findAll(Example.of(new EntityNyLangsDictionary(keyCode)));
            for (EntityNyLangsDictionary entity : lists) {
                vo = new LangsCountryMessageVo();
                vo.setMessage(entity.getMessage());
                vo.setLangsKey(LangsCountry.toEnum(entity.getLangsCountry()).getKey());
                newMessageVos.add(vo);
            }
            next.getLangsMessageList().clear();
            next.getLangsMessageList().addAll(newMessageVos);
        }
    }


    private List<Long> getCategoryIdsByBaseName(final String baseNameStr) {
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
        return categoryIds;
    }

    private List<EntityNyLangsDictionary> getEntityNyLangsDictionaries(final LangsDictionaryRequestVo requestVo, final List<Long> categoryIds) {
        final Long baseNameId = requestVo.getBaseNameId();
        /**
         * categoryIds和baseNameId都存在时已BaseNameID为主
         */
        if (baseNameId != null) {
            // 查询所有数据
            return dictionaryDao.findAll(new Specification() {

                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                    List<Predicate> predicate = new ArrayList<Predicate>();
                    predicate.add(cb.equal(root.get("category").get("id"), baseNameId));//baseNameId
                    if (StringUtils.isNotEmpty(requestVo.getKeyCode())) {
                        predicate.add(cb.equal(root.get("keyCode"), requestVo.getKeyCode()));
                    }
                    if (StringUtils.isNotEmpty(requestVo.getMessage())) {
                        predicate.add(cb.like(root.get("message"), "%" + requestVo.getMessage() + "%"));
                    }
                    Predicate[] arrays = new Predicate[predicate.size()];
                    return query.where(predicate.toArray(arrays)).getRestriction();
                }
            });
        } else {
            // 查询所有数据
            return dictionaryDao.findAll(new Specification() {

                @Override
                public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
                    List<Predicate> predicate = new ArrayList<Predicate>();
                    if (CollectionUtils.isNotEmpty(categoryIds)) {
                        predicate.add(root.get("category").get("id").in(categoryIds));//categoryIds
                    }
                    if (StringUtils.isNotEmpty(requestVo.getKeyCode())) {
                        predicate.add(cb.equal(root.get("keyCode"), requestVo.getKeyCode()));
                    }
                    if (StringUtils.isNotEmpty(requestVo.getMessage())) {
                        predicate.add(cb.like(root.get("message"), "%" + requestVo.getMessage() + "%"));
                    }
                    Predicate[] arrays = new Predicate[predicate.size()];
                    return query.where(predicate.toArray(arrays)).getRestriction();
                }
            });
        }
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
            LangsDictionary langsDictionary = BeanUtils.copyBean(entity, new LangsDictionary());
            langsDictionary.setBaseNameStr(LangsCountry.toEnum(entity.getLangsCountry()).getValue());
            dictionaries.add(langsDictionary);
        }
        return dictionaries;
    }
}
