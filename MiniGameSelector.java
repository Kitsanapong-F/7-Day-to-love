import javax.swing.*;
import audio.AudioManager;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * MiniGameSelector: หน้าจอสำหรับสุ่มมินิเกมตัดสินชะตาในช่วงวันที่ 7
 * รับข้อมูลตัวละครและจำนวนผู้เล่นเพื่อส่งต่อข้อมูลไปยังมินิเกมที่สุ่มได้
 */
public class MiniGameSelector extends BaseFrame {
    
    private Character currentGirl;
    private String girlName;
    private int totalPlayers;
    
    // รายชื่อมินิเกมที่มีในระบบ (สามารถเพิ่มหรือลดได้ที่นี่)
    private final String[] gameOptions = {
        "Sprint Challenge", 
        "Hot Potato: Bomb Dash", 
        "Mini Sumo Arena"
    };

    public MiniGameSelector(Character girl, String name, int players) {
        super("7 Days to Love - Random Mini-Game");
        this.currentGirl = girl;
        this.girlName = name;
        this.totalPlayers = players;

        // ข้ามไปฉากจบเลยถ้าเล่นคนเดียว
        if (this.totalPlayers == 1) {
            SwingUtilities.invokeLater(() -> {
                playmain finalScene = new playmain(currentGirl, 1);
                SceneManager.switchScene(finalScene);
                StoryManager.handleEnding(finalScene, girlName);
                this.dispose();
            });
            return;
        }

        setBackgroundImage("image\\Place\\_school_in_spring_2.jpg"); 
        initUI();
    }

    private void initUI() {
        JLabel titleLabel = new JLabel("DECIDING YOUR FINAL CHALLENGE...", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 42));
        titleLabel.setForeground(new Color(255, 215, 0));
        addComponent(titleLabel, 0, 100, 1280, 80);

        JLabel infoLabel = new JLabel("Final Challenge for " + totalPlayers + " Player(s)", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Tahoma", Font.ITALIC, 22));
        infoLabel.setForeground(Color.WHITE);
        addComponent(infoLabel, 0, 180, 1280, 40);

        // ป้ายแสดงชื่อเกมที่จะสุ่ม
        JLabel randomGameLabel = new JLabel("???", SwingConstants.CENTER);
        randomGameLabel.setFont(new Font("Tahoma", Font.BOLD, 55));
        randomGameLabel.setForeground(Color.CYAN);
        addComponent(randomGameLabel, 0, 320, 1280, 100);

        // ปุ่มสำหรับกดเพื่อเริ่มสุ่ม
        JButton spinBtn = new JButton("SPIN ROULETTE!");
        styleButton(spinBtn);
        int spinBtnWidth = 300;
        int spinCenterX = (1280 - spinBtnWidth) / 2;
        addComponent(spinBtn, spinCenterX, 480, spinBtnWidth, 60);

        spinBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_click.wav");
            spinBtn.setVisible(false); // ซ่อนปุ่มหลังกดแล้ว
            startRandomizer(randomGameLabel);
        });

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    /**
     * ระบบแอนิเมชันสลับชื่อเกมไปมา และหยุดที่ผลลัพธ์สุดท้าย
     */
    private void startRandomizer(JLabel displayLabel) {
        Random rand = new Random();
        int finalGameIndex = rand.nextInt(gameOptions.length); // เลือกล็อกมงผลลัพธ์ไว้ก่อน
        
        Timer timer = new Timer(100, null); // ความเร็วในการกระพริบสลับชื่อ
        final int[] ticks = {0};
        final int maxTicks = 25; // จำนวนรอบที่กระพริบก่อนหยุด

        timer.addActionListener(e -> {
            ticks[0]++;
            // สุ่มโชว์ชื่อมั่วๆ ระหว่างหมุน
            int tempIndex = rand.nextInt(gameOptions.length);
            displayLabel.setText(gameOptions[tempIndex]);
            
            // ใช้เสียงพิมพ์ตอนหมุนรูเล็ต (สามารถเปลี่ยนเป็นเสียงอื่นได้)
            AudioManager.playSound("undertale_type.wav"); 

            if (ticks[0] >= maxTicks) {
                timer.stop();
                // แสดงผลลัพธ์ที่แท้จริง
                displayLabel.setText(gameOptions[finalGameIndex]);
                displayLabel.setForeground(Color.YELLOW);
                AudioManager.playSound("umamusume_click.wav"); // เสียงตอนยืนยันผล
                
                // หน่วงเวลาให้ผู้เล่นดูชื่อเกม 1.5 วินาที ก่อนตัดเข้ามินิเกม
                Timer delay = new Timer(1500, evt -> launchMiniGame(finalGameIndex));
                delay.setRepeats(false);
                delay.start();
            }
        });
        timer.start();
    }

    /**
     * ดึงหน้าต่างมินิเกมตามลำดับที่สุ่มได้
     */
    private void launchMiniGame(int gameIndex) {
        System.out.println("[System] Launching Game: " + gameOptions[gameIndex]);
        
        JFrame gameFrame = new JFrame(gameOptions[gameIndex] + " - Final Countdown");
        JPanel gamePanel = null;

        // สลับเข้าคลาสของมินิเกมแต่ละตัว
        if (gameIndex == 0) {
            gamePanel = new HotPotatoGame(currentGirl, girlName, totalPlayers);
        } else if (gameIndex == 1) {
            gamePanel = new HotPotatoGame(currentGirl, girlName, totalPlayers);
        } else if (gameIndex == 2) {
            gamePanel = new HotPotatoGame(currentGirl, girlName, totalPlayers);
        }

        // ระบบ Fallback ป้องกันเกมพัง: ถ้าสุ่มได้เกมที่ยังเขียนคลาสไม่เสร็จ จะดึง SprintGame มาเล่นแทนชั่วคราว
        if (gamePanel == null) {
            System.out.println("[System] Game class not instantiated yet. Fallback to SprintGame.");
            gamePanel = new HotPotatoGame(currentGirl, girlName, totalPlayers);
        }

        gameFrame.add(gamePanel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        
        this.dispose(); // ปิดหน้าสุ่ม
    }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {}
}