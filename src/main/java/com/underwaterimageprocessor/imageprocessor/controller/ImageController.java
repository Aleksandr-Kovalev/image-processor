package com.underwaterimageprocessor.imageprocessor.controller;

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

import java.util.Map;

import org.apache.commons.codec.binary.Base64;

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
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {

        System.out.println("ImageController - uploadImage - Enter");
        BufferedImage editedImage = imageService.uploadImage(file);

        byte[] byteImg = ImageUtils.toByteArray(editedImage, "jpg");
        String encodedImg = Base64.encodeBase64String(byteImg);

        return ResponseEntity.status(HttpStatus.OK).body(encodedImg);
    }


}
