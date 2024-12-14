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
package com.jackli.auth.core.util;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.*;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import com.jackli.common.pojo.CommonResult;
import com.jackli.common.util.CommonServletUtil;

@Slf4j
public class AuthExceptionUtil {

    /**
     * 根据错误类型获取对应的CommonResult（只处理SaToken相关异常）
     *
     * @author lijinchang
     * @date 2021/10/11 15:52
     **/
    public static CommonResult<String> getCommonResult(Exception e) {
        CommonResult<String> commonResult;
        if (e instanceof NotLoginException) {

            // 如果是未登录异常 401
            NotLoginException notLoginException = (NotLoginException) e;
            commonResult = CommonResult.get(HttpStatus.HTTP_UNAUTHORIZED, notLoginException.getMessage(), null);
        } else if (e instanceof NotRoleException) {

            // 如果是角色异常 403
            NotRoleException notRoleException = (NotRoleException) e;
            /*commonResult = CommonResult.get(HttpStatus.HTTP_FORBIDDEN, "无此角色：" + notRoleException.getRole() +
                    "，接口地址：" + CommonServletUtil.getRequest().getServletPath(), null);*/
            commonResult = CommonResult.get(HttpStatus.HTTP_FORBIDDEN, "无此角色" , null);
            log.error("无此角色：角色:{}, 接口地址:{}", notRoleException.getRole(), CommonServletUtil.getRequest().getServletPath());
        } else if (e instanceof NotPermissionException) {

            // 如果是权限异常 403
            NotPermissionException notPermissionException = (NotPermissionException) e;
           // commonResult = CommonResult.get(HttpStatus.HTTP_FORBIDDEN, "无此权限：" + notPermissionException.getPermission(), null);
            log.error("无此权限,权限路径:{}", notPermissionException.getPermission());
            commonResult = CommonResult.get(HttpStatus.HTTP_FORBIDDEN, "无此权限", null);
        } else if (e instanceof DisableServiceException) {

            // 如果是被封禁异常 403
            DisableServiceException disableServiceException = (DisableServiceException) e;
            commonResult = CommonResult.get(HttpStatus.HTTP_FORBIDDEN, "账号被封禁：" + disableServiceException.getDisableTime() + "秒后解封", null);
        } else if (e instanceof SaTokenException) {

            // 如果是SaToken异常 直接返回
            SaTokenException saTokenException = (SaTokenException) e;
            commonResult = CommonResult.error(saTokenException.getMessage());
        } else {
            // 未知异常才打印
            e.printStackTrace();
            // 未知异常返回服务器异常（此处不可能执行进入，因为本方法处理的一定是SaToken的异常，此处仅为安全性考虑）
            commonResult = CommonResult.error("服务器异常");
        }
        log.error(">>> {}，请求地址：{}", commonResult.getMsg(), SaHolder.getRequest().getUrl());
        return commonResult;
    }
}
