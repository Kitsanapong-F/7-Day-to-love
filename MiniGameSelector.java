import javax.swing.*;
import audio.AudioManager;
import java.awt.*;
import java.awt.event.*;

/**
 * MiniGameSelector: หน้าจอสำหรับเลือกมินิเกมตัดสินชะตาในช่วงวันที่ 7
 * รับข้อมูลตัวละครและจำนวนผู้เล่นเพื่อส่งต่อข้อมูลไปยังมินิเกมที่เลือก
 */
public class MiniGameSelector extends BaseFrame {
    
    private Character currentGirl;
    private String girlName;
    private int totalPlayers;

    /**
     * Constructor รับค่าข้อมูลจาก StoryManager
     * @param girl ข้อมูลตัวละครที่กำลังเล่น
     * @param name ชื่อรูทตัวละคร (Akari, Reina, Shiori)
     * @param players จำนวนผู้เล่นที่ร่วมเล่น (1-3 คน)
     */
    public MiniGameSelector(Character girl, String name, int players) {
        super("7 Days to Love - Select Mini-Game");
        this.currentGirl = girl;
        this.girlName = name;
        this.totalPlayers = players;
        
        // ตั้งค่าพื้นหลัง
        setBackgroundImage("image\\Place\\_school_in_spring_2.jpg"); 
        initUI();
    }

    private void initUI() {
        // 1. ส่วนหัวข้อ (Title)
        JLabel titleLabel = new JLabel("CHOOSE YOUR FINAL CHALLENGE!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 42));
        titleLabel.setForeground(new Color(255, 215, 0)); // สีทองตัดสินชะตา
        addComponent(titleLabel, 0, 80, 1280, 80);

        // แสดงข้อมูลโหมดปัจจุบัน
        JLabel infoLabel = new JLabel("Final Challenge for " + totalPlayers + " Player(s)", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Tahoma", Font.ITALIC, 22));
        infoLabel.setForeground(Color.WHITE);
        addComponent(infoLabel, 0, 150, 1280, 40);

        // 2. สร้างปุ่มเลือกมินิเกม
        String[] gameOptions = {"Sprint Challenge (Speed)", "Heart Catch (Agility)", "Coming Soon..."};
        int startY = 250;
        int spacingY = 90;
        int btnWidth = 420;
        int btnHeight = 70;
        int centerX = (1280 - btnWidth) / 2;

        for (int i = 0; i < gameOptions.length; i++) {
            final int gameIndex = i;
            JButton btn = new JButton(gameOptions[i]);
            styleButton(btn); // ใช้สไตล์ปุ่มจาก BaseFrame
            btn.setFont(new Font("Tahoma", Font.BOLD, 24));
            
            // ปิดปุ่มที่ยังไม่พัฒนา (Coming Soon)
            if (i >= 1) btn.setEnabled(false);

            addComponent(btn, centerX, startY + (i * spacingY), btnWidth, btnHeight);
            
            btn.addActionListener(e -> {
                AudioManager.playSound("umamusume_click.wav");
                System.out.println("[System] Launching Game: " + gameOptions[gameIndex]);
                
                // เริ่มมินิเกมที่เลือก
                launchMiniGame(gameIndex);
            });
        }

        // 3. ปุ่มย้อนกลับ (Back)
        JButton backBtn = new JButton("BACK");
        styleButton(backBtn);
        int backBtnWidth = 160;
        int backCenterX = (1280 - backBtnWidth) / 2;
        addComponent(backBtn, backCenterX, 580, backBtnWidth, 45);
        
        backBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_back.wav");
            // กลับไปหน้า playmain (ใช้พารามิเตอร์เดิม)
            SceneManager.switchScene(new playmain(currentGirl, totalPlayers));
        });
        
        // Refresh หน้าจอ
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * เมธอดสำหรับเปลี่ยนหน้าไปยังมินิเกมที่เลือก
     * @param gameIndex ลำดับของเกมที่เลือก
     */
    private void launchMiniGame(int gameIndex) {
        // สลับไปหน้ามินิเกมที่เลือก โดยส่งข้อมูลที่จำเป็นไปด้วย
        if (gameIndex == 0) {
            // สร้าง JFrame ใหม่สำหรับครอบ SprintGame เพื่อให้ KeyListener ทำงานแยกอิสระ
            JFrame gameFrame = new JFrame("Sprint Game - Final Countdown");
            
            // เรียกใช้ SprintGame พร้อมส่งพารามิเตอร์ครบถ้วนตามความต้องการของคลาส
            SprintGame gamePanel = new SprintGame(currentGirl, girlName, totalPlayers);
            
            gameFrame.add(gamePanel);
            gameFrame.pack();
            gameFrame.setLocationRelativeTo(null); // แสดงกึ่งกลางหน้าจอ
            gameFrame.setVisible(true);
            
            // ปิดหน้าจอเลือกมินิเกม
            this.dispose();
        }
    }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        // อัปเดตตำแหน่งเมื่อมีการขยายหน้าต่าง
    }
}