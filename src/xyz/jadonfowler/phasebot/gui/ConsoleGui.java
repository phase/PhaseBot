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
    JTextPane chat;
    JTextField input;
    JScrollPane consoleScrollPane;
    JScrollPane chatScrollPane;
    StyledDocument consoleDocument;
    StyledDocument chatDocument;
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
                console.setBackground(new Color(50, 50, 50));
                consoleDocument = console.getStyledDocument();
                chat = new JTextPane();
                chat.setEditable(false);
                chat.setFont(new Font("Open Sans", Font.PLAIN, 12));
                chat.setOpaque(false);
                chat.setBackground(new Color(50, 50, 50));
                chatDocument = chat.getStyledDocument();
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
                consoleScrollPane = new JScrollPane(console);
                consoleScrollPane.setBorder(null);
                consoleScrollPane.setOpaque(false);
                consoleScrollPane.getViewport().setOpaque(false);
                chatScrollPane = new JScrollPane(chat);
                chatScrollPane.setBorder(null);
                chatScrollPane.setOpaque(false);
                chatScrollPane.getViewport().setOpaque(false);
                JTabbedPane tabs = new JTabbedPane();
                tabs.addTab("Console", null, consoleScrollPane, "Console for PhaseBot");
                tabs.addTab("Chat", null, chatScrollPane, "Incoming chat messages");
                tabs.setBackground(new Color(50, 50, 50));
                frame.add(input, BorderLayout.SOUTH);
                frame.add(tabs, BorderLayout.CENTER);
                //frame.add(consoleScrollPane, BorderLayout.WEST);
                //frame.add(chatScrollPane, BorderLayout.EAST);
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

    public void addChatMessage(String s) {
        try {
            Style style = console.addStyle("Style", null);
            chatDocument.insertString(chatDocument.getLength(), s + "\n", style);
        }
        catch (Exception e) {}
    }

    public void print(String s, boolean trace, Color c) {
        try {
            Style style = console.addStyle("Style", null);
            StyleConstants.setForeground(style, c);
            if (trace) {
                Throwable t = new Throwable();
                StackTraceElement[] elements = t.getStackTrace();
                String caller = elements[0].getClassName();
                s = caller + " > " + s;
            }
            consoleDocument.insertString(consoleDocument.getLength(), s, style);
        }
        catch (Exception e) {}
    }

    public void println(String s, boolean trace) {
        print(s +"\n", trace);
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
            consoleDocument.remove(0, consoleDocument.getLength());
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
