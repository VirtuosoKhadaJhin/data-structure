package com.nuanyou.cms.component;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

public abstract class FileClient {

    abstract String uploadFile(String filePath, InputStream is) throws IOException;

    public String uploadFile(String filePath, File file) throws IOException {
        return uploadFile(filePath, new FileInputStream(file));
    }

    public String uploadFile(InputStream is, String suffix) throws IOException {
        return uploadFile(buildPath(suffix), is);
    }

    public String uploadFile(File file, String suffix) throws IOException {
        return uploadFile(buildPath(suffix), file);
    }

    public InputStream queryFile(String path) throws IOException {
        return queryFile(path);
    }

    public static String buildPath(String suffix) {
        StringBuilder filePath = new StringBuilder();
        filePath.append(System.currentTimeMillis());
        filePath.append(String.valueOf(Math.abs(ThreadLocalRandom.current().nextLong())).substring(0, 7));

        if (!suffix.startsWith("."))
            filePath.append(".");
        filePath.append(suffix);
        return filePath.toString();
    }


}