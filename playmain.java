import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class playmain extends BaseFrame {
    private JPanel transitionPanel;
    private JLabel transitionLabel;
    private int pointer = 0; // เพิ่มตัวแปร pointer เพื่อรองรับ StoryManager
    private int nextDayTarget = -1;
    private int ap = 0;           // แต้ม AP เริ่มต้น
    private int giftCount = 0;    // ตัวนับการให้ของขวัญ (Max 2)
    private int dateCount = 0;    // ตัวนับการไปเดท (Max 1)

    // เพิ่มตัวแปรสำหรับปุ่มเพื่อให้เรียกใช้ใน onPositionUpdated ได้
    private JButton giftBtn, datingBtn, nextBtn;

    private JLabel apLabel; // เลเบลสำหรับแสดงแต้ม AP
    private Character currentGirl; // สมมติว่ามีตัวแปรนี้เก็บข้อมูลตัวละคร
    private int currentDay = 1; // 1. เพิ่มตัวแปรนี้ครับ

    // --- Akari / Dialogue state (merged from playAkari) ---
    private DialogueLine[] currentQueue;
    // 'pointer' already declared above and used for StoryManager; it will also be used for dialogue progress
    private int score = 0; // route score (Akari)
    private boolean isWaitingForResponse = false;
    private Timer typewriterTimer;

    public void setEventMenuVisible(boolean visible) {
        if (giftBtn != null) giftBtn.setVisible(visible);
        if (datingBtn != null) datingBtn.setVisible(visible);
        if (nextBtn != null) nextBtn.setVisible(visible);
        
        // ตรวจสอบว่าสร้าง textWindow หรือยังก่อนสั่ง setVisible 
        if (textWindow != null) {
            textWindow.setVisible(!visible);
        }
    }

    public void earnAP() {
        this.ap++;
        updateAPDisplay(); 
    }

    private void updateAPDisplay() {
        // อัปเดตตัวเลข AP บนหน้าจอ (ควรสร้าง Label รองรับไว้)
        if (apLabel != null) apLabel.setText("AP: " + ap);
    }

    private void updateUI() {
        updateAPDisplay();
    }

    public boolean canPerformAction(int cost, String type) {
        if (type.equals("gift") && giftCount >= 2) {
            JOptionPane.showMessageDialog(this, "คุณให้ของขวัญครบ 2 ครั้งแล้ว!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (type.equals("date") && dateCount >= 1) {
            JOptionPane.showMessageDialog(this, "คุณไปเดทได้เพียง 1 ครั้งเท่านั้น!", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (ap < cost) {
            JOptionPane.showMessageDialog(this, "แต้ม AP ไม่พอ! (ต้องการ " + cost + " AP)", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        ap -= cost;
        if (type.equals("gift")) giftCount++;
        if (type.equals("date")) dateCount++;
        updateUI();
        return true;
    }

    // (Old top-level listeners removed; listeners are added in the constructor)

    public boolean spendAP(int cost) {
        if (ap >= cost) {
            ap -= cost;
            return true;
        }
        return false;
    }

    public playmain() {
        super("7 Days to Love");
        this.currentGirl = null;
        setBackgroundImage("image\\Place\\_school_in_spring_2.jpg");
        initGameUI();
    }

    public playmain(Character selectedGirl) {
        super("7 Days to Love - " + selectedGirl.getName());
        this.currentGirl = selectedGirl;
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

        nextBtn = new JButton("skip");
        styleButton(nextBtn);
        addComponent(nextBtn, 950, 460, 250, 50);

        styleButton(giftBtn);
        addComponent(giftBtn, 950, 300, 250, 50);
        giftBtn.addActionListener(e -> {
            if (canPerformAction(1, "gift")) {
                currentGirl.addScore(10); 
                JOptionPane.showMessageDialog(this, "ให้ของขวัญสำเร็จ!");
            }
        });

        styleButton(datingBtn);
        addComponent(datingBtn, 950, 380, 250, 50);
        datingBtn.addActionListener(e -> {
            if (canPerformAction(2, "date")) {
                DatingEvent.startDate(this, currentGirl.getName(), currentDay);
            }
        });

        styleButton(nextBtn);
        addComponent(nextBtn, 950, 460, 250, 50);
        nextBtn.addActionListener(e -> handleDayTransition());

       giftBtn.addActionListener(e -> {
        if (canPerformAction(1, "gift")) {
            currentGirl.addScore(10); 
            JOptionPane.showMessageDialog(this, "ให้ของขวัญสำเร็จ!");
        }
    });

    datingBtn.addActionListener(e -> {
        if (canPerformAction(2, "date")) {
            setEventMenuVisible(false); // ปิดเมนูก่อนเข้าฉากเดท
            DatingEvent.startDate(this, currentGirl.getName(), currentDay);
        }
    });

    nextBtn.addActionListener(e -> {
        setEventMenuVisible(false);
        handleDayTransition();
    });

    // ตั้งค่าเริ่มต้นให้เมนูปิดไว้ก่อน จนกว่าบทสนทนาประจำวันจะจบ
    setEventMenuVisible(false);

        // Dialogue mouse-click support (advance or stop typewriter)
        mainPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (typewriterTimer != null && typewriterTimer.isRunning()) {
                    stopTypewriter(currentQueue[pointer - 1].text);
                    return;
                }
                if (!choicePanel.isVisible()) {
                    advanceDialogue();
                }
            }
        });

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
        dialogueArea = new JTextArea();

        nameLabel.setFont(new Font("Tahoma", Font.BOLD, 26));
        nameLabel.setForeground(new Color(255, 200, 0));
        textWindow.add(nameLabel);

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

    // --- Dialogue methods merged from playAkari ---
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
        mainPanel.repaint();

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
        BaseFrame.styleChoiceButton(btnA);
        BaseFrame.styleChoiceButton(btnB);

        btnA.addActionListener(e -> { score += scoreA; handleSelection(resA); StoryManager.onChoiceSelected(this, scoreA); });
        btnB.addActionListener(e -> { score += scoreB; handleSelection(resB); StoryManager.onChoiceSelected(this, scoreB); });

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

    public int getCurrentGirlScore() { return this.score; }

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
    public void hideChoices() { if (choicePanel != null) { choicePanel.setVisible(false); choicePanel.removeAll(); } }

    public static void main(String[] args) {
        // ใช้ invokeLater เพื่อความปลอดภัยของ Thread ใน Swing
        SwingUtilities.invokeLater(() -> {
            playmain m = new playmain();
            m.display();
        });
    }

    public void setNextDayTarget(int targetDay) {
        this.nextDayTarget = targetDay;
    }

    private void handleDayTransition() {
        if (nextDayTarget != -1) {
            this.currentDay = nextDayTarget;
            nextDayTarget = -1; 
        } else {
            this.currentDay++;
        }
        // เรียกใช้ StoryManager เพื่อดึงเนื้อเรื่องตามตัวละครที่เลือก
        if (currentGirl == null) {
            JOptionPane.showMessageDialog(this, "No character selected.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StoryManager.runStory(this, currentGirl.getName(), currentDay);
    }

    // เมธอดสนับสนุนการทำงานข้ามไฟล์
    public void setBackgroundImage(String path) { super.setBackgroundImage(path); }
    public void setResponseMode(boolean mode) { /* จัดการสถานะโหมดเนื้อเรื่อง */ }

// แก้ไขส่วนการเปลี่ยนวันใน playmain.java (ในเมธอดที่จัดการเมื่อจบ Dialogue)
    
}