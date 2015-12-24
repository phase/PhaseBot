package xyz.jadonfowler.phasebot.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.text.*;
import xyz.jadonfowler.phasebot.*;

public class ConsoleGui {

    static final Color DARK_GREY = new Color(50, 50, 50);
    JFrame frame;
    JTextPane console;
    JTextPane chat;
    JTextPane logger;
    JTextField input;
    JScrollPane consoleScrollPane;
    JScrollPane chatScrollPane;
    JScrollPane loggerScrollPane;
    StyledDocument consoleDocument;
    StyledDocument chatDocument;
    StyledDocument loggerDocument;
    public boolean trace = true;
    ArrayList<String> recentInputs = new ArrayList<String>();
    int recentInputId = 0;
    int recentInputMax = 10;
    SimpleAttributeSet background = new SimpleAttributeSet();

    public ConsoleGui() {
        new Thread(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }
                catch (Exception e) {}
                //StyleConstants.setBackground(background, new Color(230, 255, 230));
                UIDefaults defaults = UIManager.getDefaults();
                defaults.put("TextPane[Enabled].backgroundPainter", DARK_GREY);
                defaults.put("TextPane.background", new ColorUIResource(DARK_GREY));
                defaults.put("TextPane.inactiveBackground", new ColorUIResource(DARK_GREY));
                frame = new JFrame();
                frame.setTitle("PhaseBot: " + PhaseBot.HOST);
                frame.setDefaultCloseOperation(3);
                console = new JTextPane();
                console.setEditable(false);
                console.setFont(new Font("Open Sans", Font.PLAIN, 12));
                console.setOpaque(false);
                // console.setBackground(new Color(50, 50, 50));
                consoleDocument = console.getStyledDocument();
                chat = new JTextPane();
                chat.setEditable(false);
                chat.setFont(new Font("Open Sans", Font.PLAIN, 12));
                chat.setOpaque(false);
                chatDocument = chat.getStyledDocument();
                logger = new JTextPane();
                logger.setEditable(false);
                logger.setFont(new Font("Open Sans", Font.PLAIN, 12));
                logger.setOpaque(false);
                loggerDocument = logger.getStyledDocument();
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
                loggerScrollPane = new JScrollPane(logger);
                loggerScrollPane.setBorder(null);
                loggerScrollPane.setOpaque(false);
                loggerScrollPane.getViewport().setOpaque(false);
                JTabbedPane tabs = new JTabbedPane();
                tabs.setBackground(DARK_GREY);
                tabs.addTab("Console", null, consoleScrollPane, "Console for PhaseBot");
                console.putClientProperty("Nimbus.Overrides", defaults);
                console.putClientProperty("Nimbus.Overrides.InferitDefaults", true);
                console.setBackground(DARK_GREY);
                tabs.addTab("Chat", null, chatScrollPane, "Incoming chat messages");
                chat.putClientProperty("Nimbus.Overrides", defaults);
                chat.putClientProperty("Nimbus.Overrides.InferitDefaults", true);
                chat.setBackground(DARK_GREY);
                tabs.addTab("Log", null, loggerScrollPane, "Internal Logger");
                logger.putClientProperty("Nimbus.Overrides", defaults);
                logger.putClientProperty("Nimbus.Overrides.InferitDefaults", true);
                logger.setBackground(DARK_GREY);
                frame.add(input, BorderLayout.SOUTH);
                frame.add(tabs, BorderLayout.CENTER);
                frame.getContentPane().setBackground(DARK_GREY);
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
        print(s, trace, Color.BLACK);
    }
    
    public void log(String s) {
        try {
            StyleConstants.setForeground(background, Color.BLACK);
            loggerDocument.insertString(loggerDocument.getLength(), s + "\n", background);
            logger.setCaretPosition(logger.getDocument().getLength());
        }
        catch (Exception e) {}
    }

    public void addChatMessage(String s) {
        try {
            StyleConstants.setForeground(background, Color.BLACK);
            chatDocument.insertString(chatDocument.getLength(), s + "\n", background);
            chat.setCaretPosition(chat.getDocument().getLength());
        }
        catch (Exception e) {}
    }

    public void print(String s, boolean trace, Color c) {
        try {
            StyleConstants.setForeground(background, c);
            if (trace) {
                Throwable t = new Throwable();
                StackTraceElement[] elements = t.getStackTrace();
                String caller = elements[0].getClassName();
                s = caller + " > " + s;
            }
            consoleDocument.insertString(consoleDocument.getLength(), s, background);
        }
        catch (Exception e) {}
        scrollBottom();
    }

    public void println(String s, boolean trace) {
        print(s + "\n", trace);
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
            println("$ " + s, false, new Color(92, 196, 88));
            PhaseBot.getBot().runCommand(s, true);
        }
        catch (Exception e) {
            println("Error > " + e.getMessage(), trace, new Color(255, 155, 155));
        }
    }
}
