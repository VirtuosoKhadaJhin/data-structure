package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.MerchantCat;
import com.nuanyou.cms.model.MerchantCatVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Locale;

/**
 * Created by nuanyou on 2016/9/6.
 */
public interface MerchantCatService {

    void add(MerchantCat merchantCat);

    /**
     * 分页查找一级分类(含中英文)
     *
     * @param merchantCat
     * @param index
     * @param locale
     * @return
     */
    List<MerchantCatVo> findParentCat(final MerchantCat merchantCat, Integer index, final Locale locale);

    /**
     * 分页查询二级分类(含中英文)
     *
     * @editBy mylon
     * @editReason 优化商户分类
     * @param merchantCatVo
     * @param index
     * @param locale
     * @param pcatId
     * @return
     */
    Page<MerchantCatVo> findChildCat(final MerchantCatVo merchantCatVo, Integer index, final Locale locale, final Long pcatId);

    /**
     * 保存或更新商户分类
     *
     * @param merchantCatVo
     */
    void updateMerchantCat(MerchantCatVo merchantCatVo);

    /**
     * 根据ID删除分类
     *
     * @param merchantCatVo
     * @return
     */
    Boolean delCat(MerchantCatVo merchantCatVo);

    List<MerchantCat> getIdNameList();

    MerchantCat saveNotNull(MerchantCat entity);

}
