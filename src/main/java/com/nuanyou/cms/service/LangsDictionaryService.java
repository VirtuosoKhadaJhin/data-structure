package com.nuanyou.cms.service;

import com.nuanyou.cms.model.LangsDictionary;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Locale;

/**
 * Created by Byron on 2017/5/26.
 */
public interface LangsDictionaryService {

    LangsDictionary findLangsDictionary(Long id);

    Page<LangsDictionary> findAllDictionary(LangsDictionary request);

    LangsDictionary findLangsDictionary(String keyCode, Locale locale);

    LangsDictionary addLangsDictionary(LangsDictionary entity);

    LangsDictionary updateLangsDictionary(LangsDictionary entity);

    void deleteLangsDictionary(Long id);

    List<LangsDictionary> findIdNameListByCat(Long id);
}
