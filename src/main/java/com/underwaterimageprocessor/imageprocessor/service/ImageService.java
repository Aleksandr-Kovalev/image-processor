package com.underwaterimageprocessor.imageprocessor.service;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import com.underwaterimageprocessor.imageprocessor.util.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class ImageService {

    //TODO edits using BufferedImage and Graphics2D, openimaj

    public String uploadImage(MultipartFile file, String author, String fileType) throws IOException{

        System.out.println("ImageService - uploadImage - Enter");
        OrgImage orgImage =  new OrgImage();
        boolean imageSaved;
        BufferedImage image = ImageIO.read(file.getInputStream());
        byte[] byteImg = ImageUtils.toByteArray(image, fileType);

        orgImage.setImageData(byteImg);
        orgImage.setAuthor(author);
        orgImage.setRequestDate(java.time.ZonedDateTime.now()
                                    .format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss")));
        orgImage.setFileType(fileType);

        imageSaved = ImageUtils.saveOrgImage(orgImage);

        if(imageSaved)
            return "Image uploaded successfully.";
        else
            return "Image uploaded failed.";

    }

    public BufferedImage whiteBalanceAdjust(MultipartFile file) throws IOException{

    //TODO
        return null;

    }

}
