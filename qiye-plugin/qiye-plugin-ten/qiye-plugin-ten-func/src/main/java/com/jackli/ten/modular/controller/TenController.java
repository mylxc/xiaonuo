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
package com.jackli.ten.modular.controller;

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
import com.jackli.ten.modular.entity.TenStorage;
import com.jackli.ten.modular.param.*;
import com.jackli.ten.modular.result.TenDbsSelectorResult;
import com.jackli.ten.modular.service.TenService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 多租户控制器
 *
 * @author lijinchang
 * @date 2022/7/6 21:52
 */
@Api(tags = "多租户控制器")
@ApiSupport(author = "JACKLI_TEAM", order = 1)
@RestController
@Validated
public class TenController {

    @Resource
    private TenService tenService;

    /**
     * 获取租户分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取租户分页")
    @GetMapping("/ten/storage/page")
    public CommonResult<Page<TenStorage>> page(TenStoragePageParam tenStoragePageParam) {
        return CommonResult.data(tenService.page(tenStoragePageParam));
    }

    /**
     * 租户选择器
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("租户选择器")
    @GetMapping("/ten/storage/tenSelector")
    public CommonResult<List<TenStorage>> tenSelector(TenStorageSelectorParam tenStorageSelectorParam) {
        return CommonResult.data(tenService.tenSelector(tenStorageSelectorParam));
    }

    /**
     * 添加租户
     *
     * @author lijinchang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加租户")
    @CommonLog("添加租户")
    @PostMapping("/ten/storage/add")
    public CommonResult<String> add(@RequestBody @Valid TenStorageAddParam tenStorageAddParam) {
        tenService.add(tenStorageAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑租户
     *
     * @author lijinchang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑租户")
    @CommonLog("编辑租户")
    @PostMapping("/ten/storage/edit")
    public CommonResult<String> edit(@RequestBody @Valid TenStorageEditParam tenStorageEditParam) {
        tenService.edit(tenStorageEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除租户
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除租户")
    @CommonLog("删除租户")
    @PostMapping("/ten/storage/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<TenStorageIdParam> tenStorageIdParamList) {
        tenService.delete(tenStorageIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取租户详情
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取租户详情")
    @GetMapping("/ten/storage/detail")
    public CommonResult<TenStorage> detail(@Valid TenStorageIdParam TenStorageIdParam) {
        return CommonResult.data(tenService.detail(TenStorageIdParam));
    }

    /* ====租户部分所需要用到的选择器==== */

    /**
     * 获取租户未被使用数据源列表
     *
     * @author yubaoshan
     * @date 2022/7/11 17:30
     */
    @ApiOperationSupport(order = 6)
    @ApiOperation("获取租户未被使用数据源列表")
    @GetMapping("/ten/storage/dbsList")
    public CommonResult<List<TenDbsSelectorResult>> dbsList() {
        return CommonResult.data(tenService.dbsList());
    }
}
