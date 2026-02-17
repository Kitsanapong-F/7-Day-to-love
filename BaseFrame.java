import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseFrame extends JFrame {
    protected GameBackground mainPanel;
    private Map<Component, Rectangle> originalBounds = new ConcurrentHashMap<>();
    
    // ประกาศไว้ให้คลาสลูกเรียกใช้ได้เลย ไม่ต้องประกาศซ้ำ
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

    // เมธอดว่างให้คลาสลูกเขียนทับ (Hook Method)
    protected void onPositionUpdated(double scaleX, double scaleY) {
    }

    public void addComponent(Component comp, int x, int y, int width, int height) {
        comp.setBounds(x, y, width, height);
        originalBounds.put(comp, new Rectangle(x, y, width, height));
        mainPanel.add(comp);
        mainPanel.revalidate();
        mainPanel.repaint();
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
        
        // *** จุดที่ต้องเพิ่ม: เรียกใช้ Hook Method เพื่อให้ playAkari ทำงานได้ ***
        onPositionUpdated(scaleX, scaleY);
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void setBackgroundImage(String path) { 
        mainPanel.updateImage(path); 
    }

    public static void styleButton(JButton btn) {
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 90, 120));
        btn.setFocusable(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void clearDynamicComponents(JPanel panel) {
        if (panel != null) {
            panel.removeAll();
            panel.revalidate();
            panel.repaint();
            mainPanel.repaint();
        }
    }

    public void styleChoiceButton(JButton btn) {
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(45, 45, 65, 255));
        btn.setFocusable(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
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
        super.paintComponent(g); // ล้างพื้นหลังเก่า (สำคัญมาก!)
        if (img != null) {
            g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }
}