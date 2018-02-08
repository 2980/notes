/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bobross;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author B Ricks, PhD <bricks@unomaha.edu>
 */
public class BobRoss {

    public static void main(String[] args) throws IOException {
        
        int width = 640;
        int height = 480;
       
       // BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        //BufferedImage bi = ImageIO.read(new File("beach.jpeg"));
        
        
        //width = bi.getWidth();
        //height = bi.getHeight();
        
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
               
                int r = (int) interpolate(0, height, 50, 200, y);
                int g = (int) interpolate(0, height, 50, 200, y);
                int b = 220;
                
                //Prevent an exception by keeping values within [0,255]
                r = clamp255(r);
                g = clamp255(g);
                b = clamp255(b);
                
                Color newColor = new Color(r, g, b);
                
                //Now work in the hsv domain
                /*float[] hsbvals = new float[3];
                Color.RGBtoHSB(r, g, b, hsbvals);
                
                float h = hsbvals[0];
                float s = hsbvals[1];
                float v = hsbvals[2];
                
                //Prevent problems by clamping our s and v values
                //h shouldn't be clamped, it's on a circle
                s = clamp1(s);
                v = clamp1(v);*/                
                
                //newColor = Color.getHSBColor(h, s, v);
                                
                out.setRGB(x, y, newColor.getRGB());
                
            }
        }    
        
        Graphics2D g2D = (Graphics2D)out.getGraphics();
        
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        //2 Philosophies about graphics objects
        /*
        
            win16 and ever after
            Any time you draw to the graphics object, you have to pass the color.
            Good for refactoring
        
            Java etc. uses the state machine approach. (Similar to OpenGL)
            JavaScript also does this
            Good for multiple calls to the same color.        
        */
        
        //Common theme between graphics libraires
        //2 parallel sets of draw calls
        //Stroke calls and fill calls
        //Stoke means hollow
        //Fill means fill
        
        Color Brown = new Color(64, 43, 16);
        
        g2D.setColor(Brown);
        
        /*
        2 Rectangle philosophies
        Approach 1 - x1, y1, x2, y2
        Approach 2 - x1, y1, wwidth, height
        */
        Polygon range = new Polygon();
        
        int horizonY = 400;
        int maxMountain = 150;
        
        range.addPoint(0, horizonY);
        range.addPoint(0, horizonY - maxMountain/2);
        
        //Draw a mountain range...
        
        /*
        Kinds of noise are distingushed by color
        White noise - TV static, radio static, Math.random()
        - Given k and K+1, there is no relationship between k and k+1
        Other kinds of noise have this property:
        - Given k, k+1, and k+ alot, k and k+1 are similar, k an k+ alot are unrelated.
        -Mountains are brown noise or Brownian motion.
        */
        
        for(int i = 0; i < 49; i++)
        {
            range.addPoint((int)interpolate(0, 48, 0, width, i), horizonY - (int) (maxMountain * Math.random()));
        }
        
        range.addPoint(width, horizonY - maxMountain/2);        
        range.addPoint(width, horizonY);
        
        
        
        
        g2D.fill(range);
        
        //If we don't call this, nothing will be saved.
        g2D.dispose();
        
        
        
        ImageIO.write(out, "png", new File("out.png"));       
    }    
    
    public static float interpolate(float minX, float maxX, float minTheta, float maxTheta, float x)
    {
        return (x - minX)/(maxX) * (maxTheta - minTheta) + minTheta;
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
