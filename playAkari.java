import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class playAkari extends BaseFrame {
    // ตัวแปรควบคุมเนื้อเรื่อง
    private DialogueLine[] currentQueue;
    private int pointer = 0;
    private int score = 0;
    private int currentDay = 1; 
    private boolean isWaitingForResponse = false; 

    public playAkari() {
        super("7 Days to Love - Akari Route");
        initGameUI();
    }

    private void initGameUI() {
        // 1. สร้างตัวละคร (spritePanel มีอยู่ใน BaseFrame แล้ว)
        spritePanel = new CharacterPanel("");
        addComponent(spritePanel, 500, 100, 200, 500);

        // 2. สร้างกล่องข้อความ (textWindow มีอยู่ใน BaseFrame แล้ว)
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

        // 3. สร้าง Choice Panel (choicePanel มีอยู่ใน BaseFrame แล้ว)
        choicePanel = new JPanel(new GridLayout(2, 1, 0, 20));
        choicePanel.setOpaque(false);
        choicePanel.setVisible(false);
        addComponent(choicePanel, 820, 220, 420, 220);

        // ระบบคลิกหน้าจอ
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (choicePanel != null && !choicePanel.isVisible()) {
                    advanceDialogue();
                }
            }
        });
    }

    // --- เมธอดสำหรับแก้ Error ใน StoryManager (ต้องเป็น public) ---

    public void setDialoguePointer(int p) { 
        this.pointer = p; 
    }

    public void hideChoices() { 
        if (choicePanel != null) {
            clearDynamicComponents(choicePanel); 
            choicePanel.setVisible(false); 
        }
    }

    // --- เมธอดจัดการ Layout และ Z-Order (ตัวละครอยู่หลัง) ---

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        if (nameLabel != null) 
            nameLabel.setBounds((int)(30 * scaleX), (int)(10 * scaleY), (int)(300 * scaleX), (int)(35 * scaleY));
        if (dialogueArea != null) 
            dialogueArea.setBounds((int)(30 * scaleX), (int)(55 * scaleY), (int)(1120 * scaleX), (int)(90 * scaleY));

        // จัดลำดับเลเยอร์ให้ตัวละครอยู่หลังกล่องข้อความเสมอ
        if (spritePanel != null) {
            mainPanel.setComponentZOrder(spritePanel, mainPanel.getComponentCount() - 1);
        }
    }

    // --- เมธอด Logic เกม ---

    public void advanceDialogue() {
        if (currentQueue == null) return;
        if (pointer < currentQueue.length) {
            DialogueLine line = currentQueue[pointer];
            dialogueArea.setText(""); 
            nameLabel.setText(line.characterName);
            dialogueArea.setText(line.text);
            if (line.spritePath != null && !line.spritePath.equals("ads")) {
                spritePanel.updateImage(line.spritePath);
            }
            pointer++;
            mainPanel.repaint(); 
        } else {
            if (choicePanel.getComponentCount() > 0 && !isWaitingForResponse) {
                choicePanel.setVisible(true);
                isWaitingForResponse = true;
            } else {
                nextDay();
            }
        }
    }

    public void runDayLogic(String bg, DialogueLine[] story, String[] choices, int scoreA, int scoreB, DialogueLine[] resA, DialogueLine[] resB) {
        setBackgroundImage(bg);
        hideChoices();
        isWaitingForResponse = false;
        setDialogueQueue(story);
        
        JButton btnA = new JButton("<html><center>" + choices[0] + "</center></html>");
        JButton btnB = new JButton("<html><center>" + choices[1] + "</center></html>");
        styleChoiceButton(btnA); 
        styleChoiceButton(btnB);

        btnA.addActionListener(e -> { score += scoreA; handleSelection(resA); });
        btnB.addActionListener(e -> { score += scoreB; handleSelection(resB); });

        choicePanel.add(btnA);
        choicePanel.add(btnB);
        choicePanel.revalidate();
    }

    private void handleSelection(DialogueLine[] response) {
        choicePanel.setVisible(false);
        isWaitingForResponse = false;
        setDialogueQueue(response);
    }

    public void setDialogueQueue(DialogueLine[] queue) {
        this.currentQueue = queue;
        this.pointer = 0;
        advanceDialogue();
    }

    private void nextDay() {
        currentDay++;
        if (currentDay <= 7) StoryManager.runAkari(this, currentDay);
    }

    public int getCurrentGirlScore() { return this.score; }
}