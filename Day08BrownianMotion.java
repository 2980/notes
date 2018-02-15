/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

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
                
                
                                
                out.setRGB(x, y, newColor.getRGB());
                
            }
        }    
        
        Graphics2D g2D = (Graphics2D)out.getGraphics();
        
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        
        
        Color BrownInFront = new Color(104, 93, 50);
        Color BrownInMiddle = new Color(48, 45, 41);
        Color BrownInBack = new Color(24, 24, 20);
        
        
        drawMountainRange(g2D, BrownInBack, width, height, 300, 300, 2, 75, 75);
        drawMountainRange(g2D, BrownInMiddle, width, height, 350, 150, 4, 50, 100);
        drawMountainRange(g2D, BrownInFront, width, height, 400, 75, 8, 100, 50);
        
        //If we don't call this, nothing will be saved.
        g2D.dispose();
        
        
        
        ImageIO.write(out, "png", new File("out.png"));       
    }    

    private static void drawMountainRange(Graphics2D g2D, 
            Color Brown, 
            int width, int height, 
            int horizonY, 
            int maxMountain, 
            int iterations,
            int leftY, int rightY
    ) {
        g2D.setColor(Brown);
        
        Polygon range = new Polygon();
        
        
        //The lowest a mountain can be is at y=400
        //The highest a mountain can be is 400 - 150 = 250
        //We start at 400 - 75 = 325.
        //So I want my first point in the range 325 +- 75
        
        range.addPoint(0, height);
        
        
        int[] heights = new int[(int)Math.pow(2, iterations) - 1 + 2];
        
        //Set the extreme points
        heights[0] = horizonY - leftY;
        heights[heights.length - 1] = horizonY - rightY;
        
        
        int leftIndex = 0;
        int rightIndex = heights.length - 1;        
        int currentIndex = rightIndex/2;
        int maxChange = 75;
        
        recurseOnMountains(heights, leftIndex, rightIndex, currentIndex, maxChange, iterations - 1);;
        
        
        
        //in heights, x of [0] = 0,
        //x of [length - 1] = width
        
        for(int i = 0; i < heights.length; i++)
        {
            range.addPoint((int)(i/((float)heights.length-1) * width) ,heights[i]);
        }        
        
        range.addPoint(width, height);             
        g2D.fill(range);
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

    private static void recurseOnMountains(int[] heights, int leftIndex, int rightIndex, int currentIndex, int maxChange, int remainingSteps) {
        int average = (heights[leftIndex] + heights[rightIndex])/2;
        //Random number between -1 and 1: ((Math.random() * 2) - 1)
        heights[currentIndex] = (int)(average + ((Math.random() * 2) - 1) * maxChange);
        
        if(remainingSteps > 0)
        {
            //if left == 0 and right == 2, current == 1
            //if left == 0 and right == 4, current  == 2
            // the recurse on....0, right ==2, current == 1
            //if left == 0, current == 4
            // then recurse on 2
            // if current == 4 and right == 8, recurse on 6
            //Keep on recursing
            recurseOnMountains(heights, leftIndex, currentIndex, (leftIndex + currentIndex)/2, maxChange / 2, remainingSteps - 1);
            recurseOnMountains(heights, currentIndex, rightIndex, (currentIndex + rightIndex)/2, maxChange / 2, remainingSteps - 1);
        
        }
    }
}
