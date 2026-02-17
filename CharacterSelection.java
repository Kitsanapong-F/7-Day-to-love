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

<<<<<<< HEAD
    public CharacterSelection(boolean startFullscreen) {
        this.isFullscreen = startFullscreen;
        setTitle("7 Days to Love - Character Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        applyFullscreenState();

        BackgroundPanel bg = new BackgroundPanel("image\\Bgscene\\_front_of_classroom_1.jpg");
        bg.setLayout(new BorderLayout());

        // 1. TOP: Navigation
        JButton backBtn = new JButton("<- BACK TO MENU");
        backBtn.setFocusable(false);
        backBtn.addActionListener(e -> {

           new StartGame(); //แก้เป็นหน้าเกมที่ นายต้นทำไว้
            this.dispose();
        });
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        top.add(backBtn);
        bg.add(top, BorderLayout.NORTH);

        // 2. CENTER: Character Grid
        JPanel centerGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        centerGrid.setOpaque(false);
        centerGrid.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        // Using your specific file names from the screenshot
        centerGrid.add(createGirlCard("Akari", "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png", "Your energetic childhood friend."));
        centerGrid.add(createGirlCard("Shiori", "image\\Shiori\\fa52fd12-fbd0-4119-8341-014c0b18c47a.png", "The quiet, mysterious librarian."));
        centerGrid.add(createGirlCard("Reina", "image\\Reina\\a1fd8ce0-99fb-4881-bbee-4677d1b32676.png", "The proud school heiress."));

        bg.add(centerGrid, BorderLayout.CENTER);

        // 3. SOUTH: Bio and Select (Below the characters)
        JPanel bottomUI = new JPanel();
        bottomUI.setLayout(new BoxLayout(bottomUI, BoxLayout.Y_AXIS));
        bottomUI.setOpaque(false);

        bioLabel = new JLabel("Click a heroine to learn her story...", SwingConstants.CENTER);
        bioLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        bioLabel.setForeground(Color.WHITE);
        bioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        selectBtn = new JButton("SELECT");
        selectBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        selectBtn.setEnabled(false);
        selectBtn.setFocusable(false);
        selectBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomUI.add(bioLabel);
        bottomUI.add(Box.createRigidArea(new Dimension(0, 15)));
        bottomUI.add(selectBtn);
        bottomUI.add(Box.createRigidArea(new Dimension(0, 40)));
        bg.add(bottomUI, BorderLayout.SOUTH);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F11) toggleFullscreen();
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) System.exit(0);
            }
        });

        add(bg);
        setVisible(true);
        this.requestFocus();
=======
    public CharacterSelection(boolean fs) {
        super("Select Heroine");
        // อย่าลืมตรวจสอบว่ามีโฟลเดอร์และไฟล์ภาพตาม Path นี้จริง
        setBackgroundImage("image/Bgscene/_front_of_classroom_1.jpg");
        setupUI();
>>>>>>> 54a9fcba7aa7ee1f9be818c1ad60dfdadec9d406
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
            JOptionPane.showMessageDialog(this, "Starting your 7 days with " + selectedName);
            // ตรงนี้ให้คุณเชื่อมต่อไปยังหน้า VisualNovelUI (หน้าเล่นเกม) ในอนาคต
            // SceneManager.switchScene(new VisualNovelUI(selectedName));
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