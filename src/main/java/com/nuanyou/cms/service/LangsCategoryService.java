package com.nuanyou.cms.service;

import com.nuanyou.cms.model.LangsCategory;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by Byron on 2017/5/27.
 */
public interface LangsCategoryService {

    List<LangsCategory> findAllCategories();

    Page<LangsCategory> findAllCategories(LangsCategory langsCategory);

    LangsCategory findLangsCategory(Long id);

    LangsCategory save(LangsCategory langsCat);

    LangsCategory update(LangsCategory langsCat);

    void delete(Long id);
}
