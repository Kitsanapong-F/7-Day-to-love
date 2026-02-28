// package minigameall;

// import java.awt.*;
// import java.awt.event.*;
// import javax.swing.*;
// import java.util.Random;
// import javax.sound.sampled.*; 
// import java.io.File;

// public class WhackAMoleGame {
//     private JFrame frame;
//     private JButton[] holes = new JButton[9];
//     private int score = 0;
//     private int timeLeft = 30;
//     private JLabel scoreLabel, timeLabel;
//     private Timer gameTimer, moleTimer;
//     private int currentMoleIndex = -1;

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

//         setupTimers();
//     }

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
//     }
// }