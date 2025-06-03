package ru.nsu.fit.yus.mafia.gui.view;

import ru.nsu.fit.yus.mafia.model.Player;
import ru.nsu.fit.yus.mafia.model.messages.LastWordType;
import ru.nsu.fit.yus.mafia.model.messages.MessageType;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class GuiView extends JFrame {
    private JPanel playersPanel;
    private JLabel timeIconLabel;
    private JPanel topPanel;
    private JPanel dayIconPanel;
    private JLabel titleLabel;
    private JPanel titlePanel;
    private final Map<String, JPanel> playerPanels = new HashMap<>();
    private final Map<String, JLabel> iconLabels = new HashMap<>();
    private final Map<String, JTextField> speechFields = new HashMap<>();
    private final Map<String, JPanel> centerPanels = new HashMap<>();

    private final List<String> playersList = new ArrayList<>();


    public GuiView() {
        super("MAFIA — The Game");
        setIcon();
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);

        setupLayout();
        setupWindowListener();
    }

     private void setIcon() {
        String iconPath = "/ru/nsu/fit/yus/mafia/icon.png";
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(iconPath)));
        setIconImage(icon.getImage());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // TOP PANEL
        topPanel = new JPanel(new BorderLayout());

        // Панель с иконкой времени суток
        timeIconLabel = new JLabel();
        dayIconPanel = new JPanel();
        dayIconPanel.add(timeIconLabel);
        topPanel.add(dayIconPanel, BorderLayout.WEST);

        // Панель с заголовком
        titleLabel = new JLabel("MAFIA");
        titleLabel.setFont(new Font("Bernard MT Condensed", Font.BOLD, 48));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel = new JPanel(new BorderLayout());
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

        JLabel iconLabel = new JLabel(getDefaultIcon());
        JLabel nameLabel = new JLabel(name);
        JTextField speechField = new JTextField("");
        JLabel voteLabel = new JLabel("Vote: -");

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(nameLabel, BorderLayout.NORTH);
        centerPanel.add(speechField, BorderLayout.CENTER);
        centerPanel.add(voteLabel, BorderLayout.SOUTH);

        playerRow.add(iconLabel, BorderLayout.WEST);
        playerRow.add(centerPanel, BorderLayout.CENTER);

        centerPanels.put(name, centerPanel);
        playersPanel.add(playerRow);
        playerPanels.put(name, playerRow);
        iconLabels.put(name, iconLabel);
        speechFields.put(name, speechField);
    }

    public void removePlayer(String name) {
        // 1. Удаляем из playerPanels и получаем панель
        JPanel row = playerPanels.remove(name);

        // 2. Проверяем, что игрок существовал
        if (row == null) {
            System.err.println("Player not found: " + name); // на скорую руку сделал, прошу не ругать
            return;
        }

        // 3. Удаляем из всех связанных коллекций
        playersPanel.remove(row);
        iconLabels.remove(name);
        playersList.remove(name);

        // 4. Обновляем UI
        playersPanel.revalidate();
        playersPanel.repaint();

        changeScreen();
    }

    public void setPlayerSpeech(String name, String message) {
        JPanel row = playerPanels.get(name);
        if (row != null) {
            JTextField speechField = (JTextField) ((JPanel) row.getComponent(1)).getComponent(1);
            speechField.setText(message);

            Timer timer = new Timer(12000, _ -> speechField.setText(""));
            timer.setRepeats(false);
            timer.start();
            changeScreen();
        }
        //changeScreen();
    }

    public void showVote(String fromPlayer, String toPlayer) {
        JPanel row = playerPanels.get(fromPlayer);
        if (row != null) {
            JLabel voteLabel = (JLabel) ((JPanel) row.getComponent(1)).getComponent(2);
            voteLabel.setText("Vote: " + toPlayer);
            //changeScreen();

            Timer timer = new Timer(12000, _ -> voteLabel.setText("Vote: -"));
            timer.setRepeats(false);
            timer.start();
            changeScreen();
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
        setNightMode(); // Устанавливаем ночную иконку
        clearAllMessages();

        topPanel.setBackground(new Color(42, 42, 53));
        dayIconPanel.setBackground(new Color(42, 42, 53));
        titlePanel.setBackground(new Color(42, 42, 53));
        titleLabel.setForeground(Color.WHITE);

        // Затемняем всех игроков
        for (JPanel panel : playerPanels.values()) {
            panel.setBackground(Color.BLACK);
        }
        for (JLabel icon : iconLabels.values()) {
            icon.setIcon(getDefaultNightIcon());
        }
        for (JTextField field : speechFields.values()) {
            field.setBackground(Color.WHITE);
        }
        for (JPanel panel : centerPanels.values()) {
            panel.setBackground(Color.lightGray);
        }
    }

    public void darkenAllPlayers() {
        clearAllMessages(); // Очищаем сообщения
        for (Map.Entry<String, JPanel> entry : playerPanels.entrySet()) {
            String name = entry.getKey();
            entry.getValue().setBackground(Color.BLACK); // Полное затемнение
            iconLabels.get(name).setIcon(getDefaultNightIcon()); // Скрываем иконку
        }
    }

    public void clearAllMessages() {
        for (String player : playersList) {
            JPanel row = playerPanels.get(player);
            JTextField speechLabel = (JTextField) ((JPanel) row.getComponent(1)).getComponent(1);
            speechLabel.setText("");
            JLabel voteLabel = (JLabel) ((JPanel) row.getComponent(1)).getComponent(2);
            voteLabel.setText("Vote: -");
        }
    }


    public void exitNightMode() {
        setDayMode();
        topPanel.setBackground(Color.WHITE);
        dayIconPanel.setBackground(Color.WHITE);
        titlePanel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.BLACK);

        clearAllMessages();

        // Восстанавливаем стандартный вид
        playersPanel.setBackground(Color.WHITE);
        for (Map.Entry<String, JPanel> entry : playerPanels.entrySet()) {
            entry.getValue().setBackground(Color.WHITE);
            iconLabels.get(entry.getKey()).setIcon(getDefaultIcon());
        }
        for (JTextField field : speechFields.values()) {
            field.setBackground(Color.WHITE);
            field.setForeground(Color.BLACK);
        }
        for (JPanel panel : centerPanels.values()) {
            panel.setBackground(Color.lightGray);
        }
    }

    public ImageIcon getDefaultIcon() {
        return new ImageIcon(Objects.requireNonNull(loadIcon("/ru/nsu/fit/yus/mafia/civilian.png")));
    }

    public ImageIcon getMafiaNightIcon() {
        return new ImageIcon(Objects.requireNonNull(loadIcon("/ru/nsu/fit/yus/mafia/mafia1.png")));
    }

    public ImageIcon getSheriffNightIcon() {
        return new ImageIcon(Objects.requireNonNull(loadIcon("/ru/nsu/fit/yus/mafia/sheriff.png")));
    }

    public ImageIcon getDoctorNightIcon() {
        return new ImageIcon(Objects.requireNonNull(loadIcon("/ru/nsu/fit/yus/mafia/doctor.png")));
    }

    public ImageIcon getDefaultNightIcon() {
        return new ImageIcon(Objects.requireNonNull(loadIcon("/ru/nsu/fit/yus/mafia/night.png")));
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
        typeComboBox.addActionListener(_ -> {
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
        JOptionPane optionPane = new JOptionPane(
                "Game start! Please wait...",
                JOptionPane.INFORMATION_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Start");
        Timer timer = new Timer(1500, _ -> dialog.dispose());

        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
        /*JOptionPane.showMessageDialog(this,
                "Game starts!",
                "Start",
                JOptionPane.INFORMATION_MESSAGE);*/
    }

    void displayNightStart() {
        enterNightMode();
        /*JOptionPane.showMessageDialog(this,
                "Night falls...",
                "Night",
                JOptionPane.INFORMATION_MESSAGE);*/
        timeDialog("Night falls...");

    }

    void displayDayStart() {
        exitNightMode();
        /*JOptionPane.showMessageDialog(this,
                "The dawn came.",
                "Day",
                JOptionPane.INFORMATION_MESSAGE);*/
        timeDialog("The dawn came.");
    }


    private void timeDialog(String message) {
        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.INFORMATION_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Start");
        Timer timer = new Timer(2000, _ -> dialog.dispose());

        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }




    void displayPlayerKilled(String player) {
        //System.out.println("Victim:" + player);
        // Явное сообщение о решении
        removePlayer(player);
    }

    void displayPlayerSpoken(String player, String message) {
        //System.out.println("voter:" + player + ", mes: " + message);
        setPlayerSpeech(player, message);
    }

    void displayVote(String voter, String voted) {
        //System.out.println("voter:" + voted + ", voted: " + voted);
        showVote(voter, voted);
    }


    // ВИДНО ТОЛЬКО ЧЛЕНАМ МАФИИ
    void displayMafiaVote(String voter, String voted) {
        //System.out.println("voter:" + voted + ", voted: " + voted);
        showVote(voter, voted);
    }

    void displayMafiaDecision(String victim) {
        /*JOptionPane.showMessageDialog(this,
                "According to the results of the vote, the victim was chosen: " + victim,
                "The result",
                JOptionPane.INFORMATION_MESSAGE);*/
        JOptionPane optionPane = new JOptionPane(
                "According to the results of the vote, the victim was chosen: " + victim,
                JOptionPane.INFORMATION_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Start");
        Timer timer = new Timer(3000, _ -> dialog.dispose());

        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    void displayElimination(String player) {
        /*JOptionPane.showMessageDialog(this,
                "According to the results of the vote, the player is excluded: " + player,
                "The result",
                JOptionPane.INFORMATION_MESSAGE);
        removePlayer(player);*/
        JOptionPane optionPane = new JOptionPane(
                "According to the results of the vote, the player is excluded: " + player,
                JOptionPane.INFORMATION_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Start");
        Timer timer = new Timer(3000, _ -> dialog.dispose());

        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
        removePlayer(player);
    }

    void displayGameEnd(String winner) {
        JOptionPane.showMessageDialog(this,
                "Game over! The team won: " + winner,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    void displayGameOver() {
        JOptionPane.showMessageDialog(this,
                "Game over! Alas, you were killed.",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    void displayPlayerRole(String player, String role) {
        /*JOptionPane.showMessageDialog(this,
                "Player " + player + ", your role is: " + role,
                "About you",
                JOptionPane.INFORMATION_MESSAGE);*/
        timeDialog("Player " + player + ", your role is: " + role);
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
         }

         revalidate();
         repaint();
    }


    // ТОЛЬКО МАФИИ
    void displayLivingMafia(List<String> mafiaMembers) {
        darkenAllPlayers();

        // 2. Подсвечиваем только мафию
        for (String name : mafiaMembers) {
            JPanel panel = playerPanels.get(name);
            JLabel icon = iconLabels.get(name);

            if (panel != null && icon != null) {
                panel.setBackground(new Color(42, 42, 53)); // Серый фон
                icon.setIcon(getMafiaNightIcon()); // Спец. иконка мафии ночью
                panel.setVisible(true); // На случай, если был скрыт
            }
        }
    }

    // ТОЛЬКО ШЕРИФУ
    void displayLivingSheriff(String sheriff) {
        darkenAllPlayers();

        // 2. Подсвечиваем только мафию
        JPanel panel = playerPanels.get(sheriff);
        JLabel icon = iconLabels.get(sheriff);

        if (panel != null && icon != null) {
            panel.setBackground(new Color(42, 42, 53));
            icon.setIcon(getSheriffNightIcon());
            panel.setVisible(true); // На случай, если был скрыт
        }

    }

    public void displaySheriffInvestigation(String target, boolean isMafia) {
        String result;
        if (isMafia)
            result = (target + " is a mafia member!");
        else
            result = (target + " is not a mafia member.");

        JOptionPane optionPane = new JOptionPane(
                result,
                JOptionPane.INFORMATION_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Start");
        Timer timer = new Timer(3000, _ -> dialog.dispose());

        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);



        /*JOptionPane.showMessageDialog(this,
                 result,
                "Sheriff",
                JOptionPane.INFORMATION_MESSAGE);*/
    }

    // ТОЛЬКО ДОКТОРУ
    void displayLivingDoctor(String doctor) {
        darkenAllPlayers();

        // 2. Подсвечиваем только мафию
        JPanel panel = playerPanels.get(doctor);
        JLabel icon = iconLabels.get(doctor);

        if (panel != null && icon != null) {
            panel.setBackground(new Color(42, 42, 53));
            icon.setIcon(getDoctorNightIcon());
            panel.setVisible(true); // На случай, если был скрыт
        }
    }

    void displayDoctorChosen(String target) {
        /*JOptionPane.showMessageDialog(this,
                "The doctor chose the patient: " + target,
                "Doctor",
                JOptionPane.INFORMATION_MESSAGE);*/

        JOptionPane optionPane = new JOptionPane(
                "The doctor chose the patient: " + target,
                JOptionPane.INFORMATION_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Start");
        Timer timer = new Timer(2000, _ -> dialog.dispose());

        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    void displayLastWord(String player, String message) {
        /*JOptionPane.showMessageDialog(this,
                "The last word of the player " + player + ": \n" + message,
                "The last word",
                JOptionPane.INFORMATION_MESSAGE);*/

        JOptionPane optionPane = new JOptionPane(
                "The last word of the player " + player + ": \n" + message,
                JOptionPane.INFORMATION_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Start");
        Timer timer = new Timer(4000, _ -> dialog.dispose());

        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
    }

    void displayMessage(String message) {
        /*JOptionPane.showMessageDialog(this,
                message,
                null,
                JOptionPane.INFORMATION_MESSAGE);*/

        JOptionPane optionPane = new JOptionPane(
                message,
                JOptionPane.INFORMATION_MESSAGE
        );

        JDialog dialog = optionPane.createDialog("Start");
        Timer timer = new Timer(3000, _ -> dialog.dispose());

        timer.setRepeats(false);
        timer.start();

        dialog.setVisible(true);
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


    // Для закрывающего окошка
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                showExitConfirmation();
            }
        });
    }

    private void showExitConfirmation() {
        // Создаем немодальный диалог
        JOptionPane optionPane = new JOptionPane(
                "Are you sure you want to exit?",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.YES_NO_OPTION
        );

        JDialog dialog = optionPane.createDialog(this, "Game exit");
        dialog.setModal(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Обработка ответа
        optionPane.addPropertyChangeListener(e -> {
            if (e.getPropertyName().equals(JOptionPane.VALUE_PROPERTY)) {
                Object value = optionPane.getValue();
                dialog.dispose();

                if (value instanceof Integer && (Integer)value == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        dialog.setVisible(true);
    }

}
