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
package com.jackli.web.core.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jackli.common.pojo.CommonResult;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常页面处理器，覆盖默认的Whitelabel Error Page
 *
 * @author lijinchang
 * @date 2022/2/11 15:41
 **/
@Slf4j
@RestController
public class GlobalErrorViewController {

    /**
     * Error页面视图，直接响应JSON
     *
     * @author lijinchang
     * @date 2022/2/11 16:11
     **/
    @RequestMapping("/errorView")
    public CommonResult<String> globalError(HttpServletRequest request) {
        CommonResult<String> commonResult = new CommonResult<>(404, "路径不存在", null);
        Object model = request.getAttribute("model");
        if(ObjectUtil.isNotEmpty(model)) {
            JSONObject errorObj = JSONUtil.parseObj(model);
            Integer code = errorObj.getInt("code");
            String msg = errorObj.getStr("msg");
            if(ObjectUtil.isAllNotEmpty(code, msg)) {
                commonResult.setCode(code).setMsg(msg);
            } else if(ObjectUtil.isNotEmpty(msg)) {
                commonResult = CommonResult.error(msg);
            } else {
                commonResult = CommonResult.error();
            }
            if(model instanceof Exception){
                Exception exception = (Exception) model;
                exception.printStackTrace();
            }
        }
        log.error(">>> {}", commonResult.getMsg());
        return commonResult;
    }
}
