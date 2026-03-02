import javax.swing.*;

import audio.AudioManager;

import java.awt.*;
import java.awt.event.*;

/**
 * PlayerSelectionScene: หน้าจอสำหรับเลือกจำนวนผู้เล่น (1-3 คน)
 * รับข้อมูลตัวละครที่เลือกมาจาก CharacterSelection และส่งต่อไปยัง playmain
 */
public class PlayerSelectionScene extends BaseFrame {
    
    private Character selectedHeroine;

    // Constructor รับค่า Character ที่เลือกมาจากหน้า CharacterSelection
    public PlayerSelectionScene(Character heroine) {
        super("7 Days to Love - Select Players");
        this.selectedHeroine = heroine;
        
        // ตั้งค่าพื้นหลัง (ตรวจสอบ Path ให้ถูกต้องตามเครื่องของคุณ)
        setBackgroundImage("image\\cover\\Gemini_Generated_Image_fd9035fd9035fd90.png"); 
        initUI();
    }

    private void initUI() {
        // 1. ส่วนหัวข้อ (Title)
        //JLabel titleLabel = new JLabel("SELECT NUMBER OF PLAYERS", SwingConstants.CENTER);
        //titleLabel.setFont(new Font("Tahoma", Font.BOLD, 42));
        //titleLabel.setForeground(new Color(255, 105, 180)); // สีชมพูเข้ม
       // addComponent(titleLabel, 340, 80, 600, 80);

        // แสดงชื่อตัวละครที่กำลังเลือกเล่น
        //JLabel infoLabel = new JLabel("Dating with: " + selectedHeroine.getName(), SwingConstants.CENTER);
        //infoLabel.setFont(new Font("Tahoma", Font.ITALIC, 22));
       // infoLabel.setForeground(Color.WHITE);
        //addComponent(infoLabel, 340, 150, 600, 40);

        // 2. สร้างปุ่มเลือกจำนวนผู้เล่น (1, 2, 3 คน)
        String[] playerOptions = {"1 Player", "2 Players", "3 Players"};
        int startY = 250;   // ตำแหน่ง Y เริ่มต้น
        int spacingY = 90;  // ระยะห่างระหว่างปุ่ม
        int btnWidth = 320;
        int btnHeight = 65;
        int centerX = (1280 - btnWidth) / 2; // คำนวณกึ่งกลางหน้าจอ

        for (int i = 0; i < playerOptions.length; i++) {
            final int playerCount = i + 1;
            JButton btn = new JButton(playerOptions[i]);
            styleButton(btn); // ใช้สไตล์ปุ่มจาก BaseFrame
            btn.setFont(new Font("Tahoma", Font.BOLD, 24));
            
            addComponent(btn, centerX, startY + (i * spacingY), btnWidth, btnHeight);
            
            btn.addActionListener(e -> {
                AudioManager.playSound("umamusume_click.wav");
                System.out.println("[System] Starting game with " + playerCount + " players for " + selectedHeroine.getName());
                
                // เริ่มเกมโดยส่งค่าไปยัง playmain ไฟล์เดียว (Universal Version)
                startGame(playerCount);
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
            // กลับไปหน้าเลือกตัวละคร
            SceneManager.switchScene(new CharacterSelection());
        });
        
        // Refresh หน้าจอ
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * เมธอดสำหรับเปลี่ยนหน้าไปยัง playmain
     * @param players จำนวนผู้เล่นที่เลือก
     */
    private void startGame(int players) {
        // ใช้ SceneManager สลับหน้าไปยัง playmain โดยตรง
        // ไม่ต้องแยก playmainReina หรือ playmainShiori แล้ว
        SceneManager.switchScene(new playmain(selectedHeroine, players));
    }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        // อัปเดตตำแหน่ง Component เมื่อมีการขยายหน้าต่าง (ถ้าจำเป็น)
    }
}