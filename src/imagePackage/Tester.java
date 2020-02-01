package imagePackage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Tester
{
    double [] precision, accuracy, recall, fScore;

    String outputPath = "output/out";

    File f, f1, maskFile;
    BufferedImage image, image1, mask;

    double threshold = 0.25;

    double [][][] ratio;

    int white = new Color(255,255,255).getRGB();
    int black = new Color(0,0,0).getRGB();

    public double average(double [] ar)
    {
        double sum = 0;

        for(int i =0;i<ar.length;i++)
        {
            sum+=ar[i];
        }

        return  sum/ar.length;
    }

    public void test()
    {
        precision = new double[Trainer.folds];
        accuracy = new double[Trainer.folds];
        recall = new double[Trainer.folds];
        fScore = new double[Trainer.folds];

        try{
            for(int z=0;z<Trainer.folds;z++)
            {
                double tn = 0, tp = 0, fn =0, fp = 0, total = 0;

                Trainer t = new Trainer();

                FileInputStream fin = new FileInputStream("training"+z+".txt");
                ObjectInputStream istream = new ObjectInputStream(fin);

                ratio = (double[][][])istream.readObject();

                for(int k=0;k<Trainer.totalData;k++)
                {
                    if(Trainer.isTestData[z][k])
                    {
                        f = new File(t.filePath+t.getNumber(k)+".jpg");
                        f1 = new File(outputPath+z+"/"+t.getNumber(k)+".bmp");
                        maskFile = new File(t.maskFilePath+t.getNumber(k)+".bmp");
                        image = ImageIO.read(f);
                        mask = ImageIO.read(maskFile);
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
                                    //image1.setRGB(i,j,white);
                                }

                                else
                                {
                                    image1.setRGB(i,j,white);
                                }

                                if(mask.getRGB(i,j)==white && mask.getRGB(i,j)==image1.getRGB(i,j))
                                {
                                    tn++;
                                }

                                else if(mask.getRGB(i,j)==white && mask.getRGB(i,j)!=image1.getRGB(i,j))
                                {
                                    fp++;
                                }

                                else if(mask.getRGB(i,j)!=white && mask.getRGB(i,j)==image1.getRGB(i,j))
                                {
                                    tp++;
                                }

                                else if(mask.getRGB(i,j)!=white && mask.getRGB(i,j)!=image1.getRGB(i,j))
                                {
                                    fn++;
                                }

                            }
                        }

                        ImageIO.write(image1,"bmp",f1);
                    }
                }

                total = tp+tn+fp+fn;

                accuracy[z] = (tp+tn)/total;
                precision[z] = tp/(tp+fp);
                recall[z] = tp/(tp+fn);
                fScore[z] = 2*precision[z]*recall[z]/(precision[z]+recall[z]);
            }

            System.out.println("Accuracy: "+average(accuracy));
            System.out.println("F-Score: "+average(fScore));

        }catch(IOException e)
        {
            e.printStackTrace();
        }catch (ClassNotFoundException e1)
        {
            e1.printStackTrace();
        }
    }
}
