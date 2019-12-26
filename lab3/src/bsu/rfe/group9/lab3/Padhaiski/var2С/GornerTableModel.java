package bsu.rfe.group9.lab3.Padhaiski.var2С;

import javax.swing.table.AbstractTableModel;
@SuppressWarnings("serial")

public class GornerTableModel extends AbstractTableModel {
    private Double from;
    private Double to;
    private Double step;
    private Double[] coefficients;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        return 4;
    }

    public int getRowCount() {
        //return (int)((float)(to-from)/step)+1;
        return (int)((to-from)/step)+1;
    }

    private Double calcGorner(Double x){//2 -4 6 -8
        Double result = 0.0;
        for (int i = 0; i < coefficients.length ; i++)
        {
            result = (result*x+coefficients[i]);
        }
        return result;
    }

    private Double calcPow(Double x){
        Double result = 0.0;
        for (int i = 0; i < coefficients.length; i++)
        {
            result += coefficients[i]*Math.pow(x, coefficients.length-i-1);
        }
        return result;
    }

    public Object getValueAt(int row, int col) //для csv
    {
        double x = from + step*row;
        switch(col)
        {
            case 0:
                return x;
            case 1:
                return calcGorner(x);
            case 2:
                return calcPow(x);
            case 3:
                return Math.abs(calcGorner(x)-calcPow(x));
            default:
                break;
        }
        return null;
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            case 1:
                return "Вычисление многочлена по схеме Горнера";
            case 2:
                return "Вычисление многочлена с помощью pow";
            case 3:
                return "Разница столбцов 2 и 3";
            default:
                return "error";
        }
    }

   public Class<?> getColumnClass(int col)
   {
       return Double.class;//все столбцы типа Double
   }
}
