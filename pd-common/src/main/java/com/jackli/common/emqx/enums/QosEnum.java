package com.jackli.common.emqx.enums;

/**
 * @author wangsanhu
 */
public enum QosEnum {

    /**
     * QoS0：消息被网络发出后触发一次
     * QoS1：当收到broker的PUBACK消息后触发
     * QoS2：当收到broer的PUBCOMP消息后触发
     */
    QoS0(0), QoS1(1), QoS2(2);


    private final int value;

    QosEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
