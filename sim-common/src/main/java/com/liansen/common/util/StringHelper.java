package com.liansen.common.util;

/**
 * @Author: cdy
 * @Date: 2018/12/28 15:23
 * @Version 1.0
 */
public class StringHelper {
    public static String getObjectValue(Object obj){
        return obj==null?"":obj.toString();
    }
}
