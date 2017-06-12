package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.EntityNyLangsDictionary;
import com.nuanyou.cms.model.LangsDictionary;
import com.nuanyou.cms.model.LangsDictionaryRequestVo;
import com.nuanyou.cms.model.LangsDictionaryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Locale;

/**
 * Created by Byron on 2017/5/26.
 */
public interface LangsDictionaryService {

    LangsDictionary findLangDictionary(Long id);

    LangsDictionaryVo findLangsDictionary(Long id);

    Page<LangsDictionaryVo> findAllDictionary(LangsDictionaryRequestVo requestVo);

    Page<LangsDictionaryVo> findAllLocalDictionary(LangsDictionaryRequestVo requestVo);

    LangsDictionaryVo findLangsDictionary(String keyCode, Locale locale);

    LangsDictionary addLangsDictionary(LangsDictionary entity);

    LangsDictionary updateLangsDictionary(LangsDictionary entity);

    void deleteLangsDictionary(Long id);

    List<LangsDictionary> findIdNameListByCat(Long id);

    boolean verifykeyCode(LangsDictionaryVo dictionaryVo);

    String saveLangsDictionary(LangsDictionaryVo dictionaryVo);

    /**
     * 新增单个语言记录
     *
     * @param vo
     * @return
     */
    LangsDictionary saveMessage(LangsDictionaryRequestVo vo);

    /**
     * suggest搜索
     *
     * @param key
     * @return
     */
    List<LangsDictionary> findSuggestSearch(String key);

    /**
     * 删除多语言记录
     *
     * @param requestVo
     */
    void remove(LangsDictionaryRequestVo requestVo);

    /**
     * 修改单个语言
     *
     * @param dictionaryVo
     * @return
     */
    EntityNyLangsDictionary modifyLangsDictionary(LangsDictionaryVo dictionaryVo);
}
