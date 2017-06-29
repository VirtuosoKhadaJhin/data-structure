package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.mission.BdMerchantTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Byron on 2017/6/29.
 */
public interface MissionMerchatTrackDao extends JpaRepository<BdMerchantTrack, Long>, JpaSpecificationExecutor {

}
