package com.nuanyou.cms.util;

import com.nuanyou.cms.commons.APIException;
import com.nuanyou.cms.commons.ResultCodes;
import com.nuanyou.cms.config.ImageSpec;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by alan on 17/2/28.
 */
public class ImageUtils {

    public static File process(InputStream inputStream, ImageSpec imageSpec) throws IOException {
        String fileType = imageSpec.format;

        BufferedImage sourceImg = ImageIO.read(inputStream);
        Thumbnails.Builder builder = Thumbnails.of(sourceImg);

        if (ImageSpec.CLIP.equals(imageSpec.process)) {
            builder = builder.sourceRegion(Positions.CENTER, imageSpec.width, imageSpec.height)
                    .size(imageSpec.width, imageSpec.height);

        } else if (ImageSpec.ZOMM.equals(imageSpec.process)) {
            BufferedImage img = builder.size(imageSpec.width, imageSpec.height).keepAspectRatio(true).asBufferedImage();
            builder = Thumbnails.of(getImage(imageSpec.width, imageSpec.height, Color.WHITE))
                    .watermark(img, 1).size(imageSpec.width, imageSpec.height);

        } else {
            int result = Math.abs(imageSpec.width - sourceImg.getWidth());
            if (result > Math.round(sourceImg.getWidth() / 100)) {
                throw new APIException(ResultCodes.NotConformSize);
            }
            result = Math.abs(imageSpec.height - sourceImg.getHeight());
            if (result > Math.round(sourceImg.getHeight() / 100)) {
                throw new APIException(ResultCodes.NotConformSize);
            }
            builder = builder.size(sourceImg.getWidth(), sourceImg.getHeight());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        builder.outputFormat(imageSpec.format).toOutputStream(bos);
        bos.close();

        if (bos.size() > imageSpec.dateSize) {
            builder = Thumbnails.of(new ByteArrayInputStream(bos.toByteArray()))
                    .size(imageSpec.width, imageSpec.height)
                    .outputQuality(0.6f)
                    .outputFormat(imageSpec.format);

            bos = new ByteArrayOutputStream();
            builder.toOutputStream(bos);
        }

        return new File(fileType, bos.toByteArray());
    }

    private static BufferedImage getImage(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        return image;
    }

    public static class File {
        private String fileType;
        private byte[] data;

        public File(String fileType, byte[] data) {
            this.fileType = fileType;
            this.data = data;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }
    }
}