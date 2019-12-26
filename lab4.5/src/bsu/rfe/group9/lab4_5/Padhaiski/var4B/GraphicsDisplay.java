package bsu.rfe.group9.lab4_5.Padhaiski.var4B;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel
{
    private ArrayList<Double[]> graphicsData;
    private ArrayList<Double[]> originalData;
    boolean scaleMode = false;
    boolean changeMode = false;
    private int selectedMarker = -1;
    //private Double[][] graphicsData;
    private double[][] viewport = new double[2][2];
    private double[] originalPoint = new double[2];

    private double scaleX;
    private double scaleY;

    // Список координат точек для построения графика
    private Rectangle2D.Double selectionRect = new Rectangle2D.Double();

    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true;
    private boolean showMarkers = true;

    // Границы диапазона пространства, подлежащего отображению
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
/*    private double minX1;
    private double maxX1;
    private double minY1;
    private double maxY1;*/


    // Используемый масштаб отображения
    private double scale;

    // Различные стили черчения линий
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke selectionStroke;

    private static DecimalFormat formatter=(DecimalFormat) NumberFormat.getInstance();

    // Различные шрифты отображения надписей
    private Font axisFont;
    private Font labelsFont;

    public GraphicsDisplay() {
        // Цвет заднего фона области отображения - белый
        setBackground(Color.WHITE);

        // Сконструировать необходимые объекты, используемые в рисовании
        // Перо для рисования графика
        selectionStroke = new BasicStroke(1.0F, 0, 0, 10.0F, new float[] { 10, 10 }, 0.0F);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] {12, 3}, 0.0f);
        // Перо для рисования осей координат
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        // Перо для рисования контуров маркеров
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
        labelsFont = new Font("Serif",0,18);
        addMouseMotionListener(new MouseMotionHandler());
        addMouseListener(new MouseHandler());
    }

    // Данный метод вызывается из обработчика элемента меню "Открыть файл с графиком"
    // главного окна приложения в случае успешной загрузки данных
    public void showGraphics(ArrayList<Double[]> graphicsData)
    {
        // Сохранить массив точек во внутреннем поле класса
        this.graphicsData = graphicsData;


        this.originalData = new ArrayList(graphicsData.size());
        for (Double[] point : graphicsData) {
            Double[] newPoint = new Double[2];
            newPoint[0] = new Double(point[0].doubleValue());
            newPoint[1] = new Double(point[1].doubleValue());
            this.originalData.add(newPoint);
        }
        this.minX = ((Double[])graphicsData.get(0))[0].doubleValue();
        this.maxX = ((Double[])graphicsData.get(graphicsData.size() - 1))[0].doubleValue();
        this.minY = ((Double[])graphicsData.get(0))[1].doubleValue();
        this.maxY = this.minY;

        for (int i = 1; i < graphicsData.size(); i++) {
            if (((Double[])graphicsData.get(i))[1].doubleValue() < this.minY) {
                this.minY = ((Double[])graphicsData.get(i))[1].doubleValue();
            }
            if (((Double[])graphicsData.get(i))[1].doubleValue() > this.maxY) {
                this.maxY = ((Double[])graphicsData.get(i))[1].doubleValue();
            }
        }

        zoomToRegion(minX, maxY, maxX, minY);
    }

    // Методы-модификаторы для изменения параметров отображения графика
    // Изменение любого параметра приводит к перерисовке области
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }

    /* Метод-помощник, осуществляющий преобразование координат.
    * Оно необходимо, т.к. верхнему левому углу холста с координатами
    * (0.0, 0.0) соответствует точка графика с координатами (minX, maxY),
    где
    * minX - это самое "левое" значение X, а
    * maxY - самое "верхнее" значение Y.
    */
    protected Point2D.Double xyToPoint(double x, double y)
    {
        // Вычисляем смещение X от самой левой точки (minX)
        double deltaX = x - viewport[0][0];
        // Вычисляем смещение Y от точки верхней точки (maxY)
        double deltaY = viewport[0][1] - y;
        return new Point2D.Double(deltaX*scaleX, deltaY*scaleY);
    }

    protected double[] translatePointToXY(int x, int y)
    {
        return new double[] { this.viewport[0][0] + x / this.scaleX, this.viewport[0][1] - y / this.scaleY };
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY)
    {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    // Отрисовка графика по прочитанным координатам
    protected void paintGraphics(Graphics2D canvas)
    {
        // Выбрать линию для рисования графика
        canvas.setStroke(graphicsStroke);
        // Выбрать цвет линии
        canvas.setColor(Color.BLUE);

        /* Будем рисовать линию графика как путь, состоящий из множества
        сегментов (GeneralPath)
         * Начало пути устанавливается в первую точку графика, после чего
        прямой соединяется со
        * следующими точками
        */
        GeneralPath graphics = new GeneralPath();

        Double currentX = null;
        Double currentY = null;
        for(Double[] point : this.graphicsData)
        {
            if ((point[0].doubleValue() >= this.viewport[0][0]) && (point[1].doubleValue() <= this.viewport[0][1]) &&
                    (point[0].doubleValue() <= this.viewport[1][0]) && (point[1].doubleValue() >= this.viewport[1][1]))
            {
                if ((currentX != null) && (currentY != null)) {
                    canvas.draw(new Line2D.Double(xyToPoint(currentX.doubleValue(), currentY.doubleValue()),
                            xyToPoint(point[0].doubleValue(), point[1].doubleValue())));
                }
                currentX = point[0];
                currentY = point[1];
            }
        }
        // Отобразить график
        canvas.draw(graphics);
    }

    // Метод, обеспечивающий отображение осей координат
    protected void paintAxis(Graphics2D canvas)
    {
        // Установить особое начертание для осей
        canvas.setStroke(this.axisStroke);
        // Оси рисуются чѐрным цветом
        canvas.setColor(java.awt.Color.BLACK);
        // Стрелки заливаются чѐрным цветом
        canvas.setPaint(Color.BLACK);
        // Подписи к координатным осям делаются специальным шрифтом
        canvas.setFont(this.axisFont);
        // Создать объект контекста отображения текста - для получения
        //характеристик устройства (экрана)
        FontRenderContext context=canvas.getFontRenderContext();
        // Определить, должна ли быть видна ось Y на графике
        if (!(viewport[0][0] > 0|| viewport[1][0] < 0)){
            // Она должна быть видна, если левая граница показываемой
            //области (minX) <= 0.0,
            // а правая (maxX) >= 0.0
            // Сама ось - это линия между точками (0, maxY) и (0, minY)
            canvas.draw(new Line2D.Double(xyToPoint(0, viewport[0][1]),
                    xyToPoint(0, viewport[1][1])));
            canvas.draw(new Line2D.Double(xyToPoint(-(viewport[1][0] - viewport[0][0]) * 0.0025,
                    viewport[0][1] - (viewport[0][1] - viewport[1][1]) * 0.015),xyToPoint(0,viewport[0][1])));
            canvas.draw(new Line2D.Double(xyToPoint((viewport[1][0] - viewport[0][0]) * 0.0025,
                    viewport[0][1] - (viewport[0][1] - viewport[1][1]) * 0.015),
                    xyToPoint(0, viewport[0][1])));
            // Определить, сколько места понадобится для надписи "y"
            Rectangle2D bounds = axisFont.getStringBounds("y",context);//y
            Point2D.Double labelPos = xyToPoint(0.0, viewport[0][1]);
            // Вывести надпись в точке с вычисленными координатами
            canvas.drawString("y",(float)labelPos.x + 10,(float)(labelPos.y + bounds.getHeight() / 2));

            /*Rectangle2D bounds2 = axisFont.getStringBounds("0", context);//y
            Point2D.Double labelPos2 = xyToPoint(0.0, 0);
            canvas.drawString("0",(float)labelPos2.x + 10,(float)labelPos2.y + 30);

            Rectangle2D bounds3 = axisFont.getStringBounds("1", context);//y
            Point2D.Double labelPos3 = xyToPoint(0.0, 1);
            canvas.drawString("1",(float)labelPos3.x + 15,(float)labelPos3.y);

            Rectangle2D bounds4 = axisFont.getStringBounds("-", context);//y
            Point2D.Double labelPos4 = xyToPoint(0.0, 1);
            canvas.drawString("-",(float)labelPos4.x - 5,(float)labelPos4.y);*/
        }
        if (!(viewport[1][1] > 0.0D || viewport[0][1] < 0.0D)){
            canvas.draw(new Line2D.Double(xyToPoint(viewport[0][0],0),
                    xyToPoint(viewport[1][0],0)));
            canvas.draw(new Line2D.Double(xyToPoint(viewport[1][0] - (viewport[1][0] - viewport[0][0]) * 0,
                    (viewport[0][1] - viewport[1][1]) * 0.005), xyToPoint(viewport[1][0], 0)));
            canvas.draw(new Line2D.Double(xyToPoint(viewport[1][0] - (viewport[1][0] - viewport[0][0]) * 0.01,
                    -(viewport[0][1] - viewport[1][1]) * 0.005), xyToPoint(viewport[1][0], 0)));
            Rectangle2D bounds = axisFont.getStringBounds("x",context);//x
            Point2D.Double labelPos = xyToPoint(this.viewport[1][0],0.0D);
            canvas.drawString("x",(float)(labelPos.x - bounds.getWidth() - 10),(float)(labelPos.y - bounds.getHeight() / 2));

            /*Rectangle2D bounds5 = axisFont.getStringBounds("1", context);//y
            Point2D.Double labelPos5 = xyToPoint(1, 0);
            canvas.drawString("1",(float)labelPos5.x,(float)labelPos5.y - 27);

            Rectangle2D bounds6 = axisFont.getStringBounds("|", context);//y
            Point2D.Double labelPos6 = xyToPoint(1, 0);
            canvas.drawString("|",(float)labelPos6.x,(float)labelPos6.y + 10);*/
        }
        if (selectedMarker >= 0)
        {
            canvas.setFont(labelsFont);
            Point2D.Double point = xyToPoint(((Double[])graphicsData.get(selectedMarker))[0].doubleValue(),
                    ((Double[])graphicsData.get(selectedMarker))[1].doubleValue());
            String label = "X=" + formatter.format(((Double[])graphicsData.get(selectedMarker))[0]) +
                    ", Y=" + formatter.format(((Double[])graphicsData.get(selectedMarker))[1]);
            Rectangle2D bounds = labelsFont.getStringBounds(label, context);
            canvas.setColor(Color.BLACK);
            canvas.drawString(label, (float)(point.getX()), (float)(point.getY() - bounds.getHeight()));
        }
    }

    // Отображение маркеров точек, по которым рисовался график
    protected void paintMarkers(Graphics2D canvas)
    {
        // Шаг 1 - Установить специальное перо для черчения контуров маркеров
        canvas.setStroke(markerStroke);
        //canvas.setColor(Color.RED);
        //canvas.setPaint(Color.RED);
        // Шаг 2 - Организовать цикл по всем точкам графика
        for(Double[] point: graphicsData)
        {
            String in = (point[1] + "").replace(".", "");
            char[] ch = in.toCharArray();
            canvas.setColor(Color.BLACK);
            for (int i = 0; i < ch.length - 1; i++) {
                if (ch[i] > ch[i+1]){
                    canvas.setColor(Color.GREEN);
                    break;
                }
            }
//            Ellipse2D.Double marker = new Ellipse2D.Double();
//            Point2D.Double center = xyToPoint(point[0], point[1]);
//            Point2D.Double corner = shiftPoint(center, 3, 3);
//            marker.setFrameFromCenter(center, corner);
//            canvas.draw(marker);
//            canvas.fill(marker);
            GeneralPath element = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(point[0], point[1]);
            element.moveTo(lineEnd.getX() + 5, lineEnd.getY() + 5);
            element.lineTo(element.getCurrentPoint().getX() - 11, element.getCurrentPoint().getY());
            element.lineTo(element.getCurrentPoint().getX(), element.getCurrentPoint().getY() - 11);
            element.lineTo(element.getCurrentPoint().getX() + 11, element.getCurrentPoint().getY());
            element.lineTo(element.getCurrentPoint().getX(), element.getCurrentPoint().getY() + 11);
            element.lineTo(element.getCurrentPoint().getX() - 11, element.getCurrentPoint().getY() - 11);
            element.moveTo(element.getCurrentPoint().getX() + 11, element.getCurrentPoint().getY());
            element.lineTo(element.getCurrentPoint().getX() - 11, element.getCurrentPoint().getY() + 11);
            canvas.draw(element);
        }

    }

    // Метод отображения всего компонента, содержащего график
    public void paintComponent(Graphics g)
    {
        /* Шаг 1 - Вызвать метод предка для заливки области цветом заднего фона
         * Эта функциональность - единственное, что осталось в наследство от
         * paintComponent класса JPanel
         */
        super.paintComponent(g);

        scaleX=this.getSize().getWidth() / (this.viewport[1][0] - this.viewport[0][0]);
        scaleY=this.getSize().getHeight() / (this.viewport[0][1] - this.viewport[1][1]);

        // Шаг 2 - Если данные графика не загружены (при показе компонента
        // при запуске программы) - ничего не делать
        if(graphicsData == null || graphicsData.size() == 0) return;

        Graphics2D canvas = (Graphics2D)g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        if(showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if(showMarkers) paintMarkers(canvas);

        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
        paintSelection(canvas);
    }



    protected int findSelectedPoint(int x, int y)
    {
        if (graphicsData == null) return -1;
        int pos = 0;
        for(Double[] point : graphicsData)
        {
            Point2D.Double screenPoint = xyToPoint(point[0].doubleValue(), point[1].doubleValue());
            double distance = (screenPoint.getX()-x)*(screenPoint.getX()-x)+(screenPoint.getY()-y)*(screenPoint.getY()-y);
            if(distance < 100) return pos;
            pos++;
        }
        return -1;
    }

    private void paintSelection(Graphics2D canvas) {
        canvas.setStroke(selectionStroke);
        canvas.setColor(Color.BLACK);
        canvas.draw(selectionRect);
    }

    public void zoomToRegion(double x1,double y1,double x2,double y2)	{
        this.viewport[0][0]=x1;
        this.viewport[0][1]=y1;
        this.viewport[1][0]=x2;
        this.viewport[1][1]=y2;

        this.repaint();
    }

    public class MouseHandler extends MouseAdapter {

        public void mouseClicked(MouseEvent ev)
        {
            if(ev.getButton() == 3)
            {
                zoomToRegion(minX, maxY, maxX, minY);
                repaint();
            }
            //else originalPoint = translatePointToXY(ev.getX(), ev.getY());
        }

        public void mousePressed(MouseEvent ev)
        {
            if (ev.getButton() != 1) return;
            selectedMarker = findSelectedPoint(ev.getX(), ev.getY());
            originalPoint = translatePointToXY(ev.getX(), ev.getY());
            if (selectedMarker >= 0) {
                changeMode = true;
                setCursor(Cursor.getPredefinedCursor(8));
            } else {
                scaleMode = true;
                setCursor(Cursor.getPredefinedCursor(5));
                selectionRect.setFrame(ev.getX(), ev.getY(), 1.0D, 1.0D);
            }
        }

        public void mouseReleased(MouseEvent ev)
        {
            if (ev.getButton() != 1) return;

            setCursor(Cursor.getPredefinedCursor(0));
            if (changeMode) {
                changeMode = false;
            } else {
                scaleMode = false;
                double[] finalPoint = translatePointToXY(ev.getX(), ev.getY());
                viewport = new double[2][2];
                zoomToRegion(originalPoint[0], originalPoint[1], finalPoint[0], finalPoint[1]);
                setCursor(Cursor.getPredefinedCursor(0));
                selectionRect.setFrame(ev.getX(), ev.getY(), 0, 0);
                repaint();
            }
        }

    }

    public class MouseMotionHandler implements MouseMotionListener
    {

        @Override
        public void mouseDragged(MouseEvent e) {
            setCursor(Cursor.getPredefinedCursor(5));
            double width = e.getX() - selectionRect.getX();
            double height = e.getY() - selectionRect.getY();
            selectionRect.setFrame(selectionRect.getX(), selectionRect.getY(), width, height);
            repaint();
//            double width = e.getX() - selectionRect.getX();
//            if (width < 5.0D) {
//                width = 5.0D;
//            }
//            double height = e.getY() - selectionRect.getY();
//            if (height < 5.0D) {
//                height = 5.0D;
//            }
//            selectionRect.setFrame(selectionRect.getX(), selectionRect.getY(), width, height);
//            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            selectedMarker = findSelectedPoint(e.getX(), e.getY());
            if(selectedMarker >= 0)
            {
                setCursor(Cursor.getPredefinedCursor(12));
            }
            else
            {
                setCursor(Cursor.getPredefinedCursor(0));
            }
            repaint();
        }
    }

}
