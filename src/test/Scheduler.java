package test;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class Scheduler {

JButton VDay, VWeek, Task, Exit;
JFrame wframe, dframe, tframe;

JLabel head;




public void CreateFrame() {
    JFrame frame = new JFrame("Main Menu");     

//    ButtonListener btnlst = new ButtonListener();
    JPanel panel = new JPanel();
    panel.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.weightx = 1;
    c.weighty = .25;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 1;
    c.gridheight = 1;
    c.fill = GridBagConstraints.BOTH;
    head = new JLabel("The Plain Scheduler");
    panel.add(head, c);
    c.weightx = 1;
    c.weighty = .25;
    c.gridx = 0;
    c.gridy = 1;
    c.gridheight = 2;
    c.gridwidth = 3;
    VDay = new JButton("View Day");
    panel.add(VDay, c);
    c.weightx = 1;
    c.weighty = .25;
    c.gridx = 0;
    c.gridy = 3;
    c.gridheight = 2;
    c.gridwidth = 3;
    VWeek = new JButton("View Week");
    panel.add(VWeek,c);
    c.weightx = 1;
    c.weighty = .25;
    c.gridx = 0;
    c.gridy = 5;
    c.gridheight = 2;
    c.gridwidth = 3;
    Task = new JButton("Assign/Edit Tasks");
    panel.add(Task, c);
    c.weightx = 1;
    c.weighty = .25;
    c.gridx = 0;
    c.gridy = 7;
    c.gridheight = 1;
    c.gridwidth = 2;
    Exit = new JButton("Exit");
    panel.add(Exit, c);

//    VDay.addActionListener(btnlst);
//   VWeek.addActionListener(btnlst);
//    Task.addActionListener(btnlst);
//    Exit.addActionListener(btnlst);

    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {
    Scheduler scheduler = new Scheduler();
    scheduler.CreateFrame();
    }
}
