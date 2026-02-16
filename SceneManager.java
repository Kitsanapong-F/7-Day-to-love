public class SceneManager {
    private static BaseFrame currentScene;

    public static void switchScene(BaseFrame newScene) {
        if (currentScene != null) currentScene.dispose();
        currentScene = newScene;
        currentScene.display();
    }
}