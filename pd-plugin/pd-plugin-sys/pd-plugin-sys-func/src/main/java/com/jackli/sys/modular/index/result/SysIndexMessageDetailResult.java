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
package com.jackli.sys.modular.index.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 站内信详情结果
 *
 * @author lijinchang
 * @date 2022/7/31 16:39
 */
@Getter
@Setter
public class SysIndexMessageDetailResult {

    /** id */
    @ApiModelProperty(value = "主键", position = 1)
    private String id;

    /** 分类 */
    @ApiModelProperty(value = "分类", position = 2)
    private String category;

    /** 主题 */
    @ApiModelProperty(value = "主题", position = 3)
    private String subject;

    /** 正文 */
    @ApiModelProperty(value = "正文", position = 4)
    private String content;

    /** 扩展信息 */
    @ApiModelProperty(value = "扩展信息", position = 5)
    private String extJson;

    /** 接收信息集合 */
    @ApiModelProperty(value = "接收信息集合", position = 6)
    private List<DevReceiveInfo> receiveInfoList;

    /**
     * 接收信息类
     *
     * @author lijinchang
     * @date 2022/7/31 16:42
     */
    @Getter
    @Setter
    public static class DevReceiveInfo {

        /** 接收人ID */
        @ApiModelProperty(value = "接收人ID", position = 1)
        private String receiveUserId;

        /** 接收人姓名 */
        @ApiModelProperty(value = "接收人姓名", position = 2)
        private String receiveUserName;

        /** 是否已读 */
        @ApiModelProperty(value = "是否已读", position = 3)
        private Boolean read;
    }
}
