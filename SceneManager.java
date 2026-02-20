import java.awt.*;

public class SceneManager {
    private static BaseFrame currentScene;

    public static void switchScene(BaseFrame newScene) {
        Rectangle previousBounds = null;
        int previousState = Frame.NORMAL;

        // 1. ถ้ามีหน้าจอเก่าอยู่ ให้จำขนาดและตำแหน่งไว้
        if (currentScene != null) {
            previousBounds = currentScene.getBounds();
            previousState = currentScene.getExtendedState();
            currentScene.dispose(); // ปิดหน้าเก่า
        }

        currentScene = newScene;

        // 2. ถ้ามีข้อมูลหน้าจอเก่า ให้เอามาใส่หน้าจอใหม่ทันที
        if (previousBounds != null) {
            currentScene.setBounds(previousBounds);
            currentScene.setExtendedState(previousState);
        } else {
            // กรณีเปิดเกมครั้งแรก ให้ตั้งเป็น 1280x720 กลางจอ
            currentScene.setSize(1280, 720);
            currentScene.setLocationRelativeTo(null);
        }

        currentScene.display();
    }
}