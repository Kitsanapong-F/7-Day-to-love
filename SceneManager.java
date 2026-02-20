import javax.swing.*;
import java.awt.*;

/**
 * SceneManager: คลาสสำหรับควบคุมการเปลี่ยนหน้าจอ (Scene Switching)
 * ทำหน้าที่เป็นตัวกลางในการจัดการหน้าต่าง BaseFrame ของเกม
 */
public class SceneManager {
    // เก็บ Instance ของหน้าจอที่กำลังแสดงผลอยู่ปัจจุบัน
    private static BaseFrame currentScene;

    /**
     * สลับหน้าจอจากหน้าเก่าไปยังหน้าใหม่
     * @param newScene วัตถุหน้าจอใหม่ที่ต้องการแสดง (ต้องเป็นคลาสที่สืบทอดจาก BaseFrame)
     */
    public static void switchScene(BaseFrame newScene) {
        if (newScene == null) {
            System.err.println("[SceneManager] Error: New scene is null!");
            return;
        }

        // ใช้ SwingUtilities เพื่อให้มั่นใจว่าการเปลี่ยน UI เกิดขึ้นบน Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            Rectangle previousBounds = null;
            int previousState = Frame.NORMAL;

            // 1. ตรวจสอบและดึงข้อมูลจากหน้าจอเก่าก่อนทำลายทิ้ง
            if (currentScene != null) {
                // จำตำแหน่ง (x, y) และขนาด (width, height) ล่าสุดไว้
                previousBounds = currentScene.getBounds();
                // จำสถานะหน้าต่าง (เช่น ย่อหน้าจอ, เต็มจอ)
                previousState = currentScene.getExtendedState();
                
                // ปิดหน้าจอเก่าและคืนทรัพยากรหน่วยความจำ
                currentScene.dispose();
            }

            // 2. ตั้งค่าหน้าจอใหม่ให้กลายเป็นหน้าจอหลัก
            currentScene = newScene;

            // 3. จัดการตำแหน่งหน้าต่าง
            if (previousBounds != null) {
                // ถ้ามีหน้าจอก่อนหน้า ให้แสดงหน้าจอใหม่ในตำแหน่งและขนาดเดิมทันที
                currentScene.setBounds(previousBounds);
                currentScene.setExtendedState(previousState);
            } else {
                // ถ้าเป็นหน้าจอแรก (เช่น เปิดเกมครั้งแรก) ให้ตั้งค่าเริ่มต้น
                currentScene.setSize(1280, 720);
                currentScene.setLocationRelativeTo(null); // วางไว้กลางหน้าจอคอมพิวเตอร์
            }

            // 4. สั่งให้ BaseFrame แสดงผล
            currentScene.display();
            
            // สั่งให้หน้าจอคำนวณตำแหน่ง Component ใหม่ตามระบบ Scaling ที่เราเขียนไว้ใน BaseFrame
            currentScene.revalidate();
            currentScene.repaint();
            
            System.out.println("[SceneManager] Switched to: " + newScene.getClass().getSimpleName());
        });
    }

    /**
     * ดึงหน้าจอที่กำลังแสดงผลอยู่ปัจจุบัน
     * @return currentScene
     */
    public static BaseFrame getCurrentScene() {
        return currentScene;
    }
}