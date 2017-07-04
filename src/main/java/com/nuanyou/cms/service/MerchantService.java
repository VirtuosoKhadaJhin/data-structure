package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Channel;
import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.model.MerchantVO;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/7.
 */
public interface MerchantService {

    List<Merchant> getIdNameList();

    List<Merchant> getIdNameList(Boolean display);

    void updateScore(Long id, double score);

    void copyItem(Long sourceId, Long targetId);

    MerchantVO saveNotNull(MerchantVO vo);

    Channel genPayUrl(Long id);

    /**
     * 查询国家下的商户
     *
     * @param country
     * @return
     */
    List<Merchant> findMerchantByCountry(Long country);

    /**
     * 获取商圈下的商户
     *
     * @param district
     * @return
     */
    List<Merchant> findMerchantByDistrict(Long district);
}