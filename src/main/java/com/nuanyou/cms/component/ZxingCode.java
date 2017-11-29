package com.nuanyou.cms.component;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

public class ZxingCode {
    /**
     * 条形码编码
     *
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */

    private static final String CHARSET = "utf-8";

    private static final String PICTRUE_FORMATE_JPG = "jpg";
    /**
     * 生成条形码
     * @param contents 核销码
     */
    public static void  encode(OutputStream out ,String contents,String titleInfo,FileClient fileClient) {
        int width = 480, height = 120;//条形码大小
        int codeWidth = 3 + (7 * 6) + 5 + (7 * 6) + 3;
        codeWidth = Math.max(codeWidth, width);
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents,BarcodeFormat.CODE_128, codeWidth, height, hints);
            BufferedImage encodePath = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
            ImageIO.write(encodePath, "jpg", imageOutput);
            InputStream codeInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());//条形码输出流
            InputStream InputStream = fileClient.queryFile(titleInfo);//下载底图的输出流
            pressImage(out ,InputStream, codeInputStream, contents, -1,590, -1,-1, 1f);//添加水印图片
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @param targetImgStream 目标图片路径，流文件
     * @param encodeStream 水印图片路径，流文件
     * @param x 水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y 水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha 透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    //添加图片水印
    public static void pressImage(OutputStream out ,InputStream targetImgStream, InputStream encodeStream,String pressText ,int textX ,int textY,int x, int y, float alpha) throws IOException {
        InputStream is = null;
        try {
            Image mainImage = ImageIO.read(targetImgStream);
            int mainWidth = mainImage.getWidth(null);
            int mainHeight = mainImage.getHeight(null);
            BufferedImage bufferedImage = new BufferedImage(mainWidth, mainHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D mainGraphics = bufferedImage.createGraphics();//创建图片文件(最后合成的图片)
            mainGraphics.drawImage(mainImage, 0, 0, mainWidth, mainHeight, null);
            mainGraphics.setFont(new Font("宋体", Font.BOLD, 30));//拼接水印核销码
            mainGraphics.setColor(Color.BLACK);//拼接水印核销码
            mainGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
            //拼接核销码
            int  pressTextWidth = 30 * getLength(pressText);
            int  pressTextHeight = 20;
            int mainPressTextWidth = mainWidth - pressTextWidth;
            int mainPressTextHeight = mainHeight - pressTextHeight;
            if(textX < 0){
                textX = mainPressTextWidth / 2;
            }else if(textX > mainPressTextWidth){
                textX = mainPressTextWidth;
            }
            if(textY < 0){
                textY = mainPressTextHeight / 2;
            }else if(textY > mainPressTextHeight){
                textY = mainPressTextHeight;
            }
            mainGraphics.drawString(pressText, textX ,textY + pressTextHeight);
/*          //拼接信息文字
            int messWidth = 30 * getLength("난요우관리자앱으로.");
            int messHeight = 30;
            int mainMessWidth  = mainWidth - messWidth;
            int mainMessHeight = mainHeight - messHeight;
            if(messX < 0){
                messX = mainMessWidth / 2;
            }else if(messX > mainMessWidth){
                messX = mainMessWidth;
            }
            if(messY < 0){
                messY = mainMessHeight / 2;
            }else if(messY > mainMessHeight){
                messY = mainMessHeight;
            }
            mainGraphics.drawString("난요우관리자앱으로", messX, messY + messHeight);//添加水印*/
            //添加核销一维码
            //ImageInputStream
            Image encodeImage = ImageIO.read(encodeStream);
            int encodeWidth = encodeImage.getWidth(null);
            int encodeHeight = encodeImage.getHeight(null);
            //水印文件与底图的差值
            int mainEncodeWidth = mainWidth - encodeWidth;
            int mainEncodeHeight = mainHeight - encodeHeight;
            if(x < 0){
                x = mainEncodeWidth / 2;
            }else if(x > mainEncodeWidth){
                x = mainEncodeWidth;
            }
            if(y < 0){
                y = mainEncodeHeight / 2;
            }else if(y > mainEncodeHeight){
                y = mainEncodeHeight;
            }
            mainGraphics.drawImage(encodeImage, x, y, encodeWidth, encodeHeight, null); // 水印图片结束
            mainGraphics.dispose();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageOutputStream imageOutput = ImageIO.createImageOutputStream(byteArrayOutputStream);
            ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, imageOutput);
            is = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            int b;
            while((b = is.read())!= -1)
            {
                out.write(b);
            }
            System.out.println("添加水印图片完成!");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            is.close();
            out.close();
        }
    }

    /**
     * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
     * @param text
     * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
     */
    public static int getLength(String text) {
        int textLength = text.length();
        int length = textLength;
        for (int i = 0; i < textLength; i++) {
            if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
                length++;
            }
        }
        return (length % 2 == 0) ? length / 2 : length / 2 + 1;
    }

    /**
     * 解析条形码
     * @param imgPath
     * @return
     */
    public String decode(String imgPath) {
        BufferedImage image = null;
        Result result = null;
        try {
            image = ImageIO.read(new File(imgPath));
            if (image == null) {
                System.out.println("the decode image may be not exit.");
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            result = new MultiFormatReader().decode(bitmap, null);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}