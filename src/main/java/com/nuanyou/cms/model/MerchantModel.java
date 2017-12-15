package com.nuanyou.cms.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created by young on 2017/9/27.
 */
@ApiModel("商户")
public class MerchantModel extends BasicModel {
    @ApiModelProperty("标识")
    private Long id;
    @ApiModelProperty("暖游商户标识")
    private Long nyid;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("本地名称")
    private String localName;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("本地地址")
    private String localAddress;
    @ApiModelProperty("分类标识")
    private Long catId;
    @ApiModelProperty("分类名称")
    private String catName;
    @ApiModelProperty("子类标识")
    private Long subCatId;
    @ApiModelProperty("子类名称")
    private String subCatName;
    @ApiModelProperty("所属国家标识")
    private Long countryId;
    @ApiModelProperty("所属国家")
    private String countryName;
    @ApiModelProperty("所属城市标识")
    private Long cityId;
    @ApiModelProperty("所属城市")
    private String cityName;
    @ApiModelProperty("所属商圈标识")
    private Long districtId;
    @ApiModelProperty("所属商圈")
    private String districtName;
    @ApiModelProperty("距离")
    private Double distance;
    @ApiModelProperty("人均消费")
    private BigDecimal consume;
    @ApiModelProperty("商家电话")
    private String phone;
    @ApiModelProperty("经度")
    private Double longitude;
    @ApiModelProperty("纬度")
    private Double latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNyid() {
        return nyid;
    }

    public void setNyid(Long nyid) {
        this.nyid = nyid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Long getCatId() {
        return catId;
    }

    public void setCatId(Long catId) {
        this.catId = catId;
    }

    public Long getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(Long subCatId) {
        this.subCatId = subCatId;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public BigDecimal getConsume() {
        return consume;
    }

    public void setConsume(BigDecimal consume) {
        this.consume = consume;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
