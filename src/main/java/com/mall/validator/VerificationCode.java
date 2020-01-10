package com.mall.validator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * @Description: 验证码工具类
 * @Author: ruitao xi  ruitao.xi@luckincoffee.com
 * @Date: 2019/7/30 18:05
 */
public class VerificationCode {
    /**
     * 验证码
     */
    public static StringBuilder imgCode = new StringBuilder();

    /**
     * 验证码宽度
     */
    private static int width = 60;
    /**
     * 验证码高度
     */
    private static int height = 20;
    /**
     * 验证码字符个数
     */
    private static int codeCount = 4;
    /**
     * 字体高度
     */
    private static int fontHeight;

    /**
     * 初始化图片属性
     */
    private static void init() {
        String strWidth ="800";
        String strHeight ="300";
        String strCodeCount = "4";
        // 将配置的信息转换成数值
        width = Integer.parseInt(strWidth);
        height = Integer.parseInt(strHeight);
        codeCount = Integer.parseInt(strCodeCount);
        fontHeight = height/2;
    }

    /**
     * 获取验证码图片
     * @return String 验证码
     * @throws IOException IO异常
     */
    public static byte[] getVerifyImg() throws IOException {
        init();
        Random random = new Random();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphic = image.getGraphics();

        graphic.setColor(Color.getColor("F8F8F8"));
        graphic.fillRect(0, 0, width, height);

        Color[] colors = new Color[] { Color.BLUE, Color.GRAY, Color.GREEN, Color.RED, Color.BLACK, Color.ORANGE,
                Color.CYAN };
        // 在 "画板"上生成干扰线条
        for (int i = 0; i < 100; i++) {
            graphic.setColor(colors[random.nextInt(colors.length)]);
            final int x = random.nextInt(width);
            final int y = random.nextInt(height);
            final int w = random.nextInt(20);
            final int h = random.nextInt(20);
            final int sign1 = random.nextBoolean() ? 1 : -1;
            final int sign2 = random.nextBoolean() ? 1 : -1;
            graphic.drawLine(x, y, x + w * sign1, y + h * sign2);
        }

        // 在 "画板"上绘制字母
        graphic.setFont(new Font("Comic Sans MS", Font.BOLD, fontHeight));
        for (int i = 0; i < codeCount; i++) {
            final int temp = random.nextInt(26) + 97;
            String s = String.valueOf((char) temp);
            imgCode.append(s);
            if (imgCode.length() > 4){
                imgCode.reverse();
                imgCode.setLength(1);
            }
            graphic.setColor(colors[random.nextInt(colors.length)]);
            graphic.drawString(s, i * (width / 4), height - (height / 3));
        }
        graphic.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "png", os);
        return os.toByteArray();
    }
}
