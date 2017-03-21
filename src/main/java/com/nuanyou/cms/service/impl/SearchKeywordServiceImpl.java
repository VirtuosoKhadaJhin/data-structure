package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.SearchKeywordDao;
import com.nuanyou.cms.entity.SearchKeyword;
import com.nuanyou.cms.model.PageUtil;
import com.nuanyou.cms.service.SearchKeywordService;
import com.nuanyou.cms.util.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * Created by Felix on 2016/9/26.
 */
@Service
public class SearchKeywordServiceImpl implements SearchKeywordService {

    @Autowired
    private
    SearchKeywordDao searchKeywordDao;

    @Override
    public Page<SearchKeyword> findByCondition(int index, SearchKeyword entity) {
        Pageable pageable = new PageRequest(index - 1, PageUtil.pageSize);
        ExampleMatcher e = ExampleMatcher.matching();
        ExampleMatcher.GenericPropertyMatcher g = ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.ENDING);
        if (entity.getId() != null) {
            e = e.withMatcher("id", g.exact());
        }

        if (entity.getDisplay() != null) {
            e = e.withMatcher("display", g.exact());
        } else {
            entity.setDisplay(null);
        }
        return searchKeywordDao.findAll(Example.of(entity, e), pageable);
    }

    @Override
    public SearchKeyword saveNotNull(SearchKeyword entity) {
        if (entity.getId() == null) {
            if(entity.getCity().getId()==null){
                entity.setCity(null);
            }
            return searchKeywordDao.save(entity);
        }
        SearchKeyword oldEntity = searchKeywordDao.findOne(entity.getId());
        BeanUtils.copyBeanNotNull(entity, oldEntity);
        if (oldEntity.getCity().getId()==null){
            oldEntity.setCity(null);
        }if(oldEntity.getCountry().getId()==null){
            oldEntity.setCountry(null);
        }
        return searchKeywordDao.save(oldEntity);
    }

}