package thread;

import manager.CourseManager;

public class AutoSaveThread extends Thread {
    private boolean running = true;

    @Override
    public void run() {
        while (running) {
            CourseManager.getInstance().saveData();
            try {
                Thread.sleep(5000);
            } catch (Exception ignored) {
            }
        }
    }

    public void stopRunning() {
        running = false;
    }
}
