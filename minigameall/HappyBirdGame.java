package minigameall;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

// --- Main Game Class ---
public class HappyBirdGame extends JPanel implements ActionListener, KeyListener {
    private JFrame frame;
    private final int width = 450;
    private final int height = 700;

    private Image bgImage;
    private Image characterImage;

    private double birdY;
    private final double birdX = 110;
    private double velocity = 0;
    private final double gravity = 0.6;
    private final int birdSize = 50;
    private double rotation = 0;

    private ArrayList<Rectangle> pipes;
    private ArrayList<Color> pipeColors; 
    private Timer gameTimer;
    private int score = 0;
    private boolean gameOver = false;
    private boolean gameStarted = false;
    private Random rand = new Random();

    public HappyBirdGame() {
        frame = new JFrame("Library Escape: Book Edition");
        
        try {
            java.net.URL bgURL = getClass().getResource("/image/game/game.png");
            if (bgURL != null) {
                bgImage = new ImageIcon(bgURL).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
            java.net.URL charURL = getClass().getResource("/image/game/heart.png");
            if (charURL != null) {
                characterImage = new ImageIcon(charURL).getImage().getScaledInstance(birdSize, birdSize, Image.SCALE_SMOOTH);
            }
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(this);
        frame.addKeyListener(this);

        pipes = new ArrayList<>();
        pipeColors = new ArrayList<>();
        resetParams();
        gameTimer = new Timer(16, this);
    }

    public void start() {
        frame.setVisible(true);
        gameTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. วาดพื้นหลัง
        if (bgImage != null) {
            g2d.drawImage(bgImage, 0, 0, this);
        } else {
            g2d.setColor(new Color(45, 30, 20)); 
            g2d.fillRect(0, 0, width, height);
        }

        // 2. วาดสันหนังสือ (ท่อ)
        for (int i = 0; i < pipes.size(); i++) {
            Rectangle p = pipes.get(i);
            Color bookColor = pipeColors.get(i);

            g2d.setColor(bookColor);
            g2d.fillRoundRect(p.x, p.y, p.width, p.height, 5, 5);

            g2d.setColor(bookColor.darker());
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(p.x, p.y, p.width, p.height, 5, 5);

            g2d.setColor(new Color(255, 215, 0, 150)); 
            if (p.y == 0) { 
                g2d.fillRect(p.x + 5, p.height - 25, p.width - 10, 4);
                g2d.fillRect(p.x + 5, p.height - 40, p.width - 10, 2);
            } else { 
                g2d.fillRect(p.x + 5, p.y + 20, p.width - 10, 4);
                g2d.fillRect(p.x + 5, p.y + 35, p.width - 10, 2);
            }
        }

        // 3. วาดตัวละคร
        AffineTransform old = g2d.getTransform();
        g2d.translate(birdX + birdSize / 2.0, birdY + birdSize / 2.0);
        g2d.rotate(Math.toRadians(rotation));

        if (characterImage != null) {
            g2d.drawImage(characterImage, -birdSize / 2, -birdSize / 2, this);
        } else {
            drawDefaultHeart(g2d, -birdSize / 2, -birdSize / 2, birdSize);
        }
        g2d.setTransform(old);

        // 4. UI
        drawTextWithShadow(g2d, "Score: " + score, 25, 55, 35);
        if (!gameStarted) drawTextWithShadow(g2d, "PRESS SPACE TO READ", 80, height / 2, 24);
    }

    private void drawDefaultHeart(Graphics2D g2d, int x, int y, int size) {
        g2d.setColor(new Color(255, 80, 100));
        g2d.fillArc(x, y, size / 2, size / 2, 0, 180);
        g2d.fillArc(x + size / 2, y, size / 2, size / 2, 0, 180);
        int[] xPoly = {x, x + size, x + size / 2};
        int[] yPoly = {y + size / 4 + 2, y + size / 4 + 2, y + size};
        g2d.fillPolygon(xPoly, yPoly, 3);
    }

    private void drawTextWithShadow(Graphics2D g2d, String text, int x, int y, int size) {
        g2d.setFont(new Font("Tahoma", Font.BOLD, size));
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(text, x + 2, y + 2);
        g2d.setColor(Color.WHITE); 
        g2d.drawString(text, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            velocity += gravity;
            birdY += velocity;
            rotation = Math.min(Math.max(velocity * 4, -25), 45);

            for (Rectangle p : pipes) {
                p.x -= 4;
                if (p.intersects(new Rectangle((int)birdX + 5, (int)birdY + 5, birdSize - 10, birdSize - 10))) {
                    gameOver = true;
                }
            }

            if (!pipes.isEmpty() && pipes.get(0).x + pipes.get(0).width < 0) {
                pipes.remove(0); pipes.remove(0);
                pipeColors.remove(0); pipeColors.remove(0);
                addPipe();
                score++;
            }
            if (birdY > height || birdY < -50) gameOver = true;
        }
        if (gameOver) {
            gameTimer.stop();
            // บันทึกคะแนนเข้า Manager เมื่อเกมจบ
            ScoreManager.getInstance().addScore(score);
            showGameOverMenu();
        }
        repaint();
    }

    private void addPipe() {
        int pipeWidth = 70;
        int gap = 200;
        int h = rand.nextInt(height - gap - 200) + 100;
        int startX = pipes.isEmpty() ? width + 50 : pipes.get(pipes.size() - 1).x + 300;
        
        pipes.add(new Rectangle(startX, 0, pipeWidth, h));
        pipes.add(new Rectangle(startX, h + gap, pipeWidth, height - h - gap));
        
        pipeColors.add(new Color(rand.nextInt(150), rand.nextInt(150), rand.nextInt(150))); 
        pipeColors.add(new Color(rand.nextInt(150), rand.nextInt(150), rand.nextInt(150)));
    }

    private void showGameOverMenu() {
        int grandTotal = ScoreManager.getInstance().getTotalScore();
        String message = "Match Score: " + score + "\nGrand Total Score: " + grandTotal + "\n\nRead again?";
        
        int choice = JOptionPane.showConfirmDialog(frame, message, "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetParams();
            gameTimer.start();
        } else {
            System.exit(0);
        }
    }

    private void resetParams() {
        birdY = (double) height / 2;
        velocity = 0;
        rotation = 0;
        score = 0;
        gameOver = false;
        gameStarted = false;
        pipes.clear();
        pipeColors.clear();
        addPipe();
        addPipe();
    }

    @Override public void keyPressed(KeyEvent e) { 
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { 
            if (!gameStarted) gameStarted = true; 
            velocity = -9.0; 
        } 
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HappyBirdGame().start());
    }
}

// --- Score Manager Class (รวมอยู่ในไฟล์เดียวกัน) ---
class ScoreManager {
    private static ScoreManager instance;
    private int totalScore = 0;

    private ScoreManager() {}

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void addScore(int points) {
        this.totalScore += points;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void resetTotalScore() {
        this.totalScore = 0;
    }
}