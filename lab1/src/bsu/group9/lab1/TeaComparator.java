package bsu.group9.lab1;


import java.util.Comparator;

public class TeaComparator implements Comparator<Tea>{

    public int compare(Tea arg0, Tea arg1) {
// если 1-ый объект = null, то он всегда "больше", т.е. перемещается
// в конец массива
        if (arg0==null) return 1;
// если 2-ой объект = null, а 1-ый - нет (не сработала предыдущая
// строчка), то 1-ый всегда меньше, т.е. перемешается в начало массива
        if (arg1==null) return -1;
// если оба объекта не null, то результат сравнения определяется
// сравнением их color
        if (arg0.getColor().length() < arg1.getColor().length()) return 1;
        if (arg0.getColor().length() > arg1.getColor().length()) return -1;
        return 0;
//return arg0.getColor().compareTo(arg1.getColor());
    }
}