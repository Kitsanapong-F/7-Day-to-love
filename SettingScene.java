import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SettingScene extends BaseFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SettingScene settings = new SettingScene();
            settings.setVisible(true);
        });
    }

    public SettingScene() {
        super("7 Days to Love - Setting");
        // เล่นเพลง BGM ทันทีที่เข้าหน้า Setting
        BGMManager.playBGM("Blue_Archive_Irasshaimase.wav");
        setBackgroundImage("image\\Place\\_school_in_spring_2.jpg");
        initUI();
    }

    private void initUI() {
        // 1. สร้างแผงหน้าต่างกึ่งโปร่งใส (Glass Panel)
        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 220)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.dispose();
            }
        };
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setOpaque(false);
        glassPanel.setBorder(new EmptyBorder(40, 50, 40, 50));

        // 2. หัวข้อ SETTING
        JLabel titleLabel = new JLabel("SETTING");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 45));
        titleLabel.setForeground(new Color(255, 105, 180));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(titleLabel);
        
        // ระยะห่างระหว่างหัวข้อกับ Slider
        glassPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // 3. Slider สำหรับ Music (BGM)
        glassPanel.add(createSliderRow("Music Volume", 70, true)); 
        glassPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // 4. Slider สำหรับ SFX
        glassPanel.add(createSliderRow("SFX Volume", 70, false));

        // 5. ปุ่ม BACK (วางไว้ข้างในกรอบ)
        glassPanel.add(Box.createRigidArea(new Dimension(0, 60))); // ระยะห่างจาก Slider ตัวสุดท้าย
        
        JButton backBtn = new JButton("BACK");
        styleButton(backBtn); // ใช้สไตล์ปุ่มจาก BaseFrame
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT); // จัดปุ่มให้อยู่กึ่งกลางกรอบขาว
        backBtn.setPreferredSize(new Dimension(200, 50)); // กำหนดขนาดปุ่ม
        backBtn.setMaximumSize(new Dimension(200, 50));

        backBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_back.wav");
            // กลับไปหน้าเมนูหลัก
            BGMManager.stopBGM();
            SceneManager.switchScene(new StartGame()); 
        });
        
        glassPanel.add(backBtn);

        // 6. นำ glassPanel ไปวางกลางจอ (ใช้พิกัด X, Y จาก BaseFrame)
        addComponent(glassPanel, 340, 110, 600, 500);
        
        // อัปเดตเลเยอร์หน้าจอ
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createSliderRow(String labelText, int initialValue, boolean isBGM) {
        JPanel row = new JPanel(new BorderLayout(20, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(500, 60));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Tahoma", Font.BOLD, 20));
        label.setForeground(new Color(100, 100, 100));
        label.setPreferredSize(new Dimension(150, 30));

        JSlider slider = new JSlider(0, 100, initialValue);
        slider.setOpaque(false);
        slider.setUI(new PinkSliderUI(slider));

        slider.addChangeListener(e -> {
            float volume = slider.getValue() / 100f;
            if (isBGM) {
                BGMManager.setVolume(volume);
            } else {
                AudioManager.setSFXVolume(volume);
            }
        });

        row.add(label, BorderLayout.WEST);
        row.add(slider, BorderLayout.CENTER);
        return row;
    }

    // --- ส่วนตกแต่ง Slider สีชมพู ---
    private static class PinkSliderUI extends BasicSliderUI {
        public PinkSliderUI(JSlider b) { super(b); }
        
        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 210, 225)); // รางสีชมพูอ่อน
            g2.fill(new RoundRectangle2D.Float(trackRect.x, trackRect.y + (trackRect.height/2)-4, trackRect.width, 8, 10, 10));
            g2.dispose();
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 105, 180)); // ปุ่มสไลด์สีชมพูเข้ม
            g2.fillOval(thumbRect.x, thumbRect.y + 2, thumbRect.width - 4, thumbRect.height - 4);
            g2.dispose();
        }
    }
}