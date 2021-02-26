package com.ejlchina.project.entity;

import com.ejlchina.searcher.bean.DbField;
import com.ejlchina.searcher.bean.SearchBean;
import lombok.Data;

@Data
@SearchBean(tables = "platform p")
public class PlatformBean {

    @DbField("p.id")
    private Long id;

    @DbField("p.phone")
    private String phone;

    @DbField("p.company")
    private String company;

    @DbField("p.name")
    private String name;

    @DbField("p.logo_url")
    private String logoUrl;

    @DbField("p.domain")
    private String domain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
