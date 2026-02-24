import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class WhackAMoleGame {
    private JFrame frame;
    private JButton[] holes = new JButton[9]; // สร้างรูตุ่น 9 รู
    private int score = 0;
    private int timeLeft = 30; // เวลาเล่น 30 วินาที
    private JLabel scoreLabel, timeLabel;
    private Timer gameTimer, moleTimer;
    private int currentMoleIndex = -1;

    public WhackAMoleGame() {

        Font thaiFont = new Font("Tahoma", Font.PLAIN, 16);

        UIManager.put("OptionPane.messageFont", thaiFont);
        UIManager.put("OptionPane.buttonFont", thaiFont);

        frame = new JFrame("เกมตีตุ่น (Whack-a-Mole)");
        frame.setUndecorated(true); // ซ่อนแถบด้านบนตามที่คุณชอบ
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout());
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);

        // --- ส่วนแสดงคะแนนและเวลา ---
        JPanel headerPanel = new JPanel(new GridLayout(1, 2));
        headerPanel.setBackground(new Color(255, 230, 230));
        scoreLabel = new JLabel("คะแนน: 0", SwingConstants.CENTER);
        timeLabel = new JLabel("เวลาเหลือ: 30", SwingConstants.CENTER);
        headerPanel.add(scoreLabel);
        headerPanel.add(timeLabel);
        frame.add(headerPanel, BorderLayout.NORTH);

        // --- ส่วนสนามตีตุ่น (3x3) ---
        JPanel gamePanel = new JPanel(new GridLayout(3, 3, 10, 10));
        gamePanel.setBackground(new Color(139, 69, 19)); // สีน้ำตาลเหมือนดิน
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 9; i++) {
            holes[i] = new JButton("O"); // "O" คือรูว่าง
            holes[i].setFont(new Font("Arial", Font.BOLD, 40));
            holes[i].setBackground(new Color(101, 67, 33));
            holes[i].setForeground(Color.WHITE);
            holes[i].setFocusPainted(false);
            
            final int index = i;
            holes[i].addActionListener(e -> whack(index));
            gamePanel.add(holes[i]);
        }
        frame.add(gamePanel, BorderLayout.CENTER);

        // --- ระบบเวลา (Game Logic) ---
        setupTimers();
    }

    private void setupTimers() {
        // Timer สำหรับลดเวลาถอยหลังทุก 1 วินาที
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            timeLabel.setText("เวลาเหลือ: " + timeLeft);
            if (timeLeft <= 0) {
                stopGame();
            }
        });

        // Timer สำหรับสุ่มตำแหน่งตุ่นทุก 0.8 วินาที
        moleTimer = new Timer(800, e -> moveMole());
    }

    private void moveMole() {
        // ล้างตำแหน่งตุ่นเก่า
        if (currentMoleIndex != -1) {
            holes[currentMoleIndex].setText("O");
            holes[currentMoleIndex].setBackground(new Color(101, 67, 33));
        }

        // สุ่มตำแหน่งตุ่นใหม่
        Random rand = new Random();
        currentMoleIndex = rand.nextInt(9);
        holes[currentMoleIndex].setText("(•̀ᴗ•́)"); // ตัวตุ่นโผล่!
        holes[currentMoleIndex].setBackground(Color.YELLOW);
    }

    private void whack(int index) {
        if (index == currentMoleIndex) {
            score += 10;
            scoreLabel.setText("คะแนน: " + score);
            holes[index].setText("X_X"); // ตีโดนแล้ว
            holes[index].setBackground(Color.RED);
            currentMoleIndex = -1; // ตีแล้วหายไป
        }
    }

    public void start() {
        JOptionPane.showMessageDialog(frame, "กติกา: ตีตุ่นที่โผล่มาให้เร็วที่สุดใน 30 วินาที!", "เริ่มเกม", JOptionPane.INFORMATION_MESSAGE);
        frame.setVisible(true);
        gameTimer.start();
        moleTimer.start();
    }

    private void stopGame() {
        gameTimer.stop();
        moleTimer.stop();
        int choice = JOptionPane.showConfirmDialog(frame, "จบเกม! คะแนนของคุณคือ: " + score + "\nเล่นใหม่อีกครั้งไหม?", "Time Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        score = 0;
        timeLeft = 30;
        scoreLabel.setText("คะแนน: 0");
        timeLabel.setText("เวลาเหลือ: 30");
        start();
    }
}