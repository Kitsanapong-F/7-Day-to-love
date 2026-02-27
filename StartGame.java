import javax.swing.*;

public class StartGame extends BaseFrame {
    public StartGame() {
        super("7 Days to Love - Main Menu");
        // แนะนำ: หากรันบน Mac/Linux อาจต้องใช้ / แทน \\ 
        // แต่สำหรับ Windows ใช้ image\\... ถูกต้องแล้วครับ
        BGMManager.playBGM("Blue_Archive_MX_Adventure.wav");
        setBackgroundImage("image\\cover\\15a52c75-9650-403e-b795-101302a74f6b.png");
        initUI();
    }

    private void initUI() {
        // เพิ่มชื่อเกม (Game Title) ให้ดูสวยงาม
        // ปุ่ม New Game
        JButton newGameBtn = new JButton("New Game");
        styleButton(newGameBtn);
        addComponent(newGameBtn, 850, 300, 250, 50);

        JButton settingBtn = new JButton(" Setting");
        styleButton( settingBtn);
        addComponent( settingBtn, 850, 380, 250, 50);

        // ปุ่ม Exit
        JButton exitBtn = new JButton("Exit");
        styleButton(exitBtn);
        addComponent(exitBtn, 850, 460, 250, 50);

        // Action: ไปหน้าเลือกตัวละคร
        newGameBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_con.wav");
            SceneManager.switchScene(new CharacterSelection()); 
        });
        

        settingBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_con.wav");
            SceneManager.switchScene(new SettingScene()); 
        });
        

        // Action: ออกจากเกม
        exitBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_click.wav");
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to exit?", "Exit Game", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                AudioManager.playSound("sound/umamusume_back.wav");
                System.exit(0);
            }
            if (confirm == JOptionPane.YES_OPTION) {
                AudioManager.saveSettings(); // บันทึกก่อนปิดเกม
                System.exit(0);
            }
        });
    }
         public static void main(String[] args) {
        // ตั้งค่า Look and Feel ให้ดูเป็นสากล
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) { /* Ignore */ }

        // เริ่มต้นเกมผ่าน SceneManager
        SwingUtilities.invokeLater(() -> {
            AudioManager.loadSettings();
            SceneManager.switchScene(new StartGame());
        });
    }
}