package com.liansen.common.exception.auth;


import com.liansen.common.base.BaseResponse;
import com.liansen.common.constant.CommonConstants;

/**
 * Created by cdy on 2017/8/25.
 */
public class TokenForbiddenResponse extends BaseResponse {
    public TokenForbiddenResponse(String message) {
        super(CommonConstants.TOKEN_FORBIDDEN_CODE, message);
    }
}
