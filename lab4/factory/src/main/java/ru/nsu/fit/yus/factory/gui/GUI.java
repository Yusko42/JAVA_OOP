package ru.nsu.fit.yus.factory.gui;

import ru.nsu.fit.yus.factory.Factory;
import ru.nsu.fit.yus.factory.dealer.Dealer;
import ru.nsu.fit.yus.factory.details.Accessory;
import ru.nsu.fit.yus.factory.supplier.Supplier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame {
    private final Factory factory;

    private JLabel bodyCountLabel;
    private JLabel motorCountLabel;
    private JLabel accessoryCountLabel;
    private JLabel carCountLabel;
    private JLabel soldCarsLabel;
    private JLabel queueSizeLabel;
    private final JLabel bodySuppliedLabel;
    private final JLabel motorSuppliedLabel;
    private final JLabel accessorySuppliedLabel;


    private static final int MAX_DELAY_MS = 3000;

    public GUI(Factory factory) {
        this.factory = factory;
        setTitle("Factory Control Panel");
        setSize(800, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Слайдеры для поставщиков/Suppliers
        JPanel suppliersPanel = new JPanel(new GridLayout(3, 3));
        suppliersPanel.setBorder(BorderFactory.createTitledBorder("Suppliers"));


        // Начальные значения
        bodySuppliedLabel = new JLabel("Supplied: 0");
        motorSuppliedLabel = new JLabel("Supplied: 0");
        accessorySuppliedLabel = new JLabel("Supplied: 0");


        suppliersPanel.add(new JLabel("Body Supplier Speed (ms):"));
        suppliersPanel.add(createSliderPanel("", factory.getSupplierBodyDelay(), factory.getSupplierBody()::setDelay));
        suppliersPanel.add(bodySuppliedLabel);


        suppliersPanel.add(new JLabel("Motor Supplier Speed (ms):"));
        suppliersPanel.add(createSliderPanel("", factory.getSupplierMotorDelay(), factory.getSupplierMotor()::setDelay));
        suppliersPanel.add(motorSuppliedLabel);


        suppliersPanel.add(new JLabel("Accessory Supplier Speed (ms):"));
        suppliersPanel.add(createSliderPanel("", factory.getSupplierAccessoryDelay(), delay -> {
            for (Supplier<?> s : factory.getAccessorySuppliers()) {
                s.setDelay(delay);
            }
        }));
        suppliersPanel.add(accessorySuppliedLabel);



        // Хранилище/Storages
        JPanel storagesPanel = createStoragesPanel();

        // Дилеры/dealers
        JPanel dealersPanel = createDealersPanel();

        add(storagesPanel, BorderLayout.NORTH);
        add(suppliersPanel, BorderLayout.CENTER);
        add(dealersPanel, BorderLayout.SOUTH);

        // Закрытие окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int res = JOptionPane.showConfirmDialog(
                        GUI.this,
                        "Do you want to stop the factory and exit the program?",
                        "Exit",
                        JOptionPane.YES_NO_OPTION
                );
                if (res == JOptionPane.YES_OPTION) {
                    factory.shutdown();
                    dispose();
                    System.exit(0);
                }
            }
        });

        setVisible(true);

        // Таймер обновления метрик
        Timer timer = new Timer(500, e -> updateStats());
        timer.start();
    }

    private JPanel createSliderPanel(String label, int defaultDelay, java.util.function.IntConsumer onChange) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel text = new JLabel(label);
        JSlider slider = new JSlider(0, MAX_DELAY_MS, defaultDelay);
        slider.setMajorTickSpacing(1000);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        slider.addChangeListener(e -> {
            int value = slider.getValue();
            onChange.accept(value);
        });

        panel.add(text, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createStoragesPanel() {
        JPanel storagesPanel = new JPanel(new GridLayout(2, 5));
        storagesPanel.setBorder(BorderFactory.createTitledBorder("Storages"));

        storagesPanel.add(new JLabel(""));
        storagesPanel.add(new JLabel("Bodies"));
        storagesPanel.add(new JLabel("Motors"));
        storagesPanel.add(new JLabel("Accessories"));
        storagesPanel.add(new JLabel("Cars"));

        storagesPanel.add(new JLabel("Stored:"));
        bodyCountLabel = new JLabel();
        motorCountLabel = new JLabel();
        accessoryCountLabel = new JLabel();
        carCountLabel = new JLabel();

        storagesPanel.add(bodyCountLabel);
        storagesPanel.add(motorCountLabel);
        storagesPanel.add(accessoryCountLabel);
        storagesPanel.add(carCountLabel);

        return storagesPanel;
    }

    private JPanel createDealersPanel() {
        JPanel dealersPanel = new JPanel();
        dealersPanel.setLayout(new BoxLayout(dealersPanel, BoxLayout.X_AXIS));
        dealersPanel.setBorder(BorderFactory.createTitledBorder("Dealers"));

        soldCarsLabel = new JLabel();
        queueSizeLabel = new JLabel();

        JPanel dealerSpeedSlider = createSliderPanel("Dealer delay", factory.getDealerDelay(), delay -> {
            for (Dealer d : factory.getDealersList()) {
                d.setDelay(delay);
            }
        });

        JPanel queuePanel = new JPanel(new GridLayout(1, 2));
        queuePanel.add(new JLabel("Number of cars sold: "));
        queuePanel.add(soldCarsLabel);
        queuePanel.add(new JLabel("Number of tasks in queue: "));
        queuePanel.add(queueSizeLabel);

        dealersPanel.add(queuePanel);

        dealersPanel.add(dealerSpeedSlider);

        return dealersPanel;
    }

    private void updateStats() {
        bodyCountLabel.setText(String.valueOf(factory.getBodyStorage().getSize()));
        motorCountLabel.setText(String.valueOf(factory.getMotorStorage().getSize()));
        accessoryCountLabel.setText(String.valueOf(factory.getAccessoryStorage().getSize()));
        carCountLabel.setText(String.valueOf(factory.getCarStorage().getSize()));

        int totalSoldCars = factory.getDealersList().stream().mapToInt(Dealer::getNumberOfCarsSold).sum();
        soldCarsLabel.setText(String.valueOf(totalSoldCars));
        queueSizeLabel.setText(String.valueOf(factory.getThreadPool().getPendingTaskCount()));

        bodySuppliedLabel.setText("Supplied: " + factory.getSupplierBody().getNumberOfSuppliedDetails());
        motorSuppliedLabel.setText("Supplied: " + factory.getSupplierMotor().getNumberOfSuppliedDetails());
        int accessoryNumber = 0;
        for (Supplier<Accessory> a: factory.getAccessorySuppliers()) {
            accessoryNumber += a.getNumberOfSuppliedDetails();
        }
        accessorySuppliedLabel.setText("Supplied: " + accessoryNumber);
    }
}