package com.nuanyou.cms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {

    private static final Logger log = LoggerFactory.getLogger(IOUtils.class.getSimpleName());

    public static void close(Closeable a) {
        if (a != null)
            try {
                a.close();
            } catch (IOException e) {
                log.warn("Closeable Error", e);
            }
    }
}