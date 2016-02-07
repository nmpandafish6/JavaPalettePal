/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package master;

import gui.JColorBox;
import imagereader.ImageReader;
import static imagereader.ImageReader.getAverageHSV;
import static imagereader.ImageReader.getAverageHSV_filtered;
import static imagereader.ImageReader.imageToHistogram;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import util.ArrayUtil;

/**
 *
 * @author Nicolas
 */
public class Master {
    
    public static void main(String[] args){
        String urlString = "https://lh6.googleusercontent.com/-6kTwSQZ4PnM/UrEbgwZR0JI/AAAAAAAAfnw/7J-VX5q1H4o/s240/garibaldithumb.jpg";
        BufferedImage image = null;
        try {
            image = (BufferedImage) ImageIO.read(new URL(urlString));
        } catch (IOException ex) {
            Logger.getLogger(ImageReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        int bins = 60;
        int[] histogram = imageToHistogram(image, bins);
        int maxIndex = ArrayUtil.getMaxIndex(histogram);
        float[] hsv = getAverageHSV_filtered(image, maxIndex, bins);
        
        PalettePalGui palettePal = new PalettePalGui("Palette Pal");
        
        JColorBox colorBox0 = new JColorBox(Color.RED);
        JColorBox colorBox1 = new JColorBox(Color.ORANGE);
        JColorBox colorBox2 = new JColorBox(Color.YELLOW);
        JColorBox colorBox3 = new JColorBox(Color.GREEN);
        JColorBox colorBox4 = new JColorBox(Color.CYAN);
        JColorBox colorBox5 = new JColorBox(Color.BLUE);
        JColorBox colorBox6 = new JColorBox(Color.MAGENTA);
        JColorBox colorBox7 = new JColorBox(Color.PINK);
        
        
        palettePal.pack();
    }
}
