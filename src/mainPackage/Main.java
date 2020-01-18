package mainPackage;

import imagePackage.Tester;
import imagePackage.Trainer;

public class Main
{
    public static void main(String[] args)
    {
        Trainer trainer = new Trainer();
        trainer.initialize();
        trainer.train();

        Tester tester = new Tester();
        tester.test();
    }
}
