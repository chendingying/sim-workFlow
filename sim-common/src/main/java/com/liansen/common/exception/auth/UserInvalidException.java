package com.liansen.common.exception.auth;


import com.liansen.common.constant.CommonConstants;
import com.liansen.common.exception.BaseException;

/**
 * Created by cdy on 2017/9/8.
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, CommonConstants.EX_USER_PASS_INVALID_CODE);
    }
}
