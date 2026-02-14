import javax.swing.*;
import java.awt.*;

public class Mainplay extends BaseFrame {

    public Mainplay() {
        
        super("หน้าจอเล่นเกมหลัก");  // ส่งชื่อ Title ที่ต้องการไปให้ BaseFrame
        
        initContent();   // เพิ่ม Component อื่นๆ ของคุณที่นี่
    }

    private void initContent() {
        // เพิ่มปุ่ม
        JButton btn = new JButton("คลิกตรงนี้");
        btn.setBounds(540, 300, 200, 50);
        btn.setFont(new Font("Tahoma", Font.PLAIN, 18));
        add(btn);
    }

    public static void main(String[] args) {
        Mainplay game = new Mainplay();
        game.display(); // เรียกใช้ Method display() จาก BaseFrame
    }
}