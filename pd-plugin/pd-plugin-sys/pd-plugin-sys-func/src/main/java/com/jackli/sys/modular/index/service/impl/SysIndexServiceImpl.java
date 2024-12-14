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
package com.jackli.sys.modular.index.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import com.jackli.auth.core.pojo.SaBaseLoginUser;
import com.jackli.auth.core.util.StpLoginUserUtil;
import com.jackli.common.pojo.CommonValidList;
import com.jackli.dev.api.DevLogApi;
import com.jackli.dev.api.DevMessageApi;
import com.jackli.sys.modular.index.param.*;
import com.jackli.sys.modular.index.result.*;
import com.jackli.sys.modular.index.service.SysIndexService;
import com.jackli.sys.modular.relation.entity.SysRelation;
import com.jackli.sys.modular.relation.enums.SysRelationCategoryEnum;
import com.jackli.sys.modular.relation.service.SysRelationService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统首页Service接口实现类
 *
 * @author lijinchang
 * @date 2022/9/2 10:45
 */
@Service
public class SysIndexServiceImpl implements SysIndexService {

    @Resource
    private SysRelationService sysRelationService;

    @Resource
    private DevMessageApi devMessageApi;

    @Resource(name = "devLogApiProvider")
    private DevLogApi devLogApi;

    @Override
    public void addSchedule(SysIndexScheduleAddParam sysIndexScheduleAddParam) {
        SaBaseLoginUser loginUser = StpLoginUserUtil.getLoginUser();
        sysIndexScheduleAddParam.setScheduleUserId(loginUser.getId());
        sysIndexScheduleAddParam.setScheduleUserName(loginUser.getName());
        sysRelationService.saveRelationWithAppend(loginUser.getId(), sysIndexScheduleAddParam.getScheduleDate(),
                SysRelationCategoryEnum.SYS_USER_SCHEDULE_DATA.getValue(), JSONUtil.toJsonStr(sysIndexScheduleAddParam));
    }

    @Override
    public void deleteSchedule(CommonValidList<SysIndexScheduleIdParam> sysIndexScheduleIdParamList) {
        List<String> scheduleIdList = sysIndexScheduleIdParamList.stream().map(SysIndexScheduleIdParam::getId)
                .collect(Collectors.toList());
        if(ObjectUtil.isNotEmpty(scheduleIdList)) {
            sysRelationService.removeBatchByIds(scheduleIdList);
        }
    }

    @Override
    public List<SysIndexScheduleListResult> scheduleList(SysIndexScheduleListParam sysIndexScheduleListParam) {
        return sysRelationService.list(new LambdaQueryWrapper<SysRelation>().eq(SysRelation::getObjectId, StpUtil.getLoginIdAsString())
                .eq(SysRelation::getTargetId, sysIndexScheduleListParam.getScheduleDate())
                .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_USER_SCHEDULE_DATA.getValue()))
                .stream().map(sysRelation -> {
                    SysIndexScheduleListResult sysIndexScheduleListResult = JSONUtil.toBean(sysRelation.getExtJson(), SysIndexScheduleListResult.class);
                    sysIndexScheduleListResult.setId(sysRelation.getId());
                    return sysIndexScheduleListResult;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SysIndexMessageListResult> messageList(SysIndexMessageListParam sysIndexMessageListParam) {
        return devMessageApi.list(CollectionUtil.newArrayList(StpUtil.getLoginIdAsString()),
                ObjectUtil.isEmpty(sysIndexMessageListParam.getLimit())?10:sysIndexMessageListParam.getLimit()).stream()
                .map(jsonObject -> JSONUtil.toBean(jsonObject, SysIndexMessageListResult.class)).collect(Collectors.toList());
    }

    @Override
    public SysIndexMessageDetailResult messageDetail(SysIndexMessageIdParam sysIndexMessageIdParam) {
        return JSONUtil.toBean(devMessageApi.detail(sysIndexMessageIdParam.getId()), SysIndexMessageDetailResult.class);
    }

    @Override
    public List<SysIndexVisLogListResult> visLogList() {
        return devLogApi.currentUserVisLogList().stream()
                .map(jsonObject -> JSONUtil.toBean(jsonObject, SysIndexVisLogListResult.class)).collect(Collectors.toList());
    }

    @Override
    public List<SysIndexOpLogListResult> opLogList() {
        return devLogApi.currentUserOpLogList().stream()
                .map(jsonObject -> JSONUtil.toBean(jsonObject, SysIndexOpLogListResult.class)).collect(Collectors.toList());
    }
}
