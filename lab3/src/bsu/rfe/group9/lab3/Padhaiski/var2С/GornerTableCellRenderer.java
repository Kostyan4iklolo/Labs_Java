package bsu.rfe.group9.lab3.Padhaiski.var2С;

//визуализатор ячеек
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer
{
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
  //  private String needle = null;
    private Boolean ssimple = false;
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();

  //private boolean isSimple(int n)
  //{
  //    double d = n;//(double)
  //    for (int i = 2; i < n; i++)
  //    {
  //        double id = i;//(double)
  //        double t = (d/id)-(int)(d/id);
  //        if (t == 0) return false;
  //    }
  //    return true;
  //}

    public GornerTableCellRenderer()
    {
        formatter.setMaximumFractionDigits(5);// Показывать только 5 знаков после запятой
        formatter.setGroupingUsed(false);//не отделять тысячи ни запятыми, ни пробелами
        // Установить в качестве разделителя дробной части точку, а не запятую.
        DecimalFormatSymbols dottedDouble =	formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(label);// Разместить надпись внутри панели
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int col)
    {
        String formattedDouble = formatter.format(value);//Преобразовать double в строку с помощью форматировщика
        label.setText(formattedDouble);//Установить текст надписи равным строковому представлению числа
        boolean simple=false;//
        double d=Double.parseDouble(formattedDouble);
      if (ssimple)//
      {
          double y = (Math.abs(d)-Math.abs((int)(d)));//у присавивается дробная часть числа
          if ((y <= 0.1 || y >= 0.9) && y !=0.) simple = true;

      }
      // if ((col==1||col==2) && needle!=null && needle.equals(formattedDouble))//поиск значения многочлена
      // {
      //     panel.setBackground(Color.RED);
      // }
      // else
      // {
            panel.setBackground(Color.WHITE);
       // }
        if(simple)
           panel.setBackground(Color.GRAY);
        if (d > 0)
        {
            panel.setLayout(new FlowLayout(FlowLayout.RIGHT));//выравнивание надписи по правому краю панели
        }
        else if (d == 0)
        {
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));//выравнивание надписи по центру панели
        }
        else
        {
            panel.setLayout(new FlowLayout(FlowLayout.LEFT));//выравнивание надписи по левому краю панели
        }

        return panel;
    }

   //public void setNeedle(String needle)
   //{
   //    this.needle = needle;
   //}

    public void setSSimple(boolean ssimple)
    {
        this.ssimple = ssimple;
    }
}
