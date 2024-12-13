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
package com.jackli.sys.modular.resource.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.jackli.common.annotation.CommonLog;
import com.jackli.common.pojo.CommonResult;
import com.jackli.common.pojo.CommonValidList;
import com.jackli.sys.modular.resource.entity.SysSpa;
import com.jackli.sys.modular.resource.param.spa.SysSpaAddParam;
import com.jackli.sys.modular.resource.param.spa.SysSpaEditParam;
import com.jackli.sys.modular.resource.param.spa.SysSpaIdParam;
import com.jackli.sys.modular.resource.param.spa.SysSpaPageParam;
import com.jackli.sys.modular.resource.service.SysSpaService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * 单页面控制器
 *
 * @author lijinchang
 * @date 2022/6/27 14:14
 **/
@Api(tags = "单页面控制器")
@ApiSupport(author = "JACKLI_TEAM", order = 7)
@RestController
@Validated
public class SysSpaController {

    @Resource
    private SysSpaService sysSpaService;

    /**
     * 获取单页面分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取单页面分页")
    @GetMapping("/sys/spa/page")
    public CommonResult<Page<SysSpa>> page(SysSpaPageParam sysSpaPageParam) {
        return CommonResult.data(sysSpaService.page(sysSpaPageParam));
    }

    /**
     * 添加单页面
     *
     * @author lijinchang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加单页面")
    @CommonLog("添加单页面")
    @PostMapping("/sys/spa/add")
    public CommonResult<String> add(@RequestBody @Valid SysSpaAddParam sysSpaAddParam) {
        sysSpaService.add(sysSpaAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑单页面
     *
     * @author lijinchang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑单页面")
    @CommonLog("编辑单页面")
    @PostMapping("/sys/spa/edit")
    public CommonResult<String> edit(@RequestBody @Valid SysSpaEditParam sysSpaEditParam) {
        sysSpaService.edit(sysSpaEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除单页面
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除单页面")
    @CommonLog("删除单页面")
    @PostMapping("/sys/spa/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<SysSpaIdParam> sysSpaIdParamList) {
        sysSpaService.delete(sysSpaIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取单页面详情
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取单页面详情")
    @GetMapping("/sys/spa/detail")
    public CommonResult<SysSpa> detail(@Valid SysSpaIdParam sysSpaIdParam) {
        return CommonResult.data(sysSpaService.detail(sysSpaIdParam));
    }
}
