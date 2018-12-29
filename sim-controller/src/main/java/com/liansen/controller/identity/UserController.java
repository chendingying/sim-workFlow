package com.liansen.controller.identity;

import com.liansen.common.base.BaseController;
import com.liansen.interfaces.request.UserRequest;
import com.liansen.service.identity.UserBiz;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 人员资源控制类（用户接口）
 * @Author: cdy
 * @Date: 2018/12/28 16:50
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController<UserBiz,UserRequest> {

}
