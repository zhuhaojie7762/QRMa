package com.yihaomen.barcode;

import cn.hutool.core.codec.Base64;
import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * 生成二维码工具
 */
@Slf4j
public class QRCodeTool {

    public static ByteArrayOutputStream generateQRCode(String url) {
        int width = 70; // 二维码图片的宽度
        int height = 80; // 二维码图片的高度
        String format = "png"; // 二维码图片的格式
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int rgb = bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF;
                    bufferedImage.setRGB(x, y, rgb);
                }
            }
            ImageIO.write(bufferedImage, format, baos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos;
    }

    /**
     *
     * @param text 扫描后的内容
     * @param qrText  二维码下方东西
     * @param width 宽度
     * @param height 高度
     * @return
     * @throws WriterException
     */
    public static String generateQRCodeImage(String text,String qrText, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = Maps.newHashMap();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        // ------------------------------------------自定义文本描述-------------------------------------------------
        //在内存创建图片缓冲区 这里设置画板的宽高和类型
        BufferedImage outImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        //创建画布
        Graphics2D outg = outImage.createGraphics();
        // 在画布上画上二维码 X轴Y轴，宽度高度
        outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        // 画文字到新的面板
        outg.setColor(Color.RED);
        // 字体、字型、字号
        Font fontChinese = new Font("微软雅黑", Font.PLAIN, 12);
        outg.setFont(fontChinese);
        //drawString(文字信息、x轴、y轴)方法根据参数设置文字的坐标轴 ，根据需要来进行调整
        int strWidth = outg.getFontMetrics().stringWidth(qrText);
        outg.drawString(qrText, (width - strWidth) / 2, height - 3);
        outg.dispose();
        outImage.flush();
        image = outImage;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            boolean flag = ImageIO.write(image, "png", baos);
            System.out.println(flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return baos;
        return  "data:image/jpeg;base64," + Base64.encode(baos.toByteArray());
    }

    public static void main(String[] args) throws WriterException {
        String value =generateQRCodeImage("https://baidu.com","hello你好",120,120);
        System.out.println(value);
    }
}

