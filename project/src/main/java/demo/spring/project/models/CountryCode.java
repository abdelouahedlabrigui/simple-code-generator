package demo.spring.project.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "country_code")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryCode{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long original_id;

    public Long getOriginal_id() {
        return original_id;
    }
    public void setOriginal_id(Long original_id) {
        this.original_id = original_id;
    }
    @Column(name = "prefix_name")
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "prefix_alpha_2")
    private String alpha2;
    public String getAlpha2() {
        return alpha2;
    }
    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }
    @Column(name = "prefix_alpha_3")
    private String alpha3;
    public String getAlpha3() {
        return alpha3;
    }
    public void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
    }
    @Column(name = "prefix_country_code")
    private Long countryCode;
    public Long getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(Long countryCode) {
        this.countryCode = countryCode;
    }
    @Column(name = "prefix_iso_3166_2")
    private String iso31662;
    public String getIso31662() {
        return iso31662;
    }
    public void setIso31662(String iso31662) {
        this.iso31662 = iso31662;
    }
    @Column(name = "prefix_region")
    private String region;
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    @Column(name = "prefix_sub_region")
    private String subRegion;
    public String getSubRegion() {
        return subRegion;
    }
    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }
    @Column(name = "prefix_intermediate_region")
    private String intermediateRegion;
    public String getIntermediateRegion() {
        return intermediateRegion;
    }
    public void setIntermediateRegion(String intermediateRegion) {
        this.intermediateRegion = intermediateRegion;
    }
    @Column(name = "prefix_region_code")
    private Double regionCode;
    public Double getRegionCode() {
        return regionCode;
    }
    public void setRegionCode(Double regionCode) {
        this.regionCode = regionCode;
    }
    @Column(name = "prefix_sub_region_code")
    private Double subRegionCode;
    public Double getSubRegionCode() {
        return subRegionCode;
    }
    public void setSubRegionCode(Double subRegionCode) {
        this.subRegionCode = subRegionCode;
    }
    @Column(name = "prefix_intermediate_region_code")
    private Double intermediateRegionCode;

    public Double getIntermediateRegionCode() {
        return intermediateRegionCode;
    }
    public void setIntermediateRegionCode(Double intermediateRegionCode) {
        this.intermediateRegionCode = intermediateRegionCode;
    }

}