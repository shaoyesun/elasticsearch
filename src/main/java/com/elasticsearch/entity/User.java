package com.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * Created by root on 17-7-12.
 */
@Document(indexName = "es_test", type = "user")
public class User {
    @Id
    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String uuid;

    @Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String userName;

    @Field(type = FieldType.String, index = FieldIndex.analyzed, store = true)
    private String realName;

    @Field(type = FieldType.String, index = FieldIndex.analyzed, store = true)
    private String company;

    @Field(type = FieldType.String, index = FieldIndex.analyzed, store = true)
    private String email;

    @Field(type = FieldType.String, index = FieldIndex.analyzed, store = true)
    private String phone;

    @Field(type = FieldType.Date, index = FieldIndex.not_analyzed, store = true)
    private Date registeTime;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getRegisteTime() {
        return registeTime;
    }

    public void setRegisteTime(Date registeTime) {
        this.registeTime = registeTime;
    }
}
