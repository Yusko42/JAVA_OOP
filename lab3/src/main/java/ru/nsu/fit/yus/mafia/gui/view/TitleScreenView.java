package ru.nsu.fit.yus.mafia.gui.view;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class TitleScreenView extends JFrame {
    private final JButton newGameButton = new JButton("New game");
    private final JButton aboutButton = new JButton("About");
    private final JButton legalInfoButton = new JButton("Legal Information");
    private final JButton helpButton = new JButton("Help");
    private final JButton exitButton = new JButton("Exit");

    private final JLabel imageLabel = new JLabel();  // для картинки

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
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        /*JLabel titleLabel1 = new JLabel("Welcome to the world of...", JLabel.CENTER);
        titleLabel1.setFont(new Font("Gilroy", Font.BOLD, 16));
        titleLabel1.setOpaque(true); // делаем экземпляр непрозрачным (false - полная прозрачность)
        titleLabel1.setBackground(Color.BLACK);
        titleLabel1.setForeground(new Color(232, 225, 225)); // Собственный цвет текста
*/
        JLabel titleLabel = new JLabel("MAFIA", JLabel.CENTER);
        titleLabel.setFont(new Font("Bernard MT Condensed", Font.BOLD, 48));
        titleLabel.setForeground(Color.lightGray); // Собственный цвет текста
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Добавляем заголовок в северную часть основного layout
        add(titlePanel, BorderLayout.NORTH);

        //titleLabel2.setOpaque(true); // делаем экземпляр непрозрачным (false - полная прозрачность)
        //titleLabel2.setBackground(Color.BLACK);

        // Панель для кнопок (справа)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // сверху вниз раасполагать
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));

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
        add(buttonPanel, BorderLayout.EAST);
    }

    private void setIcon() {
        String iconPath = "/ru/nsu/fit/yus/mafia/icon.png";
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath)));
        setIconImage(icon.getImage());
    }

    private void setImage() {
        try {
            String imagePath = "/ru/nsu/fit/yus/mafia/title_mafia.jpg";
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            add(imageLabel, BorderLayout.WEST);
        } catch (Exception e) {
            System.out.println("Problem: " + e.getMessage()); // если проблема с файлом картинки
        }
    }

    private void styleButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 30));
        button.setFont(new Font("Gilroy", Font.PLAIN, 16));
    }

    private void addButton(JPanel buttonPanel, JButton button) {
        buttonPanel.add(Box.createVerticalStrut(20));
        buttonPanel.add(button);
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
