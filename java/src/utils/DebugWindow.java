package utils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
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

    // Used to count consecutive identical messages.
    private static String lastMessage = null;
    private static int lastMessageCount = 0;

    // Tracks the position of the latest log in the file.
    private static long lastLogFileStart = 0;

    // Tracks where the latest log starts in the console.
    private static int lastEntryStart = 0;

    private static final BlockingQueue<String> inputQueue =
            new LinkedBlockingQueue<>();

    private static final ArrayList<InputListener> listeners =
            new ArrayList<>();

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

    public static synchronized void addLog(String message) {

        // Convert null messages into the text "null".
        String actualMessage = String.valueOf(message);

        // Check if this message is identical to the previous one.
        boolean duplicate = actualMessage.equals(lastMessage);

        // Check whether adding a new log requires removing the oldest.
        boolean removeOldest = !duplicate && logCount == MAX_LOGS;

        if (duplicate) {
            lastMessageCount++;
        } else {
            lastMessage = actualMessage;
            lastMessageCount = 1;
        }

        // Display the count only when the message appears more than once.
        String displayMessage = lastMessageCount == 1
                ? lastMessage
                : lastMessage + " (x" + lastMessageCount + ")";

        // Save the updated message to the file.
        saveLogToFile(displayMessage, duplicate);

        if (duplicate) {

            // Update the last entry instead of adding another one.
            int lastIndex = (nextLog - 1 + MAX_LOGS) % MAX_LOGS;
            logs[lastIndex] = displayMessage;

        } else {

            // Add a new entry to the circular log array.
            logs[nextLog] = displayMessage;
            nextLog = (nextLog + 1) % MAX_LOGS;

            if (logCount < MAX_LOGS) {
                logCount++;
            }
        }

        SwingUtilities.invokeLater(() -> {

            if (duplicate) {

                // Replace the previous display of this message.
                try {
                    int documentLength = console.getDocument().getLength();

                    console.getDocument().remove(
                            lastEntryStart,
                            documentLength - lastEntryStart
                    );

                    console.append(displayMessage + "\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                // Remove the oldest entry when the console is full.
                if (removeOldest && console.getDocument().getLength() > 0) {
                    try {
                        int end = console.getLineEndOffset(0);
                        console.getDocument().remove(0, end);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Remember where this new entry starts.
                lastEntryStart = console.getDocument().getLength();

                console.append(displayMessage + "\n");
            }

            console.setCaretPosition(
                    console.getDocument().getLength()
            );
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

    private static void saveLogToFile(
            String message,
            boolean replaceLast
    ) {

        try (RandomAccessFile file =
                     new RandomAccessFile("debug.log", "rw")) {

            long entryStart = lastLogFileStart;

            if (replaceLast) {

                // Go back to the previous entry so it can be updated.
                file.seek(lastLogFileStart);

            } else {

                // Add a new entry at the end of the file.
                file.seek(file.length());
                entryStart = file.getFilePointer();
            }

            byte[] data = (message + System.lineSeparator())
                    .getBytes(StandardCharsets.UTF_8);

            file.write(data);

            // Remove the old version of the entry, if it was longer.
            file.setLength(file.getFilePointer());

            if (!replaceLast) {
                lastLogFileStart = entryStart;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void clearLogFile() {
        try (PrintWriter writer = new PrintWriter("debug.log")) {
            writer.print("");

        } catch (IOException e) {
            e.printStackTrace();
        }

        lastLogFileStart = 0;
    }
}