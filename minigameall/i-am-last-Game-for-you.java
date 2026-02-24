package minigameall;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

class miniGame {
    public static void main(String[] args) {
        // // ตั้งค่าฟอนต์ไทยให้ดูละมุน
        setUIFont(new FontUIResource("Tahoma", Font.PLAIN, 16));
        // SwingUtilities.invokeLater(() -> new DatingGameInterface());
        WhackAMoleGame game = new WhackAMoleGame();
        game.start();
    }

    public static void setUIFont(FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            if (UIManager.get(key) instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}

class DatingGameInterface {
    private long startTime;
    private boolean isRunning = false;
    private final Font thaiFont = new Font("Tahoma", Font.BOLD, 22);

    public DatingGameInterface() {
        JFrame frame = new JFrame("❤ 10-Second Heartbeat Challenge ❤");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ส่วนบน: พื้นหลังสีชมพูพาสเทลและคำถาม
        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.setBackground(new Color(255, 240, 245)); // Lavender Blush

        JLabel label = new JLabel("<html><center>\"ถ้าคุณจริงใจกับเรา...<br>ช่วยนับ 10 วินาทีในใจให้แม่นๆ หน่อยได้ไหมคะ?\"</center></html>", SwingConstants.CENTER);
        label.setFont(new Font("Tahoma", Font.ITALIC, 18));
        label.setForeground(new Color(219, 112, 147));

        // ส่วนล่าง: ปุ่มกดรูปหัวใจ (หรือปุ่มชมพู)
        JButton actionButton = new JButton("เริ่มพิสูจน์ความรัก (START)");
        actionButton.setFont(thaiFont);
        actionButton.setBackground(new Color(255, 182, 193)); // Light Pink
        actionButton.setForeground(Color.WHITE);
        actionButton.setFocusPainted(false);
        actionButton.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        actionButton.addActionListener(e -> {
            if (!isRunning) {
                startTime = System.currentTimeMillis();
                isRunning = true;
                actionButton.setText("หยุดเพื่อบอกรัก (STOP)");
                actionButton.setBackground(new Color(255, 105, 180)); // Hot Pink
            } else {
                long endTime = System.currentTimeMillis();
                isRunning = false;
                actionButton.setText("ลองจีบใหม่อีกครั้ง");
                actionButton.setBackground(new Color(255, 182, 193));

                double elapsed = (endTime - startTime) / 1000.0;
                double diff = Math.abs(10.0 - elapsed);
                showDatingResult(frame, elapsed, diff);
            }
        });

        mainPanel.add(label);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(actionButton, BorderLayout.SOUTH);
        
        frame.setLocationRelativeTo(null);
        
        // เริ่มต้นด้วยคำทักทาย
        JOptionPane.showMessageDialog(frame, 
            "วันนี้ฉันอยากรู้ว่าใจของเราจะตรงกันไหม...\nลองนับ 10 วินาทีให้เป๊ะที่สุดเพื่อพิสูจน์ดูสิ!", 
            "พบกับนานะ", 
            JOptionPane.PLAIN_MESSAGE);
            
        frame.setVisible(true);
    }

    private void showDatingResult(JFrame frame, double elapsed, double diff) {
        String title, comment;
        String icon = "❤";

        // ระบบตัดสินใจตามความแม่นยำ
        if (diff <= 0.1) {
            title = "พรหมลิขิตชัดๆ!";
            comment = String.format("โห! %.2f วินาที! ใจเราตรงกันเป๊ะเลยค่ะ ยอมเป็นแฟนก็ได้นะ!", elapsed);
        } else if (diff <= 0.5) {
            title = "เกือบหลงรักแล้ว";
            comment = String.format("%.2f วินาที... เก่งจังเลยค่ะ อีกนิดเดียวใจจะละลายแล้ว", elapsed);
        } else if (diff <= 1.5) {
            title = "คนดีที่น่าคบ";
            comment = String.format("%.2f วินาที... ก็โอเคนะคะ ลองมาพยายามด้วยกันอีกหน่อยไหม?", elapsed);
        } else {
            title = "เพื่อนกันไปก่อนนะ";
            comment = String.format("%.2f วินาที... ดูเหมือนใจเราจะยังไม่ตรงกันเท่าไหร่ค่ะ", elapsed);
            icon = "💔";
        }

        int choice = JOptionPane.showConfirmDialog(frame, 
            icon + " " + comment + "\n\nจะขอโอกาสพิสูจน์ตัวเองอีกรอบไหมคะ?", 
            title, 
            JOptionPane.YES_NO_OPTION);

        if (choice != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(frame, "โชคดีนะ... ไว้เจอกันใหม่เมื่อใจพร้อม");
            System.exit(0);
        }
    }
}