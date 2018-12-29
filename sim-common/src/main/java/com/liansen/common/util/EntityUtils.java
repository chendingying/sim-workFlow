package com.liansen.common.util;

import com.liansen.common.base.BaseContextHandler;
import com.liansen.common.jwt.JsonWebTokenUtility;
import com.liansen.common.vo.AuthTokenDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;

/**
 *  实体类相关工具类
 * 解决问题： 1、快速对实体的常驻字段，如：crtUser、crtHost、updUser等值快速注入
 * @Author: cdy
 * @Date: 2018/12/28 14:51
 * @Version 1.0
 */
public class EntityUtils {

    /**
     * token中获取用户信息
     * @param tokenHeader
     * @return
     */
    private static AuthTokenDetails parseToken(String tokenHeader) {
        JsonWebTokenUtility tokenService = new JsonWebTokenUtility();
        AuthTokenDetails authTokenDetails =
                tokenService.parseAndValidate(tokenHeader);
        return authTokenDetails;
    }

    /**
     * 快速将bean的crtUser、crtHost、crtTime、updUser、updHost、updTime附上相关值
     *
     * @param entity 实体bean
     * @author cdy
     */
    public static <T> void setCreatAndUpdatInfo(T entity) {

        setCreateInfo(entity);
        setUpdatedInfo(entity);
    }

    /**
     * 快速将bean的crtUser、crtHost、crtTime附上相关值
     *
     * @param entity 实体bean
     * @author cdy
     */
    public static <T> void setCreateInfo(T entity){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String hostIp = "";
        String name = "";
        String id = "";
        if(request!=null) {
            hostIp = request.getRemoteAddr();
            AuthTokenDetails userDetails = parseToken(StringUtils.trimToEmpty(request.getHeader("Authorization")));
            name = userDetails.getUsername();
            id = userDetails.getId().toString();
        }

        if (StringUtils.isBlank(name)) {
            name = BaseContextHandler.getUsername();
        }
        if (StringUtils.isBlank(id)) {
            id = BaseContextHandler.getUserID();
        }

        // 默认属性
        String[] fields = {"crtName","crtUser","crtHost","crtTime"};
        Field field = ReflectionUtils.getAccessibleField(entity, "crtTime");
        // 默认值
        Object [] value = null;
        if(field!=null&&field.getType().equals(Date.class)){
            value = new Object []{name,id,hostIp,new Date()};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }

    /**
     * 快速将bean的updUser、updHost、updTime附上相关值
     *
     * @param entity 实体bean
     * @author cdy
     */
    public static <T> void setUpdatedInfo(T entity){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String hostIp = "";
        String name = "";
        String id = "";
        if(request!=null) {
            AuthTokenDetails userDetails = parseToken(StringUtils.trimToEmpty(request.getHeader("Authorization")));
            hostIp = request.getRemoteAddr();
            name = userDetails.getUsername();
            id = userDetails.getId().toString();
        }

        if (StringUtils.isBlank(name)) {
            name = BaseContextHandler.getUsername();
        }
        if (StringUtils.isBlank(id)) {
            id = BaseContextHandler.getUserID();
        }

        // 默认属性
        String[] fields = {"updName","updUser","updHost","updTime"};
        Field field = ReflectionUtils.getAccessibleField(entity, "updTime");
        Object [] value = null;
        if(field!=null&&field.getType().equals(Date.class)){
            value = new Object []{name,id,hostIp,new Date()};
        }
        // 填充默认属性值
        setDefaultValues(entity, fields, value);
    }
    /**
     * 依据对象的属性数组和值数组对对象的属性进行赋值
     *
     * @param entity 对象
     * @param fields 属性数组
     * @param value 值数组
     * @author cdy
     */
    private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
        for(int i=0;i<fields.length;i++){
            String field = fields[i];
            if(ReflectionUtils.hasField(entity, field)){
                ReflectionUtils.invokeSetter(entity, field, value[i]);
            }
        }
    }
    /**
     * 根据主键属性，判断主键是否值为空
     *
     * @param entity
     * @param field
     * @return 主键为空，则返回false；主键有值，返回true
     * @author cdy
     */
    public static <T> boolean isPKNotNull(T entity,String field){
        if(!ReflectionUtils.hasField(entity, field)) {
            return false;
        }
        Object value = ReflectionUtils.getFieldValue(entity, field);
        return value!=null&&!"".equals(value);
    }
}
