import javax.swing.*;
import java.awt.*;

public class PlayerSelectionScene extends BaseFrame {

    public PlayerSelectionScene() {
        super("7 Days to Love - Select Players");
        setBackgroundImage("image\\cover\\Gemini_Generated_Image_fd9035fd9035fd90.png"); 
        initUI();
    }

    private void initUI() {
        // 1. หัวข้อหลัก - วางไว้กึ่งกลางหน้าจอ (Width 1280, ดังนั้นจุดกึ่งกลางคือ 640)
       //JLabel titleLabel = new JLabel("เลือกจำนวนผู้เล่น", SwingConstants.CENTER);
        //titleLabel.setFont(new Font("Tahoma", Font.BOLD, 48));
        //titleLabel.setForeground(new Color(30, 100, 200)); 
        // x = (1280 - 600) / 2 = 340
        //addComponent(titleLabel, 340, 80, 600, 80);

        // 2. จัดระเบียบปุ่มตัวเลือก (ใช้ Array และ Loop เพื่อให้ระยะห่างแม่นยำ)
        String[] playerOptions = {"1 Player", "2 Players", "3 Players"};
        int startY = 220;  // จุดเริ่มต้นของปุ่มแรก
        int spacingY = 80; // ระยะห่างระหว่างปุ่ม
        int btnWidth = 300;
        int btnHeight = 60;
        int centerX = (1280 - btnWidth) / 2; // คำนวณหาจุดกึ่งกลางหน้าจออัตโนมัติ (490)

        for (int i = 0; i < playerOptions.length; i++) {
            JButton btn = new JButton(playerOptions[i]);
            styleButton(btn);
            btn.setFont(new Font("Tahoma", Font.BOLD, 22));
            
            // วางปุ่มโดยคำนวณตำแหน่ง Y ต่อเนื่องกัน
            addComponent(btn, centerX, startY + (i * spacingY), btnWidth, btnHeight);
            
            final int playerCount = i + 1;
            btn.addActionListener(e -> {
                AudioManager.playSound("umamusume_click.wav");
                System.out.println("Selected Players: " + playerCount);
                // ส่งค่า playerCount ไปยังหน้าถัดไปที่นี่
            });
        }

        // 3. ปุ่มกลับ (Back/Exit) - วางให้เล็กลงและแยกสัดส่วนออกมาด้านล่าง
        JButton backBtn = new JButton("BACK");
        styleButton(backBtn);
        // วางกึ่งกลางจอเช่นกัน
        int backBtnWidth = 160;
        int backCenterX = (1280 - backBtnWidth) / 2;
        addComponent(backBtn, backCenterX, 500, backBtnWidth, 45);
        
        backBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_back.wav");
            SceneManager.switchScene(new StartGame());
        });
        
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args) {
        AudioManager.loadSettings();
        SwingUtilities.invokeLater(() -> {
            AudioManager.loadSettings();
            SceneManager.switchScene(new PlayerSelectionScene());
        });
    }
}