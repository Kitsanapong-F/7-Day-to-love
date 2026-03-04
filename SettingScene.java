import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSliderUI;

import audio.AudioManager;
import audio.BGMManager;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class SettingScene extends BaseFrame {

    public static void main(String[] args) {
        // สำหรับรันแยกเพื่อทดสอบหน้าจอ
        SwingUtilities.invokeLater(() -> {
            AudioManager.loadSettings(); // โหลดค่าก่อนแสดงผล
            SettingScene settings = new SettingScene();
            settings.setVisible(true);
        });
    }

    public SettingScene() {
        super("7 Days to Love - Setting");
        // เล่นเพลงสำหรับหน้า Setting โดยเฉพาะ
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
        
        glassPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // --- ดึงค่าปัจจุบันจาก Manager มาแสดงผลบน Slider ---
        int currentBGM = (int) (BGMManager.getVolume() * 100);
        int currentSFX = (int) (AudioManager.getSFXVolume() * 100);

        // 3. Slider สำหรับ Music (BGM)
        glassPanel.add(createSliderRow("Music Volume", currentBGM, true)); 
        glassPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // 4. Slider สำหรับ SFX
        glassPanel.add(createSliderRow("SFX Volume", currentSFX, false));

        // 5. ปุ่ม BACK & SAVE
        glassPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        
        JButton backBtn = new JButton("BACK & SAVE");
        styleButton(backBtn);
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setPreferredSize(new Dimension(200, 50));
        backBtn.setMaximumSize(new Dimension(200, 50));

        backBtn.addActionListener(e -> {
            // เล่นเสียง Back
            AudioManager.playSound("umamusume_back.wav");
            
            // บันทึกค่าลงไฟล์ settings.properties
            AudioManager.saveSettings();
            
            // กลับไปหน้าเมนูหลัก
            SceneManager.switchScene(new StartGame()); 
        });
        
        glassPanel.add(backBtn);

        // 6. วาง glassPanel ไว้กลางจอตามตำแหน่งที่คุณกำหนด
        addComponent(glassPanel, 340, 110, 600, 500);
        
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
            
            // เซฟค่าทันทีเมื่อปล่อยมือจาก Slider
            if (!slider.getValueIsAdjusting()) {
                AudioManager.saveSettings();
            }
        });

        row.add(label, BorderLayout.WEST);
        row.add(slider, BorderLayout.CENTER);
        return row;
    }

    // UI ตกแต่ง Slider สีชมพูหวานๆ
    private static class PinkSliderUI extends BasicSliderUI {
        public PinkSliderUI(JSlider b) { super(b); }
        
        @Override
        public void paintTrack(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 210, 225)); // ชมพูอ่อน
            g2.fill(new RoundRectangle2D.Float(trackRect.x, trackRect.y + (trackRect.height/2)-4, trackRect.width, 8, 10, 10));
            g2.dispose();
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 105, 180)); // ชมพูเข้ม
            g2.fillOval(thumbRect.x, thumbRect.y + 2, thumbRect.width - 4, thumbRect.height - 4);
            g2.dispose();
        }
    }
}