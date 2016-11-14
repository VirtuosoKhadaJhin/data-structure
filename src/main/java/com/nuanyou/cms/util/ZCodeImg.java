package com.nuanyou.cms.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alan.ye on 2016/10/12.
 */
public class ZCodeImg {

    private static final String Format = "gif";

    private static final Map<EncodeHintType, Object> Hints = new HashMap<>();

    static {
        // 边框大小
        Hints.put(EncodeHintType.MARGIN, 3);
        // 编码方式
        Hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 纠错级别
        Hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
    }

    public static void writeImage(String message, OutputStream os) throws Exception {
        writeImage(message, 300, os);
    }

    public static void writeImage(String message, int size, OutputStream os) throws Exception {
        writeImage(message, size, size, os);
    }

    public static void writeImage(String message, int width, int height, OutputStream os) throws Exception {
        BitMatrix matrix = new MultiFormatWriter().encode(message, BarcodeFormat.QR_CODE, width, height, Hints);
        BufferedImage image = toBufferedImage(matrix);

        ImageIO.write(image, Format, os);
        os.flush();
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                image.setRGB(x, y, matrix.get(x, y) ? Color.black.getRGB() : Color.white.getRGB());
        return image;
    }

}