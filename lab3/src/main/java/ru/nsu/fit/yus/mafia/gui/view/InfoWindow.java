package ru.nsu.fit.yus.mafia.gui.view;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class InfoWindow {
    public static void showAbout() {
        JFrame aboutFrame = createInfoFrame("About Mafia");
        setIcon(aboutFrame);

        JTextArea textArea = new JTextArea();
        setTextArea(textArea);

        String aboutText =
                "Welcome to the world of MAFIA!\n\n" +
                "   -- STORY --\n" +
                "A war broke out between two groups: the MAFIA & the CIVILIANS.\n" +
                "The goal of the mafia is to reach numerical parity with the civilians.\n" +
                "The goal of the civilians is to get rid of every mafia member before it's too late.\n\n" +
                "   -- GAMEPLAY --\n" +
                "The game consists of two phases: NIGHT and DAY.\n" +
                "At NIGHT, the mafia secretly eliminates a player.\n" +
                "During the DAY, all players discuss and vote to eliminate a suspect.\n\n" +
                "   -- ROLES --\n" +
                "MAFIA     - Knows other mafia. Eliminates players at night.\n" +
                "CIVILIAN  - No special powers. Tries to find and vote out mafia.\n" +
                "DOCTOR    - Can save one player each night.\n" +
                "DETECTIVE - Can check if one player is mafia each night.\n\n" +
                "Work together, trust no one... and survive.";

        textArea.setText(aboutText);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        aboutFrame.add(scrollPane, BorderLayout.CENTER);

        aboutFrame.setVisible(true);
    }

    // Окно "Legal Information"
    public static void showLegalInfo() {
        JFrame legalFrame = createInfoFrame("Legal Information");
        setIcon(legalFrame);

        JTextArea textArea = new JTextArea();
        setTextArea(textArea);

        String legalText =
                """
                        (c) Original game by Dimitry Davidoff     1986, 1987
                        (c) Java Adaptation by @yusk42            2025
                        
                        The program is a free implementation of 'Mafia', a social deduction game created by Dimitry Davidoff.
                        The creator of this implementation does not claim intellectual property rights of
                        authors and owners of the trademark.
                        The software is freeware. If you paid for it, you have been SCAMMED!""";

        textArea.setText(legalText);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        legalFrame.add(scrollPane, BorderLayout.CENTER);

        legalFrame.setVisible(true);
    }

    // Окно "Help"
    public static void showHelp() {
        JFrame helpFrame = createInfoFrame("Help");
        setIcon(helpFrame);

        JTextArea textArea = new JTextArea();
        setTextArea(textArea);

        String helpText = """
                Available options:
                
                New Game - Start the game
                About - Information about the game
                Legal Information - Legal information & copyrights
                Help  - List of available commands
                Exit  - Exit the game""";

        textArea.setText(helpText);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        helpFrame.add(scrollPane, BorderLayout.CENTER);

        helpFrame.setVisible(true);
    }

    // Общий метод для создания окон информации
    private static JFrame createInfoFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new BorderLayout());

        return frame;
    }

    private static void setTextArea(JTextArea textArea) {
        textArea.setEditable(false);
        textArea.setFont(new Font("Georgia", Font.ITALIC, 14));
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }

    private static void setIcon(JFrame frame) {
        String iconPath = "/ru/nsu/fit/yus/mafia/icon.png";
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(InfoWindow.class.getResource(iconPath)));
        frame.setIconImage(icon.getImage());
    }
}
