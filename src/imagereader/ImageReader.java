/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package imagereader;

import java.awt.Color;
import java.awt.image.BufferedImage;
import util.MathUtil;

/**
 *
 * @author Nicolas
 */
public class ImageReader {
    
    /**
     * Converts an image to a histogram of hue values using the hsv color model.
     * @param image The image that is to be analyzed
     * @param bins The number of columns that the histogram is to have
     * @return The histogram as an array of integer with number of occurrences logged in each cell
     */
    public static int[] imageToHistogram(BufferedImage image, int bins){
        int[] colorBins = new int[bins];//Initializes histogram
        float binDivisor = 360.0f / bins;//Calculates width of columns in histogram
        //Scans image pixel by pixel
        int width = image.getWidth();
        int height = image.getHeight();
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int argb = image.getRGB(x, y);
                int r = (argb >> (8*2)) & 0xff;
                int g = (argb >> (8*1)) & 0xff;
                int b = (argb) & 0xff;
                float[] hsv = new float[3];
                Color.RGBtoHSB(r, g, b, hsv);
                int bin = (int) Math.floor(360*hsv[0] / binDivisor);
                colorBins[bin]++;
            }
        }
        return colorBins;
    }
    
    /**
     * Returns an approximation of what color is represented by the corresponding hsv values.
     * Red, orange, yellow, green, light blue, dark blue, white, and black are the possible values
     * that may be returned.
     * @param h Hue 
     * @param s Saturation
     * @param v Value
     * @return An approximation of what color is represented by the corresponding hsv values.
     */
    public static String getClosestColor(int h, int s, int v){
        //HSV Color Set
        //TODO : Replace with Color.COLOR (Color.RED, etc.) for more precise and numerous models
        int[][] colorSet = {
                            {350,   100,    92}, //red          0
                            {37,    90,     100}, //orange      1
                            {61,    100,    100},  //yellow     2
                            {109,   80,     80}, //green        3
                            {181,   53,     81},//light blue    4
                            {242,   67,     92},//dark blue     5
                            {332,   68,     61}, //purple       6
                            {315,   89,     91},//white         7
                            {233,   100,    3},  //black        8
        };
        double deltaMin = Integer.MAX_VALUE;
        int deltaMinIndex = -1;
        //x y and z components represent coordinates in the hsv model shown as a cylinder
        double xA = s * Math.cos(Math.toRadians(h));
        double yA = s * Math.sin(Math.toRadians(h));
        double zA = v;
        //finds shortest distance between two colors
        for(int c = 0; c < colorSet.length; c++){
            double xB = colorSet[c][1] * Math.cos(Math.toRadians(colorSet[c][0]));
            double yB = colorSet[c][1] * Math.sin(Math.toRadians(colorSet[c][0]));
            double zB = colorSet[c][2];
            double delta = Math.sqrt(MathUtil.sqr(xA-xB) + MathUtil.sqr(yA-yB) + MathUtil.sqr(zA-zB));
            deltaMin = delta < deltaMin ? delta : deltaMin;
            deltaMinIndex = delta < deltaMin ? c : deltaMinIndex;
        }
        //returns appropriate color as a string
        switch (deltaMinIndex) {
            case 0: return "RED";
            case 1: return "ORANGE";
            case 2: return "YELLOW";
            case 3: return "GREEN";
            case 4: return "LIGHTBLUE";
            case 5: return "DARKBLUE";
            case 6: return "PURPLE";
            case 7: return "WHITE";
            case 8: return "BLACK";
        }
        return null;
    }
    
    /**
     * Finds the average rgb in an image
     * @param image
     * @return average rgb
     */
    public static int getAverageRGB(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        int rTotal, gTotal, bTotal;
        rTotal = gTotal = bTotal = 0;
        //Adds rgb components of each pixel
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int argb = image.getRGB(x, y);
                int r = (argb >> (8*2)) & 0xff;
                int g = (argb >> (8*1)) & 0xff;
                int b = (argb) & 0xff;
                rTotal += r;
                gTotal += g;
                bTotal += b;
            }
        }
        int pixels = width * height;
        //Divides rgb totals by total number of pixels to get average
        int rAvg = rTotal / pixels;
        int gAvg = gTotal / pixels;
        int bAvg = bTotal / pixels;
        return (rAvg << (8*2)) | (gAvg << (8*1)) | (bAvg);
    }
    
    /**
     * Finds the average hsv in an image
     * @param image
     * @return average hsv
     */
    public static float[] getAverageHSV(BufferedImage image){
        int width = image.getWidth();
        int height = image.getHeight();
        float hTotal, sTotal, vTotal;
        hTotal = sTotal = vTotal = 0;
        //Adds hsv components of each pixel
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int argb = image.getRGB(x, y);
                int r = (argb >> (8*2)) & 0xff;
                int g = (argb >> (8*1)) & 0xff;
                int b = (argb) & 0xff;
                float[] hsv = new float[3];
                Color.RGBtoHSB(r, g, b, hsv);
                hTotal += hsv[0];
                sTotal += hsv[1];
                vTotal += hsv[2];
            }
        }
        int pixels = width * height;
        //Divides hsv totals by total number of pixels to get average
        float hAvg = 360*hTotal / pixels;
        float sAvg = 100*sTotal / pixels;
        float vAvg = 100*vTotal / pixels;
        return new float[]{hAvg,sAvg,vAvg};
        
    }
    
    /**
     * Finds the average hsv in an image if individual pixel values correspond to the appropriate bin
     * @param image image
     * @param bin Desired column from histogram
     * @param totalBins Total columns from histogram
     * @return average hsv
     */
    public static float[] getAverageHSV_filtered(BufferedImage image, int bin, int totalBins){
        int width = image.getWidth();
        int height = image.getHeight();
        float hTotal, sTotal, vTotal;
        hTotal = sTotal = vTotal = 0;
        float binDivisor = 360.0f / totalBins;//width of bins
        int pixels = 0;//number of pixels that are in bin
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int argb = image.getRGB(x, y);
                int r = (argb >> (8*2)) & 0xff;
                int g = (argb >> (8*1)) & 0xff;
                int b = (argb) & 0xff;
                float[] hsv = new float[3];
                Color.RGBtoHSB(r, g, b, hsv);
                int binIndex = (int) Math.floor(360*hsv[0] / binDivisor);
                if(binIndex == bin){
                    pixels++;
                    hTotal += hsv[0];
                    sTotal += hsv[1];
                    vTotal += hsv[2];
                }
            }
        }
        float hAvg = 360*hTotal / pixels;
        float sAvg = 100*sTotal / pixels;
        float vAvg = 100*vTotal / pixels;
        return new float[]{hAvg,sAvg,vAvg};
    }
    
    
}
