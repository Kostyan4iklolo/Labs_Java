package by.tut.ilerof;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Math;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

@SuppressWarnings("serial")

public class SimpleGUI extends JFrame
{

    private JLabel showImage;
    private JTextField mem1Field;
    private JTextField mem2Field;
    private JTextField mem3Field;
 /* private JLabel jlabel1 = new javax.swing.JLabel();
    public static Image visibleImage;*/

    private static final int WIDTH = 700;
    private static final int HEIGHT = 420;
    private JTextField textFieldX;
    private JTextField textFieldY;
    private JTextField textFieldZ;
    private JTextField textFieldResult;
    private ButtonGroup radioButtons = new ButtonGroup();
    private Box hboxFormulaType = Box.createHorizontalBox();
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    private ButtonGroup radioButtonsVariable = new ButtonGroup();
    private Box hboxVariableType = Box.createHorizontalBox();

    private int formulaId = 1;

    private int variableId = 1;

    public Double calculate1(Double x, Double y, Double z)
    {
        return (Math.sin((Math.log(y) + Math.sin(y * y * Math.PI)))) * (Math.pow(x * x + Math.sin(z) + Math.pow((Math.E), Math.cos(z)), 0.25));
    }

    public Double calculate2(Double x, Double y, Double z)
    {
        return Math.pow((Math.cos((Math.pow(Math.E, x))) + Math.log((1 + y) * (1 + y)) + Math.sqrt(Math.pow(Math.E, Math.cos(x)) + (Math.sin(z * Math.PI) * Math.sin(z * Math.PI))) + Math.sqrt(1 / x) + Math.cos(y * y)), Math.sin(z));
    }

    private void addRadioButton(String buttonName, final int formulaId)
    {
        JRadioButton button = new JRadioButton(buttonName);
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                SimpleGUI.this.formulaId = formulaId;
                showImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("formula" + formulaId + ".jpg")));
                //imagePane.updateUI();
            }
        });
        radioButtons.add(button);
        hboxFormulaType.add(button);

    }

    private void addRadioButtonVariable(String buttomName, final int variableId)
    {
        JRadioButton button = new JRadioButton(buttomName);
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                SimpleGUI.this.variableId = variableId;

            }
        });
        radioButtonsVariable.add(button);
        hboxVariableType.add(button);
    }


    public SimpleGUI()
    {
        super("Вычисление формулы");
        formatter.setMaximumFractionDigits(2);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);
        hboxFormulaType.add(Box.createHorizontalGlue());

        addRadioButton("Формула 1", 1);

        addRadioButton("Формула 2", 2);

        radioButtons.setSelected(radioButtons.getElements().nextElement().getModel(), true);

        hboxFormulaType.add(Box.createHorizontalGlue());
        hboxFormulaType.setBorder(
                BorderFactory.createLineBorder(Color.blue));
        JLabel labelForX = new JLabel("X: ");
        textFieldX = new JTextField("0", 20);
        textFieldX.setMaximumSize(textFieldX.getPreferredSize());
        JLabel labelForY = new JLabel("Y: ");
        textFieldY = new JTextField("0", 20);
        textFieldY.setMaximumSize(textFieldY.getPreferredSize());
        JLabel labelForZ = new JLabel("Z: ");
        textFieldZ = new JTextField("0", 20);
        textFieldZ.setMaximumSize(textFieldZ.getPreferredSize());
        Box hboxVariables = Box.createHorizontalBox();
        hboxVariables.setBorder(BorderFactory.createLineBorder(Color.RED));
        hboxVariables.add(Box.createHorizontalGlue());
        hboxVariables.add(labelForX);
        hboxVariables.add(Box.createHorizontalStrut(5));
        hboxVariables.add(textFieldX);
        hboxVariables.add(Box.createHorizontalStrut(25));
        hboxVariables.add(labelForY);
        hboxVariables.add(Box.createHorizontalStrut(5));
        hboxVariables.add(textFieldY);
        hboxVariables.add(Box.createHorizontalStrut(25));

        hboxVariables.add(labelForZ);
        hboxVariables.add(Box.createHorizontalStrut(5));
        hboxVariables.add(textFieldZ);
        hboxVariables.add(Box.createHorizontalGlue());


        showImage = new JLabel();
        showImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("formula1.jpg")));
        Box ShowIconImage = Box.createHorizontalBox();
        ShowIconImage.add(showImage);
        radioButtons.getSelection();


        JLabel labelForResult = new JLabel("Результат: ");
        textFieldResult = new JTextField("0", 20);
        textFieldResult.setMaximumSize( textFieldResult.getPreferredSize());

        Box hboxResult = Box.createHorizontalBox();
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.add(labelForResult);
        hboxResult.add(Box.createHorizontalStrut(10));
        hboxResult.add(textFieldResult);
        hboxResult.add(Box.createHorizontalGlue());
        hboxResult.setBorder(BorderFactory.createLineBorder(Color.green));

        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener()
        {

            public void actionPerformed(ActionEvent ev)
            {

                try
                {
                    Double x = Double.parseDouble(textFieldX.getText());
                    Double y = Double.parseDouble(textFieldY.getText());
                    Double z = Double.parseDouble(textFieldZ.getText());
                    Double result;
                    if (formulaId == 1)
                        result = calculate1(x, y, z);
                    else
                        result = calculate2(x, y, z);
                    String formattedDouble = formatter.format(result);

                    textFieldResult.setText(formattedDouble);
                } catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(SimpleGUI.this, "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                textFieldX.setText("0");
                textFieldY.setText("0");
                textFieldZ.setText("0");
                textFieldResult.setText("0");
            }
        });

        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setBorder( BorderFactory.createLineBorder(Color.GREEN));


        hboxVariableType.add(Box.createHorizontalGlue());
        addRadioButtonVariable("переменная 1", 1);
        addRadioButtonVariable("переменная 2", 2);
        addRadioButtonVariable("переменная 3", 3);
        radioButtonsVariable.setSelected(radioButtonsVariable.getElements().nextElement().getModel(), true);
        hboxVariableType.add(Box.createHorizontalGlue());
        hboxVariableType.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        JButton buttonMC = new JButton("MC");
        buttonMC.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                if (variableId == 1)
                    mem1Field.setText("0.0");
                else if (variableId == 2)
                    mem2Field.setText("0.0");
                else mem3Field.setText("0.0");
            }
        });

        JButton buttonsMplus = new JButton("M+");
        buttonsMplus.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent ev)
            {
                Double result = Double.parseDouble(textFieldResult.getText());
                if (variableId == 1)
                {
                    Double a = Double.parseDouble(mem1Field.getText());
                    a += result;
                    String formattedDouble = formatter.format(a);
                    mem1Field.setText(formattedDouble);
                } else if (variableId == 2)
                {
                    Double a = Double.parseDouble(mem2Field.getText());
                    a += result;String formattedDouble = formatter.format(a);
                    mem2Field.setText(formattedDouble);
                } else
                {

                    Double a = Double.parseDouble(mem3Field.getText());
                    a += result;String formattedDouble = formatter.format(a);
                    mem3Field.setText(formattedDouble);
                }
              //  textFieldResult.setText(result.toString());
            }
        });

        Box boxMemeses = Box.createHorizontalBox();
        boxMemeses.setBorder(BorderFactory.createLineBorder(Color.red));
        boxMemeses.add(Box.createHorizontalGlue());
        mem1Field = new JTextField("0.0", 10);
        mem2Field = new JTextField("0.0", 20);
        mem3Field = new JTextField("0.0", 20);
        mem1Field.setMaximumSize(textFieldX.getPreferredSize());
        mem2Field.setMaximumSize(textFieldY.getPreferredSize());
        mem3Field.setMaximumSize(textFieldZ.getPreferredSize());
        boxMemeses.add(new JLabel("mem1: "));
        boxMemeses.add(mem1Field);
        boxMemeses.add(Box.createHorizontalStrut(25));
        boxMemeses.add(new JLabel("mem2: "));
        boxMemeses.add(mem2Field);
        boxMemeses.add(Box.createHorizontalStrut(25));
        boxMemeses.add(new JLabel("mem3: "));
        boxMemeses.add(mem3Field);
        boxMemeses.add(Box.createHorizontalGlue());
        Box buttonsVariable = Box.createHorizontalBox();
        buttonsVariable.add(Box.createHorizontalGlue());
        buttonsVariable.add(buttonMC);
        buttonsVariable.add(Box.createHorizontalStrut(30));
        buttonsVariable.add(buttonsMplus);
        buttonsVariable.setBorder(BorderFactory.createLineBorder(Color.GREEN));


        Box contentBox = Box.createVerticalBox();
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(hboxFormulaType);

        contentBox.add(ShowIconImage);

        contentBox.add(hboxVariables);
        contentBox.add(hboxResult);
        contentBox.add(hboxButtons);

        contentBox.add(hboxVariableType);
        contentBox.add(buttonsVariable);
        contentBox.add(boxMemeses);

        contentBox.add(Box.createVerticalGlue());
        getContentPane().add(contentBox, BorderLayout.CENTER);

    }
}
