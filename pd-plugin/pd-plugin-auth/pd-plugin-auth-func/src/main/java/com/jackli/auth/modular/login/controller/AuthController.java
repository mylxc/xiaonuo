/*
 * Copyright [2022] [https://www.jcxxdd.com]
 *
 * Jackli采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Jackli源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.jcxxdd.com
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队767076381@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Jackli商业授权许可，请在官网购买授权，地址为 https://www.jcxxdd.com
 */
package com.jackli.auth.modular.login.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.jackli.auth.core.enums.SaClientTypeEnum;
import com.jackli.auth.core.pojo.SaBaseLoginUser;
import com.jackli.auth.modular.login.param.AuthAccountPasswordLoginParam;
import com.jackli.auth.modular.login.param.AuthGetPhoneValidCodeParam;
import com.jackli.auth.modular.login.param.AuthPhoneValidCodeLoginParam;
import com.jackli.auth.modular.login.result.AuthPicValidCodeResult;
import com.jackli.auth.modular.login.service.AuthService;
import com.jackli.common.pojo.CommonResult;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * B端登录控制器
 *
 * @author lijinchang
 * @date 2021/12/23 21:50
 */
@Api(tags = "B端登录控制器")
@ApiSupport(author = "JACKLI_TEAM", order = 2)
@RestController
@Validated
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * B端获取图片验证码
     *
     * @author lijinchang
     * @date 2022/7/8 9:26
     **/
    @ApiOperationSupport(order = 1)
    @ApiOperation("B端获取图片验证码")
    @GetMapping("/auth/b/getPicCaptcha")
    public CommonResult<AuthPicValidCodeResult> getPicCaptcha() {
        return CommonResult.data(authService.getPicCaptcha(SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端获取手机验证码
     *
     * @author lijinchang
     * @date 2022/7/8 9:26
     **/
    @ApiOperationSupport(order = 2)
    @ApiOperation("B端获取手机验证码")
    @GetMapping("/auth/b/getPhoneValidCode")
    public CommonResult<String> getPhoneValidCode(@Valid AuthGetPhoneValidCodeParam authGetPhoneValidCodeParam) {
        return CommonResult.data(authService.getPhoneValidCode(authGetPhoneValidCodeParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端账号密码登录
     *
     * @author lijinchang
     * @date 2021/10/15 13:12
     **/
    @ApiOperationSupport(order = 3)
    @ApiOperation("B端账号密码登录")
    @PostMapping("/auth/b/doLogin")
    public CommonResult<String> doLogin(@RequestBody @Valid AuthAccountPasswordLoginParam authAccountPasswordLoginParam) {
        return CommonResult.data(authService.doLogin(authAccountPasswordLoginParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端手机验证码登录
     *
     * @author lijinchang
     * @date 2021/10/15 13:12
     **/
    @ApiOperationSupport(order = 4)
    @ApiOperation("B端手机验证码登录")
    @PostMapping("/auth/b/doLoginByPhone")
    public CommonResult<String> doLoginByPhone(@RequestBody @Valid AuthPhoneValidCodeLoginParam authPhoneValidCodeLoginParam) {
        return CommonResult.data(authService.doLoginByPhone(authPhoneValidCodeLoginParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端退出
     *
     * @author lijinchang
     * @date 2021/10/15 13:12
     **/
    @ApiOperationSupport(order = 5)
    @ApiOperation("B端退出")
    @SaCheckLogin
    @GetMapping("/auth/b/doLogout")
    public CommonResult<String> doLogout() {
        StpUtil.logout();
        return CommonResult.ok();
    }

    /**
     * B端获取用户信息
     *
     * @author lijinchang
     * @date 2021/10/15 13:12
     **/
    @ApiOperationSupport(order = 6)
    @ApiOperation("B端获取用户信息")
    @SaCheckLogin
    @GetMapping("/auth/b/getLoginUser")
    public CommonResult<SaBaseLoginUser> getLoginUser() {
        return CommonResult.data(authService.getLoginUser());
    }
}
