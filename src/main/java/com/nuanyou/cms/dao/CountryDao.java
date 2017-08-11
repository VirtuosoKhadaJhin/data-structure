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

    @Query(value = "select new Country(t.id,t.name) from Country t where t.display = true and t.id in ?1")
    List<Country> getIdNameList(List<Long> ids);

    @Query(value = "update Country t set t.radio=:radio where t.id=:id and t.display = true")
    void updateCountryRate(@Param("id") Long id, @Param("radio") BigDecimal radio);

    @Query(value = "select t from Country t where t.display = true")
    List<Country> findAllCountries();
}
