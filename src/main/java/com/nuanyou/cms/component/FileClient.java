package com.nuanyou.cms.component;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

public abstract class FileClient {

    abstract String uploadFile(String filePath, File file);

    abstract String uploadFile(String filePath, InputStream is) throws IOException;

    public String uploadFile(File file, String suffix) {
        return uploadFile(buildPath(suffix), file);
    }

    public String uploadFile(InputStream is, String suffix) throws IOException {
        return uploadFile(buildPath(suffix), is);
    }

    public static String buildPath(String suffix) {
        StringBuilder filePath = new StringBuilder();
        filePath.append(new DateTime().toString("yyyy/MM/dd/HH/mm/ss/"));
        filePath.append(Math.abs(ThreadLocalRandom.current().nextLong()));

        if (!suffix.startsWith("."))
            filePath.append(".");
        filePath.append(suffix);
        return filePath.toString();
    }

    public static String getContentType(String filePath) {
        if (filePath.endsWith("pdf"))
            return "application/pdf";
        else if (filePath.endsWith("jpg"))
            return "image/jpeg";
        else if (filePath.endsWith("png"))
            return "image/png";
        return null;
    }
}