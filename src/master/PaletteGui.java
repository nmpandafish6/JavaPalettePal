/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package master;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;

/**
 *
 * @author Nicolas
 */
public class PaletteGui extends JFrame{
    
    public PaletteGui(String name){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(550, 310));
        this.pack();
        this.setVisible(true);
    }
}
