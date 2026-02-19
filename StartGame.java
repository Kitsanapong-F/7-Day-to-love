import javax.swing.*;

public class StartGame extends BaseFrame {
    public StartGame() {
        super("7 Days to Love - Main Menu");
        setBackgroundImage("image\\cover\\15a52c75-9650-403e-b795-101302a74f6b.png");
        initUI();
    }

    private void initUI() {
        JButton newGameBtn = new JButton("New Game");
        styleButton(newGameBtn);
        addComponent(newGameBtn, 850, 300, 250, 50);

        JButton exitBtn = new JButton("Exit");
        styleButton(exitBtn);
        addComponent(exitBtn, 850, 380, 250, 50);

       newGameBtn.addActionListener(e -> {
        // สั่งสลับฉากโดยตรง
        AudioManager.playSound("umamusume_con.wav"); // เสียงเลือกเมนู
        SceneManager.switchScene(new CharacterSelection(false)); 
        });
        exitBtn.addActionListener(e -> {
            AudioManager.playSound("umamusume_back.wav");
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> SceneManager.switchScene(new StartGame()));
    }
}