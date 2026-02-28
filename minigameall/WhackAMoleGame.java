// package minigameall;

<<<<<<< HEAD
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.File;
=======
// import java.awt.*;
// import java.awt.event.*;
// import javax.swing.*;
// import java.util.Random;
// import javax.sound.sampled.*; 
// import java.io.File;
>>>>>>> origin/develop

// public class WhackAMoleGame {
//     private JFrame frame;
//     private JButton[] holes = new JButton[9];
//     private int score = 0;
//     private int timeLeft = 30;
//     private JLabel scoreLabel, timeLabel;
//     private Timer gameTimer, moleTimer;
//     private int currentMoleIndex = -1;

<<<<<<< HEAD
    // --- ส่วนการตั้งค่าไฟล์ภาพและเสียง (ตรวจสอบ Path ให้ถูกต้องนะครับ) ---
    private final String PATH_HOLE = "C:\\Users\\WINDOWS 10\\7-Day-to-love\\image\\game\\holw.png"; 
    private final String PATH_MOLE = "C:\\Users\\WINDOWS 10\\7-Day-to-love\\image\\game\\tun.png";  
    private final String PATH_HIT  = "C:\\Users\\WINDOWS 10\\7-Day-to-love\\image\\game\\dead.png";   
    private final String PATH_BG   = "C:\\Users\\WINDOWS 10\\7-Day-to-love\\image\\game\\bg.png"; 
    private final String HIT_SOUND_PATH = "C:/Users/WINDOWS 10/7-Day-to-love/sound/umamusume_click.wav";

    // กำหนดขนาดปุ่มพิกเซล
    private final int ICON_SIZE = 150;

    public WhackAMoleGame() {
        frame = new JFrame("Whack-A-Mole Adventure");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 750);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // 1. สร้าง Custom Panel สำหรับพื้นหลัง
        BackgroundPanel mainPanel = new BackgroundPanel(PATH_BG);
        mainPanel.setLayout(new BorderLayout());
        frame.setContentPane(mainPanel);

        // 2. ส่วนหัว (Score & Time)
        JPanel headerPanel = new JPanel(new GridLayout(1, 2));
        headerPanel.setOpaque(false);
        headerPanel.setPreferredSize(new Dimension(600, 100));

        scoreLabel = createCustomLabel("คะแนน: 0");
        timeLabel = createCustomLabel("เวลา: 30");

        headerPanel.add(scoreLabel);
        headerPanel.add(timeLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 3. ส่วนสนาม (Grid 3x3)
        JPanel gamePanel = new JPanel(new GridLayout(3, 3, 15, 15));
        gamePanel.setOpaque(false);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40));

        for (int i = 0; i < 9; i++) {
            holes[i] = new JButton();
            setupButton(holes[i]); 
            
            final int index = i;
            holes[i].addActionListener(e -> whack(index));
            gamePanel.add(holes[i]);
        }
        mainPanel.add(gamePanel, BorderLayout.CENTER);
=======
//     // กำหนด Path ไฟล์เสียงไว้ที่นี่เพื่อให้แก้ไขง่าย
//     private final String HIT_SOUND_PATH = "C:/Users/WINDOWS 10/7-Day-to-love/sound/umamusume_click.wav";

//     public WhackAMoleGame() {
//         Font thaiFont = new Font("Tahoma", Font.PLAIN, 16);
//         UIManager.put("OptionPane.messageFont", thaiFont);
//         UIManager.put("OptionPane.buttonFont", thaiFont);

//         frame = new JFrame("เกมตีตุ่น");
//         frame.setUndecorated(true); 
//         frame.setSize(500, 600);
//         frame.setLayout(new BorderLayout());
//         frame.setAlwaysOnTop(true);
//         frame.setLocationRelativeTo(null);
//         frame.setResizable(false);

//         JPanel headerPanel = new JPanel(new GridLayout(1, 2));
//         headerPanel.setBackground(new Color(255, 230, 230));
        
//         scoreLabel = new JLabel("คะแนน: 0", SwingConstants.CENTER);
//         scoreLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        
//         timeLabel = new JLabel("เวลาเหลือ: 30", SwingConstants.CENTER);
//         timeLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        
//         headerPanel.add(scoreLabel);
//         headerPanel.add(timeLabel);
//         frame.add(headerPanel, BorderLayout.NORTH);

//         JPanel gamePanel = new JPanel(new GridLayout(3, 3, 10, 10));
//         gamePanel.setBackground(new Color(139, 69, 19)); 
//         gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

//         for (int i = 0; i < 9; i++) {
//             holes[i] = new JButton("O");
//             holes[i].setFont(new Font("Arial", Font.BOLD, 40));
//             holes[i].setBackground(new Color(101, 67, 33));
//             holes[i].setForeground(Color.WHITE);
//             holes[i].setFocusPainted(false);
            
//             final int index = i;
//             holes[i].addActionListener(e -> whack(index));
//             gamePanel.add(holes[i]);
//         }
//         frame.add(gamePanel, BorderLayout.CENTER);
>>>>>>> origin/develop

//         setupTimers();
//     }

<<<<<<< HEAD
    private ImageIcon getScaledIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("ไม่สามารถโหลดรูปภาพได้: " + path);
            return null;
        }
    }

    private void setupButton(JButton btn) {
        btn.setIcon(getScaledIcon(PATH_HOLE)); 
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);     
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JLabel createCustomLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.BOLD, 28));
        label.setForeground(Color.WHITE);
        label.setText("<html><body style='text-shadow: 2px 2px 4px #000000;'>" + text + "</body></html>");
        return label;
    }

    private void moveMole() {
        if (currentMoleIndex != -1) {
            holes[currentMoleIndex].setIcon(getScaledIcon(PATH_HOLE));
        }

        Random rand = new Random();
        currentMoleIndex = rand.nextInt(9);
        holes[currentMoleIndex].setIcon(getScaledIcon(PATH_MOLE));
    }

    private void whack(int index) {
        if (index == currentMoleIndex) {
            score += 10;
            scoreLabel.setText("<html><body style='text-shadow: 2px 2px 4px #000000;'>คะแนน: " + score + "</body></html>");
            playSound(HIT_SOUND_PATH);
            
            holes[index].setIcon(getScaledIcon(PATH_HIT));
            currentMoleIndex = -1;

            // หยุด Timer สุ่มตุ่นชั่วคราว
            moleTimer.stop();

            // ค้างภาพโดนตีไว้นานขึ้นนิดนึง (800ms) ให้เห็นความสำเร็จ
            Timer pause = new Timer(800, e -> {
                holes[index].setIcon(getScaledIcon(PATH_HOLE));
                if (timeLeft > 0) {
                    moleTimer.start(); 
                }
            });
            pause.setRepeats(false);
            pause.start();
        }
    }

    private void setupTimers() {
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            timeLabel.setText("<html><body style='text-shadow: 2px 2px 4px #000000;'>เวลา: " + timeLeft + "</body></html>");
            if (timeLeft <= 0) stopGame();
        });
        
        // ปรับความเร็วตรงนี้: 1200ms คือ 1.2 วินาที (ช้าลงกว่าเดิมเพื่อให้ตีทัน)
        moleTimer = new Timer(1000, e -> moveMole());
    }

    public void start() {
        JOptionPane.showMessageDialog(frame, "พร้อมแล้วเริ่มเลย!", "Ready?", JOptionPane.INFORMATION_MESSAGE);
        frame.setVisible(true);
        gameTimer.start();
        moleTimer.start();
    }

    private void stopGame() {
        gameTimer.stop();
        moleTimer.stop();
        int choice = JOptionPane.showConfirmDialog(frame, "จบเกม! คะแนน: " + score + "\nเล่นใหม่ไหม?", "Time Out", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetGame();
        } else {
            System.exit(0);
        }
    }

    private void resetGame() {
        score = 0;
        timeLeft = 30;
        scoreLabel.setText("<html><body style='text-shadow: 2px 2px 4px #000000;'>คะแนน: 0</body></html>");
        timeLabel.setText("<html><body style='text-shadow: 2px 2px 4px #000000;'>เวลา: 30</body></html>");
        for (JButton hole : holes) {
            hole.setIcon(getScaledIcon(PATH_HOLE));
        }
        start();
    }

    private void playSound(String path) {
        try {
            File f = new File(path);
            if (f.exists()) {
                AudioInputStream ai = AudioSystem.getAudioInputStream(f);
                Clip clip = AudioSystem.getClip();
                clip.open(ai);
                clip.start();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    class BackgroundPanel extends JPanel {
        private Image img;
        public BackgroundPanel(String path) {
            this.img = new ImageIcon(path).getImage();
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) {
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}

    

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> new WhackAMoleGame().start());
=======
//     private void playSound(String soundFilePath) {
//         try {
//             File soundFile = new File(soundFilePath);
//             if (soundFile.exists()) {
//                 AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
//                 Clip clip = AudioSystem.getClip();
//                 clip.open(audioIn);
//                 clip.start();
//             } else {
//                 System.err.println("ไม่พบไฟล์เสียงในระบบ: " + soundFilePath);
//             }
//         } catch (Exception e) {
//             System.err.println("เกิดข้อผิดพลาดในการเล่นเสียง: " + e.getMessage());
//         }
//     }

//     private void setupTimers() {
//         gameTimer = new Timer(1000, e -> {
//             timeLeft--;
//             timeLabel.setText("เวลาเหลือ: " + timeLeft);
//             if (timeLeft <= 0) {
//                 stopGame();
//             }
//         });

//         moleTimer = new Timer(800, e -> moveMole());
//     }

//     private void moveMole() {
//         if (currentMoleIndex != -1) {
//             holes[currentMoleIndex].setText("O");
//             holes[currentMoleIndex].setBackground(new Color(101, 67, 33));
//         }

//         Random rand = new Random();
//         currentMoleIndex = rand.nextInt(9);
//         holes[currentMoleIndex].setText("(•̀ᴗ•́)");
//         holes[currentMoleIndex].setBackground(Color.YELLOW);
//     }

//     private void whack(int index) {
//         if (index == currentMoleIndex) {
//             score += 10;
//             scoreLabel.setText("คะแนน: " + score);
            
//             // เรียกใช้ Path ไฟล์เสียง Umamusume ที่ตั้งไว้
//             playSound(HIT_SOUND_PATH); 
            
//             holes[index].setText("X_X");
//             holes[index].setBackground(Color.RED);
//             currentMoleIndex = -1;
//         }
//     }

//     public void start() {
//         String msg = "กติกา: ตีตุ่นที่โผล่มาให้เร็วที่สุด!\nคุณมีเวลา 30 วินาที";
//         JOptionPane.showMessageDialog(frame, msg, "เริ่มเกม", JOptionPane.INFORMATION_MESSAGE);
//         frame.setVisible(true);
//         gameTimer.start();
//         moleTimer.start();
//     }

//     private void stopGame() {
//         gameTimer.stop();
//         moleTimer.stop();
        
//         String resultMsg = "จบเกม! คะแนนของคุณคือ: " + score + "\nเล่นใหม่อีกครั้งไหม?";
//         int choice = JOptionPane.showConfirmDialog(frame, resultMsg, "หมดเวลา!", JOptionPane.YES_NO_OPTION);
        
//         if (choice == JOptionPane.YES_OPTION) {
//             resetGame();
//         } else {
//             System.exit(0);
//         }
//     }

//     private void resetGame() {
//         score = 0;
//         timeLeft = 30;
//         scoreLabel.setText("คะแนน: 0");
//         timeLabel.setText("เวลาเหลือ: 30");
//         start();
>>>>>>> origin/develop
//     }
// }