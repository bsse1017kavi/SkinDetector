package imagePackage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Trainer
{
    double[][][] skin = new double[256][256][256];
    double[][][] nonskin = new double[256][256][256];
    double[][][] ratio = new double[256][256][256];

    String filePath = "ibtd/";
    String maskFilePath = "ibtd/Mask/";

    File f = null;
    File f1 = null;
    BufferedImage image = null;
    BufferedImage mask = null;
    int width;
    int height;

    int sum = 0, nSum = 0;

    public void initialize()
    {
        for(int i=0;i<256;i++)
        {
            for(int j=0;j<256;j++)
            {
                for(int k=0;k<256;k++)
                {
                    skin[i][j][k] = 1;
                    nonskin[i][j][k] = 1;
                }
            }
        }
    }


    public String getNumber(int currentPic)
    {
        if(currentPic<10)
        {
            return "000"+currentPic;
        }

        else if(currentPic>=10 && currentPic<100)
        {
            return "00"+currentPic;
        }

        return "0"+currentPic;
    }

    public void train()
    {
        try
        {
            for(int k=0;k<444;k++)
            {
                f = new File(filePath+getNumber(k)+".jpg");
                f1 = new File(maskFilePath+getNumber(k)+".bmp");
                image = ImageIO.read(f);
                mask = ImageIO.read(f1);

                int width = image.getWidth();
                int height = image.getHeight();

                for(int i=0;i<width;i++)
                {
                    for(int j=0;j<height;j++)
                    {
                        Color c = new Color(image.getRGB(i,j));
                        Color c1 = new Color(mask.getRGB(i,j));

                        int r = c.getRed();
                        int g = c.getGreen();
                        int b = c.getBlue();

                        int r1 = c1.getRed();
                        int g1 = c1.getGreen();
                        int b1 = c1.getBlue();

                        if(r1==255 && g1==255 && b1==255)
                        {
                            nonskin[r][g][b]++;
                        }

                        else skin[r][g][b]++;

                        //System.out.println(r+ " " + g + " " + b);
                    }
                }
            }

            for(int i=0;i<256;i++)
            {
                for(int j=0;j<256;j++)
                {
                    for(int k=0;k<256;k++)
                    {
                        sum+=skin[i][j][k];
                        nSum+=nonskin[i][j][k];
                    }
                }
            }

            for(int i=0;i<256;i++)
            {
                for(int j=0;j<256;j++)
                {
                    for(int k=0;k<256;k++)
                    {
                        skin[i][j][k]/=sum;
                        nonskin[i][j][k]/=nSum;
                        //System.out.println(skin[i][j][k]);
                    }
                }
            }

            for(int i=0;i<256;i++)
            {
                for(int j=0;j<256;j++)
                {
                    for(int k=0;k<256;k++)
                    {
                        ratio[i][j][k] = skin[i][j][k]/nonskin[i][j][k];
                        //System.out.println(ratio[i][j][k]);
                    }
                }
            }

            FileOutputStream fout = new FileOutputStream("training.txt");
            ObjectOutputStream ostream = new ObjectOutputStream(fout);

            ostream.writeObject(ratio);

        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
