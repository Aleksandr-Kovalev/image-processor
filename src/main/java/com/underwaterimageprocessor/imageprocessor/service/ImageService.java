package com.underwaterimageprocessor.imageprocessor.service;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import com.underwaterimageprocessor.imageprocessor.util.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.format.DateTimeFormatter;


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

    public BufferedImage autoWhiteBalanceAdjust(BufferedImage image) throws IOException{

        int width = image.getWidth();
        int height = image.getHeight();
        int[] histogramR = new int[256];
        int[] histogramG = new int[256];
        int[] histogramB = new int[256];

        // Compute histograms for each channel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                histogramR[r]++;
                histogramG[g]++;
                histogramB[b]++;
            }
        }

        // Find the maximum value in each channel
        int maxR = ImageUtils.findMax(histogramR);
        int maxG = ImageUtils.findMax(histogramG);
        int maxB = ImageUtils.findMax(histogramB);

        // Find the maximum intensity among all channels
        int maxIntensity = Math.max(Math.max(maxR, maxG), maxB);

        // Adjust each pixel in the image
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = image.getRGB(x, y);
                int r = (int) (((rgb >> 16) & 0xFF) * (255.0f / maxIntensity));
                int g = (int) (((rgb >> 8) & 0xFF) * (255.0f / maxIntensity));
                int b = (int) ((rgb & 0xFF) * (255.0f / maxIntensity));
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));
                int newRGB = (r << 16) | (g << 8) | b;
                result.setRGB(x, y, newRGB);
            }
        }

        return result;

    }

}
