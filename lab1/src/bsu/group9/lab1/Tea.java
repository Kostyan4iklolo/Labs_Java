package bsu.group9.lab1;


public class Tea extends Food
{

    public String color;
    public Tea (String color)
    {
        super("Чай");
        this.color = color;

    }
    public String getColor() {
        return color;
    }

    public void setColor(String drink) {
        this.color = color;
    }
    @Override
    public void consume() {
        System.out.println(this + " выпит; в нём содержится " + this.calculateCalories() + " калорий");
    }
    @Override
    public String toString() {
        return super.toString() + " " + color;
    }
    @Override
    public int calculateCalories() {
        int calories = 0;
        if (color.equals("чёрный")) {
            calories = 30;
        }
        if (color.equals("зелёный")) {
            calories = 40;
        }
        return calories;
    }
}