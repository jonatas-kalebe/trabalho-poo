package main;

import manager.CourseManager;
import thread.AutoSaveThread;
import ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        CourseManager.getInstance().loadData();
        AutoSaveThread auto = new AutoSaveThread();
        auto.start();
        javax.swing.SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
