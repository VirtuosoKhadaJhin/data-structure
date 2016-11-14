package com.nuanyou.cms.dao;

import com.nuanyou.cms.entity.Country;
import com.nuanyou.cms.entity.District;
import com.nuanyou.cms.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Alan.ye on 2016/9/7.
 */
public interface CountryDao extends JpaRepository<Country, Long> {

    @Query(value = "select new Country(t.id,t.name) from Country t")
    List<Country> getIdNameList();

    @Query(value = "update Country t set radio=:radio where id=:id")
    void updateCountryRate(@Param("id") Long id, @Param("radio") BigDecimal radio);
}
