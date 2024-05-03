package com.underwaterimageprocessor.imageprocessor.controller;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import com.underwaterimageprocessor.imageprocessor.util.ImageUtils;
import com.underwaterimageprocessor.imageprocessor.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.time.StopWatch;

@RestController
@RequestMapping("/api/v1")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<String> getWelcome(){
        return new ResponseEntity<String>("Welcome",HttpStatus.OK);
    }

    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile file,
                                              @RequestParam("author") String author,
                                              @RequestParam("fileType") String fileType) throws IOException {

        System.out.println("ImageController - uploadImage - Enter");
        String imageUploadStatus = imageService.uploadImage(file, author, fileType);

        return ResponseEntity.status(HttpStatus.OK).body(imageUploadStatus);
    }

    @GetMapping("/download/orgimage/{author}")
    public ResponseEntity<?> downloadOrgImage(@PathVariable String author){

        OrgImage orgImage;

        try {
            orgImage = ImageUtils.loadOrgImage(author);
        } catch (Exception e){
            System.out.println("Failed to fetch Image");
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to fetch Iamge");
        }

        if(orgImage.getImageData() == null || orgImage.getImageData().equals(0))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image Not Found");

        String encodedImg = Base64.encodeBase64String(orgImage.getImageData());

        return ResponseEntity.status(HttpStatus.OK).body(encodedImg);
    }

    @PostMapping("/applywhitebalance")
    public ResponseEntity<?> applyAutoWhiteBalance(@RequestParam("image") MultipartFile file,
                                                   @RequestParam("fileType") String fileType,
                                                   @RequestParam("algorithm") String alg) throws IOException{
        System.out.println("ImageController - applyAutoWhiteBalance - Enter");
        StopWatch obj = new StopWatch();

        obj.start();
        BufferedImage toEdit = ImageIO.read(file.getInputStream());
        BufferedImage editedImage = imageService.whiteBalanceAlg(toEdit, alg);

        if(editedImage == null){
            return ResponseEntity.status(HttpStatus.OK).body("Error in code");
        }

        String encodedImg = Base64.encodeBase64String(ImageUtils.toByteArray(editedImage, fileType));
        obj.stop();

        System.out.println("ImageController - applyAutoWhiteBalance - Image successfully edited.");
        System.out.println("timestamp: " + java.time.ZonedDateTime.now()
                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss")));
        System.out.println("time taken to execute: " + obj.getTime() + " ms");

        return ResponseEntity.status(HttpStatus.OK).body(encodedImg);

    }


}
