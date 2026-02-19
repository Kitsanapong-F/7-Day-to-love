import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseFrame extends JFrame {
    protected GameBackground mainPanel;
    private Map<Component, Rectangle> originalBounds = new ConcurrentHashMap<>();
    
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
        
        double scaleX = (double) getContentPane().getWidth() / 1280.0;
        double scaleY = (double) getContentPane().getHeight() / 720.0;

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

    // --- ส่วนที่แก้ไข: เพิ่ม static เพื่อให้คลาสอื่นเรียกใช้ได้โดยไม่มีเส้นแดง ---
    
    public static void styleButton(JButton btn) {
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 90, 120));
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleChoiceButton(JButton btn) {
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(45, 45, 65, 255));
        btn.setFocusable(false);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
    }

    // เมธอดพิเศษสำหรับล้างเงาตัวอักษรเก่า
    protected JTextArea createCleanTextArea() {
        return new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0,0,0,0)); 
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
    }

    public void display() { setVisible(true); }
}

class GameBackground extends JPanel {
    private Image img;
    public GameBackground(String path) { updateImage(path); }
    
    public void updateImage(String path) {
        if (path != null && !path.isEmpty()) {
            img = new ImageIcon(path).getImage();
        } else {
            img = null;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // ล้างเศษภาพเก่าออก
        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }
}