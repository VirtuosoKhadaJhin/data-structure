package com.nuanyou.cms.controller;

import com.nuanyou.cms.component.FileClient;
import com.nuanyou.cms.config.ImageSpec;
import com.nuanyou.cms.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class FileUploadController {

    @Autowired
    @Qualifier("s3")
    private FileClient fileClient;

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
            try {
                ImageUtils.File imgFile = ImageUtils.process(file.getInputStream(), imageSpec);
                fileType = imgFile.getFileType();
                is = new ByteArrayInputStream(imgFile.getData());
            } catch (Exception e) {
                response.getWriter().println("<script>parent.callbackImg('" + e.getMessage() + "')</script>");
                return;
            }
        } else {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename.contains("."))
                fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            is = file.getInputStream();
        }


        String callBackImgUrl = fileClient.uploadFile(is, fileType);
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


    @RequestMapping(value = "upload/editor", consumes = {"multipart/form-data"}, method = RequestMethod.POST)
    public void editorImgUpload(@RequestParam("imgFile") MultipartFile file, String callbackId, HttpServletResponse response) throws Exception {
        String fileType = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename.contains(".")) {
            fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        String callBackImgUrl = fileClient.uploadFile(file.getInputStream(), fileType);
        response.getWriter().println("<script>parent.callback('" + callbackId + "', '" + callBackImgUrl + "')</script>");
    }

}