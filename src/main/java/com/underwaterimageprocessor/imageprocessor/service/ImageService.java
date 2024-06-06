package com.underwaterimageprocessor.imageprocessor.service;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageService {

    public OrgImage uploadImage(MultipartFile file, String author, String fileType) throws IOException;

}
