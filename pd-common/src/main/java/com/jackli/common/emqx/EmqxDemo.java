package com.jackli.common.emqx;

import com.jackli.common.emqx.client.EmqClient;
import com.jackli.common.emqx.enums.QosEnum;
import com.jackli.common.emqx.properties.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author wangsanhu
 */
//@SpringBootApplication
@Component
public class EmqxDemo {
    @Autowired
    private EmqClient emqClient;

    @Autowired
    private MqttProperties properties;

    /**
     * 开启订阅指定主题消息
     */
    @PostConstruct
    public void init() {
        //连接服务端
        emqClient.connect(properties.getUsername(), properties.getPassword());
        //订阅一个主题
        emqClient.subscribe("testTopicC/#", QosEnum.QoS2);

    }
}
