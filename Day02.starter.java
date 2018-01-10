import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author B Ricks, PhD <bricks@unomaha.edu>
 */
public class ImageIOTest {

    public static void main(String[] args) throws IOException {
        
        int width = 640;
        int height = 480;
       
        //BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        BufferedImage bi = ImageIO.read(new File("out.png"));
        
        width = bi.getWidth();
        height = bi.getHeight();
        
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                bi.setRGB(x, y, Color.GREEN.getRGB());
            }
        }
        
        ImageIO.write(bi, "png", new File("out.png"));       
    }
}
