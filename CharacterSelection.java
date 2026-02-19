import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CharacterSelection extends BaseFrame {
    private String selectedName = "";
    private JLabel bioLabel;
    private JButton selectBtn;
    private ArrayList<CharacterPanel> panels = new ArrayList<>();
    private CharacterDetailFrame detailFrame = null; // เก็บอ้างอิงหน้าต่าง Detail

    public CharacterSelection(boolean fs) {
        super("Select Heroine");
        // อย่าลืมตรวจสอบว่ามีโฟลเดอร์และไฟล์ภาพตาม Path นี้จริง
        setBackgroundImage("image/Bgscene/_front_of_classroom_1.jpg");
        setupUI();
    }

    private void setupUI() {
        // หัวข้อหน้าจอ
        JLabel title = new JLabel("Select Your Character", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        addComponent(title, 0, 30, 1280, 50);

        // ปุ่มย้อนกลับไปหน้าเมนู
        JButton backBtn = new JButton("<- BACK");
        styleButton(backBtn);
        addComponent(backBtn, 40, 30, 120, 40);
        backBtn.addActionListener(e -> {
            SceneManager.switchScene(new StartGame());
        });

        // สร้างการ์ด 3 ใบ (ชื่อ, Path รูป, คำโปรยสั้นๆ, พิกัด x, พิกัด y)
        addCard("Akari", "image/Akari/22b9ada1-d037-49df-95c0-35e2c5531ded.png", 
                "Energetic childhood friend who's always by your side.", 120, 120);
        
        addCard("Shiori", "image/Shiori/fa52fd12-fbd0-4119-8341-014c0b18c47a.png", 
                "The mysterious librarian who loves quiet moments.", 515, 120);
        
        addCard("Reina", "image/Reina/a1fd8ce0-99fb-4881-bbee-4677d1b32676.png", 
                "The proud heiress with a hidden warm heart.", 910, 120);

        // คำโปรยด้านล่าง
        bioLabel = new JLabel("Select a heroine to start your story...", SwingConstants.CENTER);
        bioLabel.setForeground(Color.WHITE);
        bioLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
        addComponent(bioLabel, 0, 550, 1280, 40);

        // ปุ่มเลือกเพื่อเข้าสู่เกม
        selectBtn = new JButton("SELECT");
        styleButton(selectBtn);
        selectBtn.setEnabled(false); // ปิดไว้ก่อนจนกว่าจะเลือกตัวละคร
        addComponent(selectBtn, 490, 610, 300, 50);

        selectBtn.addActionListener(e -> {
            if (selectedName.equals("Akari")) {
                // 1. สร้างข้อมูลตัวละครและหน้าจอเล่น (รวม Akari) โดยใช้ playmain
                Character heroine = new Character(selectedName);
                playmain gameUI = new playmain(heroine);

                // 2. สลับหน้าจอโดยใช้ SceneManager
                SceneManager.switchScene(gameUI);

                // 3. เริ่มรันเนื้อเรื่องวันที่ 1 ผ่าน StoryManager
                StoryManager.resetGame(gameUI, "Akari");
            } else {
                JOptionPane.showMessageDialog(this, "The story for " + selectedName + " is coming soon!");
            }
        });
    }

    private void addCard(String name, String path, String bio, int x, int y) {
        // สร้าง Panel รูปตัวละคร
        CharacterPanel p = new CharacterPanel(path);
        panels.add(p);
        addComponent(p, x, y, 250, 380);
        
        // สร้างปุ่ม Detail
        JButton det = new JButton("Detail");
        styleButton(det);
        det.setFont(new Font("Tahoma", Font.BOLD, 14));
        addComponent(det, x + 75, y + 390, 100, 30);
        
        // เมื่อกด Detail ให้เปิดหน้าต่างข้อมูล (CharacterDetailFrame)
        det.addActionListener(e -> {
            if (detailFrame != null && detailFrame.isDisplayable()) {
                detailFrame.toFront(); // ถ้าเปิดค้างไว้ให้เอามาไว้ด้านหน้า
            } else {
                detailFrame = new CharacterDetailFrame(name);
            }
        });
        
        // เมื่อคลิกที่รูปตัวละครเพื่อเลือก
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // ล้างขอบของตัวอื่นออก
                for(CharacterPanel cp : panels) cp.setBorder(null);
                
                // ใส่ขอบให้ตัวที่เลือก
                p.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
                selectedName = name;
                
                // อัปเดตข้อความ Bio และสถานะปุ่ม SELECT
                bioLabel.setText("<html><center>" + bio + "</center></html>");
                selectBtn.setText("CONFIRM: " + name.toUpperCase());
                selectBtn.setEnabled(true);
            }
        });
    }
}