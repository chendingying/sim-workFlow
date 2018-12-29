package com.liansen.common.vo;

import java.util.Date;

/**
 * session信息模型
 * @Author: cdy
 * @Date: 2018/12/28 14:54
 * @Version 1.0
 */
public class AuthTokenDetails implements java.io.Serializable {

    // 用户ID
    private Long id;

    //用户名
    private String name;

    // 用户登录名
    private String username;

    // 用户IP
    private String ip;

    //截至日期
    private Date expirationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
}
