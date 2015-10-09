package xyz.jadonfowler.phasebot.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.*;
import xyz.jadonfowler.phasebot.*;

public class ConsoleGui {

    JFrame frame;
    JTextPane console;
    JTextField input;
    JScrollPane scrollPane;
    StyledDocument document;
    public boolean trace = true;
    ArrayList<String> recentInputs = new ArrayList<String>();
    int recentInputId = 0;
    int recentInputMax = 10;

    public ConsoleGui() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception e) {}
                frame = new JFrame();
                frame.setTitle("PhaseBot: " + PhaseBot.HOST);
                frame.setDefaultCloseOperation(3);
                console = new JTextPane();
                console.setEditable(false);
                console.setFont(new Font("Open Sans", Font.PLAIN, 12));
                console.setOpaque(false);
                document = console.getStyledDocument();
                input = new JTextField();
                // input.setEditable(true); //Probably don't need
                // input.setBorder(null);
                input.setCaretColor(Color.GREEN);
                input.setForeground(Color.WHITE);
                input.setFont(new Font("Open Sans", Font.PLAIN, 12));
                input.setOpaque(false);
                // Input
                input.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        String in = input.getText();
                        if (in.length() > 0) {
                            if (recentInputs.size() < 1 || !(recentInputs.get(recentInputs.size() - 1).equals(in))) {
                                recentInputs.add(in);
                                recentInputId = 0;
                            }
                            // Jaco.in.input(in);
                            performCommand(in);
                            scrollBottom();
                            input.selectAll();
                        }
                    }
                });
                // Previous inputs
                input.addKeyListener(new KeyListener() {

                    public void keyPressed(KeyEvent e) {
                        try {
                            if (e.getKeyCode() == KeyEvent.VK_UP) {
                                if (recentInputId < (recentInputMax - 1) && recentInputId < (recentInputs.size() - 1))
                                    recentInputId++;
                                input.setText(recentInputs.get(recentInputs.size() - 1 - recentInputId));
                            }
                            else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                if (recentInputId > 0) recentInputId--;
                                input.setText(recentInputs.get(recentInputs.size() - 1 - recentInputId));
                            }
                        }
                        catch (Exception x) {}
                    }

                    public void keyReleased(KeyEvent e) {}

                    public void keyTyped(KeyEvent e) {}
                });
                scrollPane = new JScrollPane(console);
                scrollPane.setBorder(null);
                scrollPane.setOpaque(false);
                scrollPane.getViewport().setOpaque(false);
                frame.add(input, BorderLayout.SOUTH);
                frame.add(scrollPane, BorderLayout.CENTER);
                frame.getContentPane().setBackground(new Color(50, 50, 50));
                frame.setSize(660, 350);// TODO Change
                frame.setLocationRelativeTo(null);
                frame.setResizable(false); // TODO Change
                frame.setVisible(true);
            }
        }).start();
    }

    public void scrollTop() {
        console.setCaretPosition(0);
    }

    public void scrollBottom() {
        console.setCaretPosition(console.getDocument().getLength());
    }

    public void print(String s, boolean trace) {
        print(s, trace, new Color(255, 255, 255));
    }

    public void print(String s, boolean trace, Color c) {
        Style style = console.addStyle("Style", null);
        StyleConstants.setForeground(style, c);
        if (trace) {
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();
            String caller = elements[0].getClassName();
            s = caller + " > " + s;
        }
        try {
            document.insertString(document.getLength(), s, style);
        }
        catch (Exception e) {}
    }

    public void println(String s, boolean trace) {
        println(s, trace, new Color(255, 255, 255));
    }

    public void println(String s, boolean trace, Color c) {
        print(s + "\n", trace, c);
    }

    public void print(Object s) {
        print(s.toString());
    }

    public void println(Object s) {
        println(s.toString());
    }

    public void print(String s) {
        print(s, false);
    }

    public void println(String s) {
        println(s, false);
    }

    public void clear() {
        try {
            document.remove(0, document.getLength());
        }
        catch (Exception e) {}
    }

    public void performCommand(String s) {
        try {
            println("$ " + s, false, Color.GREEN);
            PhaseBot.getBot().runCommand(s, true);
        }
        catch (Exception e) {
            println("Error > " + e.getMessage(), trace, new Color(255, 155, 155));
        }
    }
}
