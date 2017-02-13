package com.nuanyou.cms.controller;

import com.nuanyou.cms.config.ImageSpec;
import com.nuanyou.cms.service.FileUploadService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@Controller
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @RequestMapping(value = "upload",
            consumes = {"multipart/form-data"},
            method = RequestMethod.POST)
    public void commonUpdateImg(@RequestParam("uploadFile") MultipartFile file,
                                @RequestParam(required = false) ImageSpec imageSpec,
                                String callbackId, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");

        String fileType = "";
        InputStream is;

        if (imageSpec != null) {
            fileType = imageSpec.format;
            Thumbnails.Builder builder = Thumbnails.of(file.getInputStream());

            if (ImageSpec.CLIP.equals(imageSpec.process)) {
                builder = builder.sourceRegion(Positions.CENTER, imageSpec.width, imageSpec.height)
                        .size(imageSpec.width, imageSpec.height);

            } else if (ImageSpec.ZOMM.equals(imageSpec.process)) {
                BufferedImage img = builder.size(imageSpec.width, imageSpec.height).keepAspectRatio(true).asBufferedImage();
                builder = Thumbnails.of(getImage(imageSpec.width, imageSpec.height, Color.WHITE))
                        .watermark(img, 1).size(imageSpec.width, imageSpec.height);

            } else {
                BufferedImage img = ImageIO.read(file.getInputStream());

                int result = Math.abs(imageSpec.width - img.getWidth());
                if (result > Math.round(img.getWidth() / 100)) {
                    response.getWriter().println("<script>parent.callbackImg('" + "不符合指定尺寸" + "')</script>");
                    return;
                }
                result = Math.abs(imageSpec.height - img.getHeight());
                if (result > Math.round(img.getHeight() / 100)) {
                    response.getWriter().println("<script>parent.callbackImg('" + "不符合指定尺寸" + "')</script>");
                    return;
                }
                builder = builder.size(img.getWidth(), img.getHeight());
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

            is = new ByteArrayInputStream(bos.toByteArray());

        } else {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename.contains("."))
                fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            is = file.getInputStream();
        }


        String callBackImgUrl = fileUploadService.commonUpdateImg(is, fileType);
//        String callBackImgUrl = writeDisk(is, imageSpec);
        response.getWriter().println("<script>parent.callbackImg('" + callbackId + "', '" + callBackImgUrl + "')</script>");
    }


    private static String writeDisk(InputStream is, ImageSpec imageSpec) throws Exception {
        String pathname = "/Users/alan/Downloads/" + imageSpec + "." + imageSpec.format;
        FileOutputStream fos = new FileOutputStream(new File(pathname));
        InputStream bis = new BufferedInputStream(is);
        byte[] buff = new byte[512];
        while (bis.read(buff) != -1)
            fos.write(buff);
        fos.close();
        is.close();
        return pathname;
    }


    @RequestMapping(value = "upload/editor",
            consumes = {"multipart/form-data"},
            method = RequestMethod.POST)
    public void editorImgUpload(@RequestParam("imgFile") MultipartFile file, String callbackId, HttpServletResponse response) throws Exception {
        String fileType = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename.contains(".")) {
            fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        String callBackImgUrl = fileUploadService.commonUpdateImg(file.getInputStream(), fileType);
        response.getWriter().println("<script>parent.callback('" + callbackId + "', '" + callBackImgUrl + "')</script>");
    }


    private static BufferedImage getImage(int width, int height, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        return image;
    }

}