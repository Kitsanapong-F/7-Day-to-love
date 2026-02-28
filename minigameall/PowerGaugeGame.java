package minigameall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PowerGaugeGame extends JPanel implements KeyListener, ActionListener {
    enum GameState { WAITING, COUNTDOWN, PLAYING, FINISHED }
    private GameState currentState = GameState.WAITING;

    private double[] power = {0, 0, 0};
    private boolean[] increasing = {true, true, true};
    private boolean[] locked = {false, false, false};
    private double[] speeds = {2.5, 3.2, 2.8}; 
    
    private boolean[] pReady = {false, false, false};
    private boolean[] pActive = {false, false, false};

    private Timer timer;
    private String resultText = "รอผู้เล่นเตรียมพร้อม...";
    
    private int countdownValue = 3;
    private int frameCounter = 0;

    public PowerGaugeGame() {
        setPreferredSize(new Dimension(1280, 720));
        setBackground(new Color(40, 40, 40));
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 36));
        g2d.drawString("เกมวัดพลัง (กดเพื่อหยุดเกจ)", 400, 60);
        
        g2d.setFont(new Font("Tahoma", Font.PLAIN, 24));
        g2d.drawString(resultText, 500, 110);

        String[] labels = {"P1 (กด A)", "P2 (กด L)", "P3 (กด ขึ้น)"};
        Color[] pColors = {Color.RED, Color.GREEN, Color.BLUE};

        for (int i = 0; i < 3; i++) {
            int yPos = 200 + (i * 150);
            
            // วาดชื่อ (ถ้ายังไม่พร้อมตอน Waiting ให้สีซีดๆ)
            g2d.setColor((currentState == GameState.WAITING && !pReady[i]) ? Color.GRAY : Color.WHITE);
            g2d.drawString(labels[i] + (locked[i] ? " ล็อคแล้ว!" : "") + (pReady[i] && currentState == GameState.WAITING ? " (READY)" : ""), 50, yPos + 40);

            // วาดกรอบเกจพลัง
            g2d.setColor(Color.DARK_GRAY);
            g2d.fillRect(300, yPos, 800, 60);
            g2d.setColor(Color.WHITE);
            g2d.drawRect(300, yPos, 800, 60);

            // พื้นที่คะแนนสูงสุด
            g2d.setColor(new Color(255, 215, 0, 100));
            g2d.fillRect(650, yPos, 100, 60);

            // วาดระดับพลัง
            g2d.setColor((currentState == GameState.WAITING && !pReady[i]) ? new Color(pColors[i].getRed(), pColors[i].getGreen(), pColors[i].getBlue(), 100) : pColors[i]);
            int fillWidth = (int)((power[i] / 100.0) * 800);
            g2d.fillRect(300, yPos, fillWidth, 60);
            
            g2d.setColor(Color.WHITE);
            g2d.drawString(String.format("%.1f %%", power[i]), 1120, yPos + 40);
        }

        // Overlay ตอนรอ หรือ Countdown
        if (currentState == GameState.WAITING) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, 1280, 720);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Tahoma", Font.BOLD, 40));
            g2d.drawString("กดปุ่มของตัวเองเพื่อพร้อม", 400, 350);
            int readyCount = (pReady[0]?1:0) + (pReady[1]?1:0) + (pReady[2]?1:0);
            if (readyCount >= 2) {
                g2d.setColor(Color.YELLOW);
                g2d.drawString("กด ENTER เพื่อเริ่มเกม (เล่น " + readyCount + " คน)", 300, 450);
            }
        } else if (currentState == GameState.COUNTDOWN) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, 1280, 720);
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Tahoma", Font.BOLD, 150));
            String text = countdownValue == 0 ? "GO!" : String.valueOf(countdownValue);
            g2d.drawString(text, text.equals("GO!") ? 500 : 600, 400);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentState == GameState.COUNTDOWN) {
            frameCounter++;
            if (frameCounter >= 50) {
                frameCounter = 0;
                countdownValue--;
                if (countdownValue < 0) {
                    currentState = GameState.PLAYING;
                    resultText = "เล็งจังหวะแล้วกดปุ่ม!";
                }
            }
        } else if (currentState == GameState.PLAYING) {
            boolean allLocked = true;
            for (int i = 0; i < 3; i++) {
                if (!pActive[i]) { locked[i] = true; continue; } // คนไม่เล่นข้ามไปเลย
                
                if (!locked[i]) {
                    allLocked = false;
                    if (increasing[i]) {
                        power[i] += speeds[i];
                        if (power[i] >= 100) { power[i] = 100; increasing[i] = false; }
                    } else {
                        power[i] -= speeds[i];
                        if (power[i] <= 0) { power[i] = 0; increasing[i] = true; }
                    }
                }
            }

            if (allLocked && currentState != GameState.FINISHED) {
                currentState = GameState.FINISHED;
                determineWinner();
            }
        }
        repaint();
    }

    private void determineWinner() {
        int winner = -1;
        double maxPower = -1;
        for (int i = 0; i < 3; i++) {
            if (pActive[i] && power[i] > maxPower) {
                maxPower = power[i];
                winner = i + 1;
            }
        }
        resultText = "จบเกม! ผู้ชนะคือ Player " + winner + " ด้วยพลัง " + String.format("%.1f%%", maxPower) + " (กด ESC ออก)";
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
                resultText = "เตรียมตัว...";
            }
        } else if (currentState == GameState.PLAYING) {
            if (e.getKeyCode() == KeyEvent.VK_A && pActive[0]) locked[0] = true;
            if (e.getKeyCode() == KeyEvent.VK_L && pActive[1]) locked[1] = true;
            if (e.getKeyCode() == KeyEvent.VK_UP && pActive[2]) locked[2] = true;
        } else if (currentState == GameState.FINISHED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}