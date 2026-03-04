import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseFrame extends JFrame {
    private boolean isFullScreen = false;
    protected GameBackground mainPanel;
    private Map<Component, Rectangle> originalBounds = new ConcurrentHashMap<>();
    
    // Component หลักสำหรับการทำ Visual Novel
    protected JPanel textWindow;
    protected JLabel nameLabel;
    protected JTextArea dialogueArea;
    protected JPanel choicePanel;
    protected CharacterPanel spritePanel;

    public BaseFrame(String title) {
        setTitle(title);
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupFullscreenShortcut();

        mainPanel = new GameBackground("");
        mainPanel.setLayout(null); 
        setContentPane(mainPanel);

        // ระบบตรวจจับการเปลี่ยนขนาดหน้าจอเพื่อปรับสเกล UI
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                double scaleX = (double) getWidth() / 1280.0;
                double scaleY = (double) getHeight() / 720.0;
                onPositionUpdated(scaleX, scaleY); 
                updatePositions(); 
            }
        });

        SwingUtilities.invokeLater(() -> updatePositions());
    }

    private void setupFullscreenShortcut() {
        // สร้าง Action สำหรับการสลับโหมด
        JRootPane rootPane = this.getRootPane();
        Action toggleAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("[System] F11 Pressed - Toggling Fullscreen"); // Debug เพื่อเช็คใน Console
                toggleFullScreen();
            }
        };

        // ผูกปุ่ม F11 เข้ากับ Action
        rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_F11, 0), "toggleFS"
        );
        rootPane.getActionMap().put("toggleFS", toggleAction);
    }

    public void toggleFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (!isFullScreen) {
            // เข้าสู่โหมด Fullscreen
            if (gd.isFullScreenSupported()) {
                this.dispose(); 
                this.setUndecorated(true);
                this.setResizable(false);
                gd.setFullScreenWindow(this);
                this.setVisible(true);
                isFullScreen = true;
            }
        } else {
            // กลับสู่โหมดหน้าต่าง (Windowed)
            gd.setFullScreenWindow(null);
            this.dispose();
            this.setUndecorated(false); // คืนค่าแถบหัวหน้าต่าง
            this.setResizable(true);
            this.setSize(1280, 720); // ขนาดเริ่มต้นของคุณ
            this.setLocationRelativeTo(null);
            this.setVisible(true);
            isFullScreen = false;
        }
        
        double scaleX = (double) getWidth() / 1280.0;
        double scaleY = (double) getHeight() / 720.0;
        onPositionUpdated(scaleX, scaleY);
        // แจ้งให้ UI อัปเดตตำแหน่งปุ่มตามขนาดจอใหม่
        revalidate();
        repaint();
    }

    public void enableFullScreen() {
        // Get the default screen device
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        // 1. Remove window borders (must be done before the frame is visible)
        this.dispose(); // Temporarily close to change decoration
        this.setUndecorated(true);
        this.setResizable(false);

        // 2. Set to Fullscreen mode
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            // Fallback if Fullscreen isn't supported: Maximize the window
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setVisible(true);
        }
        
        // 3. Re-validate layout for new size
        this.revalidate();
        this.repaint();
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
            // บังคับให้ฟอนต์ปรับขนาดตาม (Optional)
            // comp.setFont(comp.getFont().deriveFont((float)(orig.height * scaleY * 0.4)));
        }
        
        onPositionUpdated(scaleX, scaleY);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void setBackgroundImage(String path) { 
        mainPanel.updateImage(path); 
    }

    // --- ส่วนของ Style สำหรับเกมจีบสาว ---
    
   public static void styleButton(JButton btn) {
    Color normalColor = new Color(255, 105, 180); // สีชมพูตอนปกติ
    Color hoverColor = new Color(255, 150, 200);  // สีชมพูอ่อนตอนเอาเมาส์วาง
    Color pressedColor = Color.WHITE;             // สีขาวตอนกด (ตามที่คุณต้องการ)

    btn.setFont(new Font("Tahoma", Font.BOLD, 18));
    btn.setForeground(Color.WHITE); // สีตัวอักษรตอนปกติ
    btn.setBackground(normalColor);
    btn.setFocusable(false);
    btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    btn.setContentAreaFilled(false); // ปิดการวาดพื้นหลังแบบดั้งเดิม
    btn.setOpaque(true);             // เพื่อให้แสดงสีที่เรากำหนดเอง

    // ใช้ ChangeListener เพื่อตรวจจับสถานะการ "กด" (Pressed)
    btn.addChangeListener(e -> {
        ButtonModel model = btn.getModel();
        if (model.isPressed()) {
            btn.setBackground(pressedColor);    // เปลี่ยนเป็นสีขาวตอนกด
            btn.setForeground(Color.BLACK);     // เปลี่ยนตัวอักษรเป็นสีดำเพื่อให้เห็นชัดบนพื้นขาว
        } else if (model.isRollover()) {
            btn.setBackground(hoverColor);      // สีตอนเมาส์วาง
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(normalColor);     // สีปกติ
            btn.setForeground(Color.WHITE);
        }
    });
}
    public static void styleChoiceButton(JButton btn) {
    btn.setFont(new Font("Tahoma", Font.BOLD, 20));
    btn.setForeground(Color.WHITE);
    btn.setFocusable(false);
    btn.setContentAreaFilled(false); // ปิดการวาดพื้นหลังมาตรฐานของ Swing
    btn.setBorderPainted(false);     // เราจะวาดขอบเองใน paintComponent

    // เพิ่ม MouseListener สำหรับเอฟเฟกต์ Hover
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

    // ใช้การวาดใหม่เพื่อให้ดูหรูหราแบบ Modern VN
    btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean isHover = Boolean.TRUE.equals(c.getClientProperty("isHover"));
            
            // 1. วาดพื้นหลัง (ไล่เฉดสีเบาๆ)
            if (isHover) {
                // เมื่อเมาส์ชี้: สีเหลืองทอง/ส้มจางๆ แบบปุ่มที่ถูกเลือก
                g2d.setPaint(new GradientPaint(0, 0, new Color(180, 140, 50, 200), 
                             0, c.getHeight(), new Color(120, 90, 30, 220)));
            } else {
                // ปกติ: สีดำโปร่งใส
                g2d.setColor(new Color(0, 0, 0, 180));
            }
            g2d.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 15, 15);

            // 2. วาดเส้นขอบ (Gold Border)
            g2d.setStroke(new BasicStroke(isHover ? 3 : 2));
            g2d.setColor(isHover ? new Color(255, 215, 0) : new Color(212, 175, 55, 150));
            g2d.drawRoundRect(1, 1, c.getWidth() - 3, c.getHeight() - 3, 15, 15);

            super.paint(g, c);
        }
    });
}

    public void display() { setVisible(true); }
}

// คลาสจัดการพื้นหลัง (ปรับปรุงระบบวาดภาพ)
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

        // 1. วาดพื้นหลัง
        if (img != null) {
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }

        // 2. ตั้งค่าการวาดตัวอักษรให้เนียน (Text Anti-aliasing)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}