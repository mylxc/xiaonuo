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
package com.jackli.dbs.modular.controller;

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
import com.jackli.dbs.modular.entity.DbsStorage;
import com.jackli.dbs.modular.param.*;
import com.jackli.dbs.modular.result.DbsTableColumnResult;
import com.jackli.dbs.modular.result.DbsTableResult;
import com.jackli.dbs.modular.service.DbsService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 数据源控制器
 *
 * @author lijinchang
 * @date 2022/6/8 19:33
 **/
@Api(tags = "数据源控制器")
@ApiSupport(author = "JACKLI_TEAM", order = 1)
@RestController
@Validated
public class DbsController {

    @Resource
    private DbsService dbsStorageService;

    /**
     * 获取数据源分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取数据源分页")
    @GetMapping("/dbs/storage/page")
    public CommonResult<Page<DbsStorage>> page(DbsStoragePageParam dbsStoragePageParam) {
        return CommonResult.data(dbsStorageService.page(dbsStoragePageParam));
    }

    /**
     * 添加数据源
     *
     * @author lijinchang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加数据源")
    @CommonLog("添加数据源")
    @PostMapping("/dbs/storage/add")
    public CommonResult<String> add(@RequestBody @Valid DbsStorageAddParam dbsStorageAddParam) {
        dbsStorageService.add(dbsStorageAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑数据源
     *
     * @author lijinchang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑数据源")
    @CommonLog("编辑数据源")
    @PostMapping("/dbs/storage/edit")
    public CommonResult<String> edit(@RequestBody @Valid DbsStorageEditParam dbsStorageEditParam) {
        dbsStorageService.edit(dbsStorageEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除数据源
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除数据源")
    @CommonLog("删除数据源")
    @PostMapping("/dbs/storage/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<DbsStorageIdParam> dbsStorageIdParamList) {
        dbsStorageService.delete(dbsStorageIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取数据源详情
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取数据源详情")
    @GetMapping("/dbs/storage/detail")
    public CommonResult<DbsStorage> detail(@Valid DbsStorageIdParam dbsStorageIdParam) {
        return CommonResult.data(dbsStorageService.detail(dbsStorageIdParam));
    }

    /* ====数据源部分所需要用到的选择器==== */

    /**
     * 获取数据库中所有表
     *
     * @author lijinchang
     * @date 2022/6/8 19:35
     **/
    @ApiOperationSupport(order = 6)
    @ApiOperation("获取数据库中所有表")
    @GetMapping("/dbs/tables")
    public CommonResult<List<DbsTableResult>> tables() {
        return CommonResult.data(dbsStorageService.tables());
    }

    /**
     * 获取数据库表中所有字段
     *
     * @author lijinchang
     * @date 2022/6/8 19:35
     **/
    @ApiOperationSupport(order = 7)
    @ApiOperation("获取数据库表中所有字段")
    @GetMapping("/dbs/tableColumns")
    public CommonResult<List<DbsTableColumnResult>> tableColumns(@Valid DbsStorageTableColumnParam dbsStorageTableColumnParam) {
        return CommonResult.data(dbsStorageService.tableColumns(dbsStorageTableColumnParam));
    }
}
