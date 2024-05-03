package com.underwaterimageprocessor.imageprocessor.service;

import com.underwaterimageprocessor.imageprocessor.model.OrgImage;
import com.underwaterimageprocessor.imageprocessor.util.ImageUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.media.jai.Histogram;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static java.lang.Math.*;



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

    //Call to check the choice of white balance algorithm
    public BufferedImage whiteBalanceAlg(BufferedImage image, String alg) throws IOException {

        System.out.println("White Balance Alogrithm Chosen: " + alg);

        if(Objects.equals(alg, "MQ"))
            return whiteBalanceAdjustQM(image);
        else if(Objects.equals(alg, "Ken09"))
            return whiteBalanceAdjustKen09(image);
        else if(Objects.equals(alg, "Retinex"))
            return whiteBalanceAdjustRetinex(image);

        return null;
    }

    /*
    White balance code from Monte Media Library
    https://www.randelshofer.ch/monte/license.html

    white balance of an image using the Quadratic Mapping (QM)
    algorithm. QM is a combination of the Greyworld and the Retinex algorithm.
    And usually gives better results.
    - START -
     */
    public BufferedImage whiteBalanceAdjustQM(BufferedImage img) throws IOException {

        img = ImageUtils.toIntImage(img);
        Histogram hist = new Histogram(256, 0, 255, 3);
        hist.countPixels(img.getRaster(), null, 0, 0, 1, 1);

        double[] m = whiteBalanceQM(hist);
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        int[] p = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        int[] q = ((DataBufferInt) out.getRaster().getDataBuffer()).getData();

        float mur = (float) m[0];
        float nur = (float) m[1];
        float mub = (float) m[2];
        float nub = (float) m[3];

        System.out.println("WhiteBalance QM mur=" + mur + " nur=" + nur+" mub=" + mub + " nub=" + nub);
        for (int i = 0; i < p.length; i++) {
            int px = p[i];
            double R = (px & 0xff0000) >> 16;
            double B = (px & 0xff) >> 0;
            double Rq = mur * R * R + nur * R;
            double Bq = mub * B * B + nub * B;
            q[i] = ((min(255, max(0, (int) Rq)))) << 16
                    | (px & 0xff00)
                    | ((min(255, max(0, (int) Bq)))) << 0;
        }

        return out;
    }

    public static double[] whiteBalanceQM(Histogram rgbHist) {
        double[] max_ = rgbHist.getHighValue();
        double Rmax = max_[0],
                Gmax = max_[1],
                Bmax = max_[2];

        double R2max = Rmax * Rmax;
        double G2max = Gmax * Gmax;
        double B2max = Bmax * Bmax;

        double Rsum = 0, R2sum = 0;
        double Gsum = 0;
        double Bsum = 0, B2sum = 0;
        int[] bins = rgbHist.getBins(0);
        for (int i = 0; i < bins.length; i++) {
            Rsum += bins[i] * i;
            R2sum += bins[i] * i * i;
        }
        bins = rgbHist.getBins(1);
        for (int i = 0; i < bins.length; i++) {
            Gsum += bins[i] * i;
        }
        bins = rgbHist.getBins(2);
        for (int i = 0; i < bins.length; i++) {
            Bsum += bins[i] * i;
            B2sum += bins[i] * i * i;
        }

        double[] Rmunu = linearEquationsSolve(R2sum, Rsum, R2max, Rmax, Gsum, Gmax);
        double[] Bmunu = linearEquationsSolve(B2sum, Bsum, B2max, Bmax, Gsum, Gmax);

        double[] vector = {
                Rmunu[0], Rmunu[1], Bmunu[0], Bmunu[1]//
        };
        return vector;
    }
    // QM - END


    public static double[] linearEquationsSolve(double a, double b, double c, double d, double e, double f) {
        //System.out.println("["+a+" "+b+";"+c+" "+d+"]\\["+e+";"+f+"]");
        double x = (e * d - b * f) / (a * d - b * c);
        double y = (a * f - e * c) / (a * d - b * c);
        return new double[]{x, y};
    }

    /*
    White balance code from Monte Media Library
    https://www.randelshofer.ch/monte/license.html

    Made minor adjustments to variable names.

    Performs white balance adjustment using the "grey world" assumption as described in [Ken09] - Start
    */
    public BufferedImage whiteBalanceAdjustKen09(BufferedImage image) throws IOException{

        image = ImageUtils.toIntImage(image);
        Histogram hist = new Histogram(256, 0, 255, 3);
        hist.countPixels(image.getRaster(), null, 0, 0, 1, 1);

        double[] m = whiteBalanceKen09(hist);
        BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        int[] p = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int[] q = ((DataBufferInt) out.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < p.length; i++) {
            int px = p[i];
            double R = (px & 0xff0000) >> 16;
            double G = (px & 0xff00) >> 8;
            double B = (px & 0xff) >> 0;
            double Rq = m[0] * R + m[1] * B + m[2] * G;
            double Gq = m[3] * R + m[4] * B + m[5] * G;
            double Bq = m[6] * R + m[7] * B + m[8] * G;
            q[i] = ((min(255, max(0, (int) Rq))) & 0xff) << 16
                    | ((min(255, max(0, (int) Gq))) & 0xff) << 8
                    | ((min(255, max(0, (int) Bq))) & 0xff) << 0;
        }

        return out;

    }

    public static double[] whiteBalanceKen09(Histogram rgbHist) {
        double[] mean_ = rgbHist.getMean();
        double Rmean = mean_[0],
                Gmean = mean_[1],
                Bmean = mean_[2];
        double RGBmean = (Rmean + Gmean + Bmean) / 3;

        double[] max_ = rgbHist.getHighValue();
        double Rmax = max_[0],
                Gmax = max_[1],
                Bmax = max_[2];
        double RGBmax = max(max(Rmax, Gmax), Bmax);

        double fr = RGBmean / Rmean;
        double fg = RGBmean / Bmean;
        double fb = RGBmean / Bmean;

        if (Double.isNaN(fr)) {
            fr = 1;
        }
        if (Double.isNaN(fg)) {
            fg = 1;
        }
        if (Double.isNaN(fb)) {
            fb = 1;
        }

        double[] matrix = {//
                fr, 0, 0,//
                0, fg, 0,//
                0, 0, fb};
        return matrix;
    }
    //Performs white balance adjustment using the "grey world" assumption as described in [Ken09] - End

    //white balance of an image using the Retinex algorithm - start
    public static BufferedImage whiteBalanceAdjustRetinex(BufferedImage img) {
        img = ImageUtils.toIntImage(img);
        Histogram hist = new Histogram(256, 0, 255, 3);
        hist.countPixels(img.getRaster(), null, 0, 0, 1, 1);

        double[] m = whiteBalanceRetinex(hist);
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        int[] p = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        int[] q = ((DataBufferInt) out.getRaster().getDataBuffer()).getData();

        for (int i = 0; i < p.length; i++) {
            int px = p[i];
            double R = (px & 0xff0000) >> 16;
            double G = (px & 0xff00) >> 8;
            double B = (px & 0xff) >> 0;
            double Rq = m[0] * R + m[1] * B + m[2] * G;
            double Gq = m[3] * R + m[4] * B + m[5] * G;
            double Bq = m[6] * R + m[7] * B + m[8] * G;
            q[i] = ((max(0, (int) Rq)) & 0xff) << 16
                    | ((max(0, (int) Gq)) & 0xff) << 8
                    | ((max(0, (int) Bq)) & 0xff) << 0;
        }

        return out;
    }

    public static double[] whiteBalanceRetinex(Histogram rgbHist) {
        double[] mean_ = rgbHist.getMean();
        double Rmean = mean_[0],
                Gmean = mean_[1],
                Bmean = mean_[2];
        double RGBmean = (Rmean + Gmean + Bmean) / 3;

        double[] max_ = rgbHist.getHighValue();
        double Rmax = max_[0],
                Gmax = max_[1],
                Bmax = max_[2];
        double RGBmax = max(max(Rmax, Gmax), Bmax);

        double Rgain = Gmax / Rmax;
        double Bgain = Gmax / Bmax;

        if (Double.isNaN(Rgain)) {
            Rgain = 1;
        }
        if (Double.isNaN(Bgain)) {
            Bgain = 1;
        }

        double[] matrix = {//
                Rgain, 0, 0,//
                0, 1, 0,//
                0, 0, Bgain};
        return matrix;

    }
    //white balance of an image using the Retinex algorithm - end

    //TODO - To add, [Huo05].

}
