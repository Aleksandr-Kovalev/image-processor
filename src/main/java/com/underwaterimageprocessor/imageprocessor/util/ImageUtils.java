package com.underwaterimageprocessor.imageprocessor.util;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;
import static java.lang.Math.*;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


public class ImageUtils {

    public static byte[] toByteArray(BufferedImage image, String format) throws IOException {

        System.out.println("ImageUtils - toByteArray - Enter ");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        byte[] byteStream = baos.toByteArray();
        return byteStream;

    }

    public static BufferedImage toBufferedImage(byte[] byteStream) throws IOException{

        System.out.println("ImageUtils - toBufferedImage - Enter ");

        InputStream is = new ByteArrayInputStream(byteStream);
        BufferedImage image = ImageIO.read(is);
        return image;
    }

    public static String saveOrgImageLocal(byte[] imageBytes, String id, String dir) throws IOException{

        System.out.println("ImageUtils - saveOrgImage - Enter");
        String path = dir + id + ".txt";
        FileUtils.writeByteArrayToFile(new File(path), imageBytes);

        return path;

    }

    public static OrgImage loadOrgImage(String author) throws Exception{

        System.out.println("ImageUtils - loadOrgImage - Enter");

        OrgImage orgImage = new OrgImage();
        Object obj = null;
        JSONObject jsonObj = null;

        try{
            obj = new JSONParser().parse(new FileReader("D:/Sandbox/image-processor/OrgImages/"
                                                                            + author + ".json"));
            jsonObj = (JSONObject) obj;
            orgImage.setAuthorId((String) jsonObj.get("author"));
            orgImage.setUploadDate((String) jsonObj.get("date"));
            orgImage.setFileType((String) jsonObj.get("fileType"));

            String base64String = (String) jsonObj.get("image");
            byte[] decodedImg =  Base64.decodeBase64(base64String);

            orgImage.setImage(decodedImg);

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.out.println(e.getMessage());
            return new OrgImage();
        }

        return orgImage;
    }

    /*
    White balance code from Monte Media Library
    https://www.randelshofer.ch/monte/license.html
     */
    public static BufferedImage toIntImage(BufferedImage img) {
        if (img.getRaster().getDataBuffer() instanceof DataBufferInt) {
            return img;
        } else {
            BufferedImage intImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = intImg.createGraphics();
            g.drawImage(img, 0, 0, null);
            g.dispose();
            return intImg;
        }
    }

    public static void RGBtoYCC(float[] rgb, float[] ycc) {
        float R = max(0f, min(1f, rgb[0]));
        float G = max(0f, min(1f, rgb[1]));
        float B = max(0f, min(1f, rgb[2]));
        float Y = 0.3f * R + 0.6f * G + 0.1f * B;
        float V = R - Y;
        float U = B - Y;
        float Cb = (U / 2f) /*+ 0.5f*/;
        float Cr = (V / 1.6f) /*+ 0.5f*/;
        ycc[0] = Y;
        ycc[1] = Cb;
        ycc[2] = Cr;
    }

}
