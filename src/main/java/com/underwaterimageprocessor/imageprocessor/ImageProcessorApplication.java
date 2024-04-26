package com.underwaterimageprocessor.imageprocessor;

import com.underwaterimageprocessor.imageprocessor.service.ImageService;
import nu.pattern.OpenCV;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImageProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImageProcessorApplication.class, args);
		OpenCV.loadShared();
	}

}
