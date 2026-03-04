import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseFrame extends JFrame {
    // static = shared across ALL frames, so fullscreen state is never lost on page change
    private static boolean isFullScreen = false;
    protected GameBackground mainPanel;
    private Map<Component, Rectangle> originalBounds = new ConcurrentHashMap<>();
    
    protected JPanel textWindow;
    protected JLabel nameLabel;
    protected JTextArea dialogueArea;
    protected JPanel choicePanel;
    protected CharacterPanel spritePanel;

    public BaseFrame(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFullscreenShortcut();

        mainPanel = new GameBackground("");
        mainPanel.setLayout(null); 
        setContentPane(mainPanel);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                double scaleX = (double) getWidth() / 1280.0;
                double scaleY = (double) getHeight() / 720.0;
                onPositionUpdated(scaleX, scaleY); 
                updatePositions(); 
            }
        });
    }

    private void setupFullscreenShortcut() {
        JRootPane rootPane = this.getRootPane();
        Action toggleAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[System] F11 Pressed - Toggling Fullscreen");
                toggleFullScreen();
            }
        };
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFS"
        );
        rootPane.getActionMap().put("toggleFS", toggleAction);
    }

    public void toggleFullScreen() {
        if (!isFullScreen) {
            enterFullScreen();
        } else {
            exitFullScreen();
        }
    }

    private void enterFullScreen() {
        this.dispose();
        this.setUndecorated(true);
        this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        isFullScreen = true;

        double scaleX = (double) getWidth() / 1280.0;
        double scaleY = (double) getHeight() / 720.0;
        onPositionUpdated(scaleX, scaleY);
        revalidate();
        repaint();
    }

    private void exitFullScreen() {
        this.dispose();
        this.setUndecorated(false);
        this.setResizable(true);
        this.setExtendedState(JFrame.NORMAL);
        this.setSize(1280, 720);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        isFullScreen = false;

        double scaleX = (double) getWidth() / 1280.0;
        double scaleY = (double) getHeight() / 720.0;
        onPositionUpdated(scaleX, scaleY);
        revalidate();
        repaint();
    }

    /**
     * เรียกโดย SceneManager เมื่อสร้างหน้าจอใหม่
     * ถ้า isFullScreen=true → แสดงแบบเต็มจอ
     * ถ้า isFullScreen=false → แสดงแบบหน้าต่างปกติ 1280x720
     */
    public void enableFullScreen() {
        if (isFullScreen) {
            enterFullScreen();
        } else {
            this.setSize(1280, 720);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            SwingUtilities.invokeLater(() -> updatePositions());
        }
    }

    protected void onPositionUpdated(double scaleX, double scaleY) {}

    public void addComponent(Component comp, int x, int y, int width, int height) {
        comp.setBounds(x, y, width, height);
        originalBounds.put(comp, new Rectangle(x, y, width, height));
        mainPanel.add(comp);
    }

    private void updatePositions() {
        if (mainPanel == null || originalBounds.isEmpty()) return;
        
        int currentW = Math.max(getContentPane().getWidth(), 1);
        int currentH = Math.max(getContentPane().getHeight(), 1);
        
        double scaleX = (double) currentW / 1280.0;
        double scaleY = (double) currentH / 720.0;

        for (Map.Entry<Component, Rectangle> entry : originalBounds.entrySet()) {
            Component comp = entry.getKey();
            Rectangle orig = entry.getValue();
            
            comp.setBounds(
                (int)(orig.x * scaleX), 
                (int)(orig.y * scaleY), 
                (int)(orig.width * scaleX), 
                (int)(orig.height * scaleY)
            );
        }
        
        onPositionUpdated(scaleX, scaleY);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void setBackgroundImage(String path) { 
        mainPanel.updateImage(path); 
    }

    public static void styleButton(JButton btn) {
        Color normalColor = new Color(255, 105, 180);
        Color hoverColor = new Color(255, 150, 200);
        Color pressedColor = Color.WHITE;

        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(normalColor);
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.addChangeListener(e -> {
            ButtonModel model = btn.getModel();
            if (model.isPressed()) {
                btn.setBackground(pressedColor);
                btn.setForeground(Color.BLACK);
            } else if (model.isRollover()) {
                btn.setBackground(hoverColor);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(normalColor);
                btn.setForeground(Color.WHITE);
            }
        });
    }

    public static void styleChoiceButton(JButton btn) {
        btn.setFont(new Font("Tahoma", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.putClientProperty("isHover", true);
                btn.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.putClientProperty("isHover", false);
                btn.repaint();
            }
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                boolean isHover = Boolean.TRUE.equals(c.getClientProperty("isHover"));
                
                if (isHover) {
                    g2d.setPaint(new GradientPaint(0, 0, new Color(180, 140, 50, 200), 
                                 0, c.getHeight(), new Color(120, 90, 30, 220)));
                } else {
                    g2d.setColor(new Color(0, 0, 0, 180));
                }
                g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);

                g2d.setStroke(new BasicStroke(isHover ? 3 : 2));
                g2d.setColor(isHover ? new Color(255, 215, 0) : new Color(212, 175, 55, 150));
                g2d.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, 15, 15);

                super.paint(g, c);
            }
        });
    }

    public void display() { setVisible(true); }
}

class GameBackground extends JPanel {
    private Image img;
    
    public GameBackground(String path) { 
        updateImage(path); 
    }
    
    public void updateImage(String path) {
        if (path != null && !path.isEmpty()) {
            this.img = new ImageIcon(path).getImage();
        } else {
            this.img = null;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (img != null) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}