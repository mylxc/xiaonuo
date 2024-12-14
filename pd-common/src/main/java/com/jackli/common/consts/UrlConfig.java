package com.jackli.common.consts;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description: 楚天路径配置
 * @author: luoxingcheng
 * @created: 2024/02/18 08:57
 */
@ConfigurationProperties(prefix = "cturl")
@Data
@Component
public class UrlConfig {

    // 装夹站完成装夹后通知MES作业任务和托盘的关联关系 url
    private String clampingUrl;

    // 任务开始时间路径
    private String taskStartTimeUrl;

    // 楚天任务结束时间路径
    private String taskEndTimeUrl;

    // 卸载物料通知mes的url
    private String unloadingMaterialsNoticeUrl;

    // 缺刀接口
    private String lackToolDateUrl;

    // 设备数据采集推送路径
    private String eqptDataPushUrl;

    // OEE接口路径
    private String oeeUrl;

    // 设备加工任务队列查询
    private String mcWorkTaskPushUrl;

    // 托盘状态查询
    private String palletDataPushUrl;

    // 刀具寿命推送路径
    private String eqptToolLifeUrl;

    // 组刀信息查询(通过刀具唯一号hiltCode查询)
    private String toolAssembleInfo;

    // 组刀信息查询(通过rfidCode查询)
    private String toolAssembleInfoByRfid;

    // 刀具寿命预警推送
    private String toolEarlyWarning;

}
