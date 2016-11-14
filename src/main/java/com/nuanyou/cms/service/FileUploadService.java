package com.nuanyou.cms.service;

import java.io.InputStream;

/**
 * Created by Kevin on 2016/9/22.
 */
public interface FileUploadService {

    String commonUpdateImg(InputStream is, String fileType) throws Exception;
}
