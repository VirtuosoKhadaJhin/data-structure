package com.nuanyou.cms.controller;

import com.nuanyou.cms.service.FileUploadService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FileUploadService fileUploadService;

    @RequestMapping(value = "upload",
            consumes = {"multipart/form-data"},
            method = RequestMethod.POST)
    public void commonUpdateImg(@RequestParam("uploadFile") MultipartFile file,
                                @RequestParam(required = false) Integer width,
                                @RequestParam(required = false) Integer height,
                                String callbackId, HttpServletResponse response) throws Exception {

        InputStream is;
        if (width != null && height != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .sourceRegion(Positions.CENTER, width, height)
                    .size(width, height)
                    .outputFormat("jpg")
                    .toOutputStream(bos);
            is = new ByteArrayInputStream(bos.toByteArray());
        } else {
            is = file.getInputStream();
        }

        String fileType = "";
        String originalFilename = file.getOriginalFilename();
        if (originalFilename.contains(".")) {
            fileType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        String callBackImgUrl = fileUploadService.commonUpdateImg(is, fileType);
        response.getWriter().println("<script>parent.callback('" + callbackId + "', '" + callBackImgUrl + "')</script>");
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

}