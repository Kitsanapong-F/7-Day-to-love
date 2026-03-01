package minigameall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class SprintGame extends JPanel implements KeyListener {
    enum GameState { WAITING, COUNTDOWN, PLAYING, FINISHED }
    private GameState currentState = GameState.WAITING;

    private int p1X = 35, p2X = 35, p3X = 35;
    private final int finishLine = 1115;
    private String winnerText = "";
    
    private boolean p1Ready = false, p2Ready = false, p3Ready = false;
    private boolean p1Active = false, p2Active = false, p3Active = false;
    private boolean p1Pressed = false, p2Pressed = false, p3Pressed = false;

    private int countdownValue = 3;
    private Timer countdownTimer, gameLoopTimer;

    // --- การตั้งค่ารูปภาพ (ใส่ Path รูปของคุณตรงนี้) ---
    private Image bgImage = new ImageIcon("image\\miniGame\\Sp\\Sp_bg2.png").getImage();
    private Image p1Image = new ImageIcon("image\\miniGame\\Sp\\P1_run.png").getImage();
    private Image p2Image = new ImageIcon("image\\miniGame\\Sp\\P2_run.png").getImage();
    private Image p3Image = new ImageIcon("image\\miniGame\\Sp\\P3_run.png").getImage();

    // รูปภาพตัวละครเชียร์ (แนะนำให้ตัดรูปตัวละคร 1 ตัว เซฟเป็น PNG แบบไม่มีพื้นหลัง)
    private Image cheerImage = new ImageIcon("image\\miniGame\\Sp\\Sp_1.png").getImage();
    
    // --- ตัวแปรสำหรับแอนิเมชันกระโดดของตัวละครเชียร์ ---
    private int cheerYOffset = 0;     // ระยะกระโดดแกน Y
    private int cheerVelocity = 0;    // ความเร็วในการกระโดด
    private final int CHEER_BASE_Y = 540; // ตำแหน่งยืนแกน Y เริ่มต้น (มุมขวาล่าง)
    
    private final int CHEER_X = 950;      // ตำแหน่งแกน X ของตัวละครเชียร์
    // คลาสสำหรับเอฟเฟกต์ฝุ่น
    class Dust {
        int x, y, life, maxLife = 20;
        Dust(int x, int y) { this.x = x; this.y = y; this.life = maxLife; }
    }
    private ArrayList<Dust> particles = new ArrayList<>();

    public SprintGame() {
        setPreferredSize(new Dimension(1280, 720));
        setFocusable(true);
        addKeyListener(this);

        countdownTimer = new Timer(1000, e -> {
            countdownValue--;
            if (countdownValue < 0) {
                currentState = GameState.PLAYING;
                countdownTimer.stop();
            }
            repaint();
        });

        // Loop สำหรับอัปเดตเอฟเฟกต์ (60 FPS)
        gameLoopTimer = new Timer(16, e -> updateEffects());
        gameLoopTimer.start();
    }

    private void updateEffects() {
        Iterator<Dust> it = particles.iterator();
        while (it.hasNext()) {
            Dust d = it.next();
            d.life--;
            d.x -= 2; // ฝุ่นลอยไปข้างหลัง
            if (d.life <= 0) it.remove();
        }
        // --- อัปเดตแอนิเมชันตัวละครเชียร์กระโดด ---
        if (cheerVelocity != 0 || cheerYOffset < 0) {
            cheerYOffset += cheerVelocity;
            cheerVelocity += 2; // แรงโน้มถ่วงดึงกลับลงมา (Gravity)
            
            if (cheerYOffset >= 0) { // ถ้าตกลงมาถึงพื้นแล้ว
                cheerYOffset = 0;
                cheerVelocity = 0;
            }
        }
        
        if (currentState == GameState.PLAYING) repaint();
    }
    
    private void addDust(int x, int y) {
        particles.add(new Dust(x, y + 25)); // ให้ฝุ่นออกตรงเท้า
    }

    // ฟังก์ชันสั่งให้ตัวละครเชียร์กระโดด
    private void triggerCheerJump() {
        if (cheerYOffset >= 0) { // จะกระโดดได้ก็ต่อเมื่อยืนอยู่บนพื้นเท่านั้น
            cheerVelocity = -15; // แรงกระโดด (ยิ่งติดลบเยอะยิ่งกระโดดสูง)
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // วาดพื้นหลัง
        if (bgImage.getWidth(null) > 0) {
            g2d.drawImage(bgImage, 0, 0, 1280, 720, this);
        } else {
            g2d.setColor(new Color(34, 139, 34)); g2d.fillRect(0, 0, 1280, 720); // สีเผื่อไม่มีรูป
            g2d.setColor(new Color(205, 133, 63)); // ลู่วิ่ง
            g2d.fillRect(0, 150, 1280, 100); g2d.fillRect(0, 300, 1280, 100); g2d.fillRect(0, 450, 1280, 100);
        }

        // วาดเส้นชัย
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillRect(finishLine, 35, 20, 520);

        // วาดฝุ่นวิ่ง (Effects)
        g2d.setColor(Color.WHITE);
        for (Dust d : particles) {
            float alpha = (float) d.life / d.maxLife;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.fillOval(d.x, d.y, 15, 15);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Reset Alpha

        // ฟังก์ชันวาดตัวละครวิ่ง
        drawPlayer(g2d, p1Image, Color.RED, p1X, 90, p1Ready || currentState != GameState.WAITING);
        drawPlayer(g2d, p2Image, Color.GREEN, p2X, 245, p2Ready || currentState != GameState.WAITING);
        drawPlayer(g2d, p3Image, Color.BLUE, p3X, 400, p3Ready || currentState != GameState.WAITING);

        // --- วาดตัวละครเชียร์มุมขวาล่าง ---
        int currentCheerY = CHEER_BASE_Y + cheerYOffset; // คำนวณตำแหน่ง Y ตอนกระโดด
        if (cheerImage.getWidth(null) > 0) {
            // วาดรูปตัวละครเชียร์ ขนาดประมาณ 120x150
            g2d.drawImage(cheerImage, CHEER_X, currentCheerY, 296, 165, this);
        } else {
            // สีเผื่อไม่มีรูป จะวาดเป็นวงรีสีชมพู
            g2d.setColor(Color.PINK); 
            g2d.fillOval(CHEER_X + 35, currentCheerY + 50, 50, 100); 
        }

        // วาด UI
        if (currentState == GameState.WAITING) {
            drawOverlay(g2d, "P1(A)  |  P2(J)  |  P3(^)", "รอผู้เล่นพร้อม...", p1Ready, p2Ready, p3Ready);
        } else if (currentState == GameState.COUNTDOWN) {
            drawCountdown(g2d);
        } else if (currentState == GameState.PLAYING) {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Tahoma", Font.BOLD, 24));
            g2d.drawString("P1 กด A", 50, 80);
            g2d.drawString("P2 กด J", 50, 235);
            g2d.drawString("P3 กด ^", 50, 385);
        } else if (currentState == GameState.FINISHED) {
            g2d.setFont(new Font("Tahoma", Font.BOLD, 60));
            g2d.setColor(Color.YELLOW);
            g2d.drawString(winnerText, 350, 625);
        }
    }

    private void drawPlayer(Graphics2D g2d, Image img, Color fallbackCol, int x, int y, boolean isActive) {
        if (!isActive) g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        if (img.getWidth(null) > 0) {
            g2d.drawImage(img, x, y, 100, 100, this);
        } else {
            g2d.setColor(fallbackCol); g2d.fillOval(x, y, 50, 50);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void drawOverlay(Graphics2D g2d, String keys, String title, boolean r1, boolean r2, boolean r3) {
        g2d.setColor(new Color(0, 0, 0, 180)); g2d.fillRect(0, 0, 1280, 720);
        g2d.setColor(Color.WHITE); g2d.setFont(new Font("Tahoma", Font.BOLD, 40)); g2d.drawString(title, 480, 200);
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 30)); g2d.drawString(keys, 450, 300);
        int c = (r1?1:0) + (r2?1:0) + (r3?1:0);
        if (c >= 2) { g2d.setColor(Color.YELLOW); g2d.drawString("กด ENTER เพื่อเริ่ม (" + c + " คน)", 450, 400); }
    }

    private void drawCountdown(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150)); g2d.fillRect(0, 0, 1280, 720);
        g2d.setColor(Color.YELLOW); g2d.setFont(new Font("Tahoma", Font.BOLD, 150));
        String t = countdownValue == 0 ? "GO!" : String.valueOf(countdownValue);
        g2d.drawString(t, t.equals("GO!") ? 500 : 600, 400);
    }

    private void checkWinner() {
        if (p1Active && p1X >= finishLine) { winnerText = "Player 1 WINS!"; currentState = GameState.FINISHED; }
        else if (p2Active && p2X >= finishLine) { winnerText = "Player 2 WINS!"; currentState = GameState.FINISHED; }
        else if (p3Active && p3X >= finishLine) { winnerText = "Player 3 WINS!"; currentState = GameState.FINISHED; }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (currentState == GameState.WAITING) {
            if (key == KeyEvent.VK_A) p1Ready = true;
            if (key == KeyEvent.VK_J) p2Ready = true; 
            if (key == KeyEvent.VK_UP) p3Ready = true;
            if (((p1Ready?1:0)+(p2Ready?1:0)+(p3Ready?1:0) == 3) || (key == KeyEvent.VK_ENTER && (p1Ready?1:0)+(p2Ready?1:0)+(p3Ready?1:0) >= 2)) {
                p1Active = p1Ready; p2Active = p2Ready; p3Active = p3Ready;
                currentState = GameState.COUNTDOWN; countdownTimer.start();
            }
            repaint(); return;
        }

        if (currentState == GameState.PLAYING) {
            int step = 15;
            if (key == KeyEvent.VK_A && !p1Pressed && p1Active) { 
                p1X += step; p1Pressed = true; addDust(p1X, 130); 
                triggerCheerJump(); // <--- เรียกให้กระโดดตอนกด
            }
            if (key == KeyEvent.VK_J && !p2Pressed && p2Active) { 
                p2X += step; p2Pressed = true; addDust(p2X, 285); 
                triggerCheerJump(); // <--- เรียกให้กระโดดตอนกด
            } 
            if (key == KeyEvent.VK_UP && !p3Pressed && p3Active) { 
                p3X += step; p3Pressed = true; addDust(p3X, 440); 
                triggerCheerJump(); // <--- เรียกให้กระโดดตอนกด
            }
            checkWinner(); repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) p1Pressed = false;
        if (e.getKeyCode() == KeyEvent.VK_J) p2Pressed = false; 
        if (e.getKeyCode() == KeyEvent.VK_UP) p3Pressed = false;
    }
    @Override public void keyTyped(KeyEvent e) {}
}