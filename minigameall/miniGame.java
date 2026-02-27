package minigameall;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

public class miniGame {
    public static void main(String[] args) {
        setUIFont(new FontUIResource("Tahoma", Font.PLAIN, 16));
        SwingUtilities.invokeLater(() -> new DatingGameInterface());
    }

    public static void setUIFont(FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (UIManager.get(key) instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}

class DatingGameInterface {
    private long startTime;
    private boolean isRunning = false;
    private final Font thaiFont = new Font("Tahoma", Font.BOLD, 22);
    private ModernHeartButton actionButton;

    public DatingGameInterface() {
        JFrame frame = new JFrame("❤ 10-Second Heartbeat Challenge ❤");
        frame.setSize(550, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // พื้นหลัง Gradient + คลื่นหัวใจ
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // --- ส่วนบน: ข้อความคำถาม ---
        JLabel label = new JLabel("<html><center>\"ถ้าคุณจริงใจกับเรา...<br>ช่วยนับ 10 วินาทีในใจให้แม่นๆ หน่อยได้ไหมคะ?\"</center></html>", SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.ITALIC, 22));
        label.setForeground(new Color(199, 21, 133));
        label.setOpaque(false); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 30, 60, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(label, gbc);

        // --- ส่วนล่าง: ปุ่มกดรูปหัวใจ ---
        actionButton = new ModernHeartButton("START");
        actionButton.setFont(thaiFont);
        actionButton.addActionListener(e -> {
            if (!isRunning) {
                startTime = System.currentTimeMillis();
                isRunning = true;
                actionButton.setLabel("STOP", new Color(255, 50, 100));
            } else {
                stopCounter(frame);
            }
        });

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(actionButton, gbc);

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        
        JOptionPane.showMessageDialog(frame, "กฎคือ: กดเริ่มแล้วนับ 10 วินาทีในใจ\nจากนั้นกดหยุดให้ใกล้เคียงที่สุด!", "ทักทายจากนานะ", JOptionPane.PLAIN_MESSAGE);
        frame.setVisible(true);
    }

    private void stopCounter(JFrame frame) {
        long endTime = System.currentTimeMillis();
        isRunning = false;

        double elapsed = (endTime - startTime) / 1000.0;
        double diff = Math.abs(10.0 - elapsed);
        
        String title;
        String resultMsg;

        // แบ่งระดับความแม่นยำ
        if (diff <= 0.1) {
            title = "❤ พรหมลิขิตชัดๆ! ❤";
            resultMsg = String.format("โอ้โห! ทำได้ %.2f วินาที\nคุณคือคนที่ใช่สำหรับเราเลย!", elapsed);
        } else if (diff <= 0.5) {
            title = "เกือบสมบูรณ์แบบ";
            resultMsg = String.format("ทำได้ %.2f วินาที\nใจเราใกล้กันมากแล้วนะ!", elapsed);
        } else {
            title = "พยายามอีกนิดนะ";
            resultMsg = String.format("ทำได้ %.2f วินาที\nลองจูนจังหวะหัวใจกันใหม่ไหม?", elapsed);
        }

        // สร้างตัวเลือกปุ่ม Play Again / Exit
        Object[] options = {"ลองอีกครั้ง", "ออกจากเกม"};
        int choice = JOptionPane.showOptionDialog(frame, 
            resultMsg + "\nคุณต้องการทำอย่างไรต่อ?", 
            title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null, 
            options, 
            options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            // เล่นต่อ: รีเซ็ตปุ่มเป็น START
            actionButton.setLabel("START", new Color(255, 182, 193));
        } else {
            // ออกจากเกม
            System.exit(0);
        }
    }
}

class GradientPanel extends JPanel {
    private int xOffset = 0;
    private Timer animTimer;

    public GradientPanel() {
        animTimer = new Timer(20, e -> {
            xOffset -= 4;
            repaint();
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint gp = new GradientPaint(0, 0, new Color(255, 230, 240), 0, getHeight(), Color.WHITE);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(new Color(255, 105, 180, 70));
        g2d.setStroke(new BasicStroke(3f));
        Path2D ekg = new Path2D.Double();
        int centerY = getHeight() / 2 + 80;
        ekg.moveTo(0, centerY);

        for (int x = 0; x < getWidth() + 20; x++) {
            int virtualX = (x - xOffset) % 200;
            double y = centerY;
            if (virtualX > 80 && virtualX < 90) y -= 15;
            else if (virtualX >= 90 && virtualX < 100) y += 20;
            else if (virtualX >= 100 && virtualX < 115) y -= 70;
            else if (virtualX >= 115 && virtualX < 130) y += 75;
            else if (virtualX >= 130 && virtualX < 145) y = centerY;
            ekg.lineTo(x, y);
        }
        g2d.draw(ekg);
    }
}

class ModernHeartButton extends JButton {
    private Color currentColor = new Color(255, 182, 193);
    private String labelText;
    private boolean isHovered = false;

    public ModernHeartButton(String label) {
        this.labelText = label;
        setPreferredSize(new Dimension(180, 180));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
            public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
        });
    }

    public void setLabel(String label, Color color) {
        this.labelText = label;
        this.currentColor = color;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Path2D heart = new Path2D.Double();
        heart.moveTo(90, 50);
        heart.curveTo(90, 47, 85, 35, 65, 35);
        heart.curveTo(35, 35, 35, 72, 35, 72);
        heart.curveTo(35, 90, 55, 112, 90, 130);
        heart.curveTo(125, 112, 145, 90, 145, 72);
        heart.curveTo(145, 72, 145, 35, 115, 35);
        heart.curveTo(100, 35, 90, 47, 90, 50);

        if (isHovered) {
            g2d.setColor(new Color(255, 105, 180, 120));
            g2d.setStroke(new BasicStroke(5f));
            g2d.draw(heart);
        }

        g2d.setColor(currentColor);
        g2d.fill(heart);

        g2d.setColor(Color.WHITE);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(labelText, (getWidth()-fm.stringWidth(labelText))/2, getHeight()/2 + 5);
    }
}