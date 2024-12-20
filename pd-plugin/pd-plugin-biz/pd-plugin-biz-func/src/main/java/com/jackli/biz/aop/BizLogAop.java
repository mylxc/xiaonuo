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
package com.jackli.biz.aop;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.jackli.auth.core.pojo.SaBaseLoginUser;
import com.jackli.auth.core.util.StpLoginUserUtil;
import com.jackli.common.annotation.CommonLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 业务日志aop切面
 *
 * @author lijinchang
 * @date 2020/3/20 11:47
 */
@Aspect
@Order
@Component

public class BizLogAop {
    /**
     * 日志切入点
     *
     * @author lijinchang
     * @date 2020/3/23 17:10
     */
    @Pointcut("@annotation(com.jackli.common.annotation.CommonLog)")
    private void getLogPointCut() {
    }

    /**
     * 操作成功返回结果记录日志
     *
     * @author lijinchang
     * @date 2022/9/2 15:24
     */
    @AfterReturning(pointcut = "getLogPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CommonLog commonLog = method.getAnnotation(CommonLog.class);
        String userName = "未知";
        try {
            SaBaseLoginUser loginUser = StpLoginUserUtil.getLoginUser();
            if(ObjectUtil.isNotNull(loginUser)) {
                userName = loginUser.getName();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 操作发生异常记录日志
     *
     * @author lijinchang
     * @date 2022/9/2 15:24
     */
    @AfterThrowing(pointcut = "getLogPointCut()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Exception exception) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CommonLog commonLog = method.getAnnotation(CommonLog.class);
        String userName = "未知";
        try {
            SaBaseLoginUser loginUser = StpLoginUserUtil.getLoginUser();
            if(ObjectUtil.isNotNull(loginUser)) {
                userName = loginUser.getName();
            }
        } catch (Exception ignored) {
        }

    }
}
