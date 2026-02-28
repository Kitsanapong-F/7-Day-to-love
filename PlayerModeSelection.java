import javax.swing.*;
import java.awt.*;

public class PlayerModeSelection extends BaseFrame {
    private Character selectedHeroine;

    public PlayerModeSelection(Character heroine) {
        super("Select Game Mode");
        this.selectedHeroine = heroine;
        // ใช้พื้นหลังห้องเรียนเพื่อให้เข้ากับบรรยากาศ
        setBackgroundImage("image\\Bgscene\\_front_of_classroom_1.jpg"); 
        setupUI();
    }

    private void setupUI() {
        // หัวข้อหน้าจอ
        JLabel title = new JLabel("CHOOSE YOUR CHALLENGE", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 42));
        title.setForeground(Color.WHITE);
        addComponent(title, 0, 100, 1280, 60);

        // คำอธิบายตัวละครที่เลือก
        JLabel info = new JLabel("Dating with: " + selectedHeroine.getName(), SwingConstants.CENTER);
        info.setFont(new Font("Tahoma", Font.ITALIC, 24));
        info.setForeground(new Color(255, 255, 200));
        addComponent(info, 0, 170, 1280, 40);

        // ปุ่มเล่นคนเดียว (Normal Mode)
        JButton soloBtn = new JButton("1 PLAYER (SOLO)");
        styleButton(soloBtn);
        soloBtn.setBackground(new Color(60, 100, 180)); // สีน้ำเงิน
        addComponent(soloBtn, 440, 280, 400, 80);
        
        soloBtn.addActionListener(e -> {
            startGame(1);
        });

        // ปุ่มเล่น 3 คน (Y8 Battle Mode)
        JButton multiBtn = new JButton("3 PLAYERS (BATTLE)");
        styleButton(multiBtn);
        multiBtn.setBackground(new Color(180, 60, 60)); // สีแดงบอกโหมดต่อสู้
        addComponent(multiBtn, 440, 400, 400, 80);
        
        multiBtn.addActionListener(e -> {
            startGame(3);
        });

        // ปุ่มกลับไปเลือกตัวละครใหม่
        JButton backBtn = new JButton("<- CHANGE HEROINE");
        styleButton(backBtn);
        addComponent(backBtn, 40, 30, 200, 45);
        backBtn.addActionListener(e -> SceneManager.switchScene(new CharacterSelection()));
    }

    private void startGame(int players) {
        String name = selectedHeroine.getName().trim();
        
        // แยกเข้าแต่ละรูทตามตัวละครที่เลือก พร้อมส่งจำนวนผู้เล่น (1 หรือ 3)
        if (name.equalsIgnoreCase("Akari")) {
            SceneManager.switchScene(new playmain(selectedHeroine, players));
        } else if (name.equalsIgnoreCase("Reina")) {
            SceneManager.switchScene(new playmainReina(selectedHeroine, players));
        } else if (name.equalsIgnoreCase("Shiori")) {
            SceneManager.switchScene(new playmainShiori(selectedHeroine, players));
        }
        this.dispose();
    }
}