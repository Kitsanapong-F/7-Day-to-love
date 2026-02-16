import javax.swing.*;
import java.awt.*;
public class BaseFrame extends JFrame{

    protected JLabel backgroundLabel;
    
    public BaseFrame(String title){
        
        setTitle(title);
        setSize(1280,720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1280,720));
        setContentPane(layeredPane);
        setLayout(null);
    }
    
    public void setBackgroundImage(String imagePath){
        if(backgroundLabel != null){
            getLayeredPane().remove(backgroundLabel);
        }
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
        
        backgroundLabel = new JLabel(new ImageIcon(img));
        backgroundLabel.setBounds(0, 0, 1280, 720);

        // เพิ่มลงใน Layer ต่ำที่สุด (DEFAULT_LAYER)
        getLayeredPane().add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);
        
        // สั่งให้วาดหน้าจอใหม่
        revalidate(); 
        repaint();
    }

    public static void styleButton(JButton btn) {
        btn.setFont(new Font("Tahoma", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 90, 120)); // สีน้ำเงินอมเทาแบบเท่ๆ
        btn.setFocusPainted(false); // เอาเส้นประรอบตัวอักษรออก
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // ใส่เส้นขอบขาว
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // เปลี่ยนเมาส์เป็นรูปมือ

        // เพิ่มเอฟเฟกต์เวลาเอาเมาส์ไปวาง (Hover Effect)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(100, 120, 150)); // สว่างขึ้น
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(70, 90, 120)); // กลับมาสีเดิม
            }
        });
    }

    public void display(){
        setVisible(true);
    }
}


//DEFAULT_LAYER = พื้นหลัง

//PALETTE_LAYER = ปุ่มและตัวละคร

//MODAL_LAYER = กล่องข้อความ (Dialogue Box)