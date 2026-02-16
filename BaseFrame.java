import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class BaseFrame extends JFrame {
    protected JLayeredPane layeredPane; 
    protected GameBackground mainPanel;
    private Map<Component, Rectangle> originalBounds = new HashMap<>();

    public BaseFrame(String title) {
        setTitle(title);
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. สร้าง LayeredPane เป็น ContentPane หลักเพื่อคุม Z-Order
        layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        setContentPane(layeredPane);

        // 2. สร้างพื้นหลังและกำหนดให้ขยายเต็มขนาดเริ่มต้น
        mainPanel = new GameBackground("");
        mainPanel.setBounds(0, 0, 1280, 720);
        layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);

        // 3. Listener สำหรับตรวจจับการเปลี่ยนขนาดหน้าจอ (Fullscreen)
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updatePositions();
            }
        });
    }

    public void addComponent(Component comp, int x, int y, int width, int height, Integer layer) {
        comp.setBounds(x, y, width, height);
        originalBounds.put(comp, new Rectangle(x, y, width, height));
        layeredPane.add(comp, layer); // เพิ่มลงใน layeredPane ตามชั้นที่ระบุ
    }

    public void updatePositions() {
        if (layeredPane == null) return;

        int currentW = getContentPane().getWidth();
        int currentH = getContentPane().getHeight();

        // สำคัญ: ขยายพื้นหลังให้เต็มหน้าจอเสมอ
        if (mainPanel != null) {
            mainPanel.setBounds(0, 0, currentW, currentH);
        }

        if (originalBounds.isEmpty()) return;

        double scaleX = (double) currentW / 1280.0;
        double scaleY = (double) currentH / 720.0;

        for (Map.Entry<Component, Rectangle> entry : originalBounds.entrySet()) {
            Component comp = entry.getKey();
            Rectangle orig = entry.getValue();

            comp.setBounds(
                (int) (orig.x * scaleX),
                (int) (orig.y * scaleY),
                (int) (orig.width * scaleX),
                (int) (orig.height * scaleY)
            );
        }
        layeredPane.revalidate();
        layeredPane.repaint();
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

    public void display() { setVisible(true); }
}

class GameBackground extends JPanel {
    private Image img;
    public GameBackground(String path) { updateImage(path); }
    public void updateImage(String path) {
        if (path != null && !path.isEmpty()) img = new ImageIcon(path).getImage();
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }
}