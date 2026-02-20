import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseFrame extends JFrame {
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

        mainPanel = new GameBackground("");
        mainPanel.setLayout(null); 
        setContentPane(mainPanel);

        // ระบบตรวจจับการเปลี่ยนขนาดหน้าจอเพื่อปรับสเกล UI
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) { 
                updatePositions(); 
            }
        });

        SwingUtilities.invokeLater(() -> updatePositions());
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
        // ใช้พื้นหลังกึ่งโปร่งใสให้ดูหรูหรา
        btn.setBackground(new Color(0, 0, 0, 150)); 
        btn.setFocusable(false);
        btn.setOpaque(false); // ต้องเป็น false เพื่อให้วาดพื้นหลังเองใน paintComponent ได้เนียนๆ
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
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