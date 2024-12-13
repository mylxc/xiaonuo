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
package com.jackli.dev.modular.info.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.jackli.common.annotation.CommonLog;
import com.jackli.common.pojo.CommonResult;
import com.jackli.common.pojo.CommonValidList;
import com.jackli.dev.modular.info.entity.LabelInfo;
import com.jackli.dev.modular.info.param.LabelInfoAddParam;
import com.jackli.dev.modular.info.param.LabelInfoEditParam;
import com.jackli.dev.modular.info.param.LabelInfoIdParam;
import com.jackli.dev.modular.info.param.LabelInfoPageParam;
import com.jackli.dev.modular.info.service.LabelInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;

/**
 * 标签信息控制器
 *
 * @author 李金昌
 * @date  2023/08/17 13:43
 */
@Api(tags = "标签信息控制器")
@ApiSupport(author = "JACKLI_TEAM", order = 1)
@RestController
@Validated
public class LabelInfoController {

    @Resource
    private LabelInfoService labelInfoService;

    /**
     * 获取标签信息分页
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取标签信息分页")
//    @SaCheckPermission("/sys/info/page")
    @GetMapping("/sys/info/page")
    public CommonResult<Page<LabelInfo>> page(LabelInfoPageParam labelInfoPageParam) {
        return CommonResult.data(labelInfoService.page(labelInfoPageParam));
    }

    /**
     * 获取标签信息列表
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    @ApiOperationSupport(order = 1)
    @ApiOperation("获取标签信息列表")
    @GetMapping("/sys/info/getList")
    public CommonResult<List<LabelInfo>> getList(LabelInfoPageParam labelInfoPageParam) {
        return CommonResult.data(labelInfoService.getList(labelInfoPageParam));
    }

    /**
     * 添加标签信息
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    @ApiOperationSupport(order = 2)
    @ApiOperation("添加标签信息")
    @CommonLog("添加标签信息")
//    @SaCheckPermission("/sys/info/add")
    @PostMapping("/sys/info/add")
    public CommonResult<String> add(@RequestBody @Valid LabelInfoAddParam labelInfoAddParam) {
        labelInfoService.add(labelInfoAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑标签信息
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    @ApiOperationSupport(order = 3)
    @ApiOperation("编辑标签信息")
    @CommonLog("编辑标签信息")
//    @SaCheckPermission("/sys/info/edit")
    @PostMapping("/sys/info/edit")
    public CommonResult<String> edit(@RequestBody @Valid LabelInfoEditParam labelInfoEditParam) {
        labelInfoService.edit(labelInfoEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除标签信息
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    @ApiOperationSupport(order = 4)
    @ApiOperation("删除标签信息")
    @CommonLog("删除标签信息")
//    @SaCheckPermission("/sys/info/delete")
    @PostMapping("/sys/info/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   CommonValidList<LabelInfoIdParam> labelInfoIdParamList) {
        labelInfoService.delete(labelInfoIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取标签信息详情
     *
     * @author 李金昌
     * @date  2023/08/17 13:43
     */
    @ApiOperationSupport(order = 5)
    @ApiOperation("获取标签信息详情")
//    @SaCheckPermission("/sys/info/detail")
    @GetMapping("/sys/info/detail")
    public CommonResult<LabelInfo> detail(@Valid LabelInfoIdParam labelInfoIdParam) {
        return CommonResult.data(labelInfoService.detail(labelInfoIdParam));
    }


    /**
     * 读导入的excel模版计算数据
     *
     * @author tianhengmiao
     * @date  2024/01/03 10:29
     */
    @ApiOperationSupport(order = 6)
    @ApiOperation("读excel模版计算数据")
    @PostMapping("/sys/info/calculate")
    public CommonResult<String> calculate(@RequestPart("file") MultipartFile file) throws IOException {
        labelInfoService.calculate(file);
        return CommonResult.ok();
    }
}
