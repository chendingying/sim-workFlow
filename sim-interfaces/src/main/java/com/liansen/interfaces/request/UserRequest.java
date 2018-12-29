package com.liansen.interfaces.request;

import com.liansen.common.util.ObjectMap;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @Author: cdy
 * @Date: 2018/12/28 16:42
 * @Version 1.0
 */
@Table(name = "admin_users", catalog="ksh")
public class UserRequest {
    private static final long serialVersionUID = 1L;

    //id
    @Id
    private Integer id;

    //登录帐号
    @Column(name = "username")
    private String userName;

    //头像url
    @Column(name = "avatar")
    private String avatar;

    //用户名
    @Column(name = "name")
    private String name;

    //密码
    @Column(name = "password")
    private String password;

    //token
    @Column(name = "remember_token")
    private String rememberToken;

    //创建时间
    @Column(name = "created_at")
    private Timestamp createdAt;

    //修改时间
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    //用户角色
    @Transient
    private List<ObjectMap> userRoles;

    //用户分组
    @Transient
    private List<ObjectMap> userGroups;

    public UserRequest() {
    }
    public UserRequest(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    @Transient
    private String token;

    @Transient
    private String access_token;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ObjectMap> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<ObjectMap> userRoles) {
        this.userRoles = userRoles;
    }

    public List<ObjectMap> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<ObjectMap> userGroups) {
        this.userGroups = userGroups;
    }
}
