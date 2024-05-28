import javax.swing.JFrame;

public class GameFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setBounds(0, 0, Globals.WINDOW_WIDTH, Globals.WINDOW_HEIGHT);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
