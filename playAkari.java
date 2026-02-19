import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class playAkari extends BaseFrame {
    private DialogueLine[] currentQueue;
    private int pointer = 0;
    private int score = 0;
    private boolean isWaitingForResponse = false;
    private Timer typewriterTimer;

    public playAkari() {
        super("7 Days to Love - Akari Route");
        initGameUI();
    }

    private void initGameUI() {
        // 1. สร้างตัวละคร
        spritePanel = new CharacterPanel("");
        addComponent(spritePanel, 500, 100, 200, 500);

        // 2. สร้างกล่องข้อความ
        textWindow = new JPanel(null);
        textWindow.setBackground(new Color(0, 0, 0, 220)); 
        
        nameLabel = new JLabel("");
        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(255, 200, 0));
        textWindow.add(nameLabel);

        // แก้ปัญหาเงาซ้อน: เรียกใช้เมธอดสร้าง TextArea แบบพิเศษจากคลาสแม่
        dialogueArea = createCleanTextArea(); 
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
            public void mouseClicked(MouseEvent e) {
                if (typewriterTimer != null && typewriterTimer.isRunning()) {
                    stopTypewriter(currentQueue[pointer - 1].text);
                    return;
                }
                if (!choicePanel.isVisible()) {
                    advanceDialogue();
                }
            }
        });
    }

    public void advanceDialogue() {
        if (currentQueue == null) return;
        
        if (pointer < currentQueue.length) {
            DialogueLine line = currentQueue[pointer];
            nameLabel.setText(line.characterName);
            startTypewriter(line.text);
            
            if (line.spritePath != null && !line.spritePath.isEmpty()) {
                spritePanel.updateImage(line.spritePath);
            }
            pointer++;
        } else {
            if (choicePanel.getComponentCount() > 0 && !isWaitingForResponse) {
                dialogueArea.setText(""); // ล้างข้อความเพื่อรอเลือกทางเลือก
                choicePanel.setVisible(true);
                isWaitingForResponse = true;
            } else {
                StoryManager.processNextDay(this);
            }
        }
    }

    private void startTypewriter(String text) {
        if (typewriterTimer != null) typewriterTimer.stop();
        dialogueArea.setText("");
        mainPanel.repaint(); // บังคับล้างกราฟิกเก่าทิ้งทั้งหมดก่อนเริ่มประโยคใหม่

        final int[] i = {0};
        typewriterTimer = new Timer(30, e -> {
            if (i[0] < text.length()) {
                dialogueArea.append(String.valueOf(text.charAt(i[0])));
                i[0]++;
            } else {
                typewriterTimer.stop();
            }
        });
        typewriterTimer.start();
    }

    private void stopTypewriter(String fullText) {
        if (typewriterTimer != null) typewriterTimer.stop();
        dialogueArea.setText(fullText);
    }

    public void runDayLogic(String bg, DialogueLine[] story, String[] choices, int scoreA, int scoreB, DialogueLine[] resA, DialogueLine[] resB) {
        setBackgroundImage(bg);
        choicePanel.removeAll();
        choicePanel.setVisible(false);
        isWaitingForResponse = false;

        JButton btnA = new JButton("<html><center>" + choices[0] + "</center></html>");
        JButton btnB = new JButton("<html><center>" + choices[1] + "</center></html>");
        
        // เรียกใช้ Static Method ที่แก้ไขแล้ว
        BaseFrame.styleChoiceButton(btnA); 
        BaseFrame.styleChoiceButton(btnB);

        btnA.addActionListener(e -> { score += scoreA; handleSelection(resA); });
        btnB.addActionListener(e -> { score += scoreB; handleSelection(resB); });

        choicePanel.add(btnA);
        choicePanel.add(btnB);
        choicePanel.revalidate();
        
        setDialogueQueue(story);
    }

    private void handleSelection(DialogueLine[] response) {
        choicePanel.setVisible(false);
        choicePanel.removeAll(); 
        isWaitingForResponse = false;
        setDialogueQueue(response);
    }

    public void setDialogueQueue(DialogueLine[] queue) {
        this.currentQueue = queue;
        this.pointer = 0;
        dialogueArea.setText("");
        nameLabel.setText("");
        advanceDialogue();
    }

    @Override
    protected void onPositionUpdated(double scaleX, double scaleY) {
        if (nameLabel != null) nameLabel.setBounds((int)(30*scaleX), (int)(10*scaleY), (int)(300*scaleX), (int)(35*scaleY));
        if (dialogueArea != null) dialogueArea.setBounds((int)(30*scaleX), (int)(55*scaleY), (int)(1120*scaleX), (int)(90*scaleY));
        if (spritePanel != null) mainPanel.setComponentZOrder(spritePanel, mainPanel.getComponentCount() - 1);
    }

    public int getCurrentGirlScore() { return this.score; }
    public void setDialoguePointer(int p) { this.pointer = p; }
    public void hideChoices() { choicePanel.setVisible(false); choicePanel.removeAll(); }
}