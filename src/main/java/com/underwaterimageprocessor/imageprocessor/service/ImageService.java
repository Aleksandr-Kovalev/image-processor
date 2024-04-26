package com.underwaterimageprocessor.imageprocessor.service;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

@Service
public class ImageService {

    //TODO edits using BufferedImage and Graphics2D, openimaj

    public BufferedImage uploadImage(MultipartFile file) throws IOException{

        System.out.println("ImageService - uploadImage - Enter");
        OrgImage orgImage =  new OrgImage();
        BufferedImage image = ImageIO.read(file.getInputStream());

        return image;
    }

    public BufferedImage whiteBalanceAdjust(MultipartFile file) throws IOException{

    //TODO
        return null;

    }

}
