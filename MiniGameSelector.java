import javax.swing.*;
import audio.AudioManager;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MiniGameSelector extends BaseFrame {
    
    private Character currentGirl;
    private String girlName;
    private int totalPlayers;
    private JPanel glassPanel; // ประกาศเป็นตัวแปรระดับคลาสเพื่อให้เข้าถึงได้จาก Listener
    
    private final String[] gameOptions = {
        "SPRINT CHALLENGE", 
        "HOT POTATO: BOMB DASH", 
        "MINI SUMO ARENA"
    };

    public MiniGameSelector(Character girl, String name, int players) {
        super("7 Days to Love - Random Mini-Game");
        this.currentGirl = girl;
        this.girlName = name;
        this.totalPlayers = players;

        if (this.totalPlayers == 1) {
            SwingUtilities.invokeLater(() -> {
                playmain finalScene = new playmain(currentGirl, 1);
                SceneManager.switchScene(finalScene);
                StoryManager.handleEnding(finalScene, girlName);
                this.dispose();
            });
            return;
        }

        setBackgroundImage("image\\cover\\Gemini_Generated_Image_q6t82lq6t82lq6t8.png"); 
        initUI();

        // เพิ่ม Listener เพื่อจัดตำแหน่งใหม่ทุกครั้งที่ขยายหน้าจอ
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                centerUI();
            }
        });
    }

    private void initUI() {
        // บังคับให้ใช้ null layout เพื่อให้ addComponent ทำงานได้ตามปกติ
        mainPanel.setLayout(null);

        glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 150)); 
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(new Color(255, 255, 255, 50)); 
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(2, 2, getWidth()-4, getHeight()-4, 30, 30);
                g2d.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setLayout(null);
        
        // เพิ่ม glassPanel เข้าไปในระบบของ BaseFrame
        // เริ่มต้นด้วยพิกัดเริ่มต้นก่อน แล้วค่อยให้ centerUI() จัดให้กึ่งกลาง
        addComponent(glassPanel, 240, 200, 800, 500);

        // ── ส่วนประกอบภายใน glassPanel ──
        JLabel infoLabel = new JLabel("Preparing challenges for " + totalPlayers + " participants...", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Tahoma", Font.ITALIC, 20));
        infoLabel.setForeground(Color.LIGHT_GRAY);
        infoLabel.setBounds(0, 100, 800, 30);
        glassPanel.add(infoLabel);

        JLabel randomGameLabel = new JLabel("READY TO SPIN?", SwingConstants.CENTER);
        randomGameLabel.setFont(new Font("Tahoma", Font.BOLD, 50));
        randomGameLabel.setForeground(new Color(0, 255, 255)); 
        randomGameLabel.setBounds(0, 200, 800, 100);
        glassPanel.add(randomGameLabel);

        JButton spinBtn = new JButton("START ROULETTE");
        spinBtn.setFont(new Font("Tahoma", Font.BOLD, 24));
        spinBtn.setFocusPainted(false);
        spinBtn.setBackground(new Color(255, 69, 0)); 
        spinBtn.setForeground(Color.WHITE);
        spinBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        spinBtn.setBounds(250, 350, 300, 70);
        
        spinBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { spinBtn.setBackground(new Color(255, 140, 0)); }
            public void mouseExited(MouseEvent e) { spinBtn.setBackground(new Color(255, 69, 0)); }
        });

        spinBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_click.wav");
            spinBtn.setEnabled(false); 
            spinBtn.setVisible(false);
            startRandomizer(randomGameLabel);
        });
        glassPanel.add(spinBtn);

        centerUI(); // เรียกจัดกึ่งกลางครั้งแรก
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // ฟังก์ชันคำนวณตำแหน่งกึ่งกลางจอ
    private void centerUI() {
        if (glassPanel != null) {
            int x = (this.getWidth() - 800) / 2;
            int y = (this.getHeight() - 500) / 2 + 50; // +50 เพื่อขยับลงมาหน่อยตามที่คุณต้องการ
            glassPanel.setBounds(x, y, 800, 500);
        }
    }

    private void startRandomizer(JLabel displayLabel) {
        Random rand = new Random();
        int finalGameIndex = rand.nextInt(gameOptions.length); 
        
        Timer timer = new Timer(80, null); 
        final int[] ticks = {0};
        final int maxTicks = 30; 

        timer.addActionListener(e -> {
            ticks[0]++;
            int tempIndex = rand.nextInt(gameOptions.length);
            displayLabel.setText(gameOptions[tempIndex]);
            displayLabel.setForeground(new Color(rand.nextInt(256), 255, rand.nextInt(256)));
            AudioManager.playSound("undertale_type.wav"); 

            if (ticks[0] >= maxTicks) {
                timer.stop();
                displayLabel.setText("▶ " + gameOptions[finalGameIndex] + " ◀");
                displayLabel.setForeground(Color.YELLOW);
                displayLabel.setFont(new Font("Tahoma", Font.BOLD, 55)); 
                AudioManager.playSound("umamusume_click.wav");
                
                Timer blink = new Timer(200, null);
                final int[] blinkCount = {0};
                blink.addActionListener(ev -> {
                    displayLabel.setVisible(!displayLabel.isVisible());
                    if (blinkCount[0]++ > 5) {
                        blink.stop();
                        displayLabel.setVisible(true);
                        launchMiniGame(finalGameIndex);
                    }
                });
                blink.start();
            }
        });
        timer.start();
    }

    private void launchMiniGame(int gameIndex) {
        Timer delay = new Timer(1000, e -> {
            JFrame gameFrame = new JFrame(gameOptions[gameIndex]);
            gameFrame.setBounds(this.getBounds()); 

            JPanel gamePanel = null;
            if (gameIndex == 0) gamePanel = new SprintGameV2(currentGirl, girlName, totalPlayers);
            else if (gameIndex == 1) gamePanel = new HotPotatoGame(currentGirl, girlName, totalPlayers);
            else if (gameIndex == 2) gamePanel = new SumoGame(currentGirl, girlName, totalPlayers);

            if (gamePanel == null) gamePanel = new SprintGameV2(currentGirl, girlName, totalPlayers);

            gameFrame.add(gamePanel);
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setLocationRelativeTo(null); 
            gameFrame.setVisible(true);
            this.dispose(); 
        });
        delay.setRepeats(false);
        delay.start();
    }

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         MiniGameSelector testFrame = new MiniGameSelector(null, "Test Player", 3);
    //         testFrame.setSize(1280, 720);
    //         testFrame.setLocationRelativeTo(null);
    //         testFrame.setVisible(true);
    //     });
    // }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {}
}