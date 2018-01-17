/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package day03;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author B Ricks, PhD <bricks@unomaha.edu>
 */
public class Day03 {

    public static void main(String[] args) throws IOException {
        
        int width = 640;
        int height = 480;
       
        //BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        BufferedImage bi = ImageIO.read(new File("beach.jpeg"));
        
        
        width = bi.getWidth();
        height = bi.getHeight();
        
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                Color pixel = new Color(bi.getRGB(x, y));
                
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();
                
                //Prevent an exception by keeping values within [0,255]
                r = clamp255(r);
                g = clamp255(g);
                b = clamp255(b);
                
                Color newColor = new Color(r, g, b);
                
                //Now work in the hsv domain
                float[] hsbvals = new float[3];
                Color.RGBtoHSB(r, g, b, hsbvals);
                
                float h = hsbvals[0];
                float s = hsbvals[1];
                float v = hsbvals[2];
                
                //Prevent problems by clamping our s and v values
                //h shouldn't be clamped, it's on a circle
                s = clamp1(s);
                v = clamp1(v);                
                
                //newColor = Color.getHSBColor(h, s, v);
                                
                out.setRGB(x, y, newColor.getRGB());
                
            }
        }        
        ImageIO.write(out, "png", new File("out.png"));       
    }    
    
    public static int clamp255(int i)
    {
        if(i < 0)
            return 0;
        if( i >= 255)
            return 255;
        return i;
    }
    
    public static float clamp1(float f)
    {
        if(f <= 0)
            return 0;
        if( f >= 1)
            return 1;
        return f;
    }
}
