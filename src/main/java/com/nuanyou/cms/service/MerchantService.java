package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.Merchant;
import com.nuanyou.cms.model.MerchantVO;

import java.util.List;

/**
 * Created by Alan.ye on 2016/9/7.
 */
public interface MerchantService {

    List<Merchant> getIdNameList();

    void updateScore(Long id, double score);

    void copyItem(Long sourceId, Long targetId);

    MerchantVO saveNotNull(MerchantVO vo);

}
