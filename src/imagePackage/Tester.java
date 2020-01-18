package imagePackage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Tester
{
    String outputPath = "output/";

    File f, f1;
    BufferedImage image, image1;

    double threshold = 0.25;

    double [][][] ratio;

    int white = new Color(255,255,255).getRGB();
    int black = new Color(0,0,0).getRGB();

    public void test()
    {
        try{
            Trainer t = new Trainer();

            FileInputStream fin = new FileInputStream("training.txt");
            ObjectInputStream istream = new ObjectInputStream(fin);

            ratio = (double[][][])istream.readObject();

            for(int k=445;k<555;k++)
            {
                f = new File(t.filePath+t.getNumber(k)+".jpg");
                f1 = new File(outputPath+t.getNumber(k)+".bmp");
                image = ImageIO.read(f);
                image1 = image;

                for(int i=0;i<image.getWidth();i++)
                {
                    for(int j=0;j<image.getHeight();j++)
                    {
                        Color c = new Color(image.getRGB(i,j));

                        int r = c.getRed();
                        int g = c.getGreen();
                        int b = c.getBlue();

                        if(ratio[r][g][b]>threshold)
                        {
                            image1.setRGB(i,j,white);
                        }

                        else
                        {
                            image1.setRGB(i,j,black);
                        }
                    }
                }

                ImageIO.write(image1,"bmp",f1);
            }

        }catch(IOException e)
        {
            e.printStackTrace();
        }catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
        }
    }
}
