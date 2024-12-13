package com.jackli.common.emqx.callback;

import com.jackli.common.emqx.client.EmqClient;
import com.jackli.common.emqx.enums.QosEnum;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 实现MqttCallbackExtended接口
 * connectComplete会自动重连，需要重新订阅主题
 *
 * @author wangsnahu
 */
@Component
public class MessageCallback implements MqttCallbackExtended {

    private static final Logger log = LoggerFactory.getLogger(MessageCallback.class);

    @Autowired
    private EmqClient emqClient;

    /**
     * 丢失了对服务端的连接后触发的回调
     *
     * @param cause
     */
    @Override
    public void connectionLost(Throwable cause) {
        // 丢失对服务端的连接后触发该方法回调，此处可以做一些特殊处理，比如重连
        log.info("丢失了对服务端的连接");

    }

    /**
     * 订阅到消息后的回调
     * 该方法由mqtt客户端同步调用,在此方法未正确返回之前，不会发送ack确认消息到broker
     * 一旦该方法向外抛出了异常客户端将异常关闭，当再次连接时；所有QoS1,QoS2且客户端未进行ack确认的 消息都将由
     * broker服务器再次发送到客户端
     *
     * @param topic
     * @param message
     * @throws Exception
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("订阅者订阅到了消息,topic={},messageid={},qos={},payload={}",
                topic,
                message.getId(),
                message.getQos(),
                new String(message.getPayload()));
    }

    /**
     * 消息发布完成且收到ack确认后的回调
     * QoS0：消息被网络发出后触发一次
     * QoS1：当收到broker的PUBACK消息后触发
     * QoS2：当收到broer的PUBCOMP消息后触发
     *
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        int messageId = token.getMessageId();
        String[] topics = token.getTopics();
        log.info("消息发布完成,messageid={},topics={}", messageId, topics);
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        log.info("连接完成...");
        try {
            // 重连后要自己重新订阅topic，这样emq服务发的消息才能重新接收到，不然的话，断开后客户端只是重新连接了服务，并没有自动订阅，导致接收不到消息
            emqClient.subscribe("testtopic/#", QosEnum.QoS2);
            log.info("订阅成功");
        } catch (Exception e) {
            log.info("订阅出现异常:{}", e);
        }

    }
}