package com.underwaterimageprocessor.imageprocessor.util;

import org.apache.commons.codec.binary.Base64;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


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


}
