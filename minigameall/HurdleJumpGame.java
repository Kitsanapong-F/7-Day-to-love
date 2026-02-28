package minigameall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class HurdleJumpGame extends JPanel implements KeyListener, ActionListener {
    enum GameState { WAITING, COUNTDOWN, PLAYING, FINISHED }
    private GameState currentState = GameState.WAITING;

    private int[] playerX = {50, 50, 50};
    private int[] playerY = {175, 325, 475};
    private int[] velocityY = {0, 0, 0};
    private int[] speedX = {4, 4, 4}; 
    private int[] stunTimer = {0, 0, 0}; 
    
    private final int[] baseFloor = {175, 325, 475};
    private final int gravity = 2;
    private final int jumpForce = -20;
    private final int finishLine = 1100;
    
    private boolean[] pReady = {false, false, false};
    private boolean[] pActive = {false, false, false};
    
    private String winnerText = "";
    private Timer timer;
    private int countdownValue = 3;
    private int frameCounter = 0; // ใช้สำหรับนับเวลาถอยหลัง

    private ArrayList<Rectangle> hurdles = new ArrayList<>();

    public HurdleJumpGame() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(new Color(135, 206, 235));
        setFocusable(true);
        addKeyListener(this);

        for (int i = 300; i < finishLine; i += 250) {
            hurdles.add(new Rectangle(i, 195, 20, 30)); 
            hurdles.add(new Rectangle(i + 50, 345, 20, 30)); 
            hurdles.add(new Rectangle(i - 50, 495, 20, 30)); 
        }

        timer = new Timer(20, this); // 50 frames = 1 วินาที
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // วาดลู่วิ่งและเส้นชัย
        g2d.setColor(new Color(200, 100, 100));
        g2d.fillRect(0, 225, 1280, 10);
        g2d.fillRect(0, 375, 1280, 10);
        g2d.fillRect(0, 525, 1280, 10);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(finishLine, 100, 20, 500);

        // วาดรั้ว
        g2d.setColor(Color.DARK_GRAY);
        for (Rectangle h : hurdles) g2d.fillRect(h.x, h.y, h.width, h.height);

        // วาดผู้เล่น
        Color[] pColors = {Color.RED, Color.GREEN, Color.BLUE};
        for (int i = 0; i < 3; i++) {
            if (currentState == GameState.WAITING && !pReady[i]) {
                g2d.setColor(new Color(pColors[i].getRed(), pColors[i].getGreen(), pColors[i].getBlue(), 100));
            } else {
                g2d.setColor(stunTimer[i] > 0 ? Color.GRAY : pColors[i]);
            }
            g2d.fillOval(playerX[i], playerY[i], 50, 50);
        }

        // หน้าจอสถานะต่างๆ
        if (currentState == GameState.WAITING) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, 1280, 720);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Tahoma", Font.BOLD, 40));
            g2d.drawString("เกมกระโดดข้ามรั้ว - เตรียมพร้อม", 350, 100);
            
            g2d.setFont(new Font("Tahoma", Font.PLAIN, 30));
            g2d.drawString("P1 (แดง) กด A เพื่อพร้อม " + (pReady[0] ? "(READY)" : ""), 400, 250);
            g2d.drawString("P2 (เขียว) กด L เพื่อพร้อม " + (pReady[1] ? "(READY)" : ""), 400, 350);
            g2d.drawString("P3 (น้ำเงิน) กด ^ เพื่อพร้อม " + (pReady[2] ? "(READY)" : ""), 400, 450);
            
            int readyCount = (pReady[0]?1:0) + (pReady[1]?1:0) + (pReady[2]?1:0);
            if (readyCount >= 2) {
                g2d.setColor(Color.YELLOW);
                g2d.drawString("กด ENTER เพื่อเริ่มเกม (เล่น " + readyCount + " คน)", 400, 600);
            }
        } else if (currentState == GameState.COUNTDOWN) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, 1280, 720);
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Tahoma", Font.BOLD, 150));
            String text = countdownValue == 0 ? "GO!" : String.valueOf(countdownValue);
            g2d.drawString(text, text.equals("GO!") ? 500 : 600, 400);
        } else if (currentState == GameState.FINISHED) {
            g2d.setFont(new Font("Tahoma", Font.BOLD, 60));
            g2d.setColor(Color.BLACK);
            g2d.drawString(winnerText, 350, 100);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState == GameState.COUNTDOWN) {
            frameCounter++;
            if (frameCounter >= 50) { // 1 วินาที
                frameCounter = 0;
                countdownValue--;
                if (countdownValue < 0) currentState = GameState.PLAYING;
            }
        } else if (currentState == GameState.PLAYING) {
            for (int i = 0; i < 3; i++) {
                if (!pActive[i]) continue; // ข้ามคนที่ไม่ได้เล่น

                if (stunTimer[i] > 0) {
                    stunTimer[i]--;
                    speedX[i] = 1; 
                } else {
                    speedX[i] = 5;
                }
                playerX[i] += speedX[i];

                playerY[i] += velocityY[i];
                if (playerY[i] < baseFloor[i]) {
                    velocityY[i] += gravity;
                } else {
                    playerY[i] = baseFloor[i];
                    velocityY[i] = 0;
                }

                Rectangle pRect = new Rectangle(playerX[i], playerY[i], 50, 50);
                for (Rectangle h : hurdles) {
                    if (pRect.intersects(h) && stunTimer[i] == 0) {
                        stunTimer[i] = 30; 
                        playerX[i] -= 20; 
                    }
                }

                if (playerX[i] >= finishLine && currentState != GameState.FINISHED) {
                    currentState = GameState.FINISHED;
                    winnerText = "Player " + (i + 1) + " Wins!";
                }
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (currentState == GameState.WAITING) {
            if (e.getKeyCode() == KeyEvent.VK_A) pReady[0] = true;
            if (e.getKeyCode() == KeyEvent.VK_L) pReady[1] = true;
            if (e.getKeyCode() == KeyEvent.VK_UP) pReady[2] = true;

            int readyCount = (pReady[0]?1:0) + (pReady[1]?1:0) + (pReady[2]?1:0);
            if (readyCount == 3 || (readyCount >= 2 && e.getKeyCode() == KeyEvent.VK_ENTER)) {
                pActive[0] = pReady[0]; pActive[1] = pReady[1]; pActive[2] = pReady[2];
                currentState = GameState.COUNTDOWN;
            }
        } else if (currentState == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_A && pActive[0] && playerY[0] == baseFloor[0]) velocityY[0] = jumpForce;
            if (e.getKeyCode() == KeyEvent.VK_L && pActive[1] && playerY[1] == baseFloor[1]) velocityY[1] = jumpForce;
            if (e.getKeyCode() == KeyEvent.VK_UP && pActive[2] && playerY[2] == baseFloor[2]) velocityY[2] = jumpForce;
        } else if (currentState == GameState.FINISHED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}