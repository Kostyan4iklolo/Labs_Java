package bsu.rfe.group9.lab3.Padhaiski.var2С;

//variant 2C
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.io.*;
import javax.swing.*;

@SuppressWarnings("serial")


public class MainFrame extends JFrame
{
    static final int WIDTH=940;
    static final int HEIGHT=480;
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    private GornerTableModel data;
    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();
    static Double coefficients[]=null;
    private AboutFrame Aframe=new AboutFrame();
    private JTextField t1;
    private JTextField t2;
    private JTextField t3;
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private Box hBoxT1;
    private JButton btnCalc;//=null
    private JButton btnClear;//=null
    private JFileChooser fileChoose;
  //  private JMenuItem menuFileToText;
  //  private JMenuItem menuFileToGraphd;
    private JMenuItem menuFileToCsv;
    private JMenuItem menuTableSimple;
  //  private JMenuItem menuTableFind;
    private JMenuItem menuHelpAbout;

    public MainFrame()
    {
        super("Многочлен");
        setSize(WIDTH,HEIGHT);
        Toolkit kit=Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,(kit.getScreenSize().height - HEIGHT)/2);
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Файл");//выпадающее меню
        JMenu menuTable = new JMenu("Таблица");
        JMenu menuHelp = new JMenu("Справка");

        fileChoose=new JFileChooser();
      //menuFileToText =new JMenuItem("Сохранить в текстовый файл");
      //menuFileToText.addActionListener(new ActionListener(){
      //                                     public void actionPerformed(ActionEvent ev) {
      //                                         fileChoose.setCurrentDirectory(new File("~"));
      //                                         if(fileChoose.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
      //                                             File file=fileChoose.getSelectedFile();
      //                                             try {
      //                                                 PrintStream out=new PrintStream(file);
      //                                                 out.println("Результат таболурование по схеме горнера");
      //                                                 out.print("Многочлен: ");
      //                                                 for(int i=0;i<coefficients.length;i++){
      //                                                     out.print(coefficients[i]+"*X^"+(coefficients.length-i-1));
      //                                                     if(i!=coefficients.length-1)
      //                                                         out.print("+");
      //                                                     else
      //                                                         out.print("\n");
      //                                                 }
      //                                                 out.print("Интервал от: ");
      //                                                 out.print(t1_1.getText());
      //                                                 out.print(" до: ");
      //                                                 out.print(t1_2.getText());
      //                                                 out.print(" с шагом: ");
      //                                                 out.println(t1_3.getText());
      //                                                 out.println("=========================");
      //                                                 for(int y=0;y<data.getRowCount();y++){
      //                                                     out.print("Значение в точке ");
      //                                                     out.print(formatter.format(data.getValueAt(y, 0)));
      //                                                     out.print(" равно ");
      //                                                     out.print(formatter.format(data.getValueAt(y, 1)));
      //                                                     out.print(", а не по горнеру ");
      //                                                     out.print(formatter.format(data.getValueAt(y, 2)));
      //                                                     out.print(" и разница состовляет:");
      //                                                     out.print(formatter.format(data.getValueAt(y, 3)));
      //                                                     out.print("\n");
      //                                                 }
      //                                             } catch (FileNotFoundException e) {
      //                                                 System.out.println("Не удалось создать файл");
      //                                             }

      //                                         }
      //                                     }
      //                                 }
      //);
      //menuFileToGraphd =new JMenuItem("Сохранить данные для построения графика");
      //menuFileToGraphd.addActionListener(new ActionListener(){
      //                                       public void actionPerformed(ActionEvent ev) {
      //                                           fileChoose.setCurrentDirectory(new File("~"));
      //                                           if(fileChoose.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION){
      //                                               File file=fileChoose.getSelectedFile();
      //                                               try {
      //                                                   DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
      //                                                   for (int i = 0; i<data.getRowCount(); i++) {
      //                                                       out.writeDouble((Double)data.getValueAt(i,0));
      //                                                       out.writeDouble((Double)data.getValueAt(i,1));
      //                                                   }
      //                                                   out.close();
      //                                               } catch (Exception e) {
      //                                                   System.out.println("Не удалость создать файл");
      //                                               }
      //                                           }
      //                                       }
      //                                   }
      //);
        menuFileToCsv =new JMenuItem("Сохранить в CSV-файл");//пункт выпадающего меню
        menuFileToCsv.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent ev)
                                            {
                                                fileChoose.setCurrentDirectory(new File("."));//текущая директория по умолчанию
                                                // Показать диалоговое окно
                                                if(fileChoose.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION)
                                                {// Если результат его показа успешный, сохранить данные в текстовый файл
                                                    File file=fileChoose.getSelectedFile();
                                                    try
                                                    {//Создать новый символьный поток вывода, направленный в указанный файл
                                                        PrintStream out=new PrintStream(file);
                                                        for (int y = 0; y < data.getRowCount(); y++)
                                                        {
                                                            for (int u = 0; u < data.getColumnCount(); u++)
                                                            {
                                                                out.print(formatter.format(data.getValueAt(y, u)));
                                                                if(u!=data.getColumnCount()-1)
                                                                    out.print(",");
                                                                else
                                                                    out.print("\n");
                                                            }
                                                        }
                                                    }
                                                    catch (FileNotFoundException e)
                                                    {

                                                    }
                                                }
                                            }
                                        }
        );
        menuTableSimple =new JMenuItem("Найти близкие к простым");
        menuTableSimple.addActionListener(new ActionListener()
                                          { //?
                                              public void actionPerformed(ActionEvent ev)
                                              {
                                                  renderer.setSSimple(true);
                                                  getContentPane().repaint();
                                              }
                                          }
        );
       //menuTableFind =new JMenuItem("Найти значения многочлена");
       //menuTableFind.addActionListener(new ActionListener(){
       //                                    public void actionPerformed(ActionEvent ev) {
       //                                        String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска",
       //                                                "Поиск значения", JOptionPane.QUESTION_MESSAGE);
       //                                        renderer.setNeedle(value);
       //                                        getContentPane().repaint();
       //                                    }
       //                                }
       //);

        menuHelpAbout = new JMenuItem("О программе");
        menuHelpAbout.addActionListener(new ActionListener()
                                        {
                                            public void actionPerformed(ActionEvent ev)
                                            {
                                                Aframe.setVisible(true);
                                            }
                                        }
        );

     //   menuFileToText.setEnabled(false);
     //   menuFileToGraphd.setEnabled(false);
        menuFileToCsv.setEnabled(false); //по умолчанию пункт меню является недоступным, т.к. данных ещё нет
        menuTableSimple.setEnabled(false);//по умолчанию пункт меню является недоступным, т.к. данных ещё нет
      //  menuTableFind.setEnabled(false);
        //menuTableCalc.setEnabled(false);


     //   menuFile.add(menuFileToText);
     //   menuFile.add(menuFileToGraphd);
        menuFile.add(menuFileToCsv);//добавить в выпадающее меню
        menuTable.add(menuTableSimple);//добавить в выпадающее меню
      //  menuTable.add(menuTableFind);
        //menuTable.add(menuTableCalc);
        menuHelp.add(menuHelpAbout);//добавить в выпадающее меню
        menuBar.add(menuFile);
        menuBar.add(menuTable);
        menuBar.add(menuHelp);
        setJMenuBar(menuBar);// Установить меню в качестве главного меню приложения
        ///////////
        l1 = new JLabel("X изменяется на интервале от:");//надписи
        l2 = new JLabel("до:");
        l3 = new JLabel("с шагом:");
        t1 =new JTextField("0.0",12);
        t1.setMaximumSize(t1.getPreferredSize()); //предотвращение масштабирования
        t2 =new JTextField("1.0",12);
        t2.setMaximumSize(t2.getPreferredSize());
        t3 =new JTextField("0.1",12);
        t3.setMaximumSize(t3.getPreferredSize());
        Box hBoxUp=Box.createHorizontalBox();
        hBoxUp.add(Box.createHorizontalGlue());
        hBoxUp.add(l1);
        hBoxUp.add(t1);
        hBoxUp.add(l2);
        hBoxUp.add(t2);
        hBoxUp.add(l3);
        hBoxUp.add(t3);
        hBoxUp.add(Box.createHorizontalGlue());

        btnCalc=new JButton("Вычислить");
        btnCalc.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {//если число в поле для "от" больше чем в "до"
              //  if((Double.parseDouble(t1_1.getText()))-(Double.parseDouble(t1_2.getText()))>0)
              //  {
               //     System.out.print("Error 1>2");
                   // return;
               // }
                data = new GornerTableModel(Double.parseDouble(t1.getText()), //?
                        Double.parseDouble(t2.getText()),Double.parseDouble(t3.getText()),coefficients);
                renderer.setSSimple(false);
                JTable table = new JTable(data);
                table.setDefaultRenderer(Double.class, renderer);
                table.setRowHeight(30);
                hBoxT1.removeAll();
                hBoxT1.add(new JScrollPane(table));
          //      menuFileToText.setEnabled(true);
          //      menuFileToGraphd.setEnabled(true);
                menuFileToCsv.setEnabled(true);
                menuTableSimple.setEnabled(true);
              //  menuTableFind.setEnabled(true);
                getContentPane().validate();//Обновить область содержания главного окна
            }
        });

        btnClear=new JButton("Очистить поля");
        btnClear.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                t1.setText("0.0");
                t2.setText("1.0");
                t3.setText("0.1");
             //   menuFileToText.setEnabled(false);
             //   menuFileToGraphd.setEnabled(false);
                menuFileToCsv.setEnabled(false);
                menuTableSimple.setEnabled(false);
              //  menuTableFind.setEnabled(false);
                hBoxT1.removeAll();
                hBoxT1.add(new JPanel());
                getContentPane().validate();
            }
        });


        Box hBoxDn=Box.createHorizontalBox();
        hBoxDn.add(Box.createHorizontalGlue());
        hBoxDn.add(btnCalc);
        hBoxDn.add(Box.createHorizontalGlue());
        hBoxDn.add(btnClear);
        hBoxDn.add(Box.createHorizontalGlue());
        hBoxT1 = Box.createHorizontalBox();
        hBoxT1.add(new JPanel());//Добавить в контейнер пустую панель, т.к. ещё нечего выводить
        getContentPane().add(hBoxUp, BorderLayout.NORTH);
        getContentPane().add(hBoxT1, BorderLayout.CENTER);
        getContentPane().add(hBoxDn, BorderLayout.SOUTH);
    }

    public static void main(String[] args)
    {
        if(args.length==0)
        {
            System.out.print("Незадан многочлен");
            return;
        }
        coefficients=new Double[args.length];
      for (int i=0; i < args.length; i++)
      {
          coefficients[i]=Double.parseDouble(args[i]);
      }
        MainFrame frame=new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
