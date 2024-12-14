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
package com.jackli.common.util;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用头像工具类，生成文字头像
 *
 * @author lijinchang
 * @date 2022/7/5 17:36
 **/
public class CommonAvatarUtil {

    /**
     * 绘制字体头像，如果是英文名，只显示首字母大写，
     * 如果是中文名，只显示最后两个字
     * 返回图片base64
     *
     * @author lijinchang
     * @date 2022/7/5 17:36
     **/
    public static String generateImg(String name) {
        int width = 100;
        int height = 100;
        int nameLength = name.length();
        String nameWritten;
        // 如果用户输入的姓名少于等于2个字符，不用截取
        if (nameLength <= 2) {
            nameWritten = name;
        } else {
            // 如果用户输入的姓名大于等于3个字符，截取后面两位
            String first = StrUtil.sub(name, 0, 1);
            if (isChinese(first)) {
                // 截取倒数两位汉字
                nameWritten = name.substring(nameLength - 2);
            } else {
                // 截取前面的两个英文字母
                nameWritten = StrUtil.sub(name, 0, 1).toUpperCase();
            }
        }
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setBackground(getRandomColor());
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.WHITE);
        Font font;
        // 两个字及以上
        if(nameWritten.length() >= 2) {
            font = new Font("微软雅黑", Font.BOLD, 30);
            g2.setFont(font);
            String firstWritten = StrUtil.sub(nameWritten, 0, 1);
            String secondWritten = StrUtil.sub(nameWritten, 0, 2);
            // 两个中文 如 言曌
            if (isChinese(firstWritten) && isChinese(secondWritten)) {
                g2.drawString(nameWritten, 20, 60);
            }
            // 首中次英 如 罗Q
            else if (isChinese(firstWritten) && !isChinese(secondWritten)) {
                g2.drawString(nameWritten, 27, 60);
                // 首英 如 AB
            } else {
                nameWritten = nameWritten.substring(0,1);
            }
        }
        // 一个字
        if(nameWritten.length() == 1) {
            // 中文
            if(isChinese(nameWritten)) {
                font = new Font("微软雅黑", Font.PLAIN, 50);
                g2.setFont(font);
                g2.drawString(nameWritten, 25, 70);
            } else {
                font = new Font("微软雅黑", Font.PLAIN, 55);
                g2.setFont(font);
                g2.drawString(nameWritten.toUpperCase(), 33, 67);
            }
        }
        return ImgUtil.toBase64DataUri(bufferedImage, "jpg");
    }

    /**
     * 获得随机颜色
     *
     * @author lijinchang
     * @date 2022/7/5 17:41
     **/
    private static Color getRandomColor() {
//        String[] beautifulColors =
//                new String[]{"114,101,230", "255,191,0", "0,162,174", "245,106,0", "24,144,255", "96,109,128"};
        String[] beautifulColors =
                new String[]{"114,101,230", "255,191,0", "0,162,174", "245,106,0", "56,158,13", "19,194,194","212,107,8",
                "179,127,235","146,84,222","114,46,209","83,29,171","57,16,133","255,197,61","250,173,20","212,136,6","173,104,0","135,77,0",
                "97,52,0","105,192,255","9,109,217","0,80,179","0,58,140","250,84,28","235,47,150","47,84,235","24,144,255","160,217,17","250,219,20",
                "248,216,0","3,150,255","234,84,85","115,103,240","50,204,188","246,65,108","40,199,111","159,68,211","98,58,162","245,85,85",
                        "140,27,171", "151,8,204","115,110,254","250,1,109","54,119,255","233,109,113","14,25,125","222,67,19","0,38,97","96,24,220","217,57,205",
                "19,12,183","166,77,182","83,18,214"};
        String[] color = beautifulColors[RandomUtil.randomInt(beautifulColors.length)].split(StrUtil.COMMA);
        return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]),
                Integer.parseInt(color[2]));
    }

    /**
     * 判断字符串是否为中文
     *
     * @author lijinchang
     * @date 2022/7/5 17:41
     **/
    private static boolean isChinese(String str) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
}
