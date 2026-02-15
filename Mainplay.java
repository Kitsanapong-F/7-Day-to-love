import javax.swing.*;

public class Mainplay extends BaseFrame {

    public Mainplay() {
        super("7 Days to Love - Gameplay");
        
        // ใส่รูปพื้นหลัง (ตรวจสอบ Path ให้ถูกต้อง)
        setBackgroundImage("image/Bgscene/_front_of_classroom_1.jpg"); 
        
        initContent();
    }

    private void initContent() {
        // สร้างปุ่ม
        JButton nextBtn = new JButton("เลือกข้อนี้");
        BaseFrame.styleButton(nextBtn);

        JButton backBtn = new JButton("กลับเมนู");
        BaseFrame.styleButton(backBtn);

        // --- จุดที่ทีมงานต้องทำ ---
        // ใส่พิกัด x, y, กว้าง, สูง ที่ต้องการ (อิงจากจอ 1280)
        // เช่น อยากให้ปุ่มอยู่ที่ x=1000, y=250
        addComponent(nextBtn, 1000, 250, 200, 50);
        // อยากให้อยู่ใต้กันพอดี
        addComponent(backBtn, 1000, 320, 200, 50);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Mainplay game = new Mainplay();
            game.display();
        });
    }
}