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
package com.jackli.web;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
import com.jackli.common.runner.AppStartupListener;

/**
 * SpringBoot方式启动类
 *
 * @author lijinchang
 * @date 2021/12/18 16:57
 */
@Slf4j
@EnableSwagger2WebMvc
@RestController
@SpringBootApplication(scanBasePackages = {"com.jackli"})
public class JackliWebApp {

    /* 解决druid 日志报错：discard long time none received connection:xxx */
    static {
        System.setProperty("druid.mysql.usePingMethod","false");
    }

    /**
     * 主启动函数
     *
     * @author lijinchang
     * @date 2022/7/30 21:42
     */
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(JackliWebApp.class);
        springApplication.run(args);
        log.info(">>> {}", JackliWebApp.class.getSimpleName().toUpperCase() + " STARTING SUCCESS");
    }

    /**
     * 首页
     *
     * @author lijinchang
     * @date 2022/7/8 14:22
     **/
    @GetMapping("/")
    public String index() {
        return "WELCOME";
    }

    @Bean
    public AppStartupListener appStartupListener(){
        return new AppStartupListener();
    }
}
