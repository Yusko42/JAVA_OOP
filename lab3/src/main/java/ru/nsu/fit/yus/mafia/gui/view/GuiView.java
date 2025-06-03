package ru.nsu.fit.yus.mafia.gui.view;

import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWordType;
import ru.nsu.fit.yus.mafia.model.messages.MessageType;
import ru.nsu.fit.yus.mafia.model.roles.Doctor;
import ru.nsu.fit.yus.mafia.model.roles.Mafia;
import ru.nsu.fit.yus.mafia.model.roles.Role;
import ru.nsu.fit.yus.mafia.model.roles.Sheriff;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class GuiView extends JFrame {
    private JPanel playersPanel;
    private JLabel timeIconLabel;
    private final Map<String, JPanel> playerPanels = new HashMap<>();
    private final Map<String, JLabel> iconLabels = new HashMap<>();
    private final List<String> playersList = new ArrayList<>();

    private Set<String> activeRolePlayers = new HashSet<>(); // Имена игроков, чьи панели сейчас активны
    private boolean isNightPhase = false;


    public GuiView() {
        super("MAFIA — The Game");
        setIcon();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        setupLayout();
    }

     private void setIcon() {
        String iconPath = "/ru/nsu/fit/yus/mafia/icon.png";
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath)));
        setIconImage(icon.getImage());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // TOP PANEL
        JPanel topPanel = new JPanel(new BorderLayout());

        // Панель с иконкой времени суток
        timeIconLabel = new JLabel(); // оставим пустым — установим позже
        JPanel dayIconPanel = new JPanel();
        dayIconPanel.add(timeIconLabel);
        topPanel.add(dayIconPanel, BorderLayout.WEST);

        // Панель с заголовком
        JLabel titleLabel = new JLabel("MAFIA");
        titleLabel.setFont(new Font("Bernard MT Condensed", Font.BOLD, 48));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(titlePanel, BorderLayout.CENTER);

        // Добавляем topPanel наверх
        add(topPanel, BorderLayout.NORTH);

        // PLAYERS PANEL
        playersPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JScrollPane scrollPane = new JScrollPane(playersPanel);
        add(scrollPane, BorderLayout.CENTER);

        setDayMode();
    }

    public void addPlayer(String name) {
        playersList.add(name);
        JPanel playerRow = new JPanel(new BorderLayout(5, 5));

        JLabel iconLabel = new JLabel(new ImageIcon(Objects.requireNonNull(loadIcon("/ru/nsu/fit/yus/mafia/civilian.png"))));
        JLabel nameLabel = new JLabel(name);
        JTextField speechField = new JTextField("");
        JLabel voteLabel = new JLabel("Vote: -");

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(nameLabel, BorderLayout.NORTH);
        centerPanel.add(speechField, BorderLayout.CENTER);
        centerPanel.add(voteLabel, BorderLayout.SOUTH);

        playerRow.add(iconLabel, BorderLayout.WEST);
        playerRow.add(centerPanel, BorderLayout.CENTER);

        playersPanel.add(playerRow);
        playerPanels.put(name, playerRow);
        iconLabels.put(name, iconLabel);
    }

    public void setPlayerSpeech(String name, String message) {
        JPanel row = playerPanels.get(name);
        if (row != null) {
            JTextField speechField = (JTextField) ((JPanel) row.getComponent(1)).getComponent(1);
            speechField.setText(message);
        }
        //changeScreen();
    }

    public void showVote(String fromPlayer, String toPlayer) {
        JPanel row = playerPanels.get(fromPlayer);
        if (row != null) {
            JLabel voteLabel = (JLabel) ((JPanel) row.getComponent(1)).getComponent(2);
            voteLabel.setText("Vote: " + toPlayer);
            changeScreen();

            Timer timer = new Timer(3000, _ -> voteLabel.setText("Vote: -"));
            timer.setRepeats(false);
            timer.start();
            changeScreen();
        }
        //changeScreen();
    }

    public void removePlayer(String name) {
        JPanel row = playerPanels.remove(name);
        if (row != null) {
            playersPanel.remove(row);
            iconLabels.remove(name);
        }
        //changeScreen();
    }


    /// ИКОНКИ НА ВРЕМЯ ДНЯ

    public void setDayMode() {
        setTimeIcon("/ru/nsu/fit/yus/mafia/day_night_icons/day_icon.png");
        getContentPane().setBackground(Color.WHITE);
    }

    public void setNightMode() {
        setTimeIcon("/ru/nsu/fit/yus/mafia/day_night_icons/night_icon.png");
        getContentPane().setBackground(new Color(30, 30, 30)); // тёмный фон
    }

    private void setTimeIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(path)));
            timeIconLabel.setIcon(icon);
        } catch (Exception e) {
            System.out.println("Time icon exception: " + e.getMessage());
        }

    }

    ///  ВХОД В НОЧНОЙ РЕЖИМ

    public void enterNightMode() {
        // Установка тёмного фона
        playersPanel.setBackground(Color.DARK_GRAY);
        getContentPane().setBackground(Color.DARK_GRAY);

        // Иконка луны
        setNightMode();

        Set<String> nightSet = new HashSet<>(playersList);

        for (Map.Entry<String, JPanel> entry : playerPanels.entrySet()) {
            String name = entry.getKey();
            JPanel panel = entry.getValue();
            JLabel icon = iconLabels.get(name);

            if (nightSet.contains(name)) {
                panel.setBackground(new Color(60, 60, 60));
                panel.setOpaque(true);

                // Выбор иконки в зависимости от класса роли
                //String roleIconPath = getNightIconForRole(role);
                //icon.setIcon(new ImageIcon(Objects.requireNonNull(loadIcon(roleIconPath))));
            } else {
                panel.setBackground(Color.DARK_GRAY);
                //icon.setIcon(toGrayscale(icon.getIcon()));
            }
        }

        //changeScreen();
    }

    public void highlightPlayersByRole(Class<? extends Role> roleClass) {
        activeRolePlayers.clear();
        for (Map.Entry<String, JPanel> entry : playerPanels.entrySet()) {
            String name = entry.getKey();
            Role role = playerRoles.get(name); // Предполагаем, что playerRoles хранит соответствие имён и ролей

            if (roleClass.isInstance(role)) {
                activeRolePlayers.add(name);
                // Подсветка панели
                entry.getValue().setBackground(new Color(80, 80, 80)); // Тёмно-серый
                iconLabels.get(name).setIcon(new ImageIcon(getRoleIcon(role))); // Иконка роли
            }
        }
    }

    public void darkenAllPlayers() {
        for (Map.Entry<String, JPanel> entry : playerPanels.entrySet()) {
            String name = entry.getKey();
            if (!activeRolePlayers.contains(name)) {
                entry.getValue().setBackground(Color.BLACK); // Полное затемнение
                iconLabels.get(name).setIcon(null); // Скрываем иконку
            }
        }
    }

    private String getNightIconForRole(Role role) {
        if (role instanceof Mafia) return "/ru/nsu/fit/yus/mafia/mafia1.png";
        if (role instanceof Doctor) return "/ru/nsu/fit/yus/mafia/doctor.png";
        if (role instanceof Sheriff) return "/ru/nsu/fit/yus/mafia/sheriff.png";
        return "/ru/nsu/fit/yus/mafia/civilian.png";
    }

    public void exitNightMode() {
        getContentPane().setBackground(Color.WHITE);
        playersPanel.setBackground(Color.WHITE);
        setDayMode();

        for (Map.Entry<String, JPanel> entry : playerPanels.entrySet()) {
            JPanel panel = entry.getValue();
            panel.setBackground(Color.WHITE);
            panel.setOpaque(true);

            String name = entry.getKey();
            JLabel icon = iconLabels.get(name);
            Image original = loadIcon("/ru/nsu/fit/yus/mafia/civilian.png"); // зависит от модели
            icon.setIcon(new ImageIcon(original));
        }

        //changeScreen();
    }

    public void setPlayerIcon(String name, Image image) {
        JLabel iconLabel = iconLabels.get(name);
        if (iconLabel != null) {
            iconLabel.setIcon(new ImageIcon(image));
        }
    }

    private Image loadIcon(String path) {
        try {
            BufferedImage originalImage = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
            int targetWidth = 50;
            int targetHeight = (int) (originalImage.getHeight() * ((double) targetWidth / originalImage.getWidth()));
            return originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.out.println("Problem loading icon: " + e.getMessage());
        }
        return null;
    }


    public String askForPlayerTarget(Player self, List<String> livingCandidates) {
        Object choice = JOptionPane.showInputDialog(
                this,
                self.getPlayerName() + ", choose the target:",
                "MAFIA — Voting",
                JOptionPane.PLAIN_MESSAGE,
                null,
                livingCandidates.toArray(),
                livingCandidates.getFirst()
        );
        return choice != null ? choice.toString() : null;
    }

    public Map<String, Object> askForMessageInput(Player self, List<String> candidates, List<MessageType> types) {
        Map<String, Object> data = new HashMap<>();

        JComboBox<MessageType> typeComboBox = new JComboBox<>(types.toArray(new MessageType[0]));
        JComboBox<String> playerComboBox = new JComboBox<>(candidates.toArray(new String[0]));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Choose the message type:"));
        panel.add(typeComboBox);
        panel.add(new JLabel("Choose the target:"));
        panel.add(playerComboBox);

        int resultOption = JOptionPane.showConfirmDialog(null, panel,
                self.getPlayerName() + ", send the message",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultOption == JOptionPane.OK_OPTION) {
            data.put("type", typeComboBox.getSelectedItem());
            data.put("target", playerComboBox.getSelectedItem());
        } else {
            return null;
        }
        return data;
    }

    public Map<String, Object> askForLastWordInput(Player self, List<String> candidates, List<LastWordType> types) {
        Map<String, Object> data = new HashMap<>();

        JComboBox<LastWordType> typeComboBox = new JComboBox<>(types.toArray(new LastWordType[0]));
        JComboBox<String> targetComboBox = new JComboBox<>(candidates.toArray(new String[0]));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Choose the last word:"));
        panel.add(typeComboBox);

        JLabel targetLabel = new JLabel("Choose the target:");
        panel.add(targetLabel);
        panel.add(targetComboBox);

        targetLabel.setVisible(false);
        targetComboBox.setVisible(false);

        // показывать цель только если выбран NAME_THE_KILLER
        typeComboBox.addActionListener(e -> {
            LastWordType selected = (LastWordType) typeComboBox.getSelectedItem();
            boolean needsTarget = selected == LastWordType.NAME_THE_KILLER;
            targetLabel.setVisible(needsTarget);
            targetComboBox.setVisible(needsTarget);
            panel.revalidate();
            panel.repaint();
        });

        int option = JOptionPane.showConfirmDialog(null, panel,
                self.getPlayerName() + ": Last word",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option != JOptionPane.OK_OPTION) {
            throw new RuntimeException("Last word input cancelled");
        }

        data.put("type", typeComboBox.getSelectedItem());
        if (typeComboBox.getSelectedItem() == LastWordType.NAME_THE_KILLER) {
            data.put("target", targetComboBox.getSelectedItem());
        }

        return data;
    }

    public void askNewMessage() {
        JOptionPane.showMessageDialog(this,
                "Please enter the message again",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }


    private void changeScreen() {
        revalidate();
        repaint();
    }



    // ДАЛЕЕ - Изменение окна по запросу Observer
    // Все методы package-private
    void displayGameStart() {
        //exitNightMode();
        JOptionPane.showMessageDialog(this,
                "Game starts!",
                "Start",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayNightStart() {
        enterNightMode();
        JOptionPane.showMessageDialog(this,
                "Night falls...",
                "Night",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayDayStart() {
        exitNightMode();
        JOptionPane.showMessageDialog(this,
                "The dawn came.",
                "Day",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayPlayerKilled(String player) {
        // Явное сообщение о решении
        removePlayer(player);
    }

    void displayPlayerSpoken(String player, String message) {
        setPlayerSpeech(player, message);
    }

    void displayVote(String voter, String voted) {
        showVote(voter, voted);
    }


    // ВИДНО ТОЛЬКО ЧЛЕНАМ МАФИИ
    void displayMafiaVote(String voter, String voted) {
        showVote(voter, voted);
    }

    void displayMafiaDecision(String victim) {
        JOptionPane.showMessageDialog(this,
                "According to the results of the vote, the victim was chosen: " + victim,
                "The result",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayElimination(String player) {
        JOptionPane.showMessageDialog(this,
                "According to the results of the vote, the player is excluded: " + player,
                "The result",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayGameEnd(String winner) {
        JOptionPane.showMessageDialog(this,
                "Game over! The team won: " + winner,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayGameOver() {
        JOptionPane.showMessageDialog(this,
                "Game over! Alas, you were killed.",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayPlayerRole(String player, String role) {
        JOptionPane.showMessageDialog(this,
                "Player " + player + ", your role is: " + role,
                "About you",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void updateLivingPlayers(List<String> players) {
         Set<String> aliveSet = new HashSet<>(players);

         for (Map.Entry<String, JPanel> entry : playerPanels.entrySet()) {
             String name = entry.getKey();
             JPanel panel = entry.getValue();
             JLabel icon = iconLabels.get(name);

             boolean isAlive = aliveSet.contains(name);
             panel.setEnabled(isAlive);
             icon.setEnabled(isAlive);

             // Серый фон + иконка при смерти
             panel.setBackground(isAlive ? Color.WHITE : Color.LIGHT_GRAY);

             // Затемнение изображения
             /*if (!isAlive) {
                 icon.setIcon(toGrayscale(icon.getIcon()));
             }*/
         }

         revalidate();
         repaint();
    }

    private Icon toGrayscale(Icon originalIcon) {
        if (!(originalIcon instanceof ImageIcon imageIcon)) return originalIcon;

        Image img = imageIcon.getImage();
        BufferedImage gray = new BufferedImage(
                img.getWidth(null),
                img.getHeight(null),
                BufferedImage.TYPE_BYTE_GRAY
        );

        Graphics g = gray.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        return new ImageIcon(gray);
    }


    // ТОЛЬКО МАФИИ
    void displayLivingMafia(List<String> mafiaMembers) {
    }

    // ТОЛЬКО ШЕРИФУ
    void displayLivingSheriff(String sheriff) {
    }

    public void displaySheriffInvestigation(String target, boolean isMafia) {
        String result;
        if (isMafia)
            result = (target + " is a mafia member!");
        else
            result = (target + " is not a mafia member.");
        JOptionPane.showMessageDialog(this,
                 result,
                "Sheriff",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // ТОЛЬКО ДОКТОРУ
    void displayLivingDoctor(String doctor) {
    }

    void displayDoctorChosen(String target) {
        JOptionPane.showMessageDialog(this,
                "The doctor chose the patient: " + target,
                "Doctor",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayLastWord(String player, String message) {
        JOptionPane.showMessageDialog(this,
                "The last word of the player " + player + ": \n" + message,
                "The last word",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayMessage(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                null,
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayNewVote() {
        JOptionPane.showMessageDialog(this,
                "The result is ambiguous, a repeat vote is assigned!",
                "The result",
                JOptionPane.INFORMATION_MESSAGE);
    }

    void displayNobodyPrisoned() {
        JOptionPane.showMessageDialog(this,
                "According to the results of the vote, no one was excluded!",
                "The result",
                JOptionPane.INFORMATION_MESSAGE);
    }

}
