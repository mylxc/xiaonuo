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
package com.jackli.web.core.provider.sys.sys;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/*import com.jackli.sys.feign.SysFeign;
import com.jackli.sys.modular.sys.SysApiProvider;*/

/**
 * 系统模块综合API Feign接口实现类
 *
 * @author dongxiayu
 * @date 2022/11/22 22:31
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class SysFeignProvider {
//public class SysFeignProvider implements SysFeign {

    //private final SysApiProvider sysApiProvider;

    /**
     * 初始化ID类型的租户系统模块数据
     *
     * @param tenId
     * @param tenName
     * @author dongxiayu
     * @date 2022/9/26 14:25
     */
   //@Override
    @RequestMapping("/feign/sys/initTenDataForCategoryId")
    public void initTenDataForCategoryId(@RequestParam(value = "tenId",required = false) String tenId, @RequestParam(value = "tenName",required = false) String tenName) {
      //  sysApiProvider.initTenDataForCategoryId(tenId, tenName);
    }

    /**
     * 删除ID类型的租户系统模块数据
     *
     * @param tenId
     * @author dongxiayu
     * @date 2022/9/26 14:25
     */
    //@Override
    @RequestMapping("/feign/sys/removeTenDataForCategoryId")
    public void removeTenDataForCategoryId(@RequestParam(value = "tenId",required = false) String tenId) {
       // sysApiProvider.removeTenDataForCategoryId(tenId);
    }
}
