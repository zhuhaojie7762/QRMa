package com.yihaomen.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;

public class Zxing {
    public static void main(String[] args) {
        Zxing zxing = new Zxing();
        //传参：二维码内容和生成路径
        if (zxing.orCode("https://baidu.com", "/Users/zhuhaojie/Pictures/c.jpeg")) {
            System.out.println("ok,成功");
        } else {
            System.out.println("no,失败");
        }
    }

    private boolean orCode(String content, String path) {
        /** 图片的宽度和高度*/
        int width = 300;
        int height = 300;
        //图片的格式String
        String format = "png";
        //定义二维码的参数
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        //定义字符集编码格式
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //纠错的等级 L > M > Q > H 纠错的能力越高可存储的越少，一般使用M
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        //设置图片边距
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            //最终生成 参数列表 （1.内容 2.格式 3.宽度 4.高度 5.二维码参数）
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            //写入到本地
            Path file = new File(path).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, format, file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
