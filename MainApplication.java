package com.dcams.main;

import com.dcams.gui.MainFrame;
import javax.swing.*;

public class MainApplication {
    public static void main(String[] args) {
        // Ensure GUI runs on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}