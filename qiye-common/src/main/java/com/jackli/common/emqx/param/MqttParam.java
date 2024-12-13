package com.jackli.common.emqx.param;

import lombok.Data;

@Data
public class MqttParam {
    private Long taskId;
    private String topic;
    private String payload;
    private Integer retain;
}
