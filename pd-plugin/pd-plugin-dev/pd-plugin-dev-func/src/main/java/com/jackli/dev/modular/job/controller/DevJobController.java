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
package com.jackli.dev.modular.job.controller;

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
import com.jackli.dev.modular.job.entity.DevJob;
import com.jackli.dev.modular.job.param.*;
import com.jackli.dev.modular.job.service.DevJobService;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 定时任务控制器
 *
 * @author lijinchang
 * @date 2022/8/5 10:48
 **/
@Api(tags = "定时任务控制器")
@ApiSupport(author = "JACKLI_TEAM", order = 7)
@RestController
@Validated
public class DevJobController {

    @Resource
    private DevJobService devJobService;

    /**
     * 获取定时任务分页
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取定时任务分页")
    @GetMapping("/dev/job/page")
    public CommonResult<Page<DevJob>> page(DevJobPageParam devJobPageParam) {
        return CommonResult.data(devJobService.page(devJobPageParam));
    }

    /**
     * 获取定时任务列表
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("获取定时任务列表")
    @GetMapping("/dev/job/list")
    public CommonResult<List<DevJob>> list(DevJobListParam devJobListParam) {
        return CommonResult.data(devJobService.list(devJobListParam));
    }

    /**
     * 添加定时任务
     *
     * @author lijinchang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("添加定时任务")
    @CommonLog("添加定时任务")
    @PostMapping("/dev/job/add")
    public CommonResult<String> add(@RequestBody @Valid DevJobAddParam devJobAddParam) {
        devJobService.add(devJobAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑定时任务
     *
     * @author lijinchang
     * @date 2022/4/24 20:47
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("编辑定时任务")
    @CommonLog("编辑定时任务")
    @PostMapping("/dev/job/edit")
    public CommonResult<String> edit(@RequestBody @Valid DevJobEditParam devJobEditParam) {
        devJobService.edit(devJobEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除定时任务
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("删除定时任务")
    @CommonLog("删除定时任务")
    @PostMapping("/dev/job/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                               CommonValidList<DevJobIdParam> devJobIdParamList) {
        devJobService.delete(devJobIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取定时任务详情
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 6)
    @ApiOperation("获取定时任务详情")
    @GetMapping("/dev/job/detail")
    public CommonResult<DevJob> detail(@Valid DevJobIdParam devJobIdParam) {
        return CommonResult.data(devJobService.detail(devJobIdParam));
    }

    /**
     * 停止定时任务
     *
     * @author lijinchang
     * @date 2021/10/13 14:01
     **/
    @ApiOperationSupport(order = 6)
    @ApiOperation("停止定时任务")
    @CommonLog("停止定时任务")
    @PostMapping("/dev/job/stopJob")
    public CommonResult<String> stopJob(@RequestBody DevJobIdParam devJobIdParam) {
        devJobService.stopJob(devJobIdParam);
        return CommonResult.ok();
    }

    /**
     * 运行定时任务
     *
     * @author lijinchang
     * @date 2021/10/13 14:01
     **/
    @ApiOperationSupport(order = 7)
    @ApiOperation("运行定时任务")
    @CommonLog("运行定时任务")
    @PostMapping("/dev/job/runJob")
    public CommonResult<String> runJob(@RequestBody @Valid DevJobIdParam devJobIdParam) {
        devJobService.runJob(devJobIdParam);
        return CommonResult.ok();
    }

    /**
     * 获取定时任务类
     *
     * @author lijinchang
     * @date 2022/4/24 20:00
     */
    @ApiOperationSupport(order = 8)
    @ApiOperation("获取定时任务类")
    @GetMapping("/dev/job/getActionClass")
    public CommonResult<List<String>> getActionClass() {
        return CommonResult.data(devJobService.getActionClass());
    }
}
