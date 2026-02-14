import javax.swing.*;
import java.awt.*;

public class Mainplay extends BaseFrame {

    public Mainplay() {
        super("หน้าจอเล่นเกมหลัก");
        
        // ใส่พื้นหลังก่อน
        setBackgroundImage("image/_front_of_classroom_1.jpg"); 
        
        // สร้างเนื้อหา
        initContent();
    }

    private void initContent() {
        JButton btn = new JButton("คลิกตรงนี้");
        btn.setBounds(540, 300, 200, 50);
        btn.setFont(new Font("Tahoma", Font.PLAIN, 18));
        
        // แก้ไข: เพิ่มปุ่มเข้าไปใน Layer ที่สูงกว่า DEFAULT_LAYER (พื้นหลัง)
        // ใช้ PALETTE_LAYER หรือ MODAL_LAYER เพื่อให้ปุ่มลอยอยู่ข้างบนเสมอ
        getLayeredPane().add(btn, JLayeredPane.PALETTE_LAYER);
    }

    public static void main(String[] args) {
        // เมื่อสร้าง Object คลาสแม่จะจัดการหน้าต่างให้เสร็จสรรพ
        Mainplay game = new Mainplay();
        game.display(); 
    }
}