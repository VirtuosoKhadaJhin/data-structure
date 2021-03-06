package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nuanyou.cms.dao.CmsUserDao;
import com.nuanyou.cms.dao.EntityNyLangsCategoryDao;
import com.nuanyou.cms.dao.EntityNyLangsDictionaryDao;
import com.nuanyou.cms.dao.EntityNyLangsMessageTipDao;
import com.nuanyou.cms.entity.*;
import com.nuanyou.cms.model.*;
import com.nuanyou.cms.model.enums.LangsCountry;
import com.nuanyou.cms.service.CountryService;
import com.nuanyou.cms.service.LangsDictionaryService;
import com.nuanyou.cms.sso.client.util.UserHolder;
import com.nuanyou.cms.sso.client.validation.vo.User;
import com.nuanyou.cms.util.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.BooleanUtils;
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
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by Byron on 2017/5/26.
 */
@Service
public class LangsDictionaryServiceImpl implements LangsDictionaryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LangsDictionaryServiceImpl.class);

    @Autowired
    private CmsUserDao cmsUserDao;

    @Autowired
    private CountryService countryService;

    @Autowired
    private EntityNyLangsCategoryDao categoryDao;

    @Autowired
    private EntityNyLangsDictionaryDao dictionaryDao;

    @Autowired
    private EntityNyLangsMessageTipDao messageTipDao;


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
                predicate.add(cb.equal(root.get("delFlag").as(Boolean.class), false));

                Predicate[] arrays = new Predicate[predicate.size()];
                return query.where(predicate.toArray(arrays)).getRestriction();
            }
        });

        List<LangsDictionary> dictionaryList = this.convertToMultipleLangsCategories(dictionaries);
        return dictionaryList;
    }

    @Override
    public void remove(LangsDictionaryRequestVo requestVo) {
        // 修改为Dao层删除
        dictionaryDao.setDelFLagKeyCodeFor(true, requestVo.getKeyCode());
        // 修改为Dao层删除
        messageTipDao.setDelFLagKeyCodeFor(true, requestVo.getKeyCode());
    }

    @Override
    public void modifyLangsDictionary(LangsDictionaryVo requestVo) {
        if (StringUtils.isNotEmpty(requestVo.getNewKeyCode())) {
            dictionaryDao.setDelFLagKeyCodeFor(true, requestVo.getKeyCode());
            requestVo.setKeyCode(requestVo.getNewKeyCode());
        }
        EntityNyLangsCategory entityNyLangsCategory = categoryDao.findOne(requestVo.getCategoryId());
        String dmail = UserHolder.getUser().getEmail();
        CmsUser cmsUser = cmsUserDao.findByEmail(dmail);
        Date nowDate = new Date();
        // 迭代每一个语言的数据
        EntityNyLangsDictionary entityNyLangsDictionary;
        // 批量保存
        List<EntityNyLangsDictionary> entityNyLangsDictionarys = Lists.newArrayList();
        for (LangsCountryMessageVo langsCountryMessageVo : requestVo.getLangsMessageList()) {
            if (StringUtils.isNotEmpty(langsCountryMessageVo.getMessage())) {
                LangsCountry langsCountry = LangsCountry.toEnum(langsCountryMessageVo.getLangsKey());
                String[] langsCountrys = langsCountry.getValue().split("-");
                entityNyLangsDictionary = new EntityNyLangsDictionary();
                entityNyLangsDictionary.setUserId(cmsUser.getId());
                if (null != langsCountryMessageVo.getId()) {
                    entityNyLangsDictionary.setId(langsCountryMessageVo.getId());
                }
                entityNyLangsDictionary.setKeyCode(requestVo.getKeyCode());
                entityNyLangsDictionary.setCategory(entityNyLangsCategory);
                entityNyLangsDictionary.setCreateDt(nowDate);
                entityNyLangsDictionary.setUpdateDt(nowDate);
                entityNyLangsDictionary.setLanguage(langsCountrys[0]);
                entityNyLangsDictionary.setCountry(langsCountrys.length > 1 ? langsCountrys[1] : langsCountrys[0]);
                entityNyLangsDictionary.setMessage(langsCountryMessageVo.getMessage());
                entityNyLangsDictionarys.add(entityNyLangsDictionary);
            } else {//message设置为null的时候，删除之前已有的
                if (null != langsCountryMessageVo.getId()) {
                    entityNyLangsDictionary = new EntityNyLangsDictionary();
                    entityNyLangsDictionary.setId(langsCountryMessageVo.getId());
                    dictionaryDao.delete(entityNyLangsDictionary);
                }
            }
        }
        dictionaryDao.save(entityNyLangsDictionarys);
    }

    @Override
    public void modifyLocalLangsDictionary(LangsDictionaryVo dictionaryVo) {
        if (StringUtils.isNotEmpty(dictionaryVo.getNewKeyCode())) {
            dictionaryDao.setDelFLagKeyCodeFor(true, dictionaryVo.getKeyCode());
            dictionaryVo.setKeyCode(dictionaryVo.getNewKeyCode());
        }
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findByKeyCode(dictionaryVo.getKeyCode());
        if (CollectionUtils.isEmpty(entityResult)) {
            return;
        }
        EntityNyLangsDictionary existsdictionary = entityResult.get(0);
        EntityNyLangsDictionary entityNyLangsDictionarey = new EntityNyLangsDictionary();
        entityNyLangsDictionarey.setCategory(existsdictionary.getCategory());

        // 查找是否有旧的添加的本地语言
        existsdictionary = new EntityNyLangsDictionary();
        existsdictionary.setKeyCode(dictionaryVo.getKeyCode());
        String[] langsCountrys = LangsCountry.toEnum(dictionaryVo.getLangsKey()).getValue().split("-");
        existsdictionary.setLanguage(langsCountrys[0]);
        existsdictionary.setCountry(langsCountrys.length > 1 ? langsCountrys[1] : langsCountrys[0]);

        // 如果输入空, 则不保存, 相当于删除了这个本地语言行
        if (StringUtils.isNotEmpty(dictionaryVo.getLocalMess())) {
            User user = UserHolder.getUser();
            CmsUser cmsUser = cmsUserDao.findByEmail(user.getEmail());
            Date nowDate = new Date();
            if (null != dictionaryVo.getId()) {
                entityNyLangsDictionarey.setId(dictionaryVo.getId());
            }
            entityNyLangsDictionarey.setKeyCode(dictionaryVo.getKeyCode());
            entityNyLangsDictionarey.setMessage(dictionaryVo.getLocalMess());
            entityNyLangsDictionarey.setCreateDt(nowDate);
            entityNyLangsDictionarey.setUpdateDt(nowDate);
            entityNyLangsDictionarey.setLanguage(langsCountrys[0]);
            entityNyLangsDictionarey.setCountry(langsCountrys.length > 1 ? langsCountrys[1] : langsCountrys[0]);
            entityNyLangsDictionarey.setUserId(cmsUser.getId());
            dictionaryDao.save(entityNyLangsDictionarey);
        } else {
            if (null != dictionaryVo.getId()) {
                entityNyLangsDictionarey.setId(dictionaryVo.getId());
                dictionaryDao.delete(entityNyLangsDictionarey);
            }
        }
    }

    @Override
    public List<LangsDictionary> viewLocalLangsDictionary(LangsDictionaryVo dictionaryVo) {
        List<LangsDictionary> dataList = Lists.newArrayList();
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findByKeyCode(dictionaryVo.getKeyCode());

        List<LangsDictionary> dictionaryList = this.convertToMultipleLangsCategories(entityResult);

        for (LangsDictionary dictionary : dictionaryList) {
            dictionary.setLangsCountryKey(LangsCountry.toEnum(dictionary.getBaseNameStr()).getKey());
            if (LangsCountry.toEnum(dictionary.getBaseNameStr()).getValue().equals(LangsCountry.EN_UK.getValue())
                    || LangsCountry.toEnum(dictionary.getBaseNameStr()).getValue().equals(LangsCountry.ZH_CN.getValue())) {
                dataList.add(dictionary);
            }
        }
        if (dictionaryVo.getCountryKey() == null) {
            return dataList;
        }
        // 如果尚未选择国家,则默认选择一个拥有的国家的语言
        if (dictionaryVo.getCountryKey() == 0) {
            for (LangsDictionary dictionary : dictionaryList) {
                dictionary.setLangsCountryKey(LangsCountry.toEnum(dictionary.getBaseNameStr()).getKey());
                if (!LangsCountry.toEnum(dictionary.getBaseNameStr()).getValue().equals(LangsCountry.EN_UK.getValue())
                        && !LangsCountry.toEnum(dictionary.getBaseNameStr()).getValue().equals(LangsCountry.ZH_CN.getValue())
                        && dictionary.getCountry().equals(dictionaryVo)) {
                    dataList.add(dictionary);
                    break;
                }
            }
        } else {
            for (LangsDictionary dictionary : dictionaryList) {
                dictionary.setLangsCountryKey(LangsCountry.toEnum(dictionary.getBaseNameStr()).getKey());
                if (!LangsCountry.toEnum(dictionary.getBaseNameStr()).getValue().equals(LangsCountry.EN_UK.getValue())
                        && !LangsCountry.toEnum(dictionary.getBaseNameStr()).getValue().equals(LangsCountry.ZH_CN.getValue())
                        && dictionary.getLangsCountryKey().equals(dictionaryVo.getCountryKey())) {
                    dataList.add(dictionary);
                    break;
                }
            }
        }

        return dataList;
    }

    private LangsDictionary verifyLangsDictionary(List<LangsDictionary> dataList, String keyCode, Integer langsKey) {
        for (LangsDictionary dictionary : dataList) {
            if (dictionary.getLangsCountryKey().equals(langsKey) && dictionary.getKeyCode().equals(keyCode)) {
                return dictionary;
            }
        }
        return null;
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
    public LangsDictionaryVo findLangsDictionary(String keyCode, Locale locale) {
        List<EntityNyLangsDictionary> entityNyLangsDictionarys = dictionaryDao.findByKeyCode(keyCode);
        List<LangsCountryMessageVo> langsMessageList = Lists.newArrayList();
        LangsDictionaryVo dictionaryVo = new LangsDictionaryVo();
        for (EntityNyLangsDictionary langsDictionary : entityNyLangsDictionarys) {
            dictionaryVo.setCategoryId(langsDictionary.getCategory().getId());
            dictionaryVo.setKeyCode(langsDictionary.getKeyCode());

            LangsCountryMessageVo messageVo = this.getLangsCountryMessageVo(langsDictionary);
            langsMessageList.add(messageVo);
        }
        dictionaryVo.setLangsMessageList(langsMessageList);

        // 查询备注
        EntityNyLangsMessageTip entityNyLangsMessageTip = messageTipDao.findByKeyCode(keyCode);

        if (entityNyLangsMessageTip != null) {
            LangsMessageTipVo langsMessageTipVo = this.convertToLangsMessageTip(entityNyLangsMessageTip);
            dictionaryVo.setMessageTip(langsMessageTipVo);
        }

        return dictionaryVo;
    }

    @Override
    public EntityNyLangsDictionary findByKeyCodeAndCountry(String keyCode, String CountryCode) {
       return dictionaryDao.findByKeyCodeAndCountry(keyCode,CountryCode);
    }


    @Override
    public String findLocalMessageByKeyCode(String keyCode, Locale locale) {
        try {
            keyCode = (new String(keyCode.getBytes("ISO-8859-1"), "utf-8")).trim();
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("throw", e);
        }
        List<EntityNyLangsDictionary> entityNyLangsDictionarys = dictionaryDao.findByKeyCode(keyCode);
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
        List<EntityNyLangsDictionary> list = dictionaryDao.findByCategoryId(id);

        List<LangsDictionary> langsDictionaries = convertToMultipleLangsCategories(list);
        return langsDictionaries;
    }

    @Override
    public boolean verifykeyCode(LangsDictionaryVo dictionaryVo) {
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findByKeyCode(dictionaryVo.getKeyCode());

        if (entityResult.size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean saveLangsDictionary(LangsDictionaryVo dictionaryVo) {
        EntityNyLangsDictionary nyLangsDictionary = null;
        List<EntityNyLangsDictionary> entities = Lists.newArrayList();

        if (CollectionUtils.isEmpty(dictionaryVo.getLangsMessageList())) {
            return null;
        }
        EntityNyLangsCategory entityNyLangsCategory = categoryDao.findOne(dictionaryVo.getCategoryId());
        if (entityNyLangsCategory == null) {
            return null;
        }

        String email = UserHolder.getUser().getEmail();
        CmsUser cmsUser = cmsUserDao.findByEmail(email);
        Date nowDate = new Date();
        for (LangsCountryMessageVo langsCountryMessageVo : dictionaryVo.getLangsMessageList()) {
            String message = langsCountryMessageVo.getMessage();
            if (StringUtils.isEmpty(message)) {
                continue;
            } else {
                nyLangsDictionary = new EntityNyLangsDictionary();
                LangsCountry langsCountry = LangsCountry.toEnum(langsCountryMessageVo.getLangsKey());
                nyLangsDictionary.setKeyCode(dictionaryVo.getKeyCode());
                nyLangsDictionary.setCategory(entityNyLangsCategory);
                nyLangsDictionary.setMessage(message);

                String[] langsCountrys = langsCountry.getValue().split("-");
                nyLangsDictionary.setLanguage(langsCountrys[0]);
                nyLangsDictionary.setCountry(langsCountrys.length > 1 ? langsCountrys[1] : langsCountrys[0]);

                nyLangsDictionary.setUserId(cmsUser.getId());
                nyLangsDictionary.setCreateDt(nowDate);
                nyLangsDictionary.setUpdateDt(nowDate);
                entities.add(nyLangsDictionary);
            }
        }
        List<EntityNyLangsDictionary> result = dictionaryDao.save(entities);

        if (result.size() == entities.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public LangsDictionary saveMessage(LangsDictionaryRequestVo vo) {
        List<EntityNyLangsDictionary> entityResult = dictionaryDao.findByKeyCode(vo.getKeyCode());

        EntityNyLangsDictionary entity = new EntityNyLangsDictionary();
        String[] splitValues = LangsCountry.toEnum(vo.getLangsKey()).getValue().split("-");

        User user = UserHolder.getUser();
        CmsUser cmsUser = cmsUserDao.findByEmail(user.getEmail());
        entity.setLanguage(splitValues[0]);
        entity.setCountry(splitValues.length > 1 ? splitValues[1] : splitValues[0]);
        entity.setKeyCode(vo.getKeyCode());
        entity.setMessage(vo.getMessage());
        entity.setCreateDt(new Date());
        entity.setUserId(cmsUser.getId());
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
        List<EntityNyLangsDictionary> dictionaries = this.getEntityNyLangsDictionaries(requestVo, categoryIds, requestVo.getCountryKey());

        //合并查询结果Entity->VO
        LinkedHashMap<String, LangsDictionaryVo> langsDictionaryMap = this.getStringLangsDictionaryVos(dictionaries, requestVo, isLocal);

        //填充message查询结果未补全
        this.searchResultFillMessage(requestVo, langsDictionaryMap.values());

        ArrayList<LangsDictionaryVo> langsDictionaryVos = Lists.<LangsDictionaryVo>newArrayList(langsDictionaryMap.values());

        Integer pageIndex = requestVo.getIndex();
        Integer pageNum = requestVo.getPageNum();

        List<LangsDictionaryVo> subList = langsDictionaryVos.subList((pageIndex - 1) * pageNum, pageIndex * pageNum > langsDictionaryVos.size() ? langsDictionaryVos.size() : pageIndex * pageNum);

        // 设置备注信息
        this.setLangsDictionaryMessageTip(subList);

        Pageable pageable = new PageRequest(pageIndex - 1, pageNum);  // 计算分页
        Page<LangsDictionaryVo> pageVOs = new PageImpl<LangsDictionaryVo>(subList, pageable, langsDictionaryVos.size());
        return pageVOs;

    }

    private LinkedHashMap<String, LangsDictionaryVo> getStringLangsDictionaryVos(List<EntityNyLangsDictionary> allDictionaries, LangsDictionaryRequestVo requestVo, Boolean isLocalLangs) {
        LinkedHashMap<String, LangsDictionaryVo> langsDictionaryMap = new LinkedHashMap<String, LangsDictionaryVo>();
        List<LangsCountryMessageVo> langsMessageList = Lists.newArrayList();
        Set<String> keyCodes = new HashSet<String>();
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
            dictionaryVo.setCategoryName(langsDictionary.getCategory().getName());
            dictionaryVo.setKeyCode(langsDictionary.getKeyCode());
            dictionaryVo.setUpdateDt(langsDictionary.getUpdateDt());

            // 查找备注,可能影响效率
/*
            EntityNyLangsMessageTip entityNyLangsMessageTip = new EntityNyLangsMessageTip();
            entityNyLangsMessageTip.setKeyCode(langsDictionary.getKeyCode());

            Example<EntityNyLangsMessageTip> example = Example.of(entityNyLangsMessageTip);
            EntityNyLangsMessageTip messTip = messageTipDao.findOne(example);

            if(null != messTip){
                LangsMessageTipVo messVo = convertToLangsMessageTip(messTip);
                dictionaryVo.setMessageTip(messVo);
            }
*/

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
        messageVo.setId(langsDictionary.getId());
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
            List<EntityNyLangsDictionary> lists = dictionaryDao.findByKeyCode(keyCode);
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

    private List<EntityNyLangsDictionary> getEntityNyLangsDictionaries(final LangsDictionaryRequestVo requestVo, final List<Long> categoryIds, final Integer countryKey) {
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
                    predicate.add(cb.equal(root.get("delFlag").as(Boolean.class), false));
                    Predicate[] arrays = new Predicate[predicate.size()];
                    ArrayList<Order> orderBys = Lists.newArrayList(cb.desc(root.get("updateDt")), cb.asc(root.get("keyCode")));
                    return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
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
                    predicate.add(cb.equal(root.get("delFlag").as(Boolean.class), false));
                    Predicate[] arrays = new Predicate[predicate.size()];
                    ArrayList<Order> orderBys = Lists.newArrayList(cb.desc(root.get("updateDt")), cb.asc(root.get("keyCode")));
                    return query.where(predicate.toArray(arrays)).orderBy(orderBys).getRestriction();
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

    private LangsMessageTipVo convertToLangsMessageTip(EntityNyLangsMessageTip entity) {
        LangsMessageTipVo message = BeanUtils.copyBean(entity, new LangsMessageTipVo());
        return message;
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

    /**
     * 填充备注信息
     *
     * @param langsDictionaryVos
     */
    public void setLangsDictionaryMessageTip(List<LangsDictionaryVo> langsDictionaryVos) {
        Collection codes = CollectionUtils.collect(langsDictionaryVos, new Transformer() {
            @Override
            public Object transform(Object input) {
                return ((LangsDictionaryVo) input).getKeyCode();
            }
        });
        if (CollectionUtils.isEmpty(codes)) {
            return;
        }
        List<EntityNyLangsMessageTip> langsMessageTips = messageTipDao.findByKeyCodes(Lists.<String>newArrayList(codes));
        Map<String, LangsMessageTipVo> map = Maps.newHashMap();
        for (EntityNyLangsMessageTip tip : langsMessageTips) {
            map.put(tip.getKeyCode(), this.convertToLangsMessageTip(tip));
        }
        for (LangsDictionaryVo vo : langsDictionaryVos) {
            LangsMessageTipVo langsMessageTipVo = map.get(vo.getKeyCode());
            vo.setMessageTip(map.get(vo.getKeyCode()));
        }
    }
}
