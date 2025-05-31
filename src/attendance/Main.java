package attendance;

import attendance.gui.MainFrame; //Imports MainFrame, the main GUI window.
import javax.swing.SwingUtilities;  // Imports SwingUtilities, which helps in safely running GUI-related code.

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
