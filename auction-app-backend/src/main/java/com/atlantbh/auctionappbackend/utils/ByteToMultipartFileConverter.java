package com.atlantbh.auctionappbackend.utils;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@AllArgsConstructor
@Component
public class ByteToMultipartFileConverter implements MultipartFile {

    private final byte[] imgContent;

    private final String imgUrl;

    private static final Logger log = LoggerFactory.getLogger(ByteToMultipartFileConverter.class);

    @Override
    public String getName() {
        return imgUrl;
    }

    @Override
    public String getOriginalFilename() {
        String[] urlParts = imgUrl.split("/");
        String originalFilename = urlParts[urlParts.length - 1];
        int slashIndex = originalFilename.lastIndexOf('/');
        if (slashIndex != -1) {
            originalFilename = originalFilename.substring(slashIndex + 1);
        }
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public Resource getResource() {
        return MultipartFile.super.getResource();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(imgContent);
        } catch (FileNotFoundException | IllegalArgumentException ex) {
            log.error("Exception in transferTo method: ", ex);
            throw new FileNotFoundException();
        }
    }
}
