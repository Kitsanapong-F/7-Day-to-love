import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class playmain extends BaseFrame {
    private JPanel transitionPanel;
    private JLabel transitionLabel;
    private int pointer = 0; // เพิ่มตัวแปร pointer เพื่อรองรับ StoryManager

    // เพิ่มตัวแปรสำหรับปุ่มเพื่อให้เรียกใช้ใน onPositionUpdated ได้
    private JButton giftBtn, datingBtn, nextBtn;

    public playmain() {
        super("7 Days to Love - playmain");
        setBackgroundImage("image\\Place\\_school_in_spring_2.jpg");
        initGameUI();
    }

    private void initGameUI() {
        // รูปหัวใจ (Sprite Panel)
        spritePanel = new CharacterPanel("image\\icon\\—Pngtree—red heart percentage_5320594.png");
        addComponent(spritePanel, 0, 0, 150, 150);
        
        // day
        spritePanel = new CharacterPanel("image\\icon\\—Pngtree—red heart percentage_5320594.png");
        addComponent(spritePanel, 1100, 0, 150, 150);

        giftBtn = new JButton("give a gift");
        styleButton(giftBtn);
        addComponent(giftBtn, 950, 300, 250, 50);

        datingBtn = new JButton("Dating");
        styleButton(datingBtn);
        addComponent(datingBtn, 950, 380, 250, 50);

        nextBtn = new JButton("Next");
        styleButton(nextBtn);
        addComponent(nextBtn, 950, 460, 250, 50);

        // 1. หน้าจอ Transition (ต้องสร้างและ addComponent ไว้)
        transitionPanel = new JPanel(new BorderLayout());
        transitionPanel.setBackground(Color.BLACK);
        transitionPanel.setVisible(false);
        transitionLabel = new JLabel("", SwingConstants.CENTER);
        transitionLabel.setForeground(Color.WHITE);
        transitionLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
        transitionPanel.add(transitionLabel, BorderLayout.CENTER);
        addComponent(transitionPanel, 0, 0, 1280, 720);

        // 2. กล่องข้อความ
        textWindow = new JPanel(null);
        textWindow.setBackground(new Color(0, 0, 0, 220)); 
        nameLabel = new JLabel("");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(255, 200, 0));
        textWindow.add(nameLabel);

        dialogueArea = new JTextArea();
        dialogueArea.setFont(new Font("Tahoma", Font.PLAIN, 24));
        dialogueArea.setForeground(Color.WHITE);
        dialogueArea.setOpaque(false);
        dialogueArea.setLineWrap(true);
        dialogueArea.setWrapStyleWord(true);
        dialogueArea.setEditable(false);
        textWindow.add(dialogueArea);
        addComponent(textWindow, 50, 520, 1180, 160);

        // 3. Choice Panel
        choicePanel = new JPanel(new GridLayout(2, 1, 0, 20));
        choicePanel.setOpaque(false);
        choicePanel.setVisible(false);
        addComponent(choicePanel, 820, 220, 420, 220);

        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!transitionPanel.isVisible() && !choicePanel.isVisible()) {
                    // advanceDialogue(); 
                }
            }
        });
    }

    public void showDayTransition(int day, String title, Runnable onComplete) {
        transitionLabel.setText("<html><div style='text-align: center;'>Day " + day + "<br><span style='font-size: 24px;'>" + title + "</span></div></html>");
        transitionPanel.setVisible(true);
        
        // บังคับให้จอดำอยู่หน้าสุด (เลเยอร์ 0) เพื่อไม่ให้ปุ่มโผล่ทะลุมา
        mainPanel.setComponentZOrder(transitionPanel, 0);

        Timer timer = new Timer(2500, e -> {
            transitionPanel.setVisible(false);
            mainPanel.repaint();
            if (onComplete != null) onComplete.run();
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        // จัดการจอดำให้เต็มจอเสมอ
        if (transitionPanel != null) {
            transitionPanel.setBounds(0, 0, getWidth(), getHeight());
        }

        // จัดการตำแหน่งข้อความภายในกล่อง
        if (nameLabel != null) 
            nameLabel.setBounds((int)(30 * scaleX), (int)(10 * scaleY), (int)(300 * scaleX), (int)(35 * scaleY));
        if (dialogueArea != null) 
            dialogueArea.setBounds((int)(30 * scaleX), (int)(55 * scaleY), (int)(1120 * scaleX), (int)(90 * scaleY));

        // บังคับให้หัวใจและปุ่มเมนูขยับตามจอ (ถ้าต้องการให้ปุ่มล็อคตำแหน่งเดิม ไม่ต้องแก้ตรงนี้)
        // แต่การปล่อยไว้เฉยๆ addComponent ใน BaseFrame จะจัดการ Bounds หลักให้อยู่แล้วครับ
    }

    // เมธอดสนับสนุน StoryManager
    public void setDialoguePointer(int p) { this.pointer = p; }
    public void hideChoices() { choicePanel.setVisible(false); }

    public static void main(String[] args) {
        // ใช้ invokeLater เพื่อความปลอดภัยของ Thread ใน Swing
        SwingUtilities.invokeLater(() -> {
            playmain m = new playmain();
            m.display();
        });
    }
}