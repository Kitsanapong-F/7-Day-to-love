import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class CharacterSelection extends JFrame {

    // ✅ เก็บ reference หน้าต่างรายละเอียด → เพื่อให้เปิดได้แค่หน้าต่างเดียว
    private CharacterDetailFrame detailFrame = null;

    // ✅ สถานะ fullscreen ของหน้าจอ
    private boolean isFullscreen;

    // ✅ label แสดง bio ใต้รูปตัวละคร
    private JLabel bioLabel;

    // ✅ ปุ่ม select ตัวละคร
    private JButton selectBtn;

    // ✅ ชื่อที่เลือกอยู่ปัจจุบัน (ใช้ toggle เลือก/ยกเลิก)
    private String selectedName = "";

    // ✅ เก็บ CharacterPanel ทุกตัว → ใช้ล้าง border ตอนเลือกตัวใหม่
    private ArrayList<CharacterPanel> characterPanels = new ArrayList<>();


    public CharacterSelection(boolean startFullscreen) {
        this.isFullscreen = startFullscreen;

        setTitle("7 Days to Love - Character Selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ✅ ใช้โหมด fullscreen / window ตาม flag
        applyFullscreenState();

        // ✅ พื้นหลังภาพ (เปลี่ยนไฟล์ได้ง่ายในอนาคต)
        BackgroundPanel bg = new BackgroundPanel("image\\Bgscene\\_front_of_classroom_1.jpg");
        bg.setLayout(new BorderLayout());


        // =========================
        // TOP BAR (Back + Title)
        // =========================

        JButton backBtn = new JButton("<- BACK TO MENU");
        backBtn.setFocusable(false);

        // ✅ กลับหน้า StartGame
        backBtn.addActionListener(e -> {
            new StartGame();
            this.dispose();
        });

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        topBar.add(backBtn, BorderLayout.WEST);

        // ✅ หัวข้อหน้า
        JLabel titleLabel = new JLabel("Select Character", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        titleLabel.setForeground(Color.WHITE);

        topBar.add(titleLabel, BorderLayout.CENTER);

        bg.add(topBar, BorderLayout.NORTH);


        // =========================
        // CENTER GRID (ตัวละคร)
        // =========================

        JPanel centerGrid = new JPanel(new GridLayout(1, 3, 20, 0));
        centerGrid.setOpaque(false);
        centerGrid.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        // ✅ สร้างการ์ดตัวละคร (ชื่อ / รูป / bio)
        centerGrid.add(createGirlCard("Akari",
                "image\\Akari\\22b9ada1-d037-49df-95c0-35e2c5531ded.png",
                "Your energetic childhood friend."));

        centerGrid.add(createGirlCard("Shiori",
                "image\\Shiori\\fa52fd12-fbd0-4119-8341-014c0b18c47a.png",
                "The quiet, mysterious librarian."));

        centerGrid.add(createGirlCard("Reina",
                "image\\Reina\\a1fd8ce0-99fb-4881-bbee-4677d1b32676.png",
                "The proud school heiress."));

        bg.add(centerGrid, BorderLayout.CENTER);


        // =========================
        // BOTTOM UI (bio + select)
        // =========================

        JPanel bottomUI = new JPanel();
        bottomUI.setLayout(new BoxLayout(bottomUI, BoxLayout.Y_AXIS));
        bottomUI.setOpaque(false);

        // ✅ bio text เปลี่ยนตามตัวละครที่เลือก
        bioLabel = new JLabel("Click a heroine to learn her story...", SwingConstants.CENTER);
        bioLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        bioLabel.setForeground(Color.WHITE);
        bioLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ✅ ปุ่ม select — เปิดใช้เมื่อมีตัวละครถูกเลือก
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


        // =========================
        // KEY CONTROL
        // =========================

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // ✅ F11 toggle fullscreen
                if (e.getKeyCode() == KeyEvent.VK_F11)
                    toggleFullscreen();

                // ✅ ESC ออกจากเกม
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    System.exit(0);
            }
        });

        add(bg);
        setVisible(true);
        this.requestFocus();
    }

    // =====================================
    // CREATE CHARACTER CARD
    // =====================================
    private JPanel createGirlCard(String name, String path, String bio) {

        JPanel card = new JPanel(new BorderLayout());
        card.setOpaque(false);

        // ✅ ชื่อตัวละครบนรูป
        JLabel nameLabel = new JLabel(name, SwingConstants.CENTER);
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        nameLabel.setForeground(Color.WHITE);

        CharacterPanel portrait = new CharacterPanel(path);

        // ✅ เก็บไว้ใช้ล้าง selection
        characterPanels.add(portrait);

        // ---------- DETAIL BUTTON ----------
        JButton detailBtn = new JButton("Detail");
        detailBtn.setFocusable(false);

        // ✅ เปิด detail ได้หลายครั้ง แต่มีได้แค่หน้าต่างเดียว
        detailBtn.addActionListener(e -> {

            if (detailFrame != null && detailFrame.isDisplayable()) {
                detailFrame.toFront();
                detailFrame.requestFocus();
                return;
            }

            detailFrame = new CharacterDetailFrame(name);
        });

        // ---------- CLICK SELECT ----------
        portrait.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                // ✅ toggle off ถ้าคลิกซ้ำตัวเดิม
                if (selectedName.equals(name)) {

                    portrait.setBorder(null);
                    selectedName = "";

                    bioLabel.setText("Click a heroine to learn her story...");
                    selectBtn.setEnabled(false);
                    selectBtn.setText("SELECT");

                    repaint();
                    return;
                }

                // ✅ clear selection ตัวอื่น
                for (CharacterPanel p : characterPanels)
                    p.setBorder(null);

                // ✅ set selection ใหม่
                selectedName = name;
                portrait.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));

                bioLabel.setText("<html><center>" + bio + "</center></html>");
                selectBtn.setText("SPEND 7 DAYS WITH " + name.toUpperCase());
                selectBtn.setEnabled(true);

                repaint();
            }
        });

        card.add(nameLabel, BorderLayout.NORTH);
        card.add(portrait, BorderLayout.CENTER);
        card.add(detailBtn, BorderLayout.SOUTH);

        return card;
    }

    // =====================================
    // FULLSCREEN CONTROL
    // =====================================
    private void applyFullscreenState() {

        GraphicsDevice gd =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice();

        if (isFullscreen) {
            dispose();
            setUndecorated(true);
            gd.setFullScreenWindow(this);
        } else {
            dispose();
            setUndecorated(false);
            gd.setFullScreenWindow(null);
            setSize(1280, 720);
            setLocationRelativeTo(null);
        }
    }

    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        applyFullscreenState();
        setVisible(true);
        this.requestFocus();
    }
}


// =====================================
// CHARACTER PANEL (รูป + hover + outline)
// =====================================
class CharacterPanel extends JPanel {

    private Image img;
    private boolean hover = false;

    public CharacterPanel(String path) {

        // ✅ โหลดรูปจาก path
        this.img = new ImageIcon(path).getImage();

        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // ✅ hover detection
        addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // ✅ hover outline (ไม่ทับพื้นหลัง)
        if (hover && getBorder() == null) {
            g2.setColor(new Color(255, 255, 255, 180));
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(3, 3,
                    getWidth()-6, getHeight()-6,
                    28, 28);
        }

        // ✅ selected highlight layer
        if (getBorder() != null) {
            g2.setColor(new Color(0, 200, 255, 110));
            g2.fillRoundRect(0, 0,
                    getWidth(), getHeight(),
                    30, 30);
        }

        // ✅ scale รูปตาม panel (รักษาอัตราส่วน)
        if (img != null) {
            int h = (int) (getHeight() * 0.95);
            double ratio = (double) h / img.getHeight(null);
            int w = (int) (img.getWidth(null) * ratio);

            int x = (getWidth() - w) / 2;
            int y = getHeight() - h;

            g.drawImage(img, x, y, w, h, this);
        }
    }
}