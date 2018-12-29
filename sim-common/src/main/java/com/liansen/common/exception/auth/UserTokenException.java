package com.liansen.common.exception.auth;


import com.liansen.common.constant.CommonConstants;
import com.liansen.common.exception.BaseException;

/**
 * Created by cdy on 2017/9/8.
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
