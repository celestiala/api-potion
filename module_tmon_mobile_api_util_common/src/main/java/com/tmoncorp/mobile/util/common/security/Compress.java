package com.tmoncorp.mobile.util.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class Compress {
    private static final Logger LOG = LoggerFactory.getLogger(Compress.class);

    private Compress() {
        throw new AssertionError("static utility class");
    }

    public static byte[] toGzipByte(byte[] data) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);

        try {
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.close();
            byte[] compressed = bos.toByteArray();
            bos.close();
            return compressed;

        } catch (IOException e) {
            LOG.error("GZip compress failed : {}", e.getMessage());
        }
        return null;
    }
}
