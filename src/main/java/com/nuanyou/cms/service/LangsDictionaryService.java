package com.nuanyou.cms.service;

import com.nuanyou.cms.model.LangsDictionary;
import com.nuanyou.cms.model.LangsDictionaryRequestVo;
import com.nuanyou.cms.model.LangsDictionaryVo;
import org.springframework.data.domain.Page;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Byron on 2017/5/26.
 */
public interface LangsDictionaryService {

    /**
     * 查询所有语言字典项列表
     *
     * @param requestVo
     * @return
     */
    Page<LangsDictionaryVo> findAllDictionary(LangsDictionaryRequestVo requestVo);

    /**
     * 查询本地语言字典项列表
     *
     * @param requestVo
     * @return
     */
    Page<LangsDictionaryVo> findAllLocalDictionary(LangsDictionaryRequestVo requestVo);

    /**
     * 根据KeyCode和Locale获取字典项
     *
     * @param keyCode
     * @param locale
     * @return
     * @throws UnsupportedEncodingException
     */
    LangsDictionaryVo findLangsDictionary(String keyCode, Locale locale) throws UnsupportedEncodingException;

    /**
     * 查询当地语Message内容
     *
     * @param keyCode
     * @param locale
     * @return
     */
    String findLocalMessageByKeyCode(String keyCode, Locale locale);

    /**
     * 根据分类id查询所有的语言
     *
     * @param id
     * @return
     */
    List<LangsDictionary> findAllLanguagesByCatId(Long id);

    /**
     * 判断keyCode是否有效
     *
     * @param dictionaryVo
     * @return
     */
    boolean verifykeyCode(LangsDictionaryVo dictionaryVo);


    /**
     * 保存新增对象
     *
     * @param dictionaryVo
     * @return
     */
    LangsDictionary saveLangsDictionary(LangsDictionaryVo dictionaryVo);

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
     */
    void modifyLangsDictionary(LangsDictionaryVo dictionaryVo);

    /**
     * 根据keyCode查询当地语言
     *
     * @param dictionaryVo
     * @return
     */
    List<LangsDictionary> viewLocalLangsDictionary(LangsDictionaryVo dictionaryVo);

}
