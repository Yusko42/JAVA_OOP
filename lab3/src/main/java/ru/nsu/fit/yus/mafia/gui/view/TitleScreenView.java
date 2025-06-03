package ru.nsu.fit.yus.mafia.gui.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class TitleScreenView extends JFrame {
    private final JButton newGameButton = new JButton("New game");
    private final JButton aboutButton = new JButton("About");
    private final JButton legalInfoButton = new JButton("Legal Information");
    private final JButton helpButton = new JButton("Help");
    private final JButton exitButton = new JButton("Exit");

    public TitleScreenView() {
        super("MAFIA — Title Screen");
        setIcon();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.BLACK); // Цвет экрана

        setImage();

        // Панель для названия игры (центр сверху)
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JLabel titleLabel = new JLabel("MAFIA", JLabel.CENTER);
        titleLabel.setFont(new Font("Bernard MT Condensed", Font.BOLD, 48));
        titleLabel.setForeground(Color.lightGray); // Собственный цвет текста
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Добавляем заголовок в северную часть основного layout
        add(titlePanel, BorderLayout.NORTH);

        // Панель для кнопок (справа)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // сверху вниз раасполагать
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 0, 0));

        // Устанавливаем характеристики кнопкам
        styleButton(newGameButton);
        styleButton(aboutButton);
        styleButton(legalInfoButton);
        styleButton(helpButton);
        styleButton(exitButton);

        addButton(buttonPanel, newGameButton);
        addButton(buttonPanel, aboutButton);
        addButton(buttonPanel, legalInfoButton);
        addButton(buttonPanel, helpButton);
        addButton(buttonPanel, exitButton);

        // Добавляем кнопки в восточную часть
        add(buttonPanel, BorderLayout.WEST);
    }

    private void setIcon() {
        String iconPath = "/ru/nsu/fit/yus/mafia/icon.png";
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath)));
        setIconImage(icon.getImage());
    }

    private void setImage() {
        try {
            String imagePath = "/ru/nsu/fit/yus/mafia/title_mafia.png";
            BufferedImage originalImage = ImageIO.read(Objects.requireNonNull(getClass().getResource(imagePath)));

            int targetWidth = (int) (getWidth() * 0.5);
            int targetHeight = (int) (originalImage.getHeight() * ((double) targetWidth / originalImage.getWidth()));

            // Масштабируем
            Image scaledImage = originalImage.getScaledInstance(
                    targetWidth,
                    targetHeight,
                    Image.SCALE_SMOOTH
            );

            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));
            add(imageLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.out.println("Problem: " + e.getMessage()); // если проблема с файлом картинки
        }
    }

    private void styleButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 30));
        button.setFont(new Font("Gilroy", Font.PLAIN, 16));
    }

    private void addButton(JPanel buttonPanel, JButton button) {
        buttonPanel.add(Box.createVerticalStrut(40));
        buttonPanel.add(button);
    }

    // Во время ввода данных:
    public String displayEnterYourName() {
        return JOptionPane.showInputDialog(this,
                "Enter your name: ",
                "Game Setup",
                JOptionPane.QUESTION_MESSAGE);
    }

    public String displayEnterTheNumberOfPlayers() {
        return JOptionPane.showInputDialog(this,
                "Enter the number of players (6-10):",
                "Game Setup",
                JOptionPane.QUESTION_MESSAGE);
    }

    public void displayInvalidNameInput() {
        JOptionPane.showMessageDialog(this,
                "You have not entered your name! Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void displayInvalidNumberInput() {
        JOptionPane.showMessageDialog(this,
                "Invalid input! Please enter a number between 6 and 10.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }


    // Геттеры для кнопок
    public JButton getNewGameButton() {
        return newGameButton;
    }

    public JButton getAboutButton() {
        return aboutButton;
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    public JButton getLegalInfoButton() {
        return legalInfoButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
