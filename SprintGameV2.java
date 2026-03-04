import javax.swing.*;
import audio.AudioManager;
import audio.BGMManager;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * SprintGame: มินิเกมวิ่งแข่งสำหรับผู้เล่น 1-3 คน
 * ใช้ KeyListener ในการรับค่าการกดรัวปุ่มเพื่อเคลื่อนที่ตัวละคร
 */
public class SprintGameV2 extends JPanel implements KeyListener {
    enum GameState { WAITING, COUNTDOWN, PLAYING, FINISHED }
    private GameState currentState = GameState.WAITING;

    private Character targetGirl;
    private String targetName;
    private int totalPlayers;

    private int p1X = 35, p2X = 35, p3X = 35;
    private final int finishLine = 1115;

    private ArrayList<Integer> finisherOrder = new ArrayList<>();

    private boolean p1Ready = false, p2Ready = false, p3Ready = false;
    private boolean p1Active = false, p2Active = false, p3Active = false;
    private boolean p1Pressed = false, p2Pressed = false, p3Pressed = false;

    private int countdownValue = 3;
    private Timer countdownTimer, gameLoopTimer;

    private Image bgImage    = new ImageIcon("image\\miniGame\\Sp\\Sp_bg2.png").getImage();
    private Image p1Image    = new ImageIcon("image\\miniGame\\Sp\\P1_run.png").getImage();
    private Image p2Image    = new ImageIcon("image\\miniGame\\Sp\\P2_run.png").getImage();
    private Image p3Image    = new ImageIcon("image\\miniGame\\Sp\\P3_run.png").getImage();
    private Image cheerImage = new ImageIcon("image\\miniGame\\Sp\\Sp_1.png").getImage();

    private int cheerYOffset = 0;
    private int cheerVelocity = 0;
    private final int CHEER_BASE_Y = 540;
    private final int CHEER_X = 950;

    class Dust {
        int x, y, life, maxLife = 20;
        Dust(int x, int y) { this.x = x; this.y = y; this.life = maxLife; }
    }
    private ArrayList<Dust> particles = new ArrayList<>();

    // ─────────────────────────────────────────────
    public SprintGameV2(Character girl, String name, int playerCount) {
        this.targetGirl   = girl;
        this.targetName   = name;
        this.totalPlayers = playerCount;

        setPreferredSize(new Dimension(1280, 720));
        setFocusable(true);
        addKeyListener(this);

        // ▶ V2: โหลดและเริ่มเล่น BGM ทันทีที่เข้าหน้านี้
        BGMManager.playBGM("BG_minigame.wav");

        countdownTimer = new Timer(1000, e -> {
            countdownValue--;
            if (countdownValue > 0) {
                AudioManager.playSound("umamusume_back.wav"); // เสียงนับ 2, 1
            } else if (countdownValue == 0) {
                AudioManager.playSound("umamusume_con.wav");  // เสียง GO!
            } else {
                currentState = GameState.PLAYING;
                countdownTimer.stop();
            }
            repaint();
        });

        gameLoopTimer = new Timer(16, e -> updateEffects());
        gameLoopTimer.start();
    }

    // ─────────────────────────────────────────────
    private void updateEffects() {
        Iterator<Dust> it = particles.iterator();
        while (it.hasNext()) {
            Dust d = it.next();
            d.life--;
            d.x -= 2;
            if (d.life <= 0) it.remove();
        }

        if (cheerVelocity != 0 || cheerYOffset < 0) {
            cheerYOffset += cheerVelocity;
            cheerVelocity += 2;
            if (cheerYOffset >= 0) {
                cheerYOffset = 0;
                cheerVelocity = 0;
            }
        }

        if (currentState == GameState.PLAYING) repaint();
    }

    // ▶ V2: ฝุ่นกระจายแบบสุ่มแทนที่จะอยู่จุดเดิมตลอด
    private void addDust(int x, int y) {
        int randomXOffset = (int)(Math.random() * 10) - 5;
        int randomYOffset = (int)(Math.random() * 10) - 5;
        particles.add(new Dust(x + randomXOffset, y + 25 + randomYOffset));
    }

    private void triggerCheerJump() {
        if (cheerYOffset >= 0) cheerVelocity = -15;
    }

    // ─────────────────────────────────────────────
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // ── ฉากหลัง ──────────────────────────────
        if (bgImage.getWidth(null) > 0) {
            g2d.drawImage(bgImage, 0, 0, 1280, 720, this);
        } else {
            g2d.setColor(new Color(34, 139, 34));
            g2d.fillRect(0, 0, 1280, 720);
            g2d.setColor(new Color(205, 133, 63));
            g2d.fillRect(0, 150, 1280, 100);
            g2d.fillRect(0, 300, 1280, 100);
            g2d.fillRect(0, 450, 1280, 100);
        }

        // ── เส้นชัย ───────────────────────────────
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(finishLine, 35, 20, 520);

        // ── ฝุ่น ──────────────────────────────────
        for (Dust d : particles) {
            float alpha = (float) d.life / d.maxLife;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.setColor(Color.WHITE);
            g2d.fillOval(d.x, d.y, 15, 15);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        // ── ตัวละคร ─ V2: WAITING โชว์ทุกคน (ลางๆ ถ้ายังไม่ Ready), หลังจากนั้นโชว์เฉพาะ Active ──
        if (currentState == GameState.WAITING) {
            if (totalPlayers >= 1) drawPlayer(g2d, p1Image, Color.RED,   p1X, 90,  p1Ready);
            if (totalPlayers >= 2) drawPlayer(g2d, p2Image, Color.GREEN, p2X, 245, p2Ready);
            if (totalPlayers >= 3) drawPlayer(g2d, p3Image, Color.BLUE,  p3X, 400, p3Ready);
        } else {
            if (p1Active) drawPlayer(g2d, p1Image, Color.RED,   p1X, 90,  true);
            if (p2Active) drawPlayer(g2d, p2Image, Color.GREEN, p2X, 245, true);
            if (p3Active) drawPlayer(g2d, p3Image, Color.BLUE,  p3X, 400, true);
        }

        // ── กองเชียร์ ─ V2: แสดงเฉพาะตอน PLAYING ──
        if (currentState == GameState.PLAYING) {
            int currentCheerY = CHEER_BASE_Y + cheerYOffset;
            if (cheerImage.getWidth(null) > 0) {
                g2d.drawImage(cheerImage, CHEER_X, currentCheerY, 296, 165, this);
            } else {
                g2d.setColor(Color.PINK);
                g2d.fillOval(CHEER_X + 35, currentCheerY + 50, 50, 100);
            }
        }

        // ── UI ตามสถานะ ───────────────────────────
        if (currentState == GameState.WAITING) {
            String keys = "P1(A)";
            if (totalPlayers >= 2) keys += "  |  P2(J)";
            if (totalPlayers >= 3) keys += "  |  P3(^)";
            drawOverlay(g2d, keys, "รอผู้เล่นพร้อม...", p1Ready, p2Ready, p3Ready);

        } else if (currentState == GameState.COUNTDOWN) {
            drawCountdown(g2d);

        } else if (currentState == GameState.PLAYING) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Tahoma", Font.BOLD, 24));
            if (p1Active) g2d.drawString("P1 กด A", 50, 80);
            if (p2Active) g2d.drawString("P2 กด J", 50, 235);
            if (p3Active) g2d.drawString("P3 กด ^", 50, 385);

        } else if (currentState == GameState.FINISHED) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, 1280, 720);

            g2d.setFont(new Font("Tahoma", Font.BOLD, 60));
            g2d.setColor(Color.YELLOW);
            g2d.drawString("สรุปอันดับการแข่ง!", 400, 150);

            g2d.setFont(new Font("Tahoma", Font.BOLD, 40));
            Color[] textColors = { Color.WHITE, Color.RED, Color.GREEN, new Color(100, 150, 255) };

            for (int i = 0; i < finisherOrder.size(); i++) {
                int playerNum = finisherOrder.get(i);
                g2d.setColor(textColors[playerNum]);
                g2d.drawString("อันดับที่ " + (i + 1) + " : Player " + playerNum, 480, 260 + (i * 80));
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Tahoma", Font.PLAIN, 24));
            g2d.drawString("กำลังเตรียมหน้าสรุปคะแนนรวม...", 480, 600);
        }
    }

    // ─────────────────────────────────────────────
    private void drawPlayer(Graphics2D g2d, Image img, Color fallbackCol, int x, int y, boolean isActive) {
        if (!isActive)
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        if (img.getWidth(null) > 0)
            g2d.drawImage(img, x, y, 100, 100, this);
        else {
            g2d.setColor(fallbackCol);
            g2d.fillOval(x, y, 50, 50);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    // ▶ V2: แสดงสถานะ Ready/Waiting รายคน + ปุ่ม ENTER เมื่อมีคนพร้อม >= 2
    private void drawOverlay(Graphics2D g2d, String keys, String title, boolean r1, boolean r2, boolean r3) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, 1280, 720);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 40));
        g2d.drawString(title, 480, 200);
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 30));
        g2d.drawString(keys, 450, 300);

        int readyCount = (r1 ? 1 : 0) + (r2 ? 1 : 0) + (r3 ? 1 : 0);
        if (readyCount >= 2) {
            g2d.setColor(Color.YELLOW);
            g2d.drawString("กด ENTER เพื่อเริ่ม (" + readyCount + " คน)", 450, 400);
        }

        // ▶ V2: แสดงสถานะรายคน
        g2d.setFont(new Font("Tahoma", Font.BOLD, 24));
        if (totalPlayers >= 1) {
            g2d.setColor(r1 ? Color.GREEN : Color.RED);
            g2d.drawString("P1: " + (r1 ? "Ready" : "Waiting"), 300, 500);
        }
        if (totalPlayers >= 2) {
            g2d.setColor(r2 ? Color.GREEN : Color.RED);
            g2d.drawString("P2: " + (r2 ? "Ready" : "Waiting"), 600, 500);
        }
        if (totalPlayers >= 3) {
            g2d.setColor(r3 ? Color.GREEN : Color.RED);
            g2d.drawString("P3: " + (r3 ? "Ready" : "Waiting"), 900, 500);
        }
    }

    private void drawCountdown(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, 1280, 720);
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 150));
        String t = countdownValue == 0 ? "GO!" : String.valueOf(countdownValue);
        g2d.drawString(t, t.equals("GO!") ? 500 : 600, 400);
    }

    // ─────────────────────────────────────────────
    private void checkFinishers() {
        if (p1Active && p1X >= finishLine && !finisherOrder.contains(1)) finisherOrder.add(1);
        if (p2Active && p2X >= finishLine && !finisherOrder.contains(2)) finisherOrder.add(2);
        if (p3Active && p3X >= finishLine && !finisherOrder.contains(3)) finisherOrder.add(3);

        int activePlayers = (p1Active ? 1 : 0) + (p2Active ? 1 : 0) + (p3Active ? 1 : 0);
        if (finisherOrder.size() == activePlayers) {
            currentState = GameState.FINISHED;
            gameLoopTimer.stop();
            BGMManager.stopBGM();
            AudioManager.playSound("win.wav");

            int[] bonus = new int[totalPlayers];
            for (int i = 0; i < finisherOrder.size(); i++) {
                int pIdx = finisherOrder.get(i) - 1;
                if (pIdx < bonus.length) {
                    bonus[pIdx] = (i == 0) ? 30 : (i == 1) ? 20 : 10;
                }
            }

            Timer delay = new Timer(2000, e -> {
                Window win = SwingUtilities.getWindowAncestor(this);
                if (win != null) win.dispose();
                SceneManager.switchScene(new ScoreBoard(targetGirl, targetName, bonus));
            });
            delay.setRepeats(false);
            delay.start();
        }
    }

    // ─────────────────────────────────────────────
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (currentState == GameState.WAITING) {
            if (key == KeyEvent.VK_A  && totalPlayers >= 1 && !p1Ready) {
                p1Ready = true; AudioManager.playSound("umamusume_con.wav");
            }
            if (key == KeyEvent.VK_J  && totalPlayers >= 2 && !p2Ready) {
                p2Ready = true; AudioManager.playSound("umamusume_con.wav");
            }
            if (key == KeyEvent.VK_UP && totalPlayers >= 3 && !p3Ready) {
                p3Ready = true; AudioManager.playSound("umamusume_con.wav");
            }

            int readyCount = (p1Ready ? 1 : 0) + (p2Ready ? 1 : 0) + (p3Ready ? 1 : 0);

            // ▶ V2: เริ่มอัตโนมัติเมื่อครบ 3 คน หรือกด ENTER เมื่อมีคนพร้อม >= 2
            boolean autoStart  = (readyCount == 3);
            boolean enterStart = (key == KeyEvent.VK_ENTER && readyCount >= 2);

            if (autoStart || enterStart) {
                p1Active = p1Ready; p2Active = p2Ready; p3Active = p3Ready;
                currentState = GameState.COUNTDOWN;
                AudioManager.playSound("umamusume_back.wav"); // เสียงเลข 3
                countdownTimer.start();
            }
            repaint();
            return;
        }

        if (currentState == GameState.PLAYING) {
            int step = 15;
            if (key == KeyEvent.VK_A  && !p1Pressed && p1Active) {
                p1X += step; p1Pressed = true; addDust(p1X, 130); triggerCheerJump();
            }
            if (key == KeyEvent.VK_J  && !p2Pressed && p2Active) {
                p2X += step; p2Pressed = true; addDust(p2X, 285); triggerCheerJump();
            }
            if (key == KeyEvent.VK_UP && !p3Pressed && p3Active) {
                p3X += step; p3Pressed = true; addDust(p3X, 440); triggerCheerJump();
            }
            checkFinishers();
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A)  p1Pressed = false;
        if (e.getKeyCode() == KeyEvent.VK_J)  p2Pressed = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) p3Pressed = false;
    }

    @Override public void keyTyped(KeyEvent e) {}
}