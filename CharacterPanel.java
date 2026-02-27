import java.awt.*;
import javax.swing.*;

/**
 * คลาสสำหรับจัดการการแสดงผล Sprite ตัวละคร 
 * รองรับการย่อขยายตามขนาดของ Panel และการอัปเดตภาพแบบ Real-time
 */
class CharacterPanel extends JPanel {
    private Image img;

    public CharacterPanel(String path) {
        updateImage(path);
        // ตั้งค่าให้โปร่งใส เพื่อให้เห็นภาพพื้นหลังของ BaseFrame
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (img != null) {
            Graphics2D g2d = (Graphics2D) g;
            // เพิ่มความคมชัดของ Sprite ตัวละครเวลาขยายจอ
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            // วาดรูปตัวละครให้เต็มพื้นที่ Panel
            g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
        }
    }

    /**
     * เมธอดสำหรับเปลี่ยนภาพตัวละคร (เช่น เปลี่ยนอารมณ์หรือท่าทาง)
     */
    public void updateImage(String path) {
        // ดักจับกรณี Path ผิดพลาด หรือค่า "ads" ที่เราพบในเนื้อเรื่อง
        if (path != null && !path.isEmpty() && !path.equals("ads")) {
            ImageIcon icon = new ImageIcon(path);
            
            // ตรวจสอบว่าโหลดภาพสำเร็จหรือไม่
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                this.img = icon.getImage();
            } else {
                System.err.println("[Error] Could not load image at: " + path);
                this.img = null;
            }
        } else {
            // หาก Path ว่าง ให้เคลียร์ภาพออก (ตัวละครหายไปจากฉาก)
            this.img = null;
        }
        repaint();
    }
}