package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.entity.MerchantCat;
import com.nuanyou.cms.model.MerchantCatVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Locale;

/**
 * Created by nuanyou on 2016/9/6.
 */
public interface MerchantCatService {
    Page<Merchant> findByCondition(Integer mchid, String name, Integer index, Integer pageSize);

    void add(MerchantCat merchantCat);

    /**
     * 分页查询商户分类(含中英文)
     *
     * @editBy mylon
     * @editReason 优化商户分类
     * @param merchantCat
     * @param index
     * @return
     */
    Page<MerchantCatVo> findByCondition(final MerchantCat merchantCat, Integer index, final Locale locale);

    /**
     * 保存或更新商户分类
     *
     * @param merchantCatVo
     */
    void updateMerchantCat(MerchantCatVo merchantCatVo);

    List<MerchantCat> getIdNameList();

    MerchantCat saveNotNull(MerchantCat entity);
}
