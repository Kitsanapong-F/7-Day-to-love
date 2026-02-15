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
    // กำหนดค่ามาตรฐานสำหรับตำแหน่งปุ่มทางขวา
    int xPos = 1000;    // ตำแหน่งห่างจากขอบซ้าย 1000 (เหลือพื้นที่ฝั่งขวา 280)
    int btnWidth = 200;
    int btnHeight = 50;

    // 1. สร้างปุ่มแรก (ปุ่มบน)
    JButton nextBtn = new JButton("ข้อ 1");
    nextBtn.setBounds(xPos, 250, btnWidth, btnHeight);
    BaseFrame.styleButton(nextBtn); // ใช้สไตล์ที่เราแต่งไว้ใน BaseFrame
    
    // 2. สร้างปุ่มที่สอง (ปุ่มล่าง)
    JButton backBtn = new JButton("ข้อ 2");
    backBtn.setBounds(xPos, 320, btnWidth, btnHeight); // วางห่างจากปุ่มแรก 70 พิกเซล
    BaseFrame.styleButton(backBtn);

    // สำคัญ: ต้องเพิ่มเข้า LayeredPane เพื่อให้ลอยทับพื้นหลัง
    getLayeredPane().add(nextBtn, JLayeredPane.PALETTE_LAYER);
    getLayeredPane().add(backBtn, JLayeredPane.PALETTE_LAYER);
}

    public static void main(String[] args) {
        // เมื่อสร้าง Object คลาสแม่จะจัดการหน้าต่างให้เสร็จสรรพ
        Mainplay game = new Mainplay();
        game.display(); 
    }
}