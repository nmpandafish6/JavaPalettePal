/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package master;

import gui.JColorBox;
import imagereader.ImageReader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import util.ArrayUtil;

/**
 *
 * @author Nicolas
 */
public class PalettePalGui extends JFrame{
    
    public PalettePalGui(String name){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout());
                
        JLabel dragAndDropText = new JLabel("Drag and Drop");
        dragAndDropText.setPreferredSize(new Dimension(150,300));
        dragAndDropText.setVerticalAlignment(JLabel.CENTER);
        dragAndDropText.setHorizontalAlignment(JLabel.CENTER);
        dragAndDropText.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    BufferedImage image = null;
                    String path = null;
                    if(evt.getTransferable().isDataFlavorSupported(new DataFlavor("application/x-java-url; class=java.net.URL"))){
                        URL url = (URL) evt.getTransferable().getTransferData(new DataFlavor("application/x-java-url; class=java.net.URL"));
                        path = url.toString();
                        image = ImageIO.read(url);
                    }else if(evt.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)){
                        List<File> droppedFiles = (List<File>)
                            evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if(droppedFiles.size() == 1){
                            path = droppedFiles.get(0).getAbsolutePath();
                            image = ImageIO.read(droppedFiles.get(0));
                        }
                    }else if(evt.getTransferable().isDataFlavorSupported(DataFlavor.imageFlavor)){
                        image = (BufferedImage) evt.getTransferable().getTransferData(DataFlavor.imageFlavor);
                        path = "Image";
                    }
                    if(image != null){
                        int[] histogram = ImageReader.imageToHistogram(image, Preferences.numberOfBins);
                        int[] sortedHistogram = ArrayUtil.looseSort(histogram);
                        PaletteGui paletteGui = new PaletteGui(path);
                        float[] hsvTotal = ImageReader.getAverageHSV(image);
                        for(int i = 0; i < Preferences.numberOfColors; i++){
                            float[] hsv = ImageReader.getAverageHSV_filtered(image, sortedHistogram[sortedHistogram.length-1-i], Preferences.numberOfBins);
                            Color color = Color.getHSBColor(hsv[0]/360.0f, hsv[1]/100f, hsv[2]/100f);
                            paletteGui.add(new JColorBox(color));
                            
                        }
                        paletteGui.pack();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                
            }
        });
        JLabel fileSelect = new JLabel("Select File");
        fileSelect.setPreferredSize(new Dimension(150,300));
        fileSelect.setVerticalAlignment(JLabel.CENTER);
        fileSelect.setHorizontalAlignment(JLabel.CENTER);
        JLabel captureScreen = new JLabel("Screen Capture");
        captureScreen.setPreferredSize(new Dimension(150,300));
        captureScreen.setVerticalAlignment(JLabel.CENTER);
        captureScreen.setHorizontalAlignment(JLabel.CENTER);
        JLabel partialCaptureScreen = new JLabel("Partial Screen Capture");
        partialCaptureScreen.setPreferredSize(new Dimension(150,300));
        partialCaptureScreen.setVerticalAlignment(JLabel.CENTER);
        partialCaptureScreen.setHorizontalAlignment(JLabel.CENTER);
        JLabel colorChooser = new JLabel("Get Color From Screen");
        colorChooser.setPreferredSize(new Dimension(150,300));
        colorChooser.setVerticalAlignment(JLabel.CENTER);
        colorChooser.setHorizontalAlignment(JLabel.CENTER);
        
        JSplitPane splitpane0 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dragAndDropText, fileSelect);
        JSplitPane splitpane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitpane0, captureScreen);
        JSplitPane splitpane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitpane1, partialCaptureScreen);
        JSplitPane splitpane3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitpane2, colorChooser);
        fileSelect.setEnabled(false);
        captureScreen.setEnabled(false);
        partialCaptureScreen.setEnabled(false);
        colorChooser.setEnabled(false);
        
        this.add(splitpane3);
        this.pack();
        this.setVisible(true);
    }
}
