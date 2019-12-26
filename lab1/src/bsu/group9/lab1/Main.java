package bsu.group9.lab1;


import java.util.Scanner;
import java.util.Arrays;

public class Main
{

    public static void main(String[] args)
    {
        Scanner inn = new Scanner(System.in);
        System.out.print("Input a number: ");
        int x = inn.nextInt();
        Tea[] breakfast = new Tea[x];

        for (int i = 0; i < x; i++) {
            if (i % 3 == 0) {
                breakfast[i] = new Tea("чёрный");
            }
            else if (i% 2 == 0){
                breakfast[i] = new Tea("зелёный");
            }
            else
                breakfast[i] = new Tea("фруктовый");

        }
        int b_num = 0;
        for (int i=0; i < breakfast.length; i++)
        {
            if (breakfast[i].getColor().equals("чёрный")) b_num++;
        }
        System.out.println("В завтраке " + b_num + " чёрного чая и " + (breakfast.length-b_num) + " зелёного чая");
        for (int i=0; i < breakfast.length; i++)
        {
            breakfast[i].consume();
        }
        Scanner in = new Scanner(System.in);
        ; for (;;)
    {
        System.out.println("Доступны параметры:\n-calories\n-sort\n-exit");
        String parameter = in.nextLine();
        if (parameter.equals("-calories"))
        {
            int sum=0;
            for (int i=0; i < breakfast.length; i++)
            {
                sum+=breakfast[i].calculateCalories();
            }
            System.out.println("В завтраке содержится " + sum + " калорий");
        }
        else if (parameter.equals("-sort"))
        {
            Arrays.sort(breakfast, new TeaComparator() );
            for (int i=0; i < breakfast.length; i++)
            {
                System.out.println(breakfast[i].toString());
            }
            System.out.println("Завтрак отсортирован!");
        }
        else if (parameter.equals("-exit"))
        {
            System.out.println("Программа завершена!");
            break;
        }
        else System.out.println("Неизвестный параметр!");
    }
    }
}