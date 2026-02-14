import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class mainMenu extends JFrame {
    private boolean isFullscreen;
    private Dimension windowedSize = new Dimension(1280, 720);

    public mainMenu(boolean startFullscreen) {
        this.isFullscreen = startFullscreen;
        setTitle("7 Days to Love - Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        applyFullscreenState();

        // Using your specific background filename from the screenshot
        BackgroundPanel mainPanel = new BackgroundPanel("15a52c75-9650-403e-b795-101302a74f6b.png");
        mainPanel.setLayout(new GridBagLayout());
        setupUI(mainPanel);
        add(mainPanel);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11) toggleFullscreen();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
            }
        });

        setVisible(true);
        this.requestFocus();
    }

    private void setupUI(JPanel panel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST; 
        gbc.weightx = 1.0;
        gbc.insets = new Insets(10, 0, 10, 100);

        JButton startBtn = new JButton("START GAME");
        startBtn.setPreferredSize(new Dimension(250, 50));
        startBtn.setFocusable(false);
        startBtn.addActionListener(e -> {
            new CharacterSelection(this.isFullscreen);
            this.dispose();
        });

        JButton exitBtn = new JButton("EXIT");
        exitBtn.setPreferredSize(new Dimension(250, 50));
        exitBtn.setFocusable(false);
        exitBtn.addActionListener(e -> System.exit(0));

        gbc.gridy = 1; panel.add(startBtn, gbc);
        gbc.gridy = 2; panel.add(exitBtn, gbc);
    }

    private void applyFullscreenState() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (isFullscreen) {
            dispose();
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            dispose();
            setUndecorated(false);
            gd.setFullScreenWindow(null);
            setSize(windowedSize);
            setLocationRelativeTo(null);
        }
    }

    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        applyFullscreenState();
        setVisible(true);
        this.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new mainMenu(false));
    }
}

// FIX: This class must be inside the file or in the same folder
class BackgroundPanel extends JPanel {
    private Image img;
    public BackgroundPanel(String path) { img = new ImageIcon(path).getImage(); }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}