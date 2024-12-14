package com.jackli.common.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jackli.common.config.LocaleResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

@Slf4j
@Component
public class I18nUtil {

    private static final String basename = "i18n/messages";

    private final LocaleResolver resolver;

    private static LocaleResolver customLocaleResolver;

    private static String path;


    public I18nUtil(LocaleResolver resolver) {
        this.resolver = resolver;
    }

    public static String getFilerMessage(String field) {
        return StrUtil.isBlank(field) ? field : getMessage("field." + field, field);
    }

    public static String getValidatorMessage(String code) {
        return StrUtil.isBlank(code) ? code : getMessage("validator." + code, code);
    }


    @PostConstruct
    public void init() {
        setBasename(basename);
        setCustomLocaleResolver(resolver);
    }

    /**
     * 获取 国际化后内容信息
     *
     * @param code 国际化key
     * @return 国际化后内容信息
     */
    public static String getMessage(String code, String defaultMessage) {
        if (ObjectUtil.isNull(code)) {
            return defaultMessage;
        }
        Locale locale = customLocaleResolver.getLocal();
        return getMessage(code, null, defaultMessage, locale);
    }

    /**
     * 获取指定语言中的国际化信息，如果没有则走英文
     *
     * @param code 国际化 key
     * @param lang 语言参数
     * @return 国际化后内容信息
     */
    public static String getMessage(String code, String defaultMessage, String lang) {
        Locale locale;
        if (StrUtil.isEmpty(lang)) {
            locale = Locale.US;
        } else {
            try {
                String[] split = lang.split("-");
                locale = new Locale(split[0], split[1]);
            } catch (Exception e) {
                locale = Locale.US;
            }
        }
        return getMessage(code, null, defaultMessage, locale);
    }

    /**
     * 获取站内信指定语言 目前只支持 中文与英文两类 默认英文
     *
     * @param code 国际化 key
     * @param lang 语言参数
     * @return 国际化后内容信息
     */
    public static String getStationLetterMessage(String code, String lang) {
        Locale locale = Objects.equals(lang, "zh-CN") ? Locale.SIMPLIFIED_CHINESE : Locale.US;
        return getMessage(code, null, code, locale);
    }


    public static String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        messageSource.setBasename(path);
        String content;
        try {
            content = messageSource.getMessage(code, args, locale);
        } catch (Exception e) {
            log.error("国际化参数获取失败===>{},{}", e.getMessage(), e);
            content = defaultMessage;
        }
        return content;

    }

    public static void setBasename(String basename) {
        I18nUtil.path = basename;
    }

    public static void setCustomLocaleResolver(LocaleResolver resolver) {
        I18nUtil.customLocaleResolver = resolver;
    }

    public static String getCodeFromValue(String value) {
        Locale locale = new Locale("zh", "CN");
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.toString());
        messageSource.setBasename(path);
        ResourceBundle bundle = ResourceBundle.getBundle(basename, locale);
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String message = messageSource.getMessage(key, null, null, locale);
            if (value.equals(message)) {
                return key;
            }
        }
        return null;
    }

}
