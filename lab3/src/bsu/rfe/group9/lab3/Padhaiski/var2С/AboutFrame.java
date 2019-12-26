package bsu.rfe.group9.lab3.Padhaiski.var2С;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class AboutFrame extends JFrame
{
    static final int WIDTH=600;
    static final int HEIGHT=450;

    public AboutFrame(){
        super("О программе");
        setSize(WIDTH,HEIGHT);
        Toolkit kit=Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,(kit.getScreenSize().height - HEIGHT)/2);

        JLabel who = new JLabel("Программу написал:");
        JLabel name = new JLabel("Константин Захаров");
        JLabel group = new JLabel("Группа №9");
        JLabel image = new JLabel("");
        Box hBoxData=Box.createVerticalBox();
        hBoxData.add(Box.createHorizontalGlue());
        hBoxData.add(who);
        hBoxData.add(name);
        hBoxData.add(group);
        hBoxData.add(Box.createHorizontalGlue());


        Box hBoxClose=Box.createHorizontalBox();
        JButton btnClose= new JButton("Закрыть");
        btnClose.addActionListener(new ActionListener()
                                   {
                                       public void actionPerformed(ActionEvent arg0) {
                                           setVisible(false);
                                       }
                                   }
        );
        hBoxClose.add(Box.createHorizontalGlue());
        hBoxClose.add(btnClose);
        hBoxClose.add(Box.createHorizontalGlue());

        Box hBoxContent=Box.createVerticalBox();
        hBoxContent.add(Box.createVerticalGlue());
        hBoxContent.add(hBoxData);
        hBoxContent.add(hBoxClose);
        hBoxContent.add(Box.createVerticalGlue());
        getContentPane().add(hBoxContent, BorderLayout.CENTER);
    }
}
