import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CharacterSelection extends BaseFrame {
    private String selectedName = "";
    private JLabel bioLabel;
    private JButton selectBtn;
    private ArrayList<CharacterPanel> panels = new ArrayList<>();
    private CharacterDetailFrame detailFrame = null;

    public CharacterSelection() {
        super("Select Heroine");
        // ตรวจสอบ Path รูปพื้นหลัง
        setBackgroundImage("image\\Bgscene\\_front_of_classroom_1.jpg");
        setupUI();
    }

    private void setupUI() {
        // หัวข้อหน้าจอ
        JLabel title = new JLabel("SELECT YOUR DESTINY", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 45));
        title.setForeground(new Color(255, 255, 255));
        // เพิ่มเงาให้ข้อความอ่านง่ายขึ้น
        addComponent(title, 0, 30, 1280, 60);

        // ปุ่มย้อนกลับไปหน้าแรก
        JButton backBtn = new JButton("<- BACK");
        styleButton(backBtn);
        addComponent(backBtn, 40, 30, 140, 45);
        backBtn.addActionListener(e -> {
            // เชื่อมต่อกลับไปหน้า Start (ต้องมีคลาส StartGame)
            // SceneManager.switchScene(new StartGame()); 
        });

        // สร้างการ์ดตัวละคร 3 ตัว (ปรับตำแหน่ง X ให้สมดุลขึ้น)
        addCard("Akari", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", 
                "Energetic childhood friend who's always by your side.", 120, 120);
        
        addCard("Shiori", "image\\Shiori\\fa52fd12-fbd0-4119-8341-014c0b18c47a.png", 
                "The mysterious librarian who loves quiet moments.", 515, 120);
        
        addCard("Reina", "image\\Reina\\a1fd8ce0-99fb-4881-bbee-4677d1b32676.png", 
                "The proud heiress with a hidden warm heart.", 910, 120);

        // ส่วนแสดงคำโปรย (Bio) พร้อมสีพื้นหลังจางๆ ให้อ่านง่าย
        bioLabel = new JLabel("Click on a heroine to see her story...", SwingConstants.CENTER);
        bioLabel.setForeground(new Color(255, 255, 200));
        bioLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        addComponent(bioLabel, 0, 540, 1280, 50);

        // ปุ่มยืนยันการเลือก
        selectBtn = new JButton("START STORY");
        styleButton(selectBtn);
        selectBtn.setEnabled(false);
        selectBtn.setBackground(new Color(50, 100, 50)); // สีเขียวเข้มบอกความพร้อม
        addComponent(selectBtn, 440, 600, 400, 60);

        selectBtn.addActionListener(e -> {
            if (selectedName.equals("Akari")) {
                // สร้าง Object Character และเข้าสู่เกม
                Character heroine = new Character(selectedName);
                playmain gameUI = new playmain(heroine);
                SceneManager.switchScene(gameUI);
                
                if (detailFrame != null) detailFrame.dispose();
                this.dispose();
            } else {
                // แสดงผลสำหรับรูทที่ยังไม่เปิด
                JOptionPane.showMessageDialog(this, 
                    "Currently, only Akari's route is available in this version!", 
                    "Under Development", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void addCard(String name, String path, String bio, int x, int y) {
        // กรอบรูปตัวละคร
        CharacterPanel p = new CharacterPanel(path);
        panels.add(p);
        p.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addComponent(p, x, y, 250, 380);
        
        // ปุ่มดูรายละเอียด (Detail)
        JButton det = new JButton("VIEW PROFILE");
        styleButton(det);
        det.setFont(new Font("Tahoma", Font.BOLD, 12));
        addComponent(det, x + 50, y + 395, 150, 35);
        
        det.addActionListener(e -> {
            // ป้องกันการเปิดหน้าต่างซ้ำซ้อน
            if (detailFrame != null) detailFrame.dispose();
            detailFrame = new CharacterDetailFrame(name);
        });
        
        // Mouse Events สำหรับการเลือก
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ล้างสถานะเก่า
                for(CharacterPanel cp : panels) {
                    cp.setBorder(null);
                }
                // ไฮไลท์ตัวใหม่ด้วยสี Cyan
                p.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 6));
                selectedName = name;
                
                bioLabel.setText("<html><center>" + bio + "</center></html>");
                selectBtn.setText("PROCEED WITH " + name.toUpperCase());
                selectBtn.setEnabled(true);
                selectBtn.setBackground(new Color(0, 150, 0));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selectedName.equals(name)) {
                    p.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!selectedName.equals(name)) {
                    p.setBorder(null);
                }
            }
        });
    }
}