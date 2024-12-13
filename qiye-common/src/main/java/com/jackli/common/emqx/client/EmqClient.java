package com.jackli.common.emqx.client;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jackli.common.emqx.callback.MessageCallback;
import com.jackli.common.emqx.enums.QosEnum;
import com.jackli.common.emqx.properties.MqttProperties;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * @author wangsanhu
 */
@Component
public class EmqClient {
    private static final Logger log = LoggerFactory.getLogger(EmqClient.class);


    private IMqttClient mqttClient;

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MessageCallback mqttCallback;

    @PostConstruct
    public void init() {
        // MqttClientPersistence是接口 实现类有：MqttDefaultFilePersistence；MemoryPersistence
        MqttClientPersistence mempersitence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(mqttProperties.getBrokerUrl(), mqttProperties.getClientId()+ IdWorker.getIdStr(), mempersitence);
        } catch (MqttException e) {
            log.error("初始化客户端mqttClient对象失败,errormsg={},brokerUrl={},clientId={}", e.getMessage(), mqttProperties.getBrokerUrl(), mqttProperties.getClientId());
        }
    }

    /**
     * 连接broker
     *
     * @param username
     * @param password
     */
    public void connect(String username, String password) {
        // 创建MQTT连接选项对象--可配置mqtt连接相关选项
        MqttConnectOptions options = new MqttConnectOptions();
        // 自动重连
        options.setAutomaticReconnect(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        /**
         * 设置为true后意味着：客户端断开连接后emq不保留会话保留会话，否则会产生订阅共享队列的存活 客户端收不到消息的情况
         * 因为断开的连接还被保留的话，emq会将队列中的消息负载到断开但还保留的客户端，导致存活的客户 端收不到消息
         * 解决该问题有两种方案:1.连接断开后不要保持；2.保证每个客户端有固定的clientId
         */
        options.setCleanSession(true);
        //设置mqtt消息回调
        mqttClient.setCallback(mqttCallback);
        // 连接broker
        try {
            mqttClient.connect(options);
        } catch (MqttException e) {
            log.error("mqtt客户端连接服务端失败,失败原因{}", e.getMessage());
        }
    }

    /**
     * 断开连接
     */
    @PreDestroy
    public void disConnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            log.error("断开连接产生异常,异常信息{}", e.getMessage());
        }
    }

    /**
     * 重连
     */
    public void reConnect() {
        try {
            mqttClient.reconnect();
        } catch (MqttException e) {
            log.error("重连失败,失败原因{}", e.getMessage());
        }
    }

    /**
     * 发布消息
     *
     * @param topic
     * @param msg
     * @param qos
     * @param retain
     */
    public void publish(String topic, String msg, QosEnum qos, boolean retain) {

        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(msg.getBytes());
        mqttMessage.setQos(qos.value());
        mqttMessage.setRetained(retain);
        try {
            if(mqttClient.isConnected()==false)
                mqttClient.reconnect();
            mqttClient.publish(topic, mqttMessage);
        } catch (MqttException e) {
            log.error("发布消息失败,errormsg={},topic={},msg={},qos={},retain={}", e.getMessage(), topic, msg, qos.value(), retain);
        }

    }

    /**
     * 订阅
     *
     * @param topicFilter
     * @param qos
     */
    public void subscribe(String topicFilter, QosEnum qos) {
        try {
            mqttClient.subscribe(topicFilter, qos.value());
        } catch (MqttException e) {
            log.error("订阅主题失败,errormsg={},topicFilter={},qos={}", e.getMessage(), topicFilter, qos.value());
        }

    }

    /**
     * 取消订阅
     *
     * @param topicFilter
     */
    public void unSubscribe(String topicFilter) {
        try {
            mqttClient.unsubscribe(topicFilter);
        } catch (MqttException e) {
            log.error("取消订阅失败,errormsg={},topicfiler={}", e.getMessage(), topicFilter);
        }
    }

}
