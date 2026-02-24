package minigameall;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class HappyBirdGame extends JPanel implements ActionListener, KeyListener {
    private JFrame frame;
    private int width = 400;
    private int height = 600;

    // ตัวแปรนก
    private int birdX = width / 4;
    private int birdY = height / 2;
    private int birdSize = 30;
    private int velocity = 0;
    private int gravity = 1;

    // ตัวแปรท่อและฉาก
    private ArrayList<Rectangle> pipes;
    private ArrayList<Point> clouds;
    private Timer gameTimer;
    private int score = 0;
    private boolean gameOver = false;
    private boolean gameStarted = false; // ระบบรอเริ่มเกม
    private Random rand = new Random();

    public HappyBirdGame() {
        frame = new JFrame("Happy Bird Deluxe");
        frame.setUndecorated(true);
        frame.setSize(width, height);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.add(this);
        frame.addKeyListener(this);

        pipes = new ArrayList<>();
        clouds = new ArrayList<>();
        // สุ่มเมฆล่วงหน้า
        for (int i = 0; i < 5; i++) {
            clouds.add(new Point(rand.nextInt(width), rand.nextInt(200)));
        }

        resetParams(); // ตั้งค่าเริ่มต้น
        gameTimer = new Timer(20, this);
    }

    public void start() {
        JOptionPane.showMessageDialog(frame, 
            "<html><body style='font-family:Tahoma;'><h2>Happy Bird!</h2>" +
            "กติกา: กด <b>Spacebar</b> เพื่อบิน!<br>" +
            "เกมจะเริ่มเมื่อคุณกดบินครั้งแรก</body></html>", 
            "เริ่มเกม", JOptionPane.INFORMATION_MESSAGE);
        frame.setVisible(true);
        gameTimer.start();
    }

    private void addPipe(boolean start) {
        int space = 180; 
        int pipeWidth = 60;
        int h = rand.nextInt(height - space - 120) + 60;
        int x = start ? width + pipes.size() * 150 : pipes.get(pipes.size() - 1).x + 300;

        pipes.add(new Rectangle(x, 0, pipeWidth, h));
        pipes.add(new Rectangle(x, h + space, pipeWidth, height - h - space));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. วาดท้องฟ้าไล่เฉดสี
        GradientPaint skyGradient = new GradientPaint(0, 0, new Color(135, 206, 250), 0, height, new Color(25, 25, 112));
        g2d.setPaint(skyGradient);
        g2d.fillRect(0, 0, width, height);

        // 2. วาดก้อนเมฆ
        g2d.setColor(new Color(255, 255, 255, 180));
        for (Point p : clouds) {
            g2d.fillOval(p.x, p.y, 60, 30);
            g2d.fillOval(p.x + 20, p.y - 10, 50, 40);
        }

        // 3. วาดท่อ
        for (Rectangle pipe : pipes) {
            g2d.setColor(new Color(50, 205, 50));
            g2d.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
            g2d.setColor(new Color(0, 100, 0));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(pipe.x, pipe.y, pipe.width, pipe.height);
        }

        // 4. วาดนก
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(birdX, birdY, birdSize, birdSize);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(birdX + 22, birdY + 6, 6, 6); // ตา
        g2d.setColor(Color.ORANGE);
        int[] px = {birdX + 28, birdX + 40, birdX + 28};
        int[] py = {birdY + 12, birdY + 18, birdY + 24};
        g2d.fillPolygon(px, py, 3); // ปาก

        // 5. วาดคะแนน
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Tahoma", Font.BOLD, 25));
        g2d.drawString("Score: " + score, 20, 40);

        // ข้อความตอนรอเริ่ม
        if (!gameStarted && !gameOver) {
            g2d.setFont(new Font("Tahoma", Font.BOLD, 20));
            g2d.drawString("กด SPACEBAR เพื่อเริ่มบิน!", width / 2 - 115, height / 2 + 100);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (gameStarted) {
                velocity += gravity;
                birdY += velocity;

                for (int i = 0; i < pipes.size(); i++) {
                    Rectangle pipe = pipes.get(i);
                    pipe.x -= 5;
                    if (pipe.intersects(new Rectangle(birdX, birdY, birdSize, birdSize))) {
                        gameOver = true;
                    }
                }

                if (pipes.get(0).x + pipes.get(0).width < 0) {
                    pipes.remove(0);
                    pipes.remove(0);
                    addPipe(false);
                    score++;
                }
            } else {
                // บินขึ้นลงเบาๆ ตอนรอ
                birdY = (height / 2) + (int) (Math.sin(System.currentTimeMillis() / 150.0) * 10);
            }

            // เมฆเคลื่อนที่ตลอดเวลา
            for (Point p : clouds) {
                p.x -= 1;
                if (p.x < -70) p.x = width + 20;
            }

            if (birdY > height || birdY < 0) {
                gameOver = true;
            }

            if (gameOver) {
                gameTimer.stop();
                repaint(); // วาดภาพสุดท้ายก่อนโชว์ Pop-up
                showGameOverMenu();
            }
        }
        repaint();
    }

    private void showGameOverMenu() {
        String msg = "Game Over! คะแนนของคุณ: " + score + "\nต้องการเล่นใหม่หรือไม่?";
        int choice = JOptionPane.showConfirmDialog(frame, msg, "จบเกม", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameStarted) gameStarted = true;
            velocity = -11; // แรงกระโดด
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    private void resetParams() {
        birdY = height / 2;
        velocity = 0;
        score = 0;
        gameOver = false;
        gameStarted = false;
        pipes.clear();
        addPipe(true);
        addPipe(true);
    }

    private void resetGame() {
        resetParams();
        gameTimer.start();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}