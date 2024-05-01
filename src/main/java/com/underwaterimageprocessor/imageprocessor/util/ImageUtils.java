package com.underwaterimageprocessor.imageprocessor.util;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;


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

    public static boolean saveOrgImage(OrgImage image) throws IOException{

        System.out.println("ImageUtils - saveOrgImage - Enter");
        System.out.println("timestamp: " + image.getRequestDate());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("author", image.getAuthor());
        jsonObject.put("date", image.getRequestDate());
        jsonObject.put("fileType", image.getFileType());
        jsonObject.put("image", (String) Base64.encodeBase64String(image.getImageData()));
        FileWriter file = null;

        try{
            file = new FileWriter("D:/Sandbox/image-processor/OrgImages/"
                                    + image.getAuthor() + ".json");
            file.write(jsonObject.toJSONString());
        } catch ( IOException e) {
            System.out.println("File creation failed.");
            System.out.println(e.getMessage());
            return false; //image upload failed
        } finally {
            file.close();
        }

        return true; //image uploaded successfully

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
            orgImage.setAuthor((String) jsonObj.get("author"));
            orgImage.setRequestDate((String) jsonObj.get("date"));
            orgImage.setFileType((String) jsonObj.get("fileType"));

            String base64String = (String) jsonObj.get("image");
            byte[] decodedImg =  Base64.decodeBase64(base64String);

            orgImage.setImageData(decodedImg);

        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.out.println(e.getMessage());
            return new OrgImage();
        }

        return orgImage;
    }


}
