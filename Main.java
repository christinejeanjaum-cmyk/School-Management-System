import javax.swing.SwingUtilities;
import ui.Menu;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    } 
}
