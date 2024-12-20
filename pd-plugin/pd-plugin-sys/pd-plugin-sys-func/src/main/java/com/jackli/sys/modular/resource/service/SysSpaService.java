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
package com.jackli.sys.modular.resource.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jackli.sys.modular.resource.entity.SysSpa;
import com.jackli.sys.modular.resource.param.spa.SysSpaAddParam;
import com.jackli.sys.modular.resource.param.spa.SysSpaEditParam;
import com.jackli.sys.modular.resource.param.spa.SysSpaIdParam;
import com.jackli.sys.modular.resource.param.spa.SysSpaPageParam;

import java.util.List;

/**
 * 单页面Service接口
 *
 * @author lijinchang
 * @date 2022/6/27 14:03
 **/
public interface SysSpaService extends IService<SysSpa> {

    /**
     * 获取单页面分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:08
     */
    Page<SysSpa> page(SysSpaPageParam sysSpaPageParam);

    /**
     * 添加单页面
     *
     * @author lijinchang
     * @date 2022/4/24 20:48
     */
    void add(SysSpaAddParam sysSpaAddParam);

    /**
     * 编辑单页面
     *
     * @author lijinchang
     * @date 2022/4/24 21:13
     */
    void edit(SysSpaEditParam sysSpaEditParam);

    /**
     * 删除单页面
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    void delete(List<SysSpaIdParam> sysSpaIdParamList);

    /**
     * 获取单页面详情
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    SysSpa detail(SysSpaIdParam sysSpaIdParam);

    /**
     * 获取单页面详情
     *
     * @author lijinchang
     * @date 2022/4/24 21:18
     */
    SysSpa queryEntity(String id);
}
