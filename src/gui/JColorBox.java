/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Nicolas
 */
public class JColorBox extends JComponent{
    
    public int width = 128;
    public int height = 128;
    public Color color = null;
    public JColorBox(Color color){
        this.color = color;
        this.setPreferredSize(new Dimension(width, height));
        this.setVisible(true);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
    }
    
    @Override
    public void paintComponent(Graphics g){
        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }
}
