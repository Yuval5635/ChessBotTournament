package utils;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DebugWindow {

    private static final JFrame frame = new JFrame("Debug Console");
    private static final JTextArea console = new JTextArea();
    private static final JTextField input = new JTextField();

    private static final int MAX_LOGS = 1000;
    private static final String[] logs = new String[MAX_LOGS];

    private static int nextLog = 0;
    private static int logCount = 0;

    private static final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();

    private static final ArrayList<InputListener> listeners = new ArrayList<>();

    static {
        clearLogFile();

        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        console.setEditable(false);
        console.setFont(new Font("Monospaced", Font.PLAIN, 14));

        input.setFont(new Font("Monospaced", Font.PLAIN, 14));

        input.addActionListener(e -> {
            String value = input.getText();

            input.setText("");

            addLog("> " + value);

            inputQueue.offer(value);

            updateListeners(value);
        });

        frame.add(new JScrollPane(console), BorderLayout.CENTER);
        frame.add(input, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void addInputListener(InputListener listener) {
        listeners.add(listener);
    }

    public static void addLog(String message) {

        saveLogToFile(message);

        logs[nextLog] = message;
        nextLog = (nextLog + 1) % MAX_LOGS;

        if (logCount < MAX_LOGS) {
            logCount++;
        }

        SwingUtilities.invokeLater(() -> {
            console.append(message + "\n");

            if (logCount == MAX_LOGS) {
                try {
                    int end = console.getLineEndOffset(0);
                    console.getDocument().remove(0, end);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            console.setCaretPosition(
                    console.getDocument().getLength());
        });
    }

    public static void clearInputs() {
        inputQueue.clear();
    }

    public static String getInput() {
        return inputQueue.poll();
    }

    public static String getInput(String prompt) {
        addLog(prompt);
        clearInputs();
        while (true) {
            String input = getInput();
            if (input != null) {
                return input;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
    }

    public static String waitForInput() {
        clearInputs();
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public static String waitForInput(String prompt) {
        addLog(prompt);
        clearInputs();
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    private static void updateListeners(String input) {
        for (InputListener listener : listeners) {
            listener.onInput(input);
        }
    }

    private static void saveLogToFile(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("debug.log", true))) {

            writer.println(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearLogFile() {
        try (@SuppressWarnings("unused")
        PrintWriter writer = new PrintWriter("debug.log")) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}