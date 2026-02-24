import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

class miniGame {
    public static void main(String[] args) {

        new interfaceGame();
    }
}

class interfaceGame {
    private long startTime;
    private boolean isRunning = false;

    public interfaceGame() {
        JFrame frame = new JFrame();
        Font thaiFont = new Font("Tahoma", Font.PLAIN, 16);

        UIManager.put("OptionPane.messageFont", thaiFont);
        UIManager.put("OptionPane.buttonFont", thaiFont);

        frame.setUndecorated(true);
        
        frame.setTitle("minigame");
        frame.setSize(500, 500);
        
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setAlwaysOnTop(true);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(205, 92, 92));
        panel.setLayout(new BorderLayout());

        JButton button = new JButton("start");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(new Color(255, 255, 255));
        button.setBackground(new Color(165, 42, 42));

        


        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isRunning) {
                    // เริ่มจับเวลา
                    startTime = System.currentTimeMillis();
                    isRunning = true;
                    button.setText("STOP");
                    button.setBackground(Color.RED);
                } else {
                    // หยุดจับเวลา
                    long endTime = System.currentTimeMillis();
                    isRunning = false;
                    button.setText("START");
                    button.setBackground(null);

                    // คำนวณผลลัพธ์
                    double elapsed = (endTime - startTime) / 1000.0;
                    double diff = Math.abs(10.0 - elapsed);

                    String message = String.format("เวลาที่คุณทำได้: %.2f วินาที\n", elapsed);
                    if (diff <= 0.2) {
                        message += "สุดยอด! ใกล้เคียงมาก!";
                    } else {
                        message += "ห่างจากเป้าหมาย " + String.format("%.2f", diff) + " วินาที";
                    }

                    // แสดงผลลัพธ์ผ่าน Pop-up
                    int choice = JOptionPane.showConfirmDialog(frame, message + "\n\nอยากลองเล่นใหม่อีกครั้งไหม?",  "จบเกม", JOptionPane.YES_NO_OPTION); // ให้มีแค่ปุ่ม Yes (เล่นต่อ) และ No (ปิดเกม)

                    if (choice == JOptionPane.YES_OPTION) {
                     // ถ้ากด Yes (เล่นต่อ) -> ไม่ต้องทำอะไร โปรแกรมจะรันต่อตามปกติ
                         System.out.println("ผู้เล่นต้องการเล่นต่อ");
                        }
                         else {
                    // ถ้ากด No หรือกดปิดหน้าต่างป๊อปอัป -> ปิดโปรแกรมทันที
                        System.exit(0); 
                        }
                }
            }
        });

        panel.add(button);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);

        String instructions = "ยินดีต้อนรับสู่ 10-Second Challenge!\n\n"
                    + "กติกา:\n"
                    + "1. กดปุ่ม START เพื่อเริ่มจับเวลา\n"
                    + "2. นับ 10 วินาทีในใจ\n"
                    + "3. กดปุ่ม STOP เมื่อคุณคิดว่าครบ 10 วินาทีพอดี\n\n"
                    + "คุณพร้อมจะพิสูจน์ความแม่นยำหรือยัง?";

        JOptionPane.showMessageDialog(frame, instructions, "วิธีการเล่น", JOptionPane.INFORMATION_MESSAGE);
        frame.setVisible(true);
    }
}