package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.CmsUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Felix on 2016/9/7.
 */
public interface CmsUserDao extends JpaRepository<CmsUser, Long>, JpaSpecificationExecutor {


    CmsUser findByEmail(String email);

    @Query (value = "SELECT distinct crc.countryid,1 FROM cms_user_role cur inner join cms_role_menu crm on cur.roleid = crm.roleid inner join cms_menu cm on crm.menuid = cm.id inner join cms_role_country crc on crm.roleid = crc.roleid where cur.userid = ?1 and cm.url = ?2",nativeQuery = true)
    List<Object[]> findCountryIdByUserMenu(Long userId, String menu);
}
