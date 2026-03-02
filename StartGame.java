import javax.swing.*;

import audio.AudioManager;
import audio.BGMManager;

import java.awt.*;

public class StartGame extends BaseFrame {
    public StartGame() {
        super("7 Days to Love - Main Menu");
        
        // เล่นเพลงประกอบหน้าเมนู
        BGMManager.playBGM("Blue_Archive_MX_Adventure.wav");
        
        // ตั้งค่าพื้นหลังหน้าปก
        setBackgroundImage("image\\cover\\15a52c75-9650-403e-b795-101302a74f6b.png");
        
        initUI();
    }

    private void initUI() {
        // --- 1. ปุ่ม New Game ---
        JButton newGameBtn = new JButton("New Game");
        styleButton(newGameBtn);
        addComponent(newGameBtn, 850, 300, 250, 50);

        // --- 2. ปุ่ม Setting ---
        JButton settingBtn = new JButton("Setting");
        styleButton(settingBtn);
        addComponent(settingBtn, 850, 380, 250, 50);

        // --- 3. ปุ่ม Exit ---
        JButton exitBtn = new JButton("Exit");
        styleButton(exitBtn);
        addComponent(exitBtn, 850, 460, 250, 50);

        // --- Action Listeners ---
        
        // เริ่มเกมใหม่ -> ไปหน้าเลือกตัวละคร
        newGameBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_con.wav");
            SceneManager.switchScene(new CharacterSelection()); 
        });

        // ไปหน้าตั้งค่า
        settingBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_con.wav");
            SceneManager.switchScene(new SettingScene()); 
        });

        // ออกจากเกม
        exitBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_click.wav");
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", "Exit Game", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                AudioManager.playSound("umamusume_back.wav"); // เล่นเสียงปิดท้าย
                AudioManager.saveSettings(); // บันทึกการตั้งค่าเสียง/ความเร็วข้อความ
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        // ตั้งค่า Look and Feel ให้เข้ากับระบบปฏิบัติการ
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { /* Ignore */ }

        // เริ่มต้นเกมผ่าน Event Dispatch Thread เพื่อความปลอดภัยของ UI
        SwingUtilities.invokeLater(() -> {
            AudioManager.loadSettings(); // โหลดค่าที่บันทึกไว้ (เช่น ระดับเสียง)
            SceneManager.switchScene(new StartGame());
        });
    }
}