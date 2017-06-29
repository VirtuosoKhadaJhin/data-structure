package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.mission.BdMerchantTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Byron on 2017/6/29.
 */
public interface MissionMerchatTrackDao extends JpaRepository<BdMerchantTrack, Long>, JpaSpecificationExecutor {

   /* @Query(value="select * from (select * from BdMerchantTrack where mchid in ?1 order by id desc) t group by t.mchid, t.userid")
    List<BdMerchantTrack> findMerchantTrackByMchId(List<Long> ids);*/
}
